# Báo cáo Task 06.1 — Model khách hàng và nhân viên

Phạm vi hoàn thành: Task 06.1. Đối chiếu trực tiếp source trong workspace tại commit nền `c9653f8` và `artifacts/Sport_Store.drawio.xml`, trang `01 - Tổng quan (20 class)`. Không sử dụng ZIP hay báo cáo audit làm nguồn hiện trạng. Kiểm tra GitHub trước khi commit xác nhận `main` vẫn ở `c9653f882853a0583d22aa27e6c6299f833d6a94`.

## Đồng bộ UML

| Model | Thuộc tính | Constructor và API |
| --- | --- | --- |
| `KhachHang` | `maKhachHang`, `hoTen`, `soDienThoai`, `diaChi`, đều `String`; mã `private final` | `KhachHang(ma, ten, sdt, diaChi)`, `getMaKhachHang()`, `getHoTen()`, `capNhatThongTin(ten, sdt, diaChi)`, `hienThiThongTin()` |
| `NhanVien` | Chỉ `maNhanVien`, `hoTen`, `soDienThoai`, `caLamViec`, đều `String`; mã và tên `private final` | `NhanVien(ma, ten, sdt, ca)`, `getMaNhanVien()`, `getHoTen()`, `capNhatLienHe(sdt)`, `doiCaLamViec(caMoi)`, `hienThiThongTin()` |

Mã được trim và chỉ nhận chữ Latin, số, `_`, `-`, nhất quán với quy tắc danh sách hiện có. Họ tên, địa chỉ khách hàng và ca làm việc không được null hoặc trắng. Số điện thoại có 9–15 chữ số, cho phép dấu `+` đầu, giữ số `0` đầu. Không áp đặt tập ca cố định chưa được thiết kế quy định.

Đã bỏ các setter không xác thực và setter mã. `capNhatThongTin()` kiểm tra toàn bộ trường trước khi cập nhật, nên dữ liệu sai không gây cập nhật một phần. Sửa tên nhân viên bằng cách tạo đối tượng hợp lệ thay thế, giữ nguyên mã. Kiểm tra trùng mã vẫn nằm ở danh sách; hai đối tượng model độc lập có thể mang cùng mã.

Getter điện thoại, địa chỉ khách hàng và ca nhân viên được giữ để phục vụ file/tìm kiếm. Hai tên hàm hiển thị cũ `thongTinKhachHang()` và `thongTinNhanVien()` còn là adapter `@Deprecated`, gọi `hienThiThongTin()`.

## Địa chỉ nhân viên cũ

Định dạng hiện tại là bảng UTF-8 có 5 cột, theo thứ tự:

```text
maNhanVien    hoTen    caLamViec    soDienThoai    diaChi
```

Dấu phân cách thực tế là Tab; `FileText` xử lý escape và ghi qua file tạm. Đã kiểm tra các nơi dùng địa chỉ: constructor mẫu trong `DuLieu`, sao chép/thêm/sửa/nhập nhiều, tìm đa khóa, thống kê, hiển thị, đọc/ghi file và menu trong `DanhSachNhanVien`, cùng các fixture ở `DanhSachTest`.

- `NhanVien` không còn thuộc tính, getter hoặc constructor 5 tham số chứa địa chỉ.
- `DanhSachNhanVien` quản lý `Map<String, String> diaChiCuTheoMa` riêng cho từng danh sách, khóa mã trim + viết hoa với `Locale.ROOT`. Không dùng registry static để tránh trộn địa chỉ giữa hai nguồn dữ liệu có cùng mã.
- Khi đọc file, cột 3 là ca và cột 4 là điện thoại được ánh xạ đúng sang constructor UML mới. Cột 5 được giữ nguyên giá trị, kể cả khoảng trắng, tiếng Việt và ký tự đã escape.
- Đọc/nhập lô dùng danh sách tạm; chỉ thay cả model và metadata sau khi kiểm tra thành công. Sửa thông tin chuẩn giữ địa chỉ; xóa nhân viên xóa metadata cùng mã; bản ghi nhân viên khác giữ nguyên metadata.
- Khi ghi, cột địa chỉ vẫn tồn tại. Nếu file đích đang có địa chỉ thật nhưng danh sách chuẩn bị ghi thiếu metadata cho cùng mã, ném `IOException` và giữ nguyên file. File đích lỗi định dạng/dữ liệu cũng bị chặn ghi.
- Ô địa chỉ trống chỉ biểu thị bản ghi chưa có địa chỉ chuyển tiếp, chẳng hạn nhân viên mới tạo bằng model UML. Không dùng giá trị đó thay địa chỉ cũ. Thống kê địa chỉ báo rõ nếu metadata không đầy đủ.
- Sao chép đầy đủ bằng `new DanhSachNhanVien(nguon)` hoặc constructor `(NhanVien[], Map<String, String>)`. `getDanhSach()` chỉ trả các bản sao model; khi chuyển chúng sang một danh sách/file khác phải chuyển cả `getDiaChiCuTheoMa()`. Map trả ra là snapshot không sửa được.
- Giữ adapter `them(nv, diaChiCu)` và `sua(ma, ten, ca, sdt, diaChiCu)`. API sửa 4 tham số của danh sách giữ thứ tự cũ `ma, ten, ca, sdt`, khác thứ tự constructor model UML `ma, ten, sdt, ca`.

Địa chỉ mẫu của 5 nhân viên được chuyển nguyên giá trị sang `DuLieu.taoDiaChiNhanVienCu()`. `Main` truyền cả mẫu model và metadata khi khởi tạo. Khi file đã tồn tại, đọc file thay thế cả hai từ file, không điền địa chỉ mẫu vào bản ghi thiếu thông tin.

