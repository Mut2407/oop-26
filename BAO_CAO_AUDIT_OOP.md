# Báo cáo audit dự án Java OOP — Cửa hàng dụng cụ thể thao

**Phạm vi:** source tại commit `79ace49` trên nhánh `main`, kiểm tra ngày 03/11/2026. Chỉ đọc source, biên dịch vào thư mục tạm và chạy test hiện có; không sửa source hay đọc nội dung các file `data/`. Báo cáo phản ánh code thực tế, không lấy README hoặc sơ đồ làm bằng chứng triển khai.

**Quy ước:** **Đã hoàn thành** = có logic và đường sử dụng; **Hoàn thành một phần** = mới có một số bước; **Chưa có** = không tìm thấy code; **Chưa kiểm chứng** = có code nhưng chưa test hoặc chưa chạy luồng thực tế. Số dòng ghi dưới dạng `đường/dẫn/File.java:dòng`.

## 1. Tổng quan dự án

Ứng dụng Java console cho một người thao tác tại một thời điểm. Entry point là `src/Main.java:20` (`Main.main`). `Main` tạo bốn danh sách từ `models.DuLieu`, đọc hoặc tạo bốn file text rồi mở menu (`src/Main.java:21-52`). `Main` giữ menu chính, menu sản phẩm, khách hàng, nhà cung cấp và phần lớn hàm nhập liệu (`src/Main.java:71-775`); `DanhSachNhanVien.quanLy` giữ menu nhân viên (`src/services/DanhSachNhanVien.java:330`).

| Mục | Kết quả và bằng chứng |
|---|---|
| Ngôn ngữ/phiên bản | Java. Source dùng `String.isBlank`, `Path.of`, `List.copyOf` nên cần tối thiểu Java 11 (`src/services/DanhSachSanPham.java:18,177`, `src/services/FileText.java:16`). Project không khai báo phiên bản mục tiêu. Máy audit có `java` và `javac` 24. |
| Build | Java thuần; không tìm thấy `pom.xml`, `build.gradle` hay `settings.gradle`. `run.ps1:1-18` gọi `javac -encoding UTF-8 -d out` rồi `java -cp out Main`. |
| Package | `src/models` dùng package `models`; `src/services` dùng package `services`; `src/Main.java` và `tests/DanhSachTest.java` ở default package. |
| Thư viện | Import trong `src/` và `tests/` chỉ dùng Java Standard Library; không có dependency ngoài trong project. |
| Số loại | 19 class sản phẩm, 1 interface, 0 enum; thêm 1 class test. `DanhSachKhachHang` và `DanhSachNhanVien` dùng mảng đối tượng trực tiếp (`src/services/DanhSachKhachHang.java:15`, `src/services/DanhSachNhanVien.java:16`). |
| Cách chạy | `powershell -ExecutionPolicy Bypass -File .\run.ps1` theo `README.md` và `run.ps1`. Chưa chạy `Main` vì khởi động có thể tạo file trong `data/` (`src/Main.java:57-60`). |

`README.md` còn mô tả trạng thái cũ, ví dụ phần sơ đồ và danh sách class ở `README.md` không phản ánh toàn bộ source hiện nay. Đánh giá dưới đây dựa trên file Java.

## 2. Cây thư mục source code

Chỉ liệt kê tên tệp; không đọc nội dung `data/`, ảnh hoặc tài liệu nhị phân. Bỏ qua `.git`, `.idea`, `out`, file sinh tự động.

```text
oop-26/
├── README.md
├── run.ps1
├── src/
│   ├── Main.java
│   ├── models/
│   │   ├── SanPham.java
│   │   ├── GiayTheThao.java
│   │   ├── DungCuTheThao.java
│   │   ├── QuanAoTheThao.java
│   │   ├── LoaiSanPham.java
│   │   ├── KhachHang.java
│   │   ├── NhanVien.java
│   │   ├── NhaCungCap.java
│   │   ├── HoaDon.java
│   │   ├── ChiTietHoaDon.java
│   │   ├── PhieuNhap.java
│   │   ├── ChiTietPhieuNhap.java
│   │   └── DuLieu.java
│   └── services/
│       ├── DanhSachSanPham.java
│       ├── DanhSachKhachHang.java
│       ├── DanhSachNhaCungCap.java
│       ├── DanhSachNhanVien.java
│       ├── LuuTruFile.java
│       └── FileText.java
├── tests/
│   └── DanhSachTest.java
├── data/                 (bốn file text; không đọc nội dung)
├── docs/
├── artifacts/
└── images/
```

## 3. Danh sách class và phương thức hiện có

Trong các bảng dưới đây, `get/set X` nghĩa là `getX(): T` và `setX(T): void`, với `T` là kiểu đã ghi ở cột thuộc tính. Mọi constructor liệt kê nhận các thuộc tính nêu trong dòng đó theo thứ tự trong source, trừ khi ghi khác. Không thấy `TODO`, `FIXME` hoặc `UnsupportedOperationException` trong source. Phần thiếu là class chưa có hoặc menu mới in thông báo.

### 3.1. Model

