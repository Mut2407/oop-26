# Quản lý cửa hàng dụng cụ thể thao

Ứng dụng Java thuần, chạy trên CMD/Console. Nguồn thiết kế chuẩn là
[Sport_Store.drawio.xml](artifacts/Sport_Store.drawio.xml), trang **01 - Tổng quan (20 class)**.

Task 06.1–06.5 đã hoàn thành cho khách hàng và nhân viên. Xem
[báo cáo Task 06](docs/BAO_CAO_TASK_06.md) để biết đối chiếu UML, tiến độ từng task,
kết quả kiểm thử và các việc cần làm ở Task 07.

## Chạy ứng dụng

Dùng JDK 21. Chạy từ thư mục repository để ứng dụng tìm đúng thư mục dữ liệu.

Windows CMD/PowerShell:

~~~powershell
powershell -ExecutionPolicy Bypass -File .\run.ps1
~~~

Linux/cloud:

~~~bash
bash run.sh
~~~

Script Linux gọi module trình biên dịch của JDK, dùng được cả trong môi trường chỉ
có lệnh java nhưng vẫn có module jdk.compiler. Không cần database, server hoặc thư viện ngoài.

## Menu

Menu chính: **1** sản phẩm, **2** danh mục, **3** khách hàng, **4** nhân viên,
**5** nhà cung cấp; **0** thoát. Số mục cũ được giữ để tiếp tục dùng các thao tác quen thuộc.

| Mục | Khách hàng | Nhân viên |
| --- | --- | --- |
| 1 | Xem danh sách | Xem danh sách |
| 2 | Thêm một | Thêm một |
| 3 | Sửa theo mã | Sửa tên/điện thoại/ca, giữ mã |
| 4 | Xóa theo mã | Xóa theo mã |
| 5 | Nhập n, có thông báo thay danh sách cũ | Nhập n, có thông báo thay danh sách cũ |
| 6 | Thêm k | Thêm k |
| 7 | Tìm nhiều tiêu chí, chính xác hoặc chứa từ khóa | Tìm nhiều tiêu chí, có bộ lọc địa chỉ chuyển tiếp |
| 8 | Thống kê địa chỉ/đầu số điện thoại | Thống kê ca; địa chỉ cũ nếu đầy đủ |
| 9 | Đọc lại file | Đọc lại file |
| 10 | Lưu file | Lưu file |
| 11 | Tìm chính xác theo mã | Tìm chính xác theo mã |
| 12 | Tìm gần đúng theo mã/tên | Tìm gần đúng theo mã/tên |
| 13 | Tìm chính xác số điện thoại | — |
| 0 | Quay lại | Quay lại |

Tìm tên tiếng Việt hỗ trợ hoa/thường và tìm không dấu. Mã trùng không phân biệt
hoa/thường. Nhập lô được kiểm tra toàn bộ trước khi áp dụng. Nhập sai được báo lỗi;
sửa/xóa không thành công, xem hoặc tìm kiếm không tự ghi file.

Các mục hóa đơn, phiếu nhập, báo cáo doanh thu vẫn thông báo chưa triển khai,
thuộc giai đoạn sau. Mục lưu/đọc chung ở menu chính đang phục vụ danh mục/sản phẩm/NCC;
khách hàng và nhân viên có mục đọc/lưu trong menu riêng.

## Dữ liệu và địa chỉ nhân viên cũ

Dữ liệu gồm năm file UTF-8 trong data/: danhmuc.txt, sanpham.txt, khachhang.txt,
nhanvien.txt, nhacungcap.txt. Repository có dữ liệu mẫu; khi sửa/chạy thử hãy dùng bản sao.
Khi file không tồn tại, ứng dụng tạo mẫu. File rỗng danh sách nhưng có header hợp lệ
được giữ rỗng, không tự thay bằng dữ liệu mẫu.

Các cột phân cách bằng Tab. Điện thoại là chuỗi, giữ số 0 đầu. Ký tự Tab, xuống dòng
và dấu gạch chéo ngược trong nội dung được escape bởi FileText.
Chỉ chỉnh file khi đã tắt chương trình; giữ đúng header và số cột.

Khách hàng vẫn đọc được KHACH_HANG_V1 và bảng 4 cột.
Nhân viên đọc được bảng cũ theo thứ tự maNhanVien, hoTen, caLamViec, soDienThoai, diaChi,
bảng 4 cột UML và bảng mới theo thứ tự:

~~~text
maNhanVien    hoTen    soDienThoai    caLamViec    diaChiCu
~~~

Dấu phân cách trong ví dụ trên được trình bày bằng khoảng trắng; file thật dùng Tab.
Cột diaChiCu là metadata lưu trữ, không phải thuộc tính của model NhanVien.
Nhân viên mới không được hỏi địa chỉ. Địa chỉ cũ vẫn được giữ theo mã, hiển thị riêng,
không lấy địa chỉ mẫu/địa chỉ từ file khác để điền vào phần còn thiếu.

LuuTruFileText tạo và kiểm tra bản .bak trước khi chuyển định dạng khách hàng/nhân viên.
Không ghi đè file lỗi hoặc bản sao lưu xung đột. File được ghi qua file tạm và thay thế
nguyên khối khi hệ thống tệp hỗ trợ. Nếu lỗi lưu, chương trình thông báo dữ liệu còn
trong bộ nhớ; dùng mục 10 để thử lại sau khi xử lý nguyên nhân.

Với API Java, dùng DanhSachNhanVien.getTatCa() và DanhSachNhanVien.tuDanhSach(...)
để chuyển giữa mảng và List có kèm metadata. List do docNhanVien() trả về cũng mang
metadata; có thể lưu sang đường dẫn mới qua ghiNhanVien() mà vẫn giữ địa chỉ.
Nếu caller dùng List thường để ghi vào file nhân viên hiện có, địa chỉ thiếu được lấy
từ chính file đích hợp lệ theo mã. Khi xuất sang file mới, hãy giữ List có metadata,
không tách chỉ các model rồi bỏ phần dữ liệu chuyển tiếp.

## Kiểm thử

Linux/cloud:

~~~bash
bash test.sh
~~~

Script biên dịch mới toàn bộ source/test vào thư mục tạm và chạy tất cả các chương trình
*Test.java với assertions bật. Các kiểm thử ghi file vào thư mục tạm; kiểm thử Console
khởi động tiến trình Main riêng, bao gồm lưu, thoát, mở lại và file lỗi.

Đã xác minh trên OpenJDK 21/Linux: **16/16 chương trình kiểm thử đạt**.
Chưa chạy trực tiếp trên Windows CMD/PowerShell.

## Cấu trúc

~~~text
src/Main.java          Menu Console
src/models/            Các model nghiệp vụ
src/services/          Danh sách, interface, file text và adapter metadata
tests/                 Kiểm thử model, CRUD, lưu trữ, UML và Console
data/                  Dữ liệu mẫu UTF-8
artifacts/             Class Diagram nguồn
images/                Ảnh sơ đồ
docs/                  Tài liệu và báo cáo theo task
~~~

DanhSachKhachHang dùng List<KhachHang> và IQuanLyDanhSach<KhachHang>.
DanhSachNhanVien giữ mảng NhanVien[]; NhanVien chỉ có mã, tên, điện thoại và ca.
Mã khách hàng/nhân viên ổn định. Ràng buộc hóa đơn tham chiếu và xóa đối tượng đã có
chứng từ sẽ được thiết kế ở Task 07.