## File thay đổi và caller

| File | Lý do |
| --- | --- |
| `src/models/KhachHang.java` | Xác thực, mã bất biến, API cập nhật/hiển thị UML, bỏ setter cũ |
| `src/models/NhanVien.java` | Model 4 thuộc tính, constructor đúng thứ tự, API điện thoại/ca, bỏ địa chỉ và setter cũ |
| `src/models/DuLieu.java` | Cập nhật constructor nhân viên, tách địa chỉ mẫu thành metadata |
| `src/services/DanhSachNhanVien.java` | Adapter model mới, metadata theo mã, bảo vệ đọc/ghi và các thao tác đang có |
| `src/Main.java` | Nạp metadata mẫu và gọi API hiển thị khách hàng mới |
| `tests/DanhSachTest.java` | Cập nhật fixture constructor nhân viên, thêm kiểm tra địa chỉ sau đọc/ghi |
| `tests/KhachHangNhanVienModelTest.java` | Mới: 6 nhóm kiểm thử model/UML |
| `tests/NhanVienDiaChiCuTest.java` | Mới: 9 nhóm kiểm thử bảo toàn địa chỉ và xử lý lỗi |
| `tests/Task061ConsoleTest.java` | Mới: 3 nhóm kiểm thử Console bằng tiến trình Java riêng |
| `docs/BAO_CAO_TASK_06_1.md` | Báo cáo và hướng dẫn kiểm chứng |

Đã kiểm tra `DanhSachKhachHang`, `LuuTruFileText`, `FileText`, `LuuTruFile`, `HoaDon` và `PhieuNhap`. Không có caller ở các lớp này cần đổi constructor nhân viên. `DanhSachKhachHang` vẫn dùng mảng; Task 06.2 chưa thực hiện. Việc bổ sung các API khách hàng/nhân viên vào `LuuTruFileText` vẫn thuộc Task 06.4. Mảng và menu số hiện có của nhân viên được giữ, phần hiển thị/nhập địa chỉ ghi rõ là dữ liệu chuyển tiếp.

## Build và kiểm thử thực tế

Môi trường OpenJDK 21 có module `jdk.compiler`, không có executable `javac`. Các lệnh đã chạy tại `/workspace/oop-26`:

```bash
set -e
mkdir -p out
sources=$(mktemp /tmp/oop26-sources.XXXXXX)
trap 'rm -f "$sources"' EXIT
find src tests -name '*.java' -print > "$sources"
java -m jdk.compiler/com.sun.tools.javac.Main -encoding UTF-8 -d out @"$sources"
for test in tests/*Test.java; do
    java -ea -cp out "$(basename "$test" .java)"
done
git diff --check
```

Kết quả: build toàn bộ source/test thành công; **10/10 chương trình kiểm thử đạt, 0 thất bại**. Trước khi sửa, 7/7 chương trình kiểm thử cũ cũng đạt. Ba chương trình mới kiểm tra 18 nhóm tình huống; không cộng chúng với số chương trình để tạo số lượng test giả.

| Chương trình | Kết quả |
| --- | --- |
| `AnhXaDanhMucTest` | Đạt |
| `DanhMucTest` | Đạt |
| `DanhSachSanPhamTest` | Đạt |
| `DanhSachTest` | Đạt |
| `LuuTruFileTextTest` | Đạt |
| `NhaCungCapTest` | Đạt |
| `SanPhamTest` | Đạt |
| `KhachHangNhanVienModelTest` | Đạt, 6 nhóm |
| `NhanVienDiaChiCuTest` | Đạt, 9 nhóm |
| `Task061ConsoleTest` | Đạt, 3 nhóm |

Kiểm thử thực tế gồm constructor đúng thứ tự, cấu trúc bốn thuộc tính và mã final, dữ liệu sai, cập nhật nguyên khối, hiển thị, địa chỉ có tiếng Việt/escape/khoảng trắng, đọc–ghi nguyên vẹn, sửa/thêm/xóa/nhập lô, snapshot độc lập, thiếu metadata và file lỗi. Test Console tạo các tiến trình `Main` mới, sửa nhân viên cũ, lưu, thoát, mở lại, thêm nhân viên mới không có địa chỉ, kiểm tra mở/quay lại các menu và từ chối khởi động với file nhân viên hỏng.

Tất cả kiểm thử ghi file dùng thư mục tạm. Hash SHA-256 của cả 5 file gốc trong `data/` và Class Diagram khớp trước/sau; không có file mới sinh trong `data/`. `git diff --check` đạt. Có hai nhóm cảnh báo build sẵn có: deprecated API ở `DanhMucTest`, unchecked ở `FileText`.

## Điểm chuyển tiếp

Task 06.1 đủ điều kiện hoàn thành trong workspace. Chưa thực hiện Task 06.2–06.5 hoặc Task 07. Class Diagram và cấu hình môi trường giữ nguyên.

Task 06.3–06.4 cần tiếp tục dùng metadata khi đưa danh sách model sang API `List<NhanVien>` và lưu sang đường dẫn mới; không xem bản sao model là bản sao đầy đủ dữ liệu chuyển tiếp. File nhân viên vẫn là bảng 5 cột hiện tại, chưa chuyển định dạng hay thêm cơ chế `.bak`. Menu ở giai đoạn này còn ô nhập địa chỉ chuyển tiếp tùy chọn; tổ chức menu cuối cùng và API `LuuTruFileText` sẽ thực hiện theo task tương ứng.

Console được kiểm thử tự động trên Linux/OpenJDK 21 với stdin/stdout. Chưa chạy trực tiếp Windows CMD/PowerShell; không coi script `run.ps1` là đã được kiểm thử trên Windows.