| Class / vai trò / loại | Thuộc tính, constructor và phương thức | Quan hệ và mức triển khai |
|---|---|---|
| `SanPham` — sản phẩm chung, **abstract** (`src/models/SanPham.java:3`) | `String maSanPham, tenSanPham, thuongHieu, maLoai`; `double giaban`; `int soLuongTon`. Constructor `(String,String,double,String,int,String)` dòng 11. `get/set` cả 6 trường; `hienThiThongTin(): String` **abstract** dòng 57; `toString(): String` dòng 59. | Cha của ba loại sản phẩm; `toString` override `Object.toString` nhưng không có annotation. Setter không kiểm giá trị, ví dụ `setSoLuongTon` dòng 46. |
| `GiayTheThao` — giày, **concrete** (`src/models/GiayTheThao.java:3`) | `double kichCoGiay`; `String mauSac, loaiDeGiay`. Constructor `(String,String,double,String,int,String,double,String,String)` dòng 8. `get/set` 3 trường riêng; `hienThiThongTin(): String` dòng 42. | `extends SanPham`, gọi `super(...)`, `@Override hienThiThongTin`; có logic hiển thị. |
| `DungCuTheThao` — dụng cụ, **concrete** (`src/models/DungCuTheThao.java:3`) | `String monTheThao, vatLieu`; `double khoiLuong`. Constructor 9 tham số dòng 7. `get/set` 3 trường riêng; `hienThiThongTin(): String` dòng 32. | `extends SanPham`, gọi `super(...)`, `@Override`; có logic hiển thị. |
| `QuanAoTheThao` — quần áo, **concrete** (`src/models/QuanAoTheThao.java:3`) | `String kichCoQuanAo`. Constructor 7 tham số dòng 5. `getKichCoQuanAo(): String`, `setKichCoQuanAo(String): void`, `hienThiThongTin(): String` dòng 18. | `extends SanPham`, gọi `super(...)`, `@Override`; có logic hiển thị. |
| `LoaiSanPham` — loại/danh mục, **concrete** (`src/models/LoaiSanPham.java:3`) | `String maLoai, tenLoai, moTa`; constructor `(String,String,String)` dòng 8; `get/set` cả 3. | Chỉ là model; không thấy chỗ khởi tạo, danh sách, file hay menu thật. |
| `KhachHang` — khách, **concrete** (`src/models/KhachHang.java:3`) | `String maKhachHang, hoTen, diaChi, soDienThoai`; constructor 4 tham số dòng 9; `get/set` cả 4; `thongTinKhachHang(): String` dòng 48. | Được `DanhSachKhachHang` quản lý. Model không tự xác thực setter. |
| `NhanVien` — nhân viên, **concrete** (`src/models/NhanVien.java:3`) | `String maNhanVien, hoTen, caLamViec, soDienThoai, diaChi`; constructor 5 tham số dòng 10; getter cả 5; setter cho mã, họ tên, ca, điện thoại; `thongTinNhanVien(): String` dòng 53. | Được `DanhSachNhanVien` quản lý. Không có `setDiaChi`; `thongTinNhanVien` không in ca, menu tự nối ca tại `src/services/DanhSachNhanVien.java:610-613`. |
| `NhaCungCap` — nhà cung cấp, **concrete** (`src/models/NhaCungCap.java:3`) | `String maNhaCungCap, tenNhaCungCap, diaChi, soDienThoai`; constructor 4 tham số dòng 9; `get/set` cả 4; `thongTinNhaCungCap(): String` dòng 44. | Được `DanhSachNhaCungCap` quản lý. Model có thể bị sửa qua tham chiếu danh sách trả về. |
| `HoaDon` — đầu chứng từ bán, **concrete** (`src/models/HoaDon.java:5`) | `String maHoaDon, maKhachHang, maNhanVien`; `Date ngayLapHoaDon`; `double tongTien`; `int soLuongMua`; constructor 6 tham số dòng 13; `get/set` cả 6. | Chưa chứa danh sách chi tiết, chưa tính tổng, trạng thái hoặc xác nhận. Chỉ có logic gán/đọc thuộc tính. |
| `ChiTietHoaDon` — dòng bán, **concrete** (`src/models/ChiTietHoaDon.java:3`) | `HoaDon hoaDon`; `SanPham sanPham`; `int soLuong`; `double donGiaBan, thanhTien`; constructor 5 tham số dòng 10; `get/set` cả 5. | Có tham chiếu đến hóa đơn và sản phẩm, nhưng chưa tính thành tiền, kiểm tồn hay được dùng trong luồng bán. |
| `PhieuNhap` — đầu chứng từ nhập, **concrete** (`src/models/PhieuNhap.java:5`) | `String maPhieuNhap, maNhanVien, maNhaCungCap`; `Date ngayLapPhieuNhap`; `double tongTien`; constructor 5 tham số dòng 12; `get/set` cả 5. | Chưa chứa chi tiết, trạng thái, tính tổng hay cập nhật kho. |
| `ChiTietPhieuNhap` — dòng nhập, **concrete** (`src/models/ChiTietPhieuNhap.java:3`) | `String maPhieuNhap`; `SanPham sanPham`; `double giaNhap, tongTien`; constructor 4 tham số dòng 9; `get/set` cả 4. | **Thiếu số lượng**; tổng tiền gán tay; chưa được dùng trong luồng nhập. |
| `DuLieu` — dữ liệu khởi tạo, **concrete** (`src/models/DuLieu.java:3`) | Không có thuộc tính và không khai báo constructor; Java cấp constructor mặc định. Bốn hàm **static**: `taoDanhSachSanPham(): SanPham[]` dòng 4, `taoDanhSachKhachHang(): KhachHang[]` dòng 25, `taoDanhSachNhanVien(): NhanVien[]` dòng 39, `taoDanhSachNhaCungCap(): NhaCungCap[]` dòng 64. | Tạo các model mẫu; `Main.main` gọi cả bốn. Không trích xuất dữ liệu mẫu ở đây. |

### 3.2. Danh sách, lưu trữ, điều phối và test

