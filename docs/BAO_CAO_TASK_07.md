# Task 07 — Hóa đơn và chi tiết hóa đơn

Ngày thực hiện: 07/11/2026. Nguồn thiết kế: artifacts/Sport_Store.drawio.xml,
trang **01 - Tổng quan (20 class)**; đã đọc hai DOCX yêu cầu/nghiệp vụ và source
hiện tại. Diagram, data/ và src/data/ được đối chiếu SHA-256 và giữ nguyên.
DOCX nghiệp vụ mô tả phiên bản 11 lớp cũ; cấu trúc/kiểu dữ liệu lấy theo UML chuẩn.

## Tiến độ và kiểm thử trước khi chuyển task

| Task | Kết quả | Toàn bộ chương trình Java đạt |
| --- | --- | --- |
| 07.0 | Khảo sát: model cũ Date/double, chưa có file hóa đơn; tra cứu trả bản sao | 16/16 |
| 07.1 | Chi tiết bất biến, BigDecimal, snapshot sản phẩm và giá | 17/17 |
| 07.2 | HoaDon/enum, dòng hàng, sinh mã, quyền package | 18/18 |
| 07.3 | Danh sách, tìm/lọc, doanh thu và bảo vệ truy vấn | 19/19 |
| 07.4 | Kho thật, toàn bộ hoặc không thay đổi, chặn xóa/thay nguồn tham chiếu | 20/20 |
| 07.5 | Lưu/đọc đúng UML, undo journal, lỗi ghi file thứ hai được phục hồi | 21/21 |
| 07.6 | Console tạo/sửa/tìm/lưu/xác nhận, đóng/mở lại | 22/22 |
| 07.7 | XML/reflection, dừng cưỡng bức JVM sau mỗi file, phục hồi khi mở lại | 24/24 |

Các con số là chương trình *Test.java, không phải số assertion hoặc test JUnit.
Lệnh `bash test.sh` build sạch toàn bộ source/test trước khi chạy. Lần cuối đạt
24/24, 0 thất bại. ConsoleLauncherCheck được chạy riêng qua PowerShell 7.4.6/Linux:
4 nhóm đạt (build và đường dẫn Unicode, loại class cũ/menu, chặn lỗi API, phục hồi).
Script suite mặc định báo SKIP phần PowerShell nếu pwsh không nằm trên PATH;
không tính phần bị bỏ qua đó thành chương trình Java đạt.
Còn cảnh báo deprecated có sẵn trong DanhMucTest.

## Đối chiếu UML

| Thành phần | Kết quả |
| --- | --- |
| ChiTietHoaDon | Đúng 3 thuộc tính SanPham/int/BigDecimal, constructor, getter và thanhTien; không có quan hệ ngược hoặc tổng tiền nhập tay |
| HoaDon | Đúng 7 thuộc tính gồm static soThuTuTiepTheo; LocalDate, quan hệ KH/NV, List chi tiết, enum |
| API HoaDon | Đủ constructor/getter/themChiTiet/xoaChiTiet/tinhTongTien/coTheChinhSua/taoMaTuDong |
| Chuyển trạng thái | danhDauDaXacNhan có quyền package; DichVuKho cùng package models, không nới thành public hoặc dùng reflection |
| TrangThaiHoaDon | Chỉ CHO_XAC_NHAN và DA_XAC_NHAN |
| DanhSachHoaDon | List<HoaDon>, đủ API thêm/tìm KH/lọc ngày/trạng thái/doanh thu/getTatCa; không ép implement interface CRUD |
| DichVuKho | Constructor và API bán hàng đúng UML; kiểm tra không thay đổi dữ liệu, xác nhận mới giảm kho thật |
| LuuTruFileText | ghiHoaDon(String,DanhSachHoaDon) và docHoaDon(String,DanhSachSanPham,DanhSachKhachHang,List<NhanVien>) đúng chữ ký |

Task077UmlHoaDonTest đọc các nhóm class ở trang 01 của XML và đối chiếu thuộc tính,
một tập chữ ký/kiểu trả về, generic, static, constructor và quyền package với reflection.
Task065UmlTest tiếp tục kiểm tra model/danh sách cũ và giới hạn Main.main <= 10 dòng.

