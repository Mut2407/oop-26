# Class Diagram - Quản lý cửa hàng dụng cụ thể thao

Bản điều chỉnh từ `yeucau_doan_OOP.doc.docx` và `class-diagram.docx` do người dùng cung cấp.

## Thành phần
- **20 class** (gồm 1 abstract class `SanPham`), 1 interface `IQuanLyDanhSach<T>` và 2 enum trạng thái (không tính 2 enum hoặc interface vào số lượng class).
- Kế thừa: `GiayTheThao`, `QuanAoTheThao`, `DungCuTheThao` kế thừa `SanPham`.
- Đa hình: override `hienThiThongTin()`; gọi qua `SanPham[]` trong `DanhSachSanPham`.
- Mảng: `DanhSachSanPham` bắt buộc giữ `SanPham[]`, không thay bằng List nếu giảng viên muốn chấm 'array'.
- Interface `IQuanLyDanhSach<T>`: triển khai tại `DanhSachSanPham`, `DanhSachKhachHang`, `DanhSachNhaCungCap`.
- Đọc/ghi file text UTF-8: `LuuTruFileText`; với đa hình cần ghi loại sản phẩm (`GIAY`, `QUAN_AO`, `DUNG_CU`).
- Static: `HoaDon.soThuTuTiepTheo`, `HoaDon.taoMaTuDong()`, tương tự `PhieuNhap`. Khi load file phải khôi phục số thứ tự để không trùng mã.
- Tồn kho: `DichVuKho` xác nhận hóa đơn khi đủ hàng, trừ kho **một lần**; xác nhận phiếu nhập thì cộng kho **một lần**. Chứng từ đã xác nhận không xóa trực tiếp.
- Doanh thu: chỉ tính hóa đơn trạng thái `DA_XAC_NHAN`.
- Tiền tệ: `BigDecimal`, không dùng `double`; ngày: `LocalDate`; `soLuongTon`, `soLuong`: `int`.
- `ChiTietHoaDon.donGiaBan` lưu giá lúc bán; `ChiTietPhieuNhap.donGiaNhap` lưu giá lúc nhập, không lấy ngược giá hiện tại của sản phẩm.
- Hóa đơn có thể không liên kết khách hàng (`KhachHang` 0..1) để hỗ trợ bán lẻ không đăng ký.
- Có thể thêm giao diện console hoặc JavaFX sau; UI không được tính trong sơ đồ OOP lõi.

## Các file
- `ClassDiagram_CuaHangDungCuTheThao.drawio`: **3 trang**, mở/edit trong https://app.diagrams.net.
- `01_TongQuan.png`, `02_SanPham_DanhSach.png`, `03_BanHang_NhapKho.png`: ảnh sơ đồ xem ngay.
- `ClassDiagram_CuaHangDungCuTheThao.puml`: PlantUML code dễ chỉnh khi phát triển.

## Chú ý
Đây là sơ đồ **đề xuất** dựa trên rubric, không phải sơ đồ đã được giảng viên phê duyệt. Method/constructor dạng `(...)` ở mức UML khái quát; khi code cần khai báo đầy đủ tham số tương ứng với từng thuộc tính bắt buộc.