| Class / vai trò / loại | Thuộc tính, constructor và phương thức | Quan hệ và mức triển khai |
|---|---|---|
| `DanhSachSanPham` — danh sách sản phẩm, **concrete** (`src/services/DanhSachSanPham.java:9`) | `List<SanPham> danhSach`; hai hằng **static** tiêu đề file. Constructor `(SanPham[])` dòng 14. `getDanhSach(): List<SanPham>`, `them(SanPham): void`, `suaTheoMa(String,SanPham): boolean`, `xoaTheoMa(String): boolean`, `timTheoMa(String): SanPham`, `timGanDung(String): List<SanPham>`, `locTheoLoai(String): List<SanPham>`, `ghiFile(String): void`, `docFile(String): void` dòng 18-140. Private `chiSo(String): int`; **static** `rong(String[],int,int): boolean`, `kiemTra(SanPham): void`, `trong(String): boolean` dòng 142-178. | `implements LuuTruFile`; dùng ba subclass sản phẩm và `FileText`. CRUD/tìm/file có logic. `getDanhSach` sao chép List, không sao chép phần tử. |
| `DanhSachKhachHang` — danh sách khách, **concrete final** (`src/services/DanhSachKhachHang.java:10`) | `KhachHang[] danhSach`, `int soLuong`; ba hằng **static**. Constructor `()` và `(KhachHang[])` dòng 18-25. `getSoLuong(): int`, `getDanhSach(): KhachHang[]`, `nhapDanhSach(KhachHang[]): void`, `them(KhachHang): void`, `themNhieu(KhachHang[]): void`, `sua(String,String,String,String): boolean`, `xoa(String): boolean`, `timTheoMa(String): KhachHang`, `timKiem(String,String,String,String,boolean): KhachHang[]`, `thongKe(): String`, `ghiFile(String): void`, `docFile(String): void` dòng 28-254. **Static** `maHopLe(String): boolean`, `soDienThoaiHopLe(String): boolean`; private helpers `thongKeTheoNhom`, `chiSoTheoMa`, `kiemTraVaSaoChep`, `saoChep`, `khop` dòng 179-328. | `implements LuuTruFile`; dùng `KhachHang`, `FileText`. CRUD, thêm nhiều, tìm, thống kê, file đều có logic; có sao chép model khi đưa vào/lấy ra. |
| `DanhSachNhanVien` — danh sách và menu nhân viên, **concrete final** (`src/services/DanhSachNhanVien.java:11`) | `NhanVien[] danhSach`, `int soLuong`; hai hằng **static**. Constructor `()` và `(NhanVien[])` dòng 19-25. API danh sách: `getSoLuong`, `getDanhSach`, `nhapDanhSach`, `them`, `themNhieu`, `sua(String,String,String,String,String): boolean`, `xoa(String): boolean`, `timTheoMa(String): NhanVien`, `timKiem(String,String,String,String,String,boolean): NhanVien[]`, `thongKe(): String`, `ghiFile(String): void`, `docFile(String): void` dòng 28-256. `quanLy(Scanner,String): void` dòng 330; private `nhapTuBanPhim` (2 overload), `suaTuBanPhim`, `timTuBanPhim`, `luu` và các helper nhập; helper **static** `rong`, `saoChep`, `khop`, `hienThi`, `docDong`, `batBuoc`, `nhapSoLuong`, `nhapDienThoai` dòng 301-695. | `implements LuuTruFile`; dùng `NhanVien`, `DanhSachKhachHang` để tái dùng kiểm mã/điện thoại, `FileText`, `Scanner`. CRUD, tìm, thống kê, menu và file có logic. |
| `DanhSachNhaCungCap` — danh sách NCC, **concrete** (`src/services/DanhSachNhaCungCap.java:9`) | `List<NhaCungCap> danhSach`; hai hằng **static**. Constructor `(NhaCungCap[])` dòng 14. `getDanhSach(): List<NhaCungCap>`, `them(NhaCungCap): void`, `suaTheoMa(String,NhaCungCap): boolean`, `xoaTheoMa(String): boolean`, `timTheoMa(String): NhaCungCap`, `timGanDung(String): List<NhaCungCap>`, `ghiFile(String): void`, `docFile(String): void` dòng 18-88. Private `chiSo(String): int`; **static** `kiemTra(NhaCungCap): void` dòng 90-103. | `implements LuuTruFile`; dùng `NhaCungCap`, `FileText`, hàm kiểm của `DanhSachKhachHang`. CRUD/file có logic, nhưng trả tham chiếu model có thể sửa. |
| `LuuTruFile` — hợp đồng file, **interface** (`src/services/LuuTruFile.java:5`) | `docFile(String): void throws IOException`; `ghiFile(String): void throws IOException`. Không có thuộc tính hoặc constructor. | Bốn `DanhSach*` triển khai; `Main.taiHoacTao`/`tuLuu` gọi qua interface (`src/Main.java:57-69`). |
| `FileText` — tiện ích text, **concrete final, package-private** (`src/services/FileText.java:12`) | Không có thuộc tính; constructor **private** dòng 13. Các hàm **static** `ghi(String,List<String>): void throws IOException`, `doc(String,String...): List<String[]> throws IOException`, `dong(String...): String`; private **static** `giaiMa(String): String` dòng 15-79. | Dùng Java NIO và UTF-8. Ghi qua file tạm, kiểm tiêu đề và giải mã escape. Không phải class `LuuTruFileText` cùng tên sơ đồ. |
| `Main` — điều phối console, **concrete** (`src/Main.java:13`) | `Scanner sc` và bốn đường dẫn file là thuộc tính **static** dòng 14-18; constructor mặc định, không khai báo. Toàn bộ phương thức **static**: `main(String[]): void`, `taiHoacTao(LuuTruFile,String): void`, `tuLuu(LuuTruFile,String): void`, `xuLyMenu(DanhSachSanPham,DanhSachKhachHang,DanhSachNhanVien,DanhSachNhaCungCap): void`, `quanLyKhachHang(DanhSachKhachHang): void`, `quanLySanPham(DanhSachSanPham): void`, `quanLyNhaCungCap(DanhSachNhaCungCap): void`, `hienThiMenu(): void`; private `hienThiDanhSachKhachHang`, `nhapKhachHang`, `nhapNhieuKhachHang`, `suaKhachHang`, `xoaKhachHang`, `timKiemKhachHang`, `xuLyFile`, `docDong`, `nhapBatBuoc`, `nhapSoNguyen`, `nhapSoDienThoai`, `hienThiSanPham`, `nhapSanPham`, `nhapSoThuc`, `nhapNhaCungCap`, `hienThiNhaCungCap` dòng 135-775. | Dùng model, bốn danh sách và `LuuTruFile`. Menu danh mục/hóa đơn/phiếu nhập chỉ in thông báo tại dòng 101-122. |
| `DanhSachTest` — test tự chạy, **concrete** (`tests/DanhSachTest.java:8`) | Không có thuộc tính; constructor mặc định. Các hàm **static** `main(String[]): void throws IOException`, private `kiemTraLuuFile(): void`, `check(boolean): void`, `expectError(Runnable): void` dòng 9-112. | Test sản phẩm/NCC và một số vòng ghi/đọc file khách/nhân viên; không dùng JUnit. |