## Quy tắc nghiệp vụ bổ sung cho phần tài liệu chưa chốt

- Bắt buộc khách hàng và nhân viên đã tồn tại, chưa có khách vãng lai.
- Một sản phẩm tối đa một dòng; sửa dòng là thay bằng chi tiết hợp lệ.
- Số lượng dương, giá không âm; giá 0 được chấp nhận theo DOCX.
- Draft có thể rỗng, xác nhận rỗng bị từ chối.
- Chỉ DA_XAC_NHAN tính doanh thu; khoảng ngày bao gồm hai đầu.
- Không có thuế/giảm giá/thanh toán/công nợ/trả hàng/hủy hoặc xóa hóa đơn đã xác nhận.
- Mã không phân biệt hoa/thường. Mã tự động dạng HD000001, bộ đếm đồng bộ từ mã
  HD + chữ số đã tạo/đọc; có thể có khoảng trống khi hủy nhập. Bộ đếm chạm giới hạn
  int thì dừng sinh tự động và báo rõ, không tràn số hoặc tái sử dụng mã.
- Các truy vấn trả snapshot; sửa draft phải qua suaHoaDonCho. Xác nhận của danh sách
  thao tác trên hóa đơn nội bộ, không xác nhận nhầm bản sao kết quả tìm kiếm.
- Khi khởi động, ba danh sách nguồn được ganHoaDon. Xóa, nhập mới và đọc file thay
  danh sách bị chặn nếu làm mất mã đang được bất kỳ hóa đơn lưu trong danh sách tham chiếu.
- Sản phẩm và KH/NV được khôi phục theo mã từ nguồn truyền vào; model lưu snapshot
  để bảo vệ dữ liệu. Không hứa giữ object identity giữa các kết quả truy vấn.

## File text và giao dịch

HOA_DON_V1 dùng Tab/escape của FileText:

- H: mã hóa đơn, ngày ISO, mã KH, mã NV, enum trạng thái, số dòng.
- D: mã SP, số lượng, đơn giá thập phân. Thành tiền/tổng tiền được tính lại.

Đọc kiểm tra header, số cột/số dòng, ngày, trạng thái, mã trùng, sản phẩm trùng,
số lượng/giá và toàn bộ tham chiếu. File lỗi không thay danh sách đang dùng.
Đọc hóa đơn đã xác nhận chỉ khôi phục trạng thái trong adapter TepHoaDon, không
thực thi DichVuKho và không thay tồn. Ghi riêng hóa đơn không tự cập nhật sản phẩm.
Ghi/đọc lại không cho làm mất hoặc đổi nội dung hóa đơn đã xác nhận đang có.
Không có file hóa đơn cũ thực tế trong checkout, nên không tuyên bố đã chuyển đổi
một định dạng cũ chưa tồn tại. Các schema KH/NV/SP Task 01–06 vẫn đọc được.

Luồng xác nhận Console sử dụng GiaoDichBanHang:

1. Kiểm tra nguồn và tạo bản sao kho/hóa đơn; xác nhận trên bản sao.
2. Ghi journal hai bản gốc dạng Base64 kèm SHA-256, flush và thay file an toàn.
3. Ghi sản phẩm, rồi hóa đơn, đọc xác thực cả hai.
4. Xóa journal là mốc commit, sau đó áp dụng hai bản sao vào bộ nhớ.
5. Lỗi ghi: phục hồi cả hai bản gốc, bộ nhớ chưa thay đổi. Nếu phục hồi thất bại,
   dừng ứng dụng thay vì cho phép tiếp tục ghi trên trạng thái chưa nhất quán.
6. Khởi động thấy journal: xác thực toàn bộ rồi rollback hai file trước khi đọc
   danh sách. Giữ journal cho đến khi phục hồi xong để có thể thử lại khi gián đoạn.

