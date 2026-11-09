# Quản lý cửa hàng dụng cụ thể thao

Ứng dụng Java console cho đồ án OOP, dùng JDK 17 trở lên, không cần database hoặc thư viện ngoài. Code áp dụng yêu cầu trong `artifacts/` và tài liệu tổ chức bạn đã gửi: thực thể, danh sách dùng mảng, lớp điều phối, kế thừa và đa hình.

## Chạy trên Windows

Mở terminal tại thư mục repository:

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File .\run.ps1
```

Chỉ biên dịch: thêm `-BuildOnly`. Chỉ định JDK: thêm `-JavaHome 'C:\duong-dan\jdk-17'`. Có thể dùng cấu hình **Sport Store - Console** trong Run and Debug của VS Code.

Trên Linux/macOS:

```bash
bash run.sh
```

`Main` nhận tùy chọn đường dẫn thư mục dữ liệu ở đối số đầu tiên, ví dụ `bash run.sh /tmp/sport-store-demo`. Khi thư mục chưa có dữ liệu, chương trình bắt đầu rỗng. Không tự sinh sản phẩm hoặc chứng từ mẫu.

## Chức năng

| Menu | Chức năng |
| --- | --- |
| 1–5 | CRUD và tìm kiếm sản phẩm, loại SP, khách hàng, nhân viên, nhà cung cấp |
| 6–7 | CRUD hóa đơn, phiếu nhập; thêm/sửa/xóa từng dòng chi tiết |
| 8 | Lưu toàn bộ dữ liệu |
| 9 | Tổng tiền bán và tổng tiền nhập: toàn bộ hoặc theo khoảng ngày, gồm cả hai đầu |
| 10 | Nhập n/thêm k sản phẩm trong một lô; lỗi một phần tử thì từ chối cả lô |
| 11 | Lọc sản phẩm theo loại hoặc khoảng giá |
| 0 | Lưu và thoát |

Menu CRUD: `1` thêm, `2` sửa, `3` xóa, `4` tìm gần đúng, `5` xem toàn bộ, `6` tìm đúng mã, `0` quay lại. Tìm gần đúng theo mã/tên; với chứng từ là mã chứng từ/mã đối tác. Hỗ trợ tìm tên tiếng Việt không dấu và không phân biệt hoa/thường.

Ví dụ thao tác từ dữ liệu rỗng: tạo loại SP → tạo nhân viên/nhà cung cấp/khách → tạo sản phẩm (tồn 0) → lập phiếu nhập → lập hóa đơn. Khi soạn chứng từ, `0. Ghi sổ` kết thúc bản soạn và kiểm tra toàn bộ trước khi cập nhật kho.

Mỗi thao tác hoàn tất được tự lưu. Nếu lỗi ghi file, chương trình thông báo rõ dữ liệu mới đang ở RAM; chọn **8. Lưu** để thử lại. Nếu đóng đầu vào (EOF) giữa thao tác, bản đang nhập bị bỏ và chỉ các thao tác đã hoàn tất được lưu. Nếu lần lưu cuối vẫn thất bại, chương trình trả mã lỗi 1.

## Quy tắc nghiệp vụ

- Mã được trim và chuẩn hóa chữ hoa; không đổi mã khi sửa, không cho mã trùng.
- Bảy danh sách dùng mảng đối tượng và `soLuong`, mở rộng khi đầy, dồn mảng khi xóa. Một `SanPham[]` chứa cả giày, quần áo, dụng cụ.
- Sản phẩm mới có tồn bằng 0; tồn đi qua phiếu nhập/hóa đơn. Sửa sản phẩm không được đổi lớp con hoặc ghi đè tồn.
- Thêm/sửa/xóa chứng từ tính chênh lệch theo từng mã SP. Kiểm tra toàn bộ trước khi thay đổi; thiếu hàng hoặc vượt `Integer.MAX_VALUE` thì giữ nguyên chứng từ và kho.
- Không xóa dữ liệu còn được tham chiếu. Không xóa sản phẩm còn tồn. Chứng từ mới phải có ít nhất một dòng; một mã SP chỉ có một dòng trong mỗi chứng từ.
- Tiền dùng `BigDecimal`; đơn giá nằm trong chi tiết và không thay đổi theo giá sản phẩm hiện tại. Tổng tiền luôn tính từ chi tiết.
- Các getter trả bản sao sâu. Muốn sửa phải gọi `QuanLyCuaHang`; sửa kết quả tìm kiếm không tự đổi dữ liệu đang lưu.
- Chứng từ có hiệu lực ngay khi ghi sổ; kiểm tra tồn hiện tại, không dựng lại tồn theo lịch sử ngày. Ứng dụng dành cho một tiến trình console.

## Đọc/ghi và dữ liệu cũ

Định dạng chính là **`data/sport-store.txt`**, text UTF-8, tiêu đề `SPORT_STORE_V2`. File chứa cả bảy nhóm dữ liệu, chi tiết và tồn hiện tại. Dùng một snapshot để tồn và chứng từ được ghi cùng nhau. File tạm nằm cùng thư mục, thay thế bằng atomic move; nếu hệ thống file không hỗ trợ thì báo lỗi và giữ file cũ. File `.bak` giữ snapshot trước lần ghi gần nhất.

Lần đầu chưa có snapshot, chương trình đọc các file `.txt` cũ đang có trong repo. Những file này được **giữ nguyên**, kể cả cột địa chỉ nhân viên cũ không thuộc model hiện tại. Sau lần lưu đầu, snapshot là nguồn dữ liệu chính. Không chỉnh các file cũ để cập nhật dữ liệu sau khi đã chuyển đổi.

Hai chứng từ mẫu `CHO_XAC_NHAN` chưa có chi tiết được nhập dưới dạng **bản nháp cũ**, hiển thị rõ và tính 0 đồng. Có thể bổ sung chi tiết bằng chức năng Sửa hoặc xóa. Các định dạng V1 khác với dữ liệu hiện có sẽ báo lỗi để tránh chuyển đổi thiếu thông tin. Các sản phẩm giày cũ chưa có chất liệu vẫn giữ chuỗi rỗng, không tự bịa giá trị.

Đọc file dựng lại đúng lớp con và liên kết theo mã, **không chạy lại cập nhật kho**. File hỏng, dòng trùng, thiếu liên kết, số/ngày không hợp lệ đều làm khởi động thất bại trước khi ghi. Báo lỗi có tên file và số dòng.

## Cấu trúc và kiểm tra

Có **23 lớp nghiệp vụ/khởi chạy theo thiết kế + 9 lớp hỗ trợ và giao diện = 32 class**, cùng **1 interface**. Chi tiết, sơ đồ và đối chiếu tiêu chí chấm điểm trong [docs/THIET_KE_HIEN_TAI.md](docs/THIET_KE_HIEN_TAI.md). UML trong `artifacts/Sport_Store.drawio.xml` được cập nhật theo code. Các báo cáo task/audit trước đây trong repo là tài liệu lịch sử; README và tài liệu thiết kế hiện tại mô tả phiên bản này.

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File .\test.ps1
```

```bash
bash test.sh
```

Bốn nhóm test kiểm tra CRUD/mảng/tìm kiếm, kho/chứng từ/thống kê, đọc ghi/chuyển đổi dữ liệu cũ, và chạy menu bằng tiến trình Java thật. Không sửa dữ liệu mẫu trong `data/`. Test mới thay thế các test gắn với API `DanhMuc`, trạng thái static toàn cục và các hàm ghi file riêng lẻ đã bỏ.
