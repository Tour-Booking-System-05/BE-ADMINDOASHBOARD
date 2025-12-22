package com.travel.demo.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ActivityAudit {

    // ORDER / CATEGORY / USER / TOUR ...
    String type();

    // ORDER / CATEGORY / USER / TOUR ...
    String entityType();

    // NEW: CREATED / UPDATED / DELETED / ...
    String action() default "";

    // Lấy entityId từ param (vd @PathVariable Integer id)
    String entityIdParam() default "id";

    // Dùng cho ORDER update status (body status là byte)
    String statusParam() default "";
    String statusField() default "status";

    // Template fallback
    String title() default "";
    String description() default "";
}