Kiểm thử thực tế dùng Runtime.halt trong JVM con sau khi ghi sản phẩm và sau khi
đã ghi hóa đơn. JVM Main mới phục hồi đúng byte của cả hai bản gốc, không trừ tồn
lặp. Journal hỏng chặn khởi động mà không ghi đè dữ liệu tốt. Đây là bảo đảm đã
kiểm chứng cho gián đoạn tiến trình; chưa mô phỏng mất điện vật lý/filesystem hỏng.
Ứng dụng phục vụ một phiên Console trên mỗi thư mục dữ liệu; chưa hỗ trợ nhiều
phiên đồng thời sửa dữ liệu hoặc công cụ ngoài sửa file khi ứng dụng đang mở.

## Console và chức năng giữ lại

Menu chính mục 6 mở quản lý hóa đơn. Có 11 mục con và quay lại: danh sách, lập chờ,
xem chi tiết, thêm/sửa/xóa dòng, xác nhận, tìm/lọc kết hợp, doanh thu, lưu và đọc lại.
Ngày yyyy-MM-dd; giá trống lấy giá hiện tại; /huy hoặc EOF hủy nhập dở.
Đọc lại hỏi nếu có draft chưa lưu. Chỉ xem/tìm không tự ghi; thất bại không tự lưu.
Tồn kho dùng dữ liệu nội bộ, không lấy tồn snapshot làm căn cứ cuối cùng.

Hồi quy giữ menu danh mục/sản phẩm/KH/NV/NCC, CRUD, nhập lô, tìm đa khóa, thống kê,
metadata địa chỉ NV, file cũ và cơ chế snapshot. Không thay mảng sản phẩm/NV,
không bỏ interface, kế thừa, đa hình, abstract hoặc constructor đã có.

## File sửa/tạo

- src/models/HoaDon.java, ChiTietHoaDon.java: chuẩn hóa model cũ; không có caller
  nghiệp vụ sử dụng constructor cũ nên không giữ adapter Date/double/setter tùy ý.
- src/models/TrangThaiHoaDon.java: enum UML.
- src/models/DichVuKho.java: nghiệp vụ kho, cùng package HoaDon.
- src/models/TepHoaDon.java: adapter file để khôi phục trạng thái đúng phạm vi package.
- src/services/DanhSachHoaDon.java: quản lý hóa đơn và snapshot.
- src/services/BaoVeThamChieu.java: bảo vệ mã theo nguồn hóa đơn, không dùng registry toàn cục.
- src/services/DanhSachSanPham.java: sao chép, batch giảm tồn thực và thay dữ liệu có kiểm tra.
- src/services/DanhSachKhachHang.java, DanhSachNhanVien.java: kiểm tra mất tham chiếu
  trước khi xóa/thay, giữ toàn bộ API cũ và metadata.
- src/services/LuuTruFileText.java: hai API hóa đơn UML.
- src/services/FileText.java: public utility dùng lại cho adapter, force file trước rename.
- src/services/GiaoDichBanHang.java: journal/rollback/commit và lỗi phục hồi bắt buộc dừng.
- src/services/MenuHoaDon.java và src/Main.java: tích hợp Console, phục hồi lúc startup,
  giữ cùng danh sách sản phẩm khi reload để không bỏ liên kết bảo vệ.
- tests/Task071ChiTietTest.java đến Task077UmlHoaDonTest.java: 8 chương trình mới;
  Task077CrashWorker.java là helper JVM con, không được tính thành test độc lập.
- README.md: hướng dẫn menu/file/quy tắc và kiểm thử hiện tại.
- docs/BAO_CAO_TASK_07.md: báo cáo này.

## Giới hạn và phần tiếp theo

- Chưa chạy trực tiếp Windows PowerShell 5.1 hoặc giao diện Antigravity IDE.
  PowerShell 7/Linux và tiến trình Java Console đã được chạy thực tế.
- Chưa triển khai PhieuNhap/ChiTietPhieuNhap/DanhSachPhieuNhap và báo cáo tổng hợp.
  Chữ ký DichVuKho.xacNhanNhapHang(PhieuNhap) có sẵn để thể hiện UML nhưng ném
  UnsupportedOperationException; không được nối vào menu và không tính là hoàn thành.
- Khoảng trống sinh mã được chấp nhận; không có yêu cầu số hóa đơn liên tục tuyệt đối.
- Chưa commit/push hoặc xuất bản môi trường trong lượt triển khai này.