**Override, super, abstract, interface, đa hình:** `SanPham.hienThiThongTin` là abstract (`src/models/SanPham.java:57`); ba subclass dùng `super(...)` và `@Override` (`src/models/GiayTheThao.java:8,41`, `src/models/DungCuTheThao.java:7,31`, `src/models/QuanAoTheThao.java:5,17`). `Main.hienThiSanPham` gọi qua tham chiếu `SanPham` (`src/Main.java:611-615`). Bốn danh sách triển khai `LuuTruFile`, được gọi đa hình qua interface ở `Main.taiHoacTao` và `tuLuu` (`src/Main.java:57-69`). `DanhSachSanPham.ghiFile` phân nhánh bằng `instanceof` để ghi thuộc tính riêng (`src/services/DanhSachSanPham.java:69-95`).

## 4. Bảng CRUD

**Có** = có phương thức và đường gọi qua menu; **API** = có trong class nhưng menu không trình bày riêng; **Chưa có** = không thấy. CRUD chứng từ được xét ở mức đầu chứng từ và các dòng thuộc nó, không suy từ getter/setter model.

| Nhóm | Create | Read: xem / mã / gần đúng, lọc | Update | Delete | Thêm nhiều, kiểm tra, thống kê/sắp xếp |
|---|---|---|---|---|---|
| Sản phẩm | **Có** `DanhSachSanPham.them`, menu (`src/services/DanhSachSanPham.java:22`; `src/Main.java:569-572`) | **Có** xem, gần đúng mã/tên, lọc mã loại; tìm đúng mã có **API** `timTheoMa` (`src/services/DanhSachSanPham.java:18,46-67`) | **Có** `suaTheoMa` dòng 29 | **Có** `xoaTheoMa` dòng 39 | Kiểm mã trùng, dữ liệu âm/không hữu hạn ở `kiemTra` dòng 154-174. **Chưa có** thêm nhiều, sắp xếp, kiểm loại/chứng từ liên quan trước xóa. |
| Danh mục | **Chưa có** | **Chưa có** | **Chưa có** | **Chưa có** | Chỉ có model `LoaiSanPham` (`src/models/LoaiSanPham.java:3`); menu `src/Main.java:101-103` chỉ in thông báo. `maLoai` của sản phẩm mới kiểm hình thức, chưa kiểm tồn tại. |
| Khách hàng | **Có** `them` (`src/services/DanhSachKhachHang.java:59`) | **Có** xem, mã, tìm chính xác/gần đúng đa khóa (`src/services/DanhSachKhachHang.java:32,135,145`) | **Có** `sua` dòng 96 | **Có** `xoa` dòng 120 | **Có** nhập mới `n`, thêm `k`, kiểm mã trùng/điện thoại, thống kê địa chỉ/đầu số (`src/services/DanhSachKhachHang.java:43-263`; `src/Main.java:135-223`). **Chưa có** sắp xếp hoặc kiểm hóa đơn liên quan trước xóa. |
| Nhà cung cấp | **Có** `them` (`src/services/DanhSachNhaCungCap.java:22`) | **Có** xem, gần đúng mã/tên; tìm đúng mã có **API** `timTheoMa` (dòng 18,46,51) | **Có** `suaTheoMa` dòng 29 | **Có** `xoaTheoMa` dòng 39 | Kiểm mã trùng/tên/địa chỉ/điện thoại dòng 22-25,97-103. **Chưa có** thêm nhiều, lọc, thống kê, sắp xếp, kiểm phiếu nhập liên quan trước xóa. |
| Nhân viên | **Có** `them` (`src/services/DanhSachNhanVien.java:58`) | **Có** xem, mã, tìm chính xác/gần đúng đa khóa (dòng 32,126,136) | **Có** `sua` dòng 91 | **Có** `xoa` dòng 111 | **Có** nhập mới `n`, thêm `k`, kiểm mã trùng/dữ liệu, thống kê ca/địa chỉ (dòng 42-205,330-420). **Chưa có** sắp xếp hoặc kiểm chứng từ liên quan trước xóa. |
| Hóa đơn | **Chưa có CRUD** | **Chưa có** | **Chưa có** | **Chưa có** | `HoaDon` và `ChiTietHoaDon` chỉ có constructor/getter/setter (`src/models/HoaDon.java:5-69`; `src/models/ChiTietHoaDon.java:3-48`); menu `src/Main.java:117-119` là placeholder. |
| Phiếu nhập | **Chưa có CRUD** | **Chưa có** | **Chưa có** | **Chưa có** | `PhieuNhap` và `ChiTietPhieuNhap` chỉ có constructor/getter/setter (`src/models/PhieuNhap.java:5-51`; `src/models/ChiTietPhieuNhap.java:3-47`); menu `src/Main.java:121-123` là placeholder. |

