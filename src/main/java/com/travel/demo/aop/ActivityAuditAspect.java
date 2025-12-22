package com.travel.demo.aop;

import com.travel.demo.annotation.ActivityAudit;
import com.travel.demo.entity.ActivityLog;
import com.travel.demo.entity.OrderStatus;
import com.travel.demo.repository.ActivityLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
@RequiredArgsConstructor
public class ActivityAuditAspect {

    private final ActivityLogRepository activityLogRepository;
    private final HttpServletRequest request;

    @Around("@annotation(com.travel.demo.annotation.ActivityAudit)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object result = pjp.proceed(); // log SUCCESS timeline

        MethodSignature sig = (MethodSignature) pjp.getSignature();
        Method method = sig.getMethod();
        ActivityAudit audit = method.getAnnotation(ActivityAudit.class);

        // ===== Actor =====
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ActorInfo actor = resolveActor(auth);

        // ===== EntityId from path variable param =====
        Long entityId = extractLongFromArgs(sig.getParameterNames(), pjp.getArgs(), audit.entityIdParam());

        // ===== Status byte -> OrderStatus (chỉ áp dụng cho ORDER) =====
        OrderStatus orderStatus = null;
        if (audit.statusParam() != null && !audit.statusParam().isBlank()) {
            Byte st = extractByteFieldFromParam(sig.getParameterNames(), pjp.getArgs(), audit.statusParam(), audit.statusField());
            orderStatus = mapToOrderStatus(st);
        }

        // ===== Action: ưu tiên action trong annotation, nếu trống thì suy ra theo HTTP method =====
        String httpMethod = request.getMethod();
        String action = (audit.action() != null && !audit.action().isBlank())
                ? audit.action()
                : inferActionFromHttpMethod(httpMethod);

        Built built = buildTimeline(audit.type(), entityId, orderStatus, actor.displayName, action);

        // Title/desc: ưu tiên built (ORDER status), nếu không có thì dùng template từ annotation
        String title = (built.title != null && !built.title.isBlank())
                ? built.title
                : applyTemplate(audit.title(), entityId, actor.displayName, actor.role, orderStatus);

        String desc = (built.description != null && !built.description.isBlank())
                ? built.description
                : applyTemplate(audit.description(), entityId, actor.displayName, actor.role, orderStatus);

        // ===== Save log =====
        ActivityLog log = new ActivityLog();
        log.setType(audit.type());
        log.setAction(built.action != null ? built.action : action); // đảm bảo có action
        log.setEntityType(audit.entityType());
        log.setEntityId(entityId);

        log.setTitle(title);
        log.setDescription((desc == null || desc.isBlank()) ? null : desc);

        log.setActorEmail(actor.displayName);
        log.setActorRole(actor.role);
        log.setAdmin(actor.isAdmin);

        log.setMethod(httpMethod);
        log.setUri(request.getRequestURI());
        log.setIpAddress(getClientIp(request));
        log.setCreatedAt(LocalDateTime.now());

        activityLogRepository.save(log);

        return result;
    }

    // =========================
    //  Timeline rules
    // =========================
    private Built buildTimeline(String type, Long entityId, OrderStatus status, String actorName, String fallbackAction) {
        // Với type != ORDER: KHÔNG tự chế title/desc nữa, dùng template annotation
        // và action sẽ theo fallbackAction (CREATED/UPDATED/DELETED)
        if (!"ORDER".equalsIgnoreCase(type)) {
            return new Built(fallbackAction, null, null);
        }

        String idPart = entityId == null ? "" : "#" + entityId;

        // Nếu không lấy được status => generic update
        if (status == null) {
            return new Built("UPDATED",
                    "Đơn hàng " + idPart + " đã được cập nhật trạng thái",
                    "Admin " + actorName + " cập nhật trạng thái đơn " + idPart);
        }

        // Map đúng enum của bạn
        return switch (status) {
            case PENDING -> new Built("PENDING",
                    "Đơn hàng " + idPart + " đang chờ xử lý",
                    "Đơn " + idPart + " chuyển sang trạng thái chờ");

            case PROCESS -> new Built("PROCESS",
                    "Đơn hàng " + idPart + " đang được xử lý",
                    "Admin " + actorName + " đang xử lý đơn " + idPart);

            case COMPLETE -> new Built("COMPLETE",
                    "Đơn hàng " + idPart + " đã hoàn thành",
                    "Admin " + actorName + " đã hoàn thành đơn " + idPart);

            case CANCEL -> new Built("CANCEL",
                    "Đơn hàng " + idPart + " đã bị huỷ",
                    "Admin " + actorName + " đã huỷ đơn " + idPart);
        };
    }

