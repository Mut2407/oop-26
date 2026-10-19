# PHÂN TÍCH NGHIỆP VỤ VÀ CÁC LỚP

**Đề tài:** Xây dựng ứng dụng quản lý cửa hàng dụng cụ thể thao  
**Phiên bản:** v1.1 19/10/2026  


## 1. Phạm vi

Sơ đồ hiện tại có **11 class**: `SanPham`, `GiayTheThao`, `DungCuTheThao`, `LoaiSanPham`, `KhachHang`, `NhanVien`, `NhaCungCap`, `HoaDon`, `ChiTietHoaDon`, `PhieuNhap`, `ChiTietPhieuNhap`.

Tài liệu Word trước đó mô tả mô hình đề xuất 20 class, 1 interface và 2 enum; **không dùng các thành phần này để mô tả sơ đồ mới**. Sơ đồ mới không định nghĩa interface, enum, lớp danh sách, lớp quản lý kho hoặc lớp lưu file.

## 2. Tổng quan nghiệp vụ

- **Quản lý loại và sản phẩm:** tạo loại hàng; lưu mã, tên, thương hiệu, giá bán, số lượng tồn và mã loại. Hai loại sản phẩm chuyên biệt được vẽ là giày thể thao và dụng cụ thể thao.
- **Quản lý đối tượng:** lưu thông tin khách hàng, nhân viên, nhà cung cấp và sử dụng mã để liên kết với chứng từ.
- **Bán hàng:** khách mua hàng → nhân viên lập `HoaDon` → tạo các `ChiTietHoaDon` (sản phẩm, số lượng, đơn giá).
- **Nhập hàng:** nhà cung cấp giao hàng → nhân viên lập `PhieuNhap` → tạo các `ChiTietPhieuNhap` (sản phẩm, số lượng, đơn giá).
- **Theo dõi tồn kho:** `SanPham` có thuộc tính `soLuongTon`. Quy tắc tự động tăng/giảm tồn, xác nhận chứng từ và hủy chứng từ chưa được mô tả rõ trong sơ đồ.

## 3. Các class và trường dữ liệu

### 3.1. SanPham — sản phẩm chung

| Thuộc tính | Kiểu trong sơ đồ | Ý nghĩa |
|---|---|---|
| `maSanPham` | `String` | Mã định danh sản phẩm |
| `tenSanPham` | `String` | Tên sản phẩm |
| `thuongHieu` | `String` | Hãng / nhãn hiệu |
| `giaBan` | `double` | Giá bán |
| `soLuongTon` | `String` | Lượng tồn, giữ nguyên kiểu đang vẽ |
| `maLoai` | `String` | Mã loại sản phẩm |

### 3.2. GiayTheThao — kế thừa SanPham

| Thuộc tính | Kiểu | Ý nghĩa |
|---|---|---|
| `kichCoGiay` | `double` | Cỡ giày |
| `mauSac` | `String` | Màu giày |
| `loaiDeGiay` | `String` | Kiểu đế |

### 3.3. DungCuTheThao — kế thừa SanPham

| Thuộc tính | Kiểu | Ý nghĩa |
|---|---|---|
| `monTheThao` | `String` | Môn thể thao |
| `khoiLuong` | `double` | Khối lượng |
| `vatLieu` | `String` | Vật liệu |

### 3.4. LoaiSanPham — phân loại sản phẩm

| Thuộc tính | Kiểu | Ý nghĩa |
|---|---|---|
| `maLoai` | `String` | Mã loại |
| `tenLoai` | `String` | Tên loại |
| `moTa` | `String` | Mô tả |

### 3.5. KhachHang — khách mua hàng

| Thuộc tính | Kiểu trong sơ đồ | Ý nghĩa |
|---|---|---|
| `maKhachHang` | `String` | Mã khách hàng |
| `hoTen` | `String` | Họ tên |
| `soDienThoai` | `int` | Số điện thoại (nên đổi thành `String` khi code) |
| `diaChi` | `String` | Địa chỉ |

### 3.6. NhanVien — người lập chứng từ

| Thuộc tính | Kiểu | Ý nghĩa |
|---|---|---|
| `maNhanVien` | `String` | Mã nhân viên |
| `hoTen` | `String` | Họ tên |
| `soDienThoai` | `String` | Số điện thoại |
| `diaChi` | `String` | Địa chỉ |

### 3.7. NhaCungCap — đơn vị cung ứng

| Thuộc tính | Kiểu | Ý nghĩa |
|---|---|---|
| `maNhaCungCap` | `String` | Mã nhà cung cấp |
| `tenNhaCungCap` | `String` | Tên nhà cung cấp |
| `soDienThoai` | `String` | Số điện thoại |
| `diaChi` | `String` | Địa chỉ |

### 3.8. HoaDon — chứng từ bán hàng