Không thấy phương thức thêm/xóa **dòng chi tiết** cho hóa đơn hoặc phiếu nhập trong các model tương ứng (`src/models/HoaDon.java:5-69`, `src/models/PhieuNhap.java:5-51`).

## 5. Bảng kiểm tra nghiệp vụ

| Luồng | Trạng thái | Đã làm được / còn thiếu |
|---|---|---|
| Tạo danh mục → sản phẩm → danh sách → tìm/sửa/xóa | **Hoàn thành một phần** | Từ tạo sản phẩm trở đi có trong `DanhSachSanPham` và `Main.quanLySanPham` (`src/services/DanhSachSanPham.java:22-67`; `src/Main.java:559-609`). Chưa quản lý `LoaiSanPham` hoặc đối chiếu `maLoai` (`src/models/LoaiSanPham.java:3`; `src/services/DanhSachSanPham.java:154-159`). |
| Chọn NCC → phiếu nhập → chi tiết → tổng → xác nhận → tăng tồn | **Chưa có luồng** | Chỉ quản lý NCC (`src/Main.java:682-733`). `PhieuNhap`/`ChiTietPhieuNhap` chưa được tạo trong menu, không có số lượng chi tiết, tính tổng, xác nhận hay tăng kho (`src/models/PhieuNhap.java:5-51`; `src/models/ChiTietPhieuNhap.java:3-47`). |
| Chọn khách → hóa đơn → chi tiết → kiểm tồn → tổng → xác nhận → giảm tồn | **Chưa có luồng** | Chỉ quản lý khách (`src/Main.java:135-223`). `HoaDon`/`ChiTietHoaDon` không được tạo trong menu, chưa kiểm tồn, tính tổng, xác nhận hay giảm kho (`src/models/HoaDon.java:5-69`; `src/models/ChiTietHoaDon.java:3-48`). |
| Ghi file → đóng/mở → khôi phục dữ liệu và liên kết | **Hoàn thành một phần** | Bốn danh sách sản phẩm/khách/NCC/nhân viên có đọc/ghi UTF-8 qua `LuuTruFile` và `FileText`; `Main.taiHoacTao` đọc khi khởi động và không ghi đè file lỗi (`src/services/LuuTruFile.java:5`; `src/services/FileText.java:15-79`; `src/Main.java:34-60`). Test kiểm vòng ghi/đọc ở mức danh sách (`tests/DanhSachTest.java:34-102`). Chưa có file danh mục/chứng từ và phục hồi liên kết; chưa kiểm vòng đóng/mở ứng dụng thật. |
| Thống kê tồn kho/doanh thu/giá trị nhập/bán chạy | **Hoàn thành một phần** | Chỉ có thống kê khách theo địa chỉ/đầu số và nhân viên theo ca/địa chỉ (`src/services/DanhSachKhachHang.java:170-222`; `src/services/DanhSachNhanVien.java:159-206`). Bốn báo cáo cửa hàng được yêu cầu **chưa có**. |

### Lỗi/rủi ro nghiệp vụ cụ thể

1. `DanhSachSanPham.timTheoMa` trả chính model đang lưu; gọi `SanPham.setSoLuongTon(-1)` hoặc đổi mã qua setter sẽ vượt qua `DanhSachSanPham.kiemTra`, gây tồn âm hoặc mã trùng trong bộ nhớ (`src/services/DanhSachSanPham.java:46-49,154-174`; `src/models/SanPham.java:22,46`). `DanhSachNhaCungCap.timTheoMa` có rủi ro tương tự (`src/services/DanhSachNhaCungCap.java:46-48`; `src/models/NhaCungCap.java:29-41`).
2. Tổng tiền/thành tiền của chứng từ được gán trực tiếp, không tính từ chi tiết: `HoaDon.setTongTien`, `PhieuNhap.setTongTien`, `ChiTietHoaDon.setThanhTien`, `ChiTietPhieuNhap.setTongTien` (`src/models/HoaDon.java:62`; `src/models/PhieuNhap.java:48`; `src/models/ChiTietHoaDon.java:45`; `src/models/ChiTietPhieuNhap.java:44`). Một đầu chứng từ và dòng chi tiết có thể mang số tiền khác nhau.
3. Chưa có trạng thái/xác nhận ở `HoaDon` và `PhieuNhap`, cũng chưa có cập nhật kho (`src/models/HoaDon.java:5-69`; `src/models/PhieuNhap.java:5-51`; `src/models/SanPham.java:46-48`). Vì luồng chưa tồn tại, chưa thể kiểm chứng việc xác nhận hai lần hoặc sửa/xóa sau xác nhận; đây là quy tắc phải thêm khi triển khai.
4. Không thấy tập hợp chi tiết trong đầu chứng từ, nên chưa có chỗ kiểm sản phẩm trùng trong một chứng từ (`src/models/HoaDon.java:5-69`; `src/models/PhieuNhap.java:5-51`). Các hàm xóa khách/NCC/sản phẩm/nhân viên chưa kiểm chứng từ liên quan (`src/services/DanhSachKhachHang.java:120`; `src/services/DanhSachNhaCungCap.java:39`; `src/services/DanhSachSanPham.java:39`; `src/services/DanhSachNhanVien.java:111`).
5. `Main.main` chỉ khôi phục bốn danh sách độc lập; chưa có dữ liệu chứng từ để nối mã khách/NCC/nhân viên/sản phẩm sau đọc file (`src/Main.java:21-38`).

## 6. Bảng đối chiếu 10 tiêu chí OOP