    private String inferActionFromHttpMethod(String method) {
        if (method == null) return "UPDATED";
        return switch (method.toUpperCase()) {
            case "POST" -> "CREATED";
            case "PUT", "PATCH" -> "UPDATED";
            case "DELETE" -> "DELETED";
            case "GET" -> "READ";
            default -> "UPDATED";
        };
    }

    // byte -> enum
    private OrderStatus mapToOrderStatus(Byte status) {
        if (status == null) return null;
        return switch (status) {
            case 0 -> OrderStatus.PENDING;
            case 1 -> OrderStatus.PROCESS;
            case 2 -> OrderStatus.COMPLETE;
            case 3 -> OrderStatus.CANCEL;
            default -> null;
        };
    }

    // =========================
    //  Actor resolve
    // =========================
    private ActorInfo resolveActor(Authentication auth) {
        String displayName = "anonymous";
        String role = "UNKNOWN";
        boolean isAdmin = false;

        if (auth != null) {
            if (auth.getAuthorities() != null && !auth.getAuthorities().isEmpty()) {
                role = auth.getAuthorities().iterator().next().getAuthority();
                isAdmin = role != null && role.toUpperCase().contains("ADMIN");
            }

            Object principal = auth.getPrincipal();
            if (principal != null) {
                if (principal instanceof com.travel.demo.entity.Accounts acc) {
                    String fullName = safeGetString(acc, "fullName");
                    String email = safeGetString(acc, "email");
                    String username = safeGetString(acc, "username");

                    if (fullName != null && !fullName.isBlank()) displayName = fullName;
                    else if (email != null && !email.isBlank()) displayName = email;
                    else if (username != null && !username.isBlank()) displayName = username;
                    else displayName = "user";
                } else {
                    displayName = auth.getName();
                }
            }
        }

        return new ActorInfo(displayName, role, isAdmin);
    }

    private String safeGetString(Object obj, String fieldName) {
        try {
            Field f = findField(obj.getClass(), fieldName);
            if (f == null) return null;
            f.setAccessible(true);
            Object v = f.get(obj);
            return v == null ? null : v.toString();
        } catch (Exception e) {
            return null;
        }
    }

    // =========================
    //  Helpers: extract args
    // =========================
    private Long extractLongFromArgs(String[] paramNames, Object[] args, String targetParam) {
        if (targetParam == null || targetParam.isBlank()) return null;
        if (paramNames == null || args == null) return null;

        for (int i = 0; i < paramNames.length; i++) {
            if (targetParam.equals(paramNames[i]) && args[i] != null) {
                Object v = args[i];
                if (v instanceof Number n) return n.longValue();
                try { return Long.parseLong(v.toString()); } catch (Exception ignored) {}
            }
        }
        return null;
    }

    private Byte extractByteFieldFromParam(String[] paramNames, Object[] args, String paramName, String fieldName) {
        if (paramName == null || paramName.isBlank()) return null;
        if (paramNames == null || args == null) return null;

        for (int i = 0; i < paramNames.length; i++) {
            if (paramName.equals(paramNames[i]) && args[i] != null) {
                Object obj = args[i];
                try {
                    Field f = findField(obj.getClass(), fieldName);
                    if (f == null) return null;
                    f.setAccessible(true);
                    Object val = f.get(obj);

                    if (val instanceof Byte b) return b;
                    if (val instanceof Number n) return n.byteValue();

                } catch (Exception ignored) {
                    return null;
                }
            }
        }
        return null;
    }

    private Field findField(Class<?> clazz, String fieldName) {
        Class<?> cur = clazz;
        while (cur != null && cur != Object.class) {
            try {
                return cur.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                cur = cur.getSuperclass();
            }
        }
        return null;
    }

    private String applyTemplate(String template, Long id, String actor, String role, OrderStatus status) {
        if (template == null) return null;
        return template
                .replace("{id}", id == null ? "" : String.valueOf(id))
                .replace("{actor}", actor == null ? "" : actor)
                .replace("{role}", role == null ? "" : role)
                .replace("{status}", status == null ? "" : status.name());
    }

    private String getClientIp(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) return xff.split(",")[0].trim();
        return request.getRemoteAddr();
    }

    private record Built(String action, String title, String description) {}
    private record ActorInfo(String displayName, String role, boolean isAdmin) {}
}
