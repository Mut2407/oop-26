# Báo cáo hoàn thành Task 06.1–06.5

Thực hiện tiếp từ commit c705ff9 (Task 06.1 đã được duyệt). Đã đọc source hiện tại,
đối chiếu artifacts/Sport_Store.drawio.xml, trang **01 - Tổng quan (20 class)**.
Không sử dụng ZIP hoặc báo cáo audit cũ làm nguồn hiện trạng.

Cập nhật ngày 2026-11-06: chỉ giữ `run.ps1` để chạy ứng dụng theo yêu cầu người dùng.
Phần kiểm thử Task 06 dưới đây ghi nhận kết quả tại thời điểm nghiệm thu; xem mục
**Sửa cách khởi động Console** cho thay đổi sau nghiệm thu.

## Tiến độ từng task

| Mốc | Công việc | Build / hồi quy trước khi chuyển tiếp |
| --- | --- | --- |
| Kiểm tra 06.1 | Xác minh lại model và metadata đã có | Thành công; 10/10 chương trình kiểm thử |
| 06.2 | List khách hàng, interface/API UML, batch/search/stats, adapter mảng | Thành công; 11/11 |
| 06.3 | Giữ mảng nhân viên; CRUD, tìm kiếm tiếng Việt, đa khóa, thống kê, metadata | Thành công; 12/12 |
| 06.4 | 4 API lưu trữ UML, List có metadata, backup/chuyển định dạng, menu | Thành công; 14/14 |
| 06.5 | Đối chiếu XML/reflection; hồi quy, Console, lỗi file, đóng/mở lại | Thành công; **16/16**, 0 thất bại |

Trong rà soát 06.5, hoàn thiện cách ghi List nhân viên thông thường về file hiện có:
địa chỉ bị thiếu trong List được phục hồi từ chính file đích hợp lệ theo mã.
Không dùng cache toàn cục hay địa chỉ từ file khác. Đã chạy lại toàn bộ kiểm thử.

## Checklist — 9/9

- [x] Model KhachHang đúng UML — 06.1.
- [x] Model NhanVien đúng bốn thuộc tính UML — 06.1.
- [x] Bảo toàn diaChi nhân viên cũ — 06.1, 06.4.
- [x] DanhSachKhachHang dùng List và interface — 06.2.
- [x] CRUD, thêm nhiều và tìm đa khóa khách hàng — 06.2.
- [x] CRUD, tìm kiếm và thống kê nhân viên — 06.3.
- [x] Menu Console khách hàng và nhân viên hoạt động — 06.4.
- [x] Lưu/đọc file khách hàng và nhân viên đúng — 06.4.
- [x] Toàn bộ test và kiểm tra UML trong phạm vi Task 06 đạt — 06.5.

## Đối chiếu UML và code

| Thành phần | Kết quả |
| --- | --- |
| KhachHang | 4 thuộc tính String: mã, tên, điện thoại, địa chỉ; mã final; constructor đúng; capNhatThongTin và hienThiThongTin |
| NhanVien | Chỉ mã, tên, điện thoại, ca; mã final; constructor (ma, ten, sdt, ca); capNhatLienHe, doiCaLamViec, hienThiThongTin; không có diaChi |
| DanhSachKhachHang | List&lt;KhachHang&gt; ds; implements IQuanLyDanhSach&lt;KhachHang&gt;; đủ them, suaTheoMa, xoaTheoMa, timTheoMa, timGanDung, timTheoSoDienThoai, getTatCa |
| DanhSachNhanVien | Giữ NhanVien[] và số lượng thực, tự mở rộng; không tự thêm quan hệ vào UML |
| LuuTruFileText | ghiKhachHang(String, DanhSachKhachHang): void; docKhachHang(String): DanhSachKhachHang; ghiNhanVien(String, List&lt;NhanVien&gt;): void; docNhanVien(String): List&lt;NhanVien&gt; |
| Main.main() | 3 dòng, chỉ gọi hàm điều phối |

Task065UmlTest đọc đúng trang XML, đối chiếu tên/kiểu thuộc tính, chữ ký phương thức,
generic của List và interface bằng reflection. Đây là đối chiếu phạm vi Task 06,
không phải tuyên bố toàn bộ 20 class đã hoàn thành chức năng.

## Chức năng đã giữ và hoàn thiện

Khách hàng và nhân viên đều giữ xem, nhập n, thêm một/thêm k, sửa/xóa theo mã,
tìm chính xác/gần đúng/đa khóa, thống kê và đọc/ghi file. Nhập lô kiểm tra toàn bộ
trước khi thay đổi; lỗi hoặc EOF giữa lô giữ nguyên dữ liệu cũ.

