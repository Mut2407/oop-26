# Quản lý cửa hàng dụng cụ thể thao

Ứng dụng Java thuần, chạy trên CMD/Console. Nguồn thiết kế chuẩn là
[Sport_Store.drawio.xml](artifacts/Sport_Store.drawio.xml), trang **01 - Tổng quan (20 class)**.

Task 06.1–06.5 đã hoàn thành cho khách hàng và nhân viên. Xem
[báo cáo Task 06](docs/BAO_CAO_TASK_06.md) để biết đối chiếu UML, tiến độ từng task,
kết quả kiểm thử của giai đoạn đó. Task 07 — Hóa đơn đã được triển khai; xem
[báo cáo Task 07](docs/BAO_CAO_TASK_07.md). Task 08–09 đã hoàn thành phiếu nhập
và báo cáo: [báo cáo Task 08–09](docs/BAO_CAO_TASK_08_09.md).

## Chạy ứng dụng

Dùng **JDK 21 trở lên** (có module `jdk.compiler`). Đặt `JAVA_HOME` trỏ đến JDK,
hoặc thêm `java` của JDK vào `PATH`. Java đi kèm extension của IDE chưa chắc đã được
thêm vào `PATH` của terminal.

Windows CMD/PowerShell:

~~~powershell
powershell -NoProfile -ExecutionPolicy Bypass -File .\run.ps1
~~~

Chỉ giữ **run.ps1** làm script chạy ứng dụng. Script luôn biên dịch toàn bộ `src/`
vào thư mục sạch `out/console`, sau đó mới chạy `Main` bằng cùng một JDK. Nếu biên dịch
lỗi, script dừng và không chạy class cũ. Script tự chọn thư mục repository làm thư mục
làm việc để đọc đúng `data/`; không phụ thuộc bộ nhớ đệm Java của IDE.

Nếu chưa cấu hình `JAVA_HOME`/`PATH`, có thể chỉ định đường dẫn JDK:

~~~powershell
powershell -NoProfile -ExecutionPolicy Bypass -File .\run.ps1 -JavaHome 'C:\Program Files\Java\jdk-21'
~~~

Để chỉ biên dịch, thêm `-BuildOnly`. Linux/macOS/cloud có PowerShell 7 dùng cùng script:

~~~bash
pwsh -NoProfile -File ./run.ps1
~~~

Không cần database, server hoặc thư viện Java ngoài.

### Chạy trong Antigravity IDE / VS Code

Mở đúng thư mục repository chứa `src/`, `data/` và `run.ps1`. Trong **Run and Debug**,
chọn **Sport Store - Console** rồi nhấn **F5**. Cấu hình dùng `run.ps1 -BuildOnly`
trước khi chạy và lấy class từ `out/console`, với terminal tương tác để nhập menu.

Nếu gặp `Unresolved compilation problems` và báo thiếu `docKhachHang`, `docNhanVien`,
`ghiKhachHang` hoặc `DanhSachNhanVien.getTatCa()`:

1. Lưu các file đang sửa, rồi cập nhật toàn bộ source bằng `git pull --ff-only origin main`.
   Nếu Git báo xung đột/thay đổi cục bộ, giữ lại các thay đổi đó và xử lý trước khi tiếp tục.
   Không chỉ chép `Main.java` hoặc giải nén `src.zip` cũ đè lên source hiện tại.
2. Mở Command Palette (`Ctrl+Shift+P`), chạy **Java: Clean Java Language Server Workspace**,
   chọn khởi động lại và xóa cache workspace của Java khi IDE hỏi.
3. Chạy cấu hình **Sport Store - Console** hoặc lệnh `powershell ... -File .\run.ps1` ở trên.

Bốn phương thức này đã có trong source Task 06.4. Lỗi trên cho biết bản biên dịch của
IDE không giải quyết được API; cần kiểm tra source đang mở và cache, không sửa chữ ký UML.

## Menu

Menu chính: **1** sản phẩm, **2** danh mục, **3** khách hàng, **4** nhân viên,
**5** nhà cung cấp, **6** hóa đơn, **7** phiếu nhập, **8** báo cáo; **0** thoát. Số mục cũ được giữ để tiếp tục dùng các thao tác quen thuộc.

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

Menu **6 — Hóa đơn** gồm: 1 xem danh sách, 2 lập hóa đơn chờ, 3 xem chi tiết,
4 thêm dòng, 5 sửa dòng, 6 xóa dòng, 7 xác nhận bán hàng, 8 tìm/lọc kết hợp,
9 doanh thu, 10 lưu, 11 đọc lại, 0 quay lại. Ngày nhập theo `yyyy-MM-dd`.
Khi lập hóa đơn, để trống mã để sinh tự động, ngày trống lấy ngày hiện tại.
Chọn mã khách hàng, nhân viên và sản phẩm đã có. Đơn giá trống lấy giá hiện tại;
đơn giá đã ghi nhận được giữ nguyên khi giá bán thay đổi. `/huy` hoặc EOF hủy
thao tác nhập đang dở. Hóa đơn chờ chưa lưu sẽ được nhắc khi rời menu.

