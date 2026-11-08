# Báo cáo Task 08.0–08.7 và 09.1–09.3

Ngày: 08/11/2026. Bắt đầu từ source commit 40c742f. Đã đối chiếu UML trang
**01 - Tổng quan (20 class)** và các yêu cầu/ngữ cảnh DOCX đã thống nhất.
Không chỉnh sửa UML/diagram, DOCX, data/ hoặc src/data/. Checksum SHA-256 các file
này được lưu trước triển khai và xác minh sau kiểm thử.

## Tiến độ theo task

| Task | Nội dung hoàn thành | Build và toàn bộ chương trình Java đạt |
| --- | --- | --- |
| 08.0 | Khảo sát model cũ, nguồn trả bản sao, journal bán hàng, nguồn tham chiếu | 24/24 |
| 08.1 | ChiTietPhieuNhap đúng UML, bất biến, giá nhập BigDecimal | 25/25 |
| 08.2 | PhieuNhap/enum, dòng hàng, static sinh mã, khóa sau nhập | 26/26 |
| 08.3 | DanhSachPhieuNhap, truy vấn snapshot, lọc và tổng giá trị nhập | 27/27 |
| 08.4 | Tăng tồn cả lô, chặn tràn số/xác nhận lặp, bảo vệ NCC/NV/SP | 28/28 |
| 08.5 | API file, adapter trạng thái, transaction nhập/bán dùng chung | 29/29 |
| 08.6 | Menu chính mục 7, lưu chờ, xác nhận và mở lại | 30/30 |
| 08.7 | Kiểm thử XML/reflection, JVM halt và phục hồi | 32/32 |
| 09.1 | BaoCaoThongKe đúng UML, tính toán bằng dữ liệu tính tay | 33/33 |
| 09.2 | Menu chính mục 8, dữ liệu sống và xử lý nhập sai | 34/34 |
| 09.3 | Nhập–bán–báo cáo–mở lại, đối chiếu UML và hồi quy | 36/36 |

Chỉ chuyển tiếp sau khi build/test bước trước đạt. Một số lỗi ở fixture/assertion
mới được phát hiện khi chạy test và được sửa trước khi ghi nhận task đạt.
Số lượng trên là chương trình *Test.java, không phải số assertion/JUnit case.

## Đối chiếu UML

- **ChiTietPhieuNhap:** đúng ba thuộc tính SanPham/int/BigDecimal; constructor và
  getSanPham/getSoLuong/getDonGiaNhap/thanhTienNhap. Không còn tổng tiền nhập tay,
  mã phiếu trong chi tiết hoặc setter tùy ý.
- **PhieuNhap:** đúng bảy thuộc tính, gồm static soThuTuTiepTheo; LocalDate ngayNhap,
  NhaCungCap, NhanVien, List chi tiết và TrangThaiPhieuNhap. Đủ constructor, getter,
  thêm/xóa chi tiết, tổng tiền, coTheChinhSua và static taoMaTuDong.
- **Trạng thái:** chỉ CHO_XAC_NHAN và DA_NHAP_KHO. danhDauDaNhapKho giữ quyền package;
  DichVuKho đã cùng package models nên không cần public setter hoặc reflection.
- **DanhSachPhieuNhap:** List<PhieuNhap>, đủ API thêm, tìm mã/NCC, lọc ngày/trạng thái,
  tổng giá trị nhập và getTatCa; không ép interface CRUD ngoài UML.
- **DichVuKho:** xacNhanNhapHang(PhieuNhap) đã thay placeholder bằng nghiệp vụ thật;
  xacNhanBanHang/kiemTraTonKho vẫn hoạt động và giữ chữ ký cũ.
- **LuuTruFileText:** ghiPhieuNhap(String,DanhSachPhieuNhap) và
  docPhieuNhap(String,DanhSachSanPham,DanhSachNhaCungCap,List<NhanVien>) đúng chữ ký.
- **BaoCaoThongKe:** đúng ba thuộc tính nguồn, constructor và năm API UML:
  doanhThuTheoKhoang, giaTriNhapTheoKhoang, sanPhamBanChay,
  canhBaoTonKho, giaTriTonKhoTheoGiaBan.
- Kiểm tra XML/reflection xác minh thuộc tính, generic, chữ ký, static và quyền
  truy cập; các test UML Task 06–07 vẫn đạt, gồm Main.main không quá 10 dòng.