Mã trùng không phân biệt hoa/thường và dữ liệu sai gây IllegalArgumentException,
nhất quán với các danh sách đã có. Sửa/xóa mã không tồn tại trả false; thêm khách hàng
thành công trả true. Mã giữ nguyên cả cách viết hoa/thường khi sửa.

Tên tiếng Việt hỗ trợ tìm không dấu, không phân biệt hoa/thường. API tìm số điện thoại
khách hàng khớp chính xác; tìm một phần điện thoại vẫn có qua tìm đa khóa.
Thống kê khách hàng giữ địa chỉ/đầu số; nhân viên có thống kê ca. Thống kê địa chỉ cũ
chỉ thực hiện khi metadata đầy đủ, nếu thiếu thì báo rõ.

Các API trả dữ liệu là bản sao: thay đổi trên kết quả tìm kiếm không sửa danh sách
nội bộ. Menu giữ số mục cũ 0–10, thêm mục 11 tìm mã, 12 tìm gần đúng; khách hàng có
thêm mục 13 tìm điện thoại. Nhập sai được báo lỗi, không làm thoát bất thường.
Xem/tìm/sửa hoặc xóa thất bại không tự lưu. Nhập n có thông báo thay thế danh sách.
Thêm/sửa nhân viên không hỏi địa chỉ; dữ liệu chuyển tiếp được hiển thị riêng.

Đã chạy hồi quy thêm/sửa/xóa sản phẩm và nhà cung cấp, thêm/sửa danh mục,
tìm/lọc/thống kê sản phẩm, mở/thoát cả năm menu bằng dữ liệu sao chép.

## Lưu trữ và bảo toàn địa chỉ

1. Khách hàng đọc KHACH_HANG_V1 và bảng UTF-8 4 cột. Chuyển V1 sang bảng có tạo .bak
   và so sánh backup với file gốc trước khi ghi.
2. Nhân viên đọc bảng cũ theo thứ tự mã, tên, ca, điện thoại, địa chỉ; bảng UML 4 cột
   mã, tên, điện thoại, ca; và bảng mới 5 cột mã, tên, điện thoại, ca, diaChiCu.
   Xác định thứ tự bằng header, không suy đoán từ giá trị.
3. Cột diaChiCu là metadata lưu trữ; model NhanVien vẫn chỉ có 4 thuộc tính.
   Giá trị cũ giữ nguyên cả tiếng Việt, khoảng trắng, Tab/xuống dòng/backslash.
4. FileText đọc header và dữ liệu cùng lúc, hỗ trợ UTF-8/BOM/escape; ghi qua file tạm,
   thay thế nguyên khối nếu hệ thống tệp hỗ trợ.
5. File lỗi, mã trùng, thiếu cột, điện thoại sai và escape lỗi bị từ chối.
   Danh sách tạm chỉ thay danh sách hiện tại sau khi đọc hoàn toàn thành công.
6. Trước ghi đè, kiểm tra file đích. Trước chuyển định dạng, tạo và đối chiếu .bak.
   Backup xung đột khiến việc ghi bị chặn; không sửa/xóa backup để vượt kiểm tra.
7. BanSaoNhanVien là List snapshot chứa metadata bên ngoài model. getTatCa()/
   docNhanVien() trả List này; tuDanhSach() khôi phục cả model và metadata.
   Lưu sang file mới hoặc lấy subList vẫn giữ địa chỉ đúng mã.
8. List thông thường ghi về file hiện có được bổ sung metadata từ chính file đích
   hợp lệ theo mã. Không lấy dữ liệu từ file khác hoặc địa chỉ mẫu. Nếu file không
   đọc/kiểm tra được thì chặn ghi.
9. Khi xuất sang đường dẫn mới, phải giữ List có metadata hoặc adapter đầy đủ.
   new ArrayList chỉ chứa model sẽ bỏ phần metadata bổ trợ; không dùng nó để xuất
   dữ liệu cũ sang file mới. Nhân viên mới không có địa chỉ cũ vẫn lưu được với ô
   metadata trống; không dùng chuỗi trống thay địa chỉ thật.
10. API DanhSachNhanVien.ghiFile cũ còn chặn ghi khi thiếu metadata. Menu hiện dùng
    LuuTruFileText. Xóa nhân viên xóa metadata cùng mã; sửa thông tin chuẩn giữ địa chỉ
    của mình và các nhân viên khác.