| Tiêu chí | Trạng thái | Class/file chứng minh | Phần còn thiếu |
|---|---|---|---|
| 1. Ít nhất 15 class | **Đã hoàn thành** | 19 class sản phẩm trong `src/models/`, `src/services/`, `src/Main.java:13`; thêm `tests/DanhSachTest.java:8`. | Không thiếu về số lượng. |
| 2. Mỗi class có constructor | **Hoàn thành một phần** | Các model như `HoaDon` có constructor khai báo (`src/models/HoaDon.java:13`); `FileText` có constructor private (`src/services/FileText.java:13`). `Main` và `DuLieu` không khai báo, dùng constructor mặc định (`src/Main.java:13`; `src/models/DuLieu.java:3`). | Nếu yêu cầu **khai báo tường minh**, cần xử lý hai class này; nếu hiểu theo Java là mỗi class có constructor, constructor mặc định đáp ứng. Test class cũng dùng constructor mặc định. |
| 3. Kế thừa hợp lý | **Đã hoàn thành** | Ba subclass `extends SanPham`, gọi `super` (`src/models/GiayTheThao.java:3,11`; `src/models/DungCuTheThao.java:3,8`; `src/models/QuanAoTheThao.java:3,6`). | Không cần thêm kế thừa chỉ để tăng số lượng. |
| 4. Cài đặt và dùng đa hình | **Đã hoàn thành** | `SanPham.hienThiThongTin` và ba override; `Main.hienThiSanPham` gọi qua `SanPham` (`src/models/SanPham.java:57`; `src/Main.java:611-615`). `Main.taiHoacTao` gọi qua `LuuTruFile` (`src/Main.java:57-60`). | Nên có test minh họa rõ trong phần thuyết trình. |
| 5. Lớp quản lý mảng đối tượng | **Đã hoàn thành** | `KhachHang[]` và `NhanVien[]` (`src/services/DanhSachKhachHang.java:15`; `src/services/DanhSachNhanVien.java:16`). | Hai danh sách còn lại dùng `List`, không trái tiêu chí. |
| 6. Đọc/ghi file text | **Hoàn thành một phần** | Bốn danh sách triển khai `LuuTruFile`, dùng `FileText` (`src/services/LuuTruFile.java:5`; `src/services/FileText.java:15-79`). | Thiếu danh mục/chứng từ/liên kết; chưa test đóng/mở app đầy đủ. |
| 7. Static field và method | **Đã hoàn thành** | Đường dẫn và `Scanner` trong `Main` (`src/Main.java:14-18`); hàm tạo mảng trong `DuLieu` (`src/models/DuLieu.java:4`); `DanhSachKhachHang.maHopLe` (`src/services/DanhSachKhachHang.java:256`). | Không thiếu. |
| 8. Abstract class/method | **Đã hoàn thành** | `SanPham` và `hienThiThongTin` (`src/models/SanPham.java:3,57`). | Không thiếu. |
| 9. Interface và class triển khai | **Đã hoàn thành** | `LuuTruFile` và bốn danh sách, ví dụ `DanhSachSanPham` (`src/services/LuuTruFile.java:5`; `src/services/DanhSachSanPham.java:9`). | Interface khác tên `IQuanLyDanhSach` trên sơ đồ nhưng được dùng thật. |
| 10. Tính sáng tạo | **Chưa kiểm chứng theo thang chấm** | Tìm đa khóa, thêm hàng loạt có kiểm toàn bộ, thống kê (`src/services/DanhSachKhachHang.java:43-222`; `src/services/DanhSachNhanVien.java:74-206`); đọc định dạng file cũ (`src/services/DanhSachSanPham.java:97-140`). | Mức chấm do giảng viên quyết định; cần một luồng nghiệp vụ có thể demo và test. |

**`main()` chưa đạt giới hạn 10 dòng:** `src/Main.java:20-55` có **36 dòng vật lý** kể cả dòng trống/ngoặc, thân hàm có **28 dòng không trống**. Menu chính in bảy mục và thoát ở `src/Main.java:763-775`, nhưng mục danh mục, hóa đơn, phiếu nhập chỉ in thông báo ở `src/Main.java:101-123`.

## 7. So sánh code với Class Diagram tham chiếu