Mỗi sản phẩm tối đa một dòng; dùng sửa dòng nếu muốn đổi số lượng/đơn giá.
Mục 10 lưu hóa đơn chờ, chưa giảm tồn. Mục 7 hỏi xác nhận `Y/N`, lưu đồng bộ
hóa đơn và tồn kho rồi mới báo thành công. Hóa đơn rỗng/thiếu hàng không được
xác nhận. Hóa đơn đã xác nhận chỉ xem/tìm; xác nhận lặp không giảm tồn.
Doanh thu chỉ tính hóa đơn `DA_XAC_NHAN`, lấy cả hai ngày biên.
Không xóa hoặc nhập lại danh sách làm mất KH/NV/SP mà hóa đơn đang tham chiếu.

Menu **7 — Phiếu nhập** có các mục con giống luồng chứng từ: 1 danh sách,
2 lập phiếu chờ, 3 chi tiết, 4 thêm dòng, 5 sửa dòng, 6 xóa dòng, 7 xác nhận
nhập kho, 8 tìm/lọc kết hợp, 9 giá trị nhập theo ngày, 10 lưu, 11 đọc lại,
0 quay lại. Chọn NCC/NV/SP đã có, ngày `yyyy-MM-dd`, mã trống tự sinh PN...
Đơn giá nhập phải nhập rõ, không lấy giá bán làm mặc định. Lưu chờ chưa tăng tồn;
xác nhận tăng toàn bộ tồn một lần và không đổi giá bán. Phiếu đã nhập kho bị khóa.
Nếu nhập sản phẩm mới, tạo sản phẩm ở menu sản phẩm trước rồi lập dòng nhận hàng.
Không có trả hàng, hủy phiếu đã nhập hoặc tự động cập nhật giá bán.

Menu **8 — Báo cáo**: 1 doanh thu theo ngày, 2 giá trị nhập theo ngày,
3 top bán chạy, 4 cảnh báo tồn, 5 giá trị tồn theo giá bán hiện tại, 0 quay lại.
Chỉ tính chứng từ đã xác nhận, ngày bao gồm hai đầu. Bán chạy tính toàn bộ lịch sử,
xếp theo số lượng đã bán, hòa thì theo mã; top 0 trả rỗng, số âm bị từ chối.
Cảnh báo lấy tồn <= ngưỡng. Giá trị tồn theo giá bán không phải giá vốn/lợi nhuận.
Các báo cáo chỉ đọc và dùng dữ liệu hiện tại sau xác nhận hoặc đọc lại.

Mục lưu/đọc chung ở menu chính đang phục vụ danh mục/sản phẩm/NCC;
khách hàng và nhân viên có mục đọc/lưu trong menu riêng.

## Dữ liệu và địa chỉ nhân viên cũ

Dữ liệu gồm năm file UTF-8 trong data/: danhmuc.txt, sanpham.txt, khachhang.txt,
nhanvien.txt, nhacungcap.txt. Hóa đơn được lưu thêm trong `data/hoadon.txt`
khi người dùng lưu hoặc xác nhận lần đầu. File hóa đơn dùng header `HOA_DON_V1`,
các bản ghi H (mã/ngày/mã KH/mã NV/trạng thái/số dòng) và D (mã SP/số lượng/đơn giá).
Không tự tạo đối tượng thay thế khi mã tham chiếu không tồn tại.

Phiếu nhập được lưu ở `data/phieunhap.txt`, header `PHIEU_NHAP_V1`, gồm H
(mã/ngày nhập/mã NCC/mã NV/trạng thái/số dòng) và D (mã SP/số lượng/đơn giá nhập).
Đọc phiếu không tăng tồn. Không xóa/thay mất NCC/NV/SP được phiếu tham chiếu.

Cả bán và nhập dùng chung `data/banhang.undo` và khóa `banhang.lock`.
Journal chứa bản gốc sản phẩm cùng hóa đơn hoặc phiếu nhập, kèm checksum SHA-256.
Định dạng journal bán hàng cũ vẫn được hỗ trợ.
Nếu chương trình dừng giữa chừng, lần mở sau phục hồi cả hai file về trước giao dịch;
đọc lại hóa đơn không trừ tồn lần nữa. Không xóa journal bằng tay. Journal lỗi làm
ứng dụng chặn khởi động; giữ các file để xử lý phục hồi. Chỉ chạy một phiên ứng dụng
trên cùng thư mục dữ liệu; không sửa file bằng công cụ khác khi đang chạy.

Repository có dữ liệu mẫu; khi sửa/chạy thử hãy dùng bản sao.
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

Script cũng chạy `ConsoleLauncherCheck` nếu có lệnh `pwsh`; nếu không có, phần kiểm tra
PowerShell được thông báo bỏ qua riêng, không tính vào số chương trình Java đạt.
Kiểm tra launcher dùng bản sao tạm có đường dẫn chứa dấu cách/tiếng Việt, mở các menu,
loại class cũ, chặn khởi động khi source thiếu API và thử lại sau khi sửa source.

Đã xác minh trên OpenJDK 21/Linux: **36/36 chương trình kiểm thử Java đạt**.
Script `run.ps1` được kiểm tra riêng bằng PowerShell 7 trên Linux; chưa chạy trực tiếp
trên Windows PowerShell 5.1 hoặc giao diện Antigravity IDE.

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
Mã khách hàng/nhân viên ổn định. Ràng buộc xóa/thay danh sách được gắn vào các
nguồn dữ liệu khi khởi động module hóa đơn. DichVuKho nằm trong package models
để giữ quyền package của HoaDon.danhDauDaXacNhan theo UML; không có setter
trạng thái công khai. DichVuKho cũng giữ quyền package của PhieuNhap.danhDauDaNhapKho theo UML.