| Thuộc tính | Kiểu trong sơ đồ | Ý nghĩa |
|---|---|---|
| `maHoaDon` | `String` | Mã hóa đơn |
| `ngayLap` | `date` | Ngày lập |
| `maKhachhang` | `String` | Mã khách hàng (đúng chữ hoa/thường trong sơ đồ) |
| `maNhanVien` | `String` | Mã nhân viên |
| `danhSachChiTietHoaDon` | `String` | Danh sách chi tiết (kiểu chưa phù hợp để biểu diễn nhiều đối tượng) |

### 3.9. ChiTietHoaDon — một dòng bán

| Thuộc tính | Kiểu | Ý nghĩa |
|---|---|---|
| `sanPham` | `SanPham` | Sản phẩm được bán |
| `soLuong` | `int` | Số lượng bán |
| `donGiaBan` | `double` | Đơn giá bán của dòng |

### 3.10. PhieuNhap — chứng từ nhập hàng

| Thuộc tính | Kiểu trong sơ đồ | Ý nghĩa |
|---|---|---|
| `maPhieuNhap` | `String` | Mã phiếu nhập |
| `ngayLap` | `date` | Ngày lập |
| `maNhaCungCap` | `String` | Mã nhà cung cấp |
| `maNhanVien` | `String` | Mã nhân viên |
| `danhSachChiTietPhieuNhap` | `String` | Danh sách chi tiết (nên dùng mảng hoặc danh sách đối tượng) |

### 3.11. ChiTietPhieuNhap — một dòng nhập

| Thuộc tính | Kiểu | Ý nghĩa |
|---|---|---|
| `sanPham` | `SanPham` | Sản phẩm nhập |
| `soLuong` | `int` | Số lượng nhập |
| `donGiaNhap` | `double` | Đơn giá nhập của dòng |

## 4. Quan hệ giữa các lớp

| Quan hệ | Các lớp | Diễn giải |
|---|---|---|
| Kế thừa | `GiayTheThao`, `DungCuTheThao` → `SanPham` | Lớp con có đặc điểm riêng và dùng thuộc tính chung |
| Phân loại | `SanPham` ↔ `LoaiSanPham` | Kết nối qua `maLoai` |
| Bán hàng | `KhachHang` — `HoaDon`; `NhanVien` — `HoaDon`; `HoaDon` — `ChiTietHoaDon` — `SanPham` | Ghi nhận người mua, người lập và chi tiết bán |
| Nhập hàng | `NhaCungCap` — `PhieuNhap`; `NhanVien` — `PhieuNhap`; `PhieuNhap` — `ChiTietPhieuNhap` — `SanPham` | Ghi nhận đơn vị giao hàng, người lập và chi tiết nhập |

## 5. Ràng buộc nghiệp vụ cần làm rõ (đề xuất)

1. Các mã định danh không trùng; lượng bán/nhập phải dương, tồn kho không âm.
2. Đơn giá không âm; giá trong mỗi chi tiết chứng từ là giá tại thời điểm giao dịch.
3. Quy định thời điểm tăng/giảm tồn và kiểm tra đủ hàng trước khi bán.
4. Xác định có cho phép bán hàng không gắn tài khoản khách hàng (khách vãng lai) hay không.

Các quy tắc trên **chưa được biểu diễn đầy đủ trên sơ đồ**; cần nhóm thống nhất trước khi triển khai.

## 6. Đề xuất chuẩn hóa kiểu dữ liệu khi viết Java

| Trường | Đang vẽ | Đề xuất |
|---|---|---|
| `SanPham.soLuongTon` | `String` | `int` |
| `KhachHang.soDienThoai` | `int` | `String` |
| `HoaDon.ngayLap`, `PhieuNhap.ngayLap` | `date` | `LocalDate` |
| `giaBan`, `donGiaBan`, `donGiaNhap` | `double` | `BigDecimal` nếu cần độ chính xác tiền tệ |
| `danhSachChiTietHoaDon` | `String` | `List<ChiTietHoaDon>` hoặc mảng |
| `danhSachChiTietPhieuNhap` | `String` | `List<ChiTietPhieuNhap>` hoặc mảng |
| `HoaDon.maKhachhang` | `String` | Đổi cách viết thành `maKhachHang` |

**Lưu ý thiết kế:** Trong repository hiện có `QuanAoTheThao.java` nhưng sơ đồ mới chưa định nghĩa lớp đó. Nếu giữ lớp, cần bổ sung vào diagram và sửa các thuộc tính đang nhầm từ giày. Sơ đồ cũng chưa quy định phương thức đa hình hoặc `abstract SanPham`; nếu tiêu chí môn học yêu cầu, bổ sung phương thức chung và `@Override` cho lớp con.

## 7. Hướng triển khai

`LoaiSanPham` → `SanPham` → `GiayTheThao`/`DungCuTheThao` → `KhachHang`/`NhanVien`/`NhaCungCap` → `HoaDon`/`ChiTietHoaDon` → `PhieuNhap`/`ChiTietPhieuNhap` → `Main` để kiểm thử.