| Class thiết kế | Class thực tế | Mức độ khớp / sai khác | Đề xuất |
|---|---|---|---|
| `SanPham` | `src/models/SanPham.java:3` | **Khớp**: abstract và có abstract method. | Giữ; bảo vệ bất biến tồn kho khi nối nghiệp vụ. |
| `GiayTheThao` | `src/models/GiayTheThao.java:3` | **Khớp**. | Giữ. |
| `QuanAoTheThao` | `src/models/QuanAoTheThao.java:3` | **Khớp** nhóm sản phẩm; có `kichCoQuanAo`. | Giữ. |
| `DungCuTheThao` | `src/models/DungCuTheThao.java:3` | **Khớp**. | Giữ. |
| `DanhMuc` | `src/models/LoaiSanPham.java:3` | **Khớp một phần ý nghĩa, khác tên**; mới là model. | Chọn tên thống nhất; hoàn thiện danh sách/menu/file, đối chiếu `maLoai`. |
| `KhachHang` | `src/models/KhachHang.java:3` | **Khớp** model, có danh sách. | Giữ; kiểm tham chiếu hóa đơn về sau. |
| `NhanVien` | `src/models/NhanVien.java:3` | **Khớp** model, có thêm `caLamViec`. | Giữ và đồng bộ sơ đồ. |
| `NhaCungCap` | `src/models/NhaCungCap.java:3` | **Khớp** model, có danh sách. | Giữ; kiểm tham chiếu phiếu nhập. |
| `HoaDon` | `src/models/HoaDon.java:5` | **Một phần**: đầu chứng từ, thiếu chi tiết/trạng thái/tính tổng. | Hoàn thiện bán hàng. |
| `ChiTietHoaDon` | `src/models/ChiTietHoaDon.java:3` | **Một phần**: có số lượng/giá, thành tiền gán tay. | Quản lý qua `HoaDon`, tính tiền và kiểm số lượng. |
| `PhieuNhap` | `src/models/PhieuNhap.java:5` | **Một phần**: đầu chứng từ, thiếu chi tiết/trạng thái. | Hoàn thiện nhập hàng. |
| `ChiTietPhieuNhap` | `src/models/ChiTietPhieuNhap.java:3` | **Một phần**: thiếu số lượng. | Bổ sung số lượng, quản lý qua `PhieuNhap`. |
| `DanhSachSanPham` | `src/services/DanhSachSanPham.java:9` | **Khớp**; dùng `List<SanPham>` thay mảng. | Giữ, bảo vệ model khi trả ra. |
| `DanhSachKhachHang` | `src/services/DanhSachKhachHang.java:10` | **Khớp**; dùng mảng. | Giữ. |
| `DanhSachNhaCungCap` | `src/services/DanhSachNhaCungCap.java:9` | **Khớp**; dùng `List`. | Giữ, bảo vệ model khi trả ra. |
| `DanhSachHoaDon` | Không tìm thấy trong `src/` | **Chưa có**. | Tạo khi đã chốt model/ràng buộc xác nhận. |
| `DanhSachPhieuNhap` | Không tìm thấy trong `src/` | **Chưa có**. | Tạo khi đã chốt model/ràng buộc xác nhận. |
| `DichVuKho` | Không tìm thấy trong `src/` | **Chưa có**. | Đặt logic kho tại một nơi nhất quán khi làm nhập/bán. |
| `LuuTruFileText` | `src/services/FileText.java:12` và `LuuTruFile.java:5` | **Khớp một phần trách nhiệm, khác cấu trúc/tên**. | Có thể giữ code hiện tại; bổ sung lưu chứng từ và sửa sơ đồ cuối. |
| `BaoCaoThongKe` | Không có class; thống kê cục bộ ở `DanhSachKhachHang.java:170`, `DanhSachNhanVien.java:159` | **Một phần**. | Chỉ tạo lớp riêng nếu báo cáo cần dùng nhiều danh sách/chứng từ. |
| `IQuanLyDanhSach` | Không có; có `src/services/LuuTruFile.java:5` | **Khác trách nhiệm**: interface hiện tại dành cho file. | Không thêm interface chỉ để khớp tên; sửa sơ đồ hoặc xác định hợp đồng chung thực sự cần. |
| `TrangThaiHoaDon` | Không có; `src/models/HoaDon.java:5-69` không có trạng thái | **Chưa có**. | Chốt quy tắc nháp/xác nhận/hủy rồi định nghĩa. |
| `TrangThaiPhieuNhap` | Không có; `src/models/PhieuNhap.java:5-51` không có trạng thái | **Chưa có**. | Chốt quy tắc nháp/xác nhận/hủy rồi định nghĩa. |

Ngoài sơ đồ tham chiếu, code có `DanhSachNhanVien` (`src/services/DanhSachNhanVien.java:11`), `DuLieu` (`src/models/DuLieu.java:3`) và `Main` (`src/Main.java:13`). Class chi tiết có thể được quản lý qua chứng từ cha; class dịch vụ không cần CRUD riêng.

## 8. Danh sách lỗi, chức năng còn thiếu và khả năng chạy

### Các vấn đề ưu tiên

1. **Menu danh mục, hóa đơn, phiếu nhập chưa hoạt động:** chỉ in chuỗi trong `Main.xuLyMenu` (`src/Main.java:101-123`).
2. **`main()` vượt giới hạn môn học:** 36 dòng vật lý tại `src/Main.java:20-55`.
3. **Đối tượng sản phẩm/NCC có thể bị sửa ngoài đường kiểm tra:** `DanhSachSanPham.timTheoMa` và `DanhSachNhaCungCap.timTheoMa` trả tham chiếu đang lưu (`src/services/DanhSachSanPham.java:46-49`; `src/services/DanhSachNhaCungCap.java:46-48`); các model có setter công khai (`src/models/SanPham.java:22-54`; `src/models/NhaCungCap.java:29-41`).
4. **Chứng từ chưa đảm bảo tiền/kho:** `HoaDon`, `ChiTietHoaDon`, `PhieuNhap`, `ChiTietPhieuNhap` chỉ gán số tiền; không có trạng thái, tính tổng, xác nhận, cập nhật tồn (`src/models/HoaDon.java:5-69`; `src/models/ChiTietHoaDon.java:3-48`; `src/models/PhieuNhap.java:5-51`; `src/models/ChiTietPhieuNhap.java:3-47`).
5. **Lưu trữ chưa bao phủ giao dịch và quan hệ:** `Main.taiHoacTao` chỉ xử lý bốn danh sách độc lập (`src/Main.java:34-60`).

### Build/test đã chạy

| Lệnh | Kết quả |
|---|---|
| `java -version; javac -version` | JDK **24**. `run.ps1` chưa khai báo `--release` (`run.ps1:1-18`). |
| `javac -encoding UTF-8 -d <thư_mục_tạm> <toàn_bộ_src_và_tests>` | **Thành công**, exit code 0, không có lỗi biên dịch; class file nằm ngoài project. |
| `java -cp <thư_mục_tạm> DanhSachTest` | **Thành công**, in `DanhSachTest OK`; test ở `tests/DanhSachTest.java:9-112`. |
| Chạy `Main` và đóng/mở ứng dụng | **Chưa kiểm chứng** vì `Main.main` có thể ghi `data/` khi khởi động (`src/Main.java:34-60`), không phù hợp audit chỉ đọc. |
| Test nhập/bán/kho/doanh thu | **Chưa có** luồng và test tương ứng; `tests/DanhSachTest.java:9-112` chỉ kiểm danh sách/file hiện có. |

