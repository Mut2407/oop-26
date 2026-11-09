# Quản lý cửa hàng dụng cụ thể thao

Ứng dụng Java OOP cơ bản chạy trên Console, dùng JDK 21 trở lên và file text UTF-8.
Không cần database hoặc thư viện Java ngoài.

## Chạy chương trình

Mở terminal tại thư mục repository, chạy:

~~~powershell
powershell -NoProfile -ExecutionPolicy Bypass -File .\run.ps1
~~~

Script biên dịch toàn bộ src/ vào out/console rồi chạy Main bằng cùng một JDK.
Thêm -BuildOnly để chỉ biên dịch, hoặc -JavaHome để chỉ định JDK.
Trong VS Code/Antigravity, dùng cấu hình Sport Store - Console trong Run and Debug.

## Chức năng

Menu chính: 1 sản phẩm, 2 danh mục, 3 khách hàng, 4 nhân viên, 5 nhà cung cấp, 0 thoát.
Các menu con dùng chung các số:

| Mục | Chức năng |
| --- | --- |
| 1 | Thêm một đối tượng |
| 2 | Sửa theo mã, giữ nguyên mã |
| 3 | Xóa theo mã |
| 4 | Tìm theo mã hoặc tên; Enter để xem toàn bộ danh sách |
| 5 | Chỉ có ở sản phẩm: thống kê tồn kho |
| 0 | Quay lại |

Thống kê tồn kho hiển thị mã, tên và số lượng tồn của từng sản phẩm, tổng số mặt hàng
và tổng số lượng tồn. Không thống kê khách hàng, nhân viên hoặc nhà cung cấp.
Danh mục đang được sản phẩm sử dụng không được xóa.

Tìm kiếm không phân biệt hoa/thường và hỗ trợ tên tiếng Việt không dấu.
Chương trình kiểm tra mã trùng, thông tin bắt buộc, số điện thoại, giá và tồn kho không âm.
Không có nhập theo lô, lọc nhiều điều kiện, lọc giá, báo cáo doanh thu hoặc menu lưu/đọc thủ công.
Các model chứng từ chưa được sử dụng đã được bỏ khỏi phạm vi hiện tại.

## Dữ liệu

Chỉ dùng data/ ở gốc repository, gồm:

- danhmuc.txt
- sanpham.txt
- khachhang.txt
- nhanvien.txt
- nhacungcap.txt

Chương trình đọc các file hiện có khi khởi động và tự lưu sau mỗi lần thêm/sửa/xóa.
Không có hàm tạo dữ liệu mẫu. Nếu thiếu file hoặc file sai định dạng, chương trình báo lỗi
và dừng khởi động; hãy khôi phục file từ bản sao, không tạo lại dữ liệu tự động.
Danh sách rỗng có header hợp lệ vẫn được giữ rỗng.

Các cột cách nhau bằng Tab; điện thoại lưu bằng chuỗi để giữ số 0 đầu.
FileText xử lý UTF-8, ký tự Tab/xuống dòng và ghi qua file tạm.
Không ghi đè file hiện có bị lỗi. Khi chuyển định dạng cũ, LuuTruFileText giữ bản .bak.
Nếu lưu thất bại, chương trình báo rõ thay đổi mới chỉ có trong bộ nhớ; cần xử lý nguyên nhân
và lưu lại thay đổi trước khi thoát.

Địa chỉ nhân viên trong file cũ được giữ riêng theo mã để không mất thông tin.
Model NhanVien vẫn chỉ có mã, tên, điện thoại và ca làm việc; nhân viên mới không bị hỏi địa chỉ.
API lưu trữ nhân viên nhận/trả trực tiếp DanhSachNhanVien, không chuyển qua List hoặc adapter.
Chỉ chỉnh file dữ liệu bằng trình soạn thảo khi chương trình đã tắt.

## Cấu trúc

~~~text
src/Main.java          Khởi động và menu chính; main() tối đa 10 dòng
src/ui/                5 menu con và NhapLieuConsole dùng chung một Scanner
src/models/            Danh mục, sản phẩm và các đối tượng đang quản lý
src/services/          4 lớp danh sách, interface CRUD và các hàm đọc/ghi text
data/                  Dữ liệu ban đầu và dữ liệu người dùng, ở gốc repository
tests/                 Kiểm thử model, CRUD, tồn kho, lưu trữ, OOP và Console
run.ps1                Script biên dịch/chạy ứng dụng
test.sh                Script kiểm thử trên Linux/cloud
~~~

DuLieu chỉ quản lý danh mục đã đọc từ file, không chứa bản ghi mẫu.
DanhSachNhanVien và DanhSachSanPham dùng mảng đối tượng.
IQuanLyDanhSach được dùng cho sản phẩm, khách hàng và nhà cung cấp.
SanPham là abstract class; ba lớp sản phẩm con ghi đè các phương thức hiển thị và phân loại.

## Kiểm thử

Trên Linux/cloud:

~~~bash
bash test.sh
~~~

Các test biên dịch từ source mới, dùng thư mục tạm hoặc bản sao dữ liệu.
ConsoleCrudTest kiểm tra CRUD, thoát/mở lại, tồn kho, xóa danh mục đang dùng,
nhập dở, thiếu file, file lỗi và việc không tự sinh dữ liệu mẫu.
NhanVienFileTest kiểm tra bảo toàn địa chỉ cũ, ký tự đặc biệt và lỗi đọc/ghi.
OopTest kiểm tra các cấu trúc OOP và giới hạn số dòng main().
ConsoleLauncherCheck là kiểm thử launcher PowerShell riêng.

Các sơ đồ trong artifacts/, ảnh sơ đồ và báo cáo task cũ là tài liệu của thiết kế trước
khi rút gọn; chưa được đồng bộ với phạm vi hiện tại.
