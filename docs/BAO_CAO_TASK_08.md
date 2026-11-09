# Task 08.0–08.7 — Phiếu nhập

Đã triển khai trên nền commit 40c742f, theo UML trang 01 và các quy tắc nghiệp vụ
đã thống nhất. UML/diagram, DOCX và dữ liệu gốc được giữ nguyên.

| Task | Phần hoàn thành | Toàn bộ chương trình Java đạt |
| --- | --- | --- |
| 08.0 | Khảo sát nguồn, API và cơ chế journal bán hàng | 24/24 |
| 08.1 | Chi tiết nhập BigDecimal, số lượng dương, snapshot | 25/25 |
| 08.2 | Phiếu nhập, enum, mã tự động, khóa chỉnh sửa | 26/26 |
| 08.3 | Danh sách, tìm/lọc, tổng giá trị nhập đã xác nhận | 27/27 |
| 08.4 | Tăng tồn cả lô, chặn tràn số/lặp, bảo vệ tham chiếu | 28/28 |
| 08.5 | File text, API UML, giao dịch nhập/bán dùng chung | 29/29 |
| 08.6 | Menu chính mục 7 và các thao tác chứng từ | 30/30 |
| 08.7 | XML/reflection, dừng JVM giữa ghi và phục hồi | 32/32 |

PhieuNhap dùng LocalDate, quan hệ NCC/NV, List chi tiết và hai trạng thái
CHO_XAC_NHAN/DA_NHAP_KHO. danhDauDaNhapKho giữ quyền package. ChiTietPhieuNhap
có đúng ba thuộc tính SanPham/int/BigDecimal. Các API danh sách và đọc/ghi
LuuTruFileText giữ chữ ký UML. Đơn giá nhập được nhập rõ, không lấy giá bán
mặc định và không đổi giá bán khi nhập kho.

Nguồn NCC/NV/SP được bảo vệ khi xóa hoặc thay danh sách làm mất mã tham chiếu.
Bảo vệ hóa đơn cũ tiếp tục hoạt động đồng thời. Phiếu chờ chưa tăng tồn;
xác nhận rỗng, tràn số hoặc lặp bị từ chối. Xem/tìm không tự ghi file.

Schema PHIEU_NHAP_V1 lưu H (mã/ngày/NCC/NV/trạng thái/số dòng) và D
(mã SP/số lượng/đơn giá). Đọc phiếu chỉ khôi phục trạng thái, không tăng tồn.
Không có dữ liệu phiếu cũ thực tế để chuyển đổi trong checkout này.

GiaoDichKho dùng chung banhang.lock/banhang.undo cho bán và nhập. Giữ tương thích
journal bán cũ. Giao dịch mới bị chặn khi còn journal cần phục hồi; startup
rollback bản gốc hai file trước khi đọc dữ liệu. Test JVM halt sau từng lần ghi
xác minh phục hồi, bao gồm chặn bán khi journal nhập còn dở và bán sau phục hồi.
Phạm vi một phiên Console trên mỗi thư mục, chưa kiểm thử mất điện vật lý.

Các file chính: PhieuNhap, ChiTietPhieuNhap, TrangThaiPhieuNhap, TepPhieuNhap,
DanhSachPhieuNhap, DichVuKho, GiaoDichKho, GiaoDichNhapHang, GiaoDichBanHang,
LuuTruFileText, BaoVeThamChieu, các danh sách nguồn, MenuPhieuNhap và Main.
Test mới: Task081–086, Task087RecoveryTest, Task087UmlNhapTest;
Task087CrashWorker là helper, không tính thành test độc lập.

Build/test dùng thư mục tạm và dữ liệu sao chép. Các test cũ Task 01–07 tiếp tục
đạt. Chưa chạy trực tiếp Windows PowerShell 5.1 hoặc giao diện Antigravity IDE.
Báo cáo tổng hợp và menu chính mục 8 thuộc commit Task 09 tiếp theo.