SHA-256 của 5 file data/*.txt và Class Diagram khớp trước/sau toàn bộ công việc.
Không tạo file trong data/ gốc. Tất cả kiểm thử ghi dữ liệu vào thư mục tạm.

## File sửa/tạo trong lượt 06.2–06.5

| File | Lý do |
| --- | --- |
| src/services/DanhSachKhachHang.java | List, interface/API UML, batch/search/stats/file, adapter |
| src/services/DanhSachNhanVien.java | Tìm kiếm/thống kê, List/metadata adapter, header cũ/mới, menu/file |
| src/services/BanSaoNhanVien.java | Mới: snapshot List giữ metadata ngoài model |
| src/services/LuuTruFileText.java | 4 API UML, metadata theo file/mã, backup/chuyển định dạng, bảo vệ ghi |
| src/services/FileText.java | Đọc bảng kèm header, UTF-8 BOM, generic đúng kiểu |
| src/Main.java | Kết nối lưu trữ/menu, tìm kiếm riêng, không tự lưu thao tác thất bại |
| tests/Task061ConsoleTest.java | Cập nhật input hồi quy do nhân viên không còn được hỏi địa chỉ |
| tests/Task062KhachHangTest.java | Mới: CRUD/List/interface, batch/search/stats, bản sao, file cũ |
| tests/Task063NhanVienTest.java | Mới: mảng, CRUD, đa khóa/thống kê, bản sao, metadata |
| tests/Task064LuuTruTest.java | Mới: định dạng, backup, BOM/escape, metadata và lỗi ghi |
| tests/Task064ConsoleTest.java | Mới: CRUD/menu, mở lại, hủy lô, kiểm tra timestamp không bị tự lưu |
| tests/Task065UmlTest.java | Mới: đối chiếu XML/reflection/API/generic/Main |
| tests/Task065RegressionTest.java | Mới: dữ liệu sao chép, menu cũ, lỗi đọc khi đang chạy/khởi động, dữ liệu rỗng |
| tests/ConsoleTestSupport.java | Mới: tiến trình Main, stdin, đợi menu, timeout, dọn thư mục tạm |
| run.sh và test.sh | Mới: chạy Console và build/test sạch trong Linux/cloud |
| README.md | Hướng dẫn hiện tại, menu, schema/metadata, lệnh kiểm thử |
| docs/BAO_CAO_TASK_06.md | Báo cáo này |

## Bằng chứng kiểm thử

Lệnh **bash test.sh** biên dịch mới toàn bộ source/test vào thư mục tạm, chạy từng
chương trình Test.java với assertions bật và giữ mã thoát của lệnh lỗi.
Kết quả: **16 đạt, 0 thất bại, không bỏ qua chương trình nào**.

| Chương trình | Kết quả |
| --- | --- |
| AnhXaDanhMucTest | Đạt |
| DanhMucTest | Đạt |
| DanhSachSanPhamTest | Đạt |
| DanhSachTest | Đạt |
| LuuTruFileTextTest | Đạt |
| NhaCungCapTest | Đạt |
| SanPhamTest | Đạt |
| KhachHangNhanVienModelTest | Đạt, 6 nhóm tình huống |
| NhanVienDiaChiCuTest | Đạt, 9 nhóm |
| Task061ConsoleTest | Đạt, 3 nhóm |
| Task062KhachHangTest | Đạt, 7 nhóm |
| Task063NhanVienTest | Đạt, 6 nhóm |
| Task064LuuTruTest | Đạt, 8 nhóm |
| Task064ConsoleTest | Đạt, 7 nhóm |
| Task065UmlTest | Đạt, 5 nhóm |
| Task065RegressionTest | Đạt, 6 nhóm |

Các nhóm tình huống phân loại phạm vi, không phải số test JUnit độc lập.
Không cộng số nhóm với số chương trình để tạo số lượng test.

Test Console chạy tiến trình Java thật: CRUD, tìm/hiển thị/thống kê, đọc/lưu,
đóng/mở lại, hủy giữa lô, nhập sai, không lưu khi thao tác thất bại. Test lỗi file
khi ứng dụng đang mở kiểm tra cả bộ nhớ/metadata được giữ và file lỗi không bị ghi đè.

run.sh đã chạy trên bản sao source và dữ liệu riêng: biên dịch, mở/xem menu
khách hàng/nhân viên và thoát thành công. git diff --check đạt.
Còn cảnh báo deprecated có sẵn trong DanhMucTest; không có lỗi build.
Môi trường: OpenJDK 21/Linux; chưa chạy trực tiếp Windows CMD/PowerShell.

## Sửa cách khởi động Console — 2026-11-06

Lỗi người dùng cung cấp là `Unresolved compilation problems` tại `Main.chay`,
báo thiếu `docKhachHang`, `docNhanVien`, `ghiKhachHang` và `DanhSachNhanVien.getTatCa`.
Source trên `main` tại commit `2d8a4bf` đã có đủ API và biên dịch sạch thành công.
Chưa truy cập được source/cache trên máy Windows của người dùng để xác định chính xác
đó là source cũ hay cache IDE. README hướng dẫn cập nhật đầy đủ source và làm sạch
Java Language Server Workspace.

| File | Thay đổi |
| --- | --- |
| run.ps1 | Luôn biên dịch toàn bộ source vào `out/console` sạch; dùng cùng JDK để biên dịch/chạy; có `-BuildOnly` và `-JavaHome`; dừng khi biên dịch lỗi; hỗ trợ đường dẫn có dấu cách/tiếng Việt |
| run.sh | Xóa theo yêu cầu chỉ giữ một script chạy ứng dụng |
| .vscode/tasks.json, .vscode/launch.json | Cấu hình **Sport Store - Console** biên dịch trước khi chạy, dùng `out/console`, terminal tương tác và thư mục làm việc của repo |
| .gitignore | Cho phép lưu hai cấu hình dùng chung, tiếp tục bỏ qua thiết lập IDE cá nhân |
| tests/ConsoleLauncherCheck.java | Kiểm tra PowerShell thật bằng tiến trình con, dữ liệu và source sao chép vào thư mục tạm |
| test.sh | Chạy thêm kiểm tra launcher khi có `pwsh`, thông báo bỏ qua riêng khi thiếu PowerShell |
| README.md | Cách chạy Windows/IDE, chọn JDK và xử lý cache Java |

Kết quả: **16/16 chương trình kiểm thử Java đạt**; kiểm tra launcher riêng đạt cả
**4 nhóm**: biên dịch với đường dẫn tiếng Việt, loại class cũ/mở đủ 5 menu, chặn chạy
khi cố ý làm thiếu API dù timestamp source cũ, và chạy lại sau khi khôi phục source.
Kiểm tra SHA-256 xác nhận dữ liệu gốc và Class Diagram không thay đổi.
Đã chạy launcher trên PowerShell 7.4.6/OpenJDK 21/Linux; chưa chạy trực tiếp Windows
PowerShell 5.1 hoặc giao diện IDE. Không sửa model, API UML hay nghiệp vụ Task 06.

## Adapter và chuẩn bị Task 07

- Khách hàng giữ constructor mảng, getDanhSach, nhapDanhSach/thêm nhiều dùng mảng,
  sua, xoa, timKiem và docFile/ghiFile. Tất cả dùng cùng nguồn List, không có mảng
  lưu chính song song.
- Nhân viên giữ API mảng, sửa cũ theo thứ tự (ma, ten, ca, sdt[, diaChiCu]),
  docFile/ghiFile, quanLy(Scanner, String). Constructor model UML vẫn là
  (ma, ten, sdt, ca).
- Hai adapter hiển thị model thongTinKhachHang/thongTinNhanVien từ 06.1 còn
  Deprecated. Getter bổ trợ phục vụ tìm kiếm/lưu trữ được giữ.
- Tra cứu theo mã và mã bất biến được kiểm thử sau đọc–ghi, đủ để bắt đầu Task 07.
  Khi đọc chứng từ, cần bảng mã → đối tượng chuẩn dùng chung trong phiên nạp;
  không dựa vào bằng nhau tham chiếu giữa các bản sao từ tìm kiếm.
- HoaDon hiện vẫn là source cũ với constructor dùng Date và mã dạng String.
  Chuẩn hóa constructor nhận KhachHang/NhanVien, lưu trữ hóa đơn, và chặn xóa
  đối tượng đã được hóa đơn tham chiếu thuộc Task 07, chưa triển khai.
- Hóa đơn, phiếu nhập và báo cáo doanh thu vẫn là thông báo chức năng giai đoạn sau.
  Không sửa Class Diagram để khớp code, không tuyên bố các chức năng đó đã chạy.

Không có blocker trong phạm vi Task 06 đã kiểm thử. Các thay đổi của lượt này nằm
trong workspace, không thay đổi/xuất bản cấu hình môi trường.