## Quy tắc nhập hàng

NCC/NV/SP phải tồn tại. Sản phẩm mới tạo trước trong menu sản phẩm, sau đó chọn
trong phiếu nhập. Một mã SP tối đa một dòng; muốn thay số lượng/giá dùng sửa dòng.
Số lượng dương, giá nhập không âm; giá 0 được chấp nhận. Đơn giá nhập phải nhập rõ,
không lấy giá bán mặc định, không tự thay giá bán khi nhận hàng.

Phiếu chờ được lưu rỗng trong lúc soạn nhưng không được xác nhận rỗng. Xác nhận
kiểm tra toàn bộ tồn sau cộng trước khi tăng bất kỳ dòng nào. Một dòng tràn int,
sản phẩm thiếu hoặc phiếu đã nhập làm toàn bộ thao tác thất bại. Thành công tăng
đúng kho nội bộ, không phải snapshot, rồi khóa phiếu. Xác nhận lặp không tăng lại.

Mã tự động PN000001..., bộ đếm độc lập hóa đơn và đồng bộ khi tạo/đọc mã PN có số.
Không tái sử dụng mã; có thể có khoảng trống khi hủy nhập. Chạm giới hạn int thì
báo hết mã tự động. Mã trùng được so không phân biệt hoa/thường.

Nguồn truy vấn giữ snapshot. Sửa phiếu qua API kiểm soát; xác nhận tác động phiếu
nội bộ danh sách. BaoVeThamChieu giữ cả nguồn hóa đơn và phiếu nhập. NV/SP bị chặn
xóa/thay mất nếu được một trong hai loại chứng từ tham chiếu; NCC được bảo vệ bởi
phiếu nhập. Các nguồn được gắn khi khởi động menu; không dùng registry toàn cục.

## Lưu trữ và phục hồi

Schema PHIEU_NHAP_V1 dùng FileText UTF-8, Tab/escape:
- H: mã phiếu, ngày ISO, mã NCC, mã NV, trạng thái, số dòng.
- D: mã SP, số lượng, đơn giá nhập thập phân chính xác.

Đọc kiểm tra cấu trúc, số dòng/cột, mã trùng, trạng thái, số lượng/giá và tham chiếu.
Không tạo đối tượng giả để bù thiếu nguồn. Đọc DA_NHAP_KHO chỉ khôi phục trạng thái,
không thực thi nhập hàng. File sai không thay dữ liệu đang dùng; không ghi đè phiếu
đã nhập kho bằng nội dung khác. Không phát hiện file phiếu nhập cũ trong checkout,
nên không tuyên bố hỗ trợ một định dạng cũ chưa tồn tại. File KH/NV/SP/NCC cũ vẫn
được đọc theo các adapter đã có.

GiaoDichKho là bộ điều phối chung cho GiaoDichBanHang và GiaoDichNhapHang:
1. Cùng dùng banhang.lock và một journal banhang.undo, tránh hai luồng ghi cùng kho
   với hai cơ chế phục hồi không phối hợp.
2. Giữ header BAN_HANG_UNDO_V1 để tương thích; bản ghi thứ hai chỉ được phép là
   hoadon.txt hoặc phieunhap.txt, bản ghi đầu luôn sanpham.txt.
3. Kiểm tra giao dịch dở dang trước prepare/ghi mới. Phải khởi động lại phục hồi
   trước khi nhập hoặc bán tiếp, không cho rollback cũ ghi đè giao dịch mới.
4. Lưu hai bản gốc Base64/checksum, ghi dữ liệu mới trên bản sao, đọc kiểm tra,
   xóa journal làm mốc commit rồi cập nhật bộ nhớ.
5. Lỗi ghi phục hồi hai bản gốc. Phục hồi thất bại dừng ứng dụng và giữ journal.
6. Startup phục hồi trước khi đọc dữ liệu. Phiếu/chứng từ tồn tại nhưng thiếu file
   nguồn làm chặn khởi động thay vì tạo mẫu thay thế.

Journal bán hàng cũ và các bài kiểm tra crash Task 07 tiếp tục đạt. JVM con mới
được halt sau file sản phẩm và sau file phiếu; Main mới phục hồi đúng cả hai bản
gốc. Kiểm tra bán hàng bị chặn khi journal nhập còn dở, và bán được sau phục hồi.
Bảo đảm được kiểm thử cho gián đoạn tiến trình; chưa mô phỏng mất điện vật lý hoặc
filesystem hỏng. Phạm vi ứng dụng là một phiên Console trên mỗi thư mục dữ liệu;
chưa hỗ trợ nhiều phiên cùng sửa hoặc chỉnh file bằng công cụ ngoài khi đang chạy.

