# 🧳 Tour Booking System – Admin Dashboard (Backend)

Backend cho **Admin Dashboard** của hệ thống **Tour Booking System**.  
Dự án cung cấp các API quản trị cho việc quản lý **tour, đơn hàng, nhân viên, danh mục, khuyến mãi**, kèm theo **Activity Log (timeline hoạt động)** và **phân quyền theo role**.
---

## 🚀 Công nghệ sử dụng

- **Java 17**
- **Spring Boot**
- **Spring Security (JWT + Role/Permission)**
- **Spring Data JPA (Hibernate)**
- **MySQL**
- **Spring AOP** (Activity Log)
- **Cloudinary** (upload ảnh)
- **Maven**

---

## 📌 Chức năng chính

### 👤 Quản lý tài khoản & phân quyền
- Đăng nhập / xác thực JWT
- Phân quyền theo **Role & Permission**
- Kiểm soát truy cập API bằng `@PreAuthorize`

### 🧑‍💼 Quản lý nhân viên (Employees)
- Thêm / sửa / xoá nhân viên
- Reset mật khẩu
- Phân trang, tìm kiếm
- Lọc theo quyền (role)

### 🧳 Quản lý Tour
- Tạo / cập nhật / xoá tour
- Nhân bản tour
- Upload hình ảnh tour (Cloudinary)
- Xoá nhiều tour

### 📦 Quản lý đơn hàng (Orders)
- Cập nhật trạng thái đơn hàng
- Theo dõi tiến trình đơn (PENDING / PROCESS / COMPLETE / CANCEL)

### 🗂️ Quản lý danh mục (Categories)
- CRUD danh mục
- Xoá nhiều danh mục

### 🎁 Quản lý khuyến mãi (Promotions)
- Tạo chương trình khuyến mãi
- Áp dụng phần trăm giảm giá
- Thời gian hiệu lực

### 🕒 Activity Log (Timeline)
- Ghi lại hoạt động Admin theo thời gian:
  - Tạo / sửa / xoá tour
  - Cập nhật đơn hàng
  - Quản lý danh mục, nhân viên
- Triển khai bằng **Spring AOP + Custom Annotation**
- Dữ liệu phục vụ UI “Hoạt động gần đây”

---

## 🧱 Kiến trúc tổng thể


Controller → Service → Repository → Database
↑
AOP (Activity Log)

- **Controller**: xử lý request/response
- **Service**: xử lý nghiệp vụ
- **Repository**: truy vấn DB
- **AOP**: tự động ghi log hoạt động

---

## 🗃️ Cấu trúc thư mục

src/main/java/com/travel/demo
├── annotation # @ActivityAudit
├── aop # ActivityAuditAspect
├── controller # REST Controllers
├── dto # Request / Response DTO
├── entity # JPA Entities
├── repository # Spring Data JPA
├── service
│ └── impl
├── security # JWT, Permission, Role
└── TravelDemoApplication.java

🧪 Chạy project
mvn clean install
mvn spring-boot:run
📄 License
Dự án phục vụ mục đích học tập và phát triển nội bộ.