Trong lúc audit, `git status` cho thấy thay đổi ở một số ảnh trong `images/` (ảnh cũ bị xóa, ảnh tên mới xuất hiện). Audit không thao tác với ảnh hoặc source.

## 9. Lộ trình triển khai theo mức ưu tiên

Đây là **đề xuất**, chưa thực hiện.

| Bước | File cần sửa/tạo sau khi duyệt | Lý do, phụ thuộc | Điều kiện hoàn thành |
|---|---|---|---|
| 1. Sửa nền tảng hiện có | Sửa `src/Main.java:20-55`, `src/services/DanhSachSanPham.java:46`, `src/services/DanhSachNhaCungCap.java:46`; giữ model sản phẩm/NCC. | Đưa `main()` về ≤10 dòng và ngăn sửa model vượt kiểm tra, trước khi kho dựa vào dữ liệu này. Không phụ thuộc tính năng mới. | Build/test đạt; `main()` ≤10 dòng; không thể tạo mã trùng hoặc tồn âm bằng cách sửa đối tượng lấy từ danh sách. |
| 2. Hoàn thiện danh mục và CRUD còn thiếu | Hoàn thiện `src/models/LoaiSanPham.java`; tạo lớp danh sách/file danh mục; sửa `src/Main.java:101-103` và kiểm `maLoai` trong `src/services/DanhSachSanPham.java:154-159`. | Danh mục phải tồn tại trước khi dùng mã loại làm quan hệ thật. | Có thêm/sửa/xóa/tìm qua menu; từ chối mã loại không tồn tại và không xóa loại đang được dùng. |
| 3. Giữ và hoàn thiện các lớp danh sách/menu cơ sở | `src/services/DanhSachKhachHang.java`, `DanhSachNhanVien.java`, `DanhSachNhaCungCap.java`, `DanhSachSanPham.java`; `src/Main.java`. | CRUD bốn nhóm đã có, nên sửa/điền chỗ thiếu; thêm kiểm liên kết khi chứng từ xuất hiện. | Các thao tác yêu cầu dùng được qua menu, kiểm mã trùng/dữ liệu sai và có test đường quan trọng. |
| 4. Hoàn thiện nhập/bán và kho | Hoàn thiện `src/models/HoaDon.java`, `ChiTietHoaDon.java`, `PhieuNhap.java`, `ChiTietPhieuNhap.java`; tạo `DanhSachHoaDon`, `DanhSachPhieuNhap` và logic kho; nối `src/Main.java:117-123`. | Phụ thuộc sản phẩm/danh mục/khách/NCC/nhân viên hợp lệ. Chốt quy tắc nháp, xác nhận, hủy, sửa/xóa. | CRUD đầu chứng từ, chi tiết qua chứng từ cha; tiền tính từ dòng; nhập tăng tồn, bán kiểm và giảm tồn đúng một lần; chặn trùng sản phẩm trong chứng từ. |
| 5. Hoàn thiện file text | Dùng mẫu `src/services/LuuTruFile.java`/`FileText.java` cho danh sách mới, sửa `src/Main.java:34-38`. | Phụ thuộc cấu trúc chứng từ cuối. | Đóng/mở giữ danh mục, tồn, chứng từ/chi tiết và liên kết; file lỗi không ghi đè dữ liệu tốt. |
| 6. Kiểm thử OOP/nghiệp vụ và báo cáo | Mở rộng `tests/DanhSachTest.java`; tạo báo cáo cửa hàng khi chứng từ đã có. | Test tồn, tiền, xác nhận lặp, xóa dữ liệu được tham chiếu, đọc lại file; báo cáo doanh thu phụ thuộc chứng từ hợp lệ. | Build/test đạt, demo được 10 tiêu chí OOP cùng luồng nhập/bán và thống kê. |
| 7. Đồng bộ sơ đồ/tài liệu | `artifacts/`, `images/`, `README.md`. | Làm sau khi code cuối ổn định. | Sơ đồ đúng class/tên/quan hệ thực tế; hướng dẫn chạy khớp chương trình. |

## 10. File source quan trọng cần cung cấp để tiếp tục chỉnh sửa

Nếu tiếp tục trong **cùng workspace**, không cần tải lại. Nếu chuyển sang phiên khác, cung cấp:

| Mục đích | File |
|---|---|
| Điều phối/menu/build | `src/Main.java`, `run.ps1` |
| Sản phẩm/danh mục | `src/models/SanPham.java`, `GiayTheThao.java`, `DungCuTheThao.java`, `QuanAoTheThao.java`, `LoaiSanPham.java`; `src/services/DanhSachSanPham.java` |
| Khách/NCC/nhân viên | `src/models/KhachHang.java`, `NhanVien.java`, `NhaCungCap.java`; `src/services/DanhSachKhachHang.java`, `DanhSachNhanVien.java`, `DanhSachNhaCungCap.java` |
| Nhập/bán | `src/models/HoaDon.java`, `ChiTietHoaDon.java`, `PhieuNhap.java`, `ChiTietPhieuNhap.java` |
| Lưu trữ/test | `src/services/LuuTruFile.java`, `FileText.java`; `tests/DanhSachTest.java` |

**Kết luận:** Nền tảng OOP và CRUD bốn nhóm dữ liệu cơ sở đã có; source biên dịch và test hiện tại đạt. Đồ án chưa hoàn thành danh mục, nhập/bán hàng, lưu chứng từ và giới hạn `main()` 10 dòng.

**Chưa kiểm:** chạy app với dữ liệu thực và vòng đóng/mở; không đọc nội dung file `data/` hoặc file nhạy cảm. Rủi ro lớn nhất khi triển khai tiếp là sai tồn kho/tổng tiền nếu quy tắc xác nhận chứng từ không nhất quán.