## Báo cáo và Console

Menu 7 có 11 mục nghiệp vụ và quay lại: danh sách, lập chờ, chi tiết, thêm/sửa/xóa
dòng, xác nhận, tìm/lọc kết hợp, giá trị nhập theo ngày, lưu và đọc lại. Có hủy/EOF,
nhắc dữ liệu chưa lưu và hỏi trước đọc lại khi có thay đổi. Xem/tìm không tự ghi.

Menu 8 có doanh thu, giá trị nhập, top bán chạy, cảnh báo tồn và giá trị tồn theo
giá bán. Chỉ tính chứng từ hoàn tất, khoảng ngày bao gồm hai đầu. Bán chạy theo số
lượng trên toàn bộ lịch sử hóa đơn xác nhận; hòa theo mã. Top 0 rỗng, top âm lỗi;
cảnh báo tồn <= ngưỡng không âm. Đếm bán dùng BigInteger để không tràn int.
Số đã bán hiển thị qua API bổ trợ, không nhét vào soLuongTon của SanPham.
Giá trị tồn dùng giá bán hiện tại, không phải giá vốn hoặc lợi nhuận.
Các danh sách được giữ cùng instance khi reload để menu/báo cáo không trỏ nguồn cũ.

Kiểm thử đầu-cuối: tồn 10, nhập 5 giá 2, bán 3 giá 4, tồn cuối 12; giá trị nhập 10,
doanh thu 12, top bán 3. Mở lại/đọc lại giữ tồn 12 và hai trạng thái hoàn tất.
Giá bán tăng 1 làm giá trị tồn tăng 12 nhưng không đổi tiền nhập/bán đã ghi nhận.
Kiểm tra đọc/xem báo cáo không đổi byte các file dữ liệu.

## File sửa/tạo

| Nhóm | File |
| --- | --- |
| Model | PhieuNhap.java, ChiTietPhieuNhap.java, TrangThaiPhieuNhap.java, TepPhieuNhap.java, DichVuKho.java |
| Danh sách/bảo vệ | DanhSachPhieuNhap.java, BaoVeThamChieu.java, DanhSachSanPham.java, DanhSachNhanVien.java, DanhSachNhaCungCap.java |
| Lưu trữ/giao dịch | LuuTruFileText.java, GiaoDichKho.java, GiaoDichBanHang.java, GiaoDichNhapHang.java |
| Menu/báo cáo | Main.java, MenuPhieuNhap.java, MenuBaoCao.java, BaoCaoThongKe.java, MenuHoaDon.java |
| Kiểm thử | Task081–086, Task087RecoveryTest/Task087UmlNhapTest, Task091–093; Task087CrashWorker là helper JVM con |
| Tài liệu | README.md và báo cáo Markdown này |

## Kết quả và giới hạn

- Toàn bộ 36/36 chương trình Java đạt, 0 thất bại, gồm 24 chương trình hồi quy cũ
  và 12 chương trình mới. Tất cả ghi dữ liệu trong thư mục tạm.
- ConsoleLauncherCheck được chạy riêng qua PowerShell 7.4.6/Linux: 4 nhóm đạt.
  test.sh mặc định báo SKIP phần launcher khi pwsh không nằm trên PATH; không
  cộng phần bỏ qua đó vào số chương trình Java đạt.
- Các menu cũ, metadata địa chỉ nhân viên, mảng sản phẩm/NV và các API cũ giữ lại.
- Chưa chạy trực tiếp Windows PowerShell 5.1 hoặc giao diện Antigravity IDE.
- Còn cảnh báo deprecated có sẵn trong DanhMucTest.
- Không triển khai trả hàng/hủy chứng từ/công nợ/lợi nhuận hoặc Task 10 ngoài phạm vi.
- Checklist phần còn lại hiện 11/12: Task 08.0–09.3 đạt; Task 10 nghiệm thu toàn đồ án
  theo mọi tiêu chí môn học và chuẩn bị demo vẫn là công việc tiếp theo.
- Phần triển khai được chia thành hai commit riêng cho Task 08 và Task 09 theo yêu cầu;
  không xuất bản môi trường.
