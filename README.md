# Quản lý cửa hàng dụng cụ thể thao

Đồ án Java console viết theo kiến thức chương 1–5 và các file tổ chức đồ án. Cấu trúc gồm **23 class + 1 interface**, đúng nhóm thực thể, danh sách và quản lý/hỗ trợ trong `To_Chuc_UML.txt`.

Code dùng constructor không tham số/có tham số/sao chép, getter/setter, `this`, `super`, mảng đối tượng, vòng lặp, `switch-case`, kế thừa, đa hình, abstract và interface. Phần generic chỉ dùng kiểu `T` của interface danh sách. Không dùng lambda, Stream, callback, reflection, menu generic hoặc cơ chế snapshot của bản trước.

## Chạy chương trình.

Khuyến nghị cài **JDK 17 hoặc mới hơn** và đặt `JAVA_HOME`. Code Java trong `src/` dùng cú pháp và API có từ Java 8; script dùng `javac` và `java` thông thường.

Windows, tại thư mục repository:

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File .\run.ps1
```

Có thể thêm `-BuildOnly` hoặc `-JavaHome 'C:\duong-dan\jdk-17'`. Trong VS Code, dùng **Sport Store - Console**.

Linux/macOS:

```bash
bash run.sh
```

`Main` nhận đối số đầu là thư mục dữ liệu. Ví dụ `java -cp out/console Main data` hoặc `bash run.sh /tmp/sport-store-demo`. Thư mục mới chưa có file sẽ bắt đầu rỗng.

## Cấu trúc 23 class

| Nhóm | Các class |
| --- | --- |
| 12 thực thể | `SanPham`, `GiayTheThao`, `QuanAoTheThao`, `DungCuTheThao`, `LoaiSP`, `KhachHang`, `NhanVien`, `NhaCungCap`, `HoaDon`, `ChiTietHoaDon`, `PhieuNhap`, `ChiTietPhieuNhap` |
| 7 danh sách | `DanhSachSanPham`, `DanhSachLoaiSP`, `DanhSachKhachHang`, `DanhSachNhanVien`, `DanhSachNhaCungCap`, `DanhSachHoaDon`, `DanhSachPhieuNhap` |
| 4 quản lý/hỗ trợ | `QuanLyCuaHang`, `Kho`, `LuuTruFileText`, `Main` |

`IQuanLyDanhSach<T>` là interface, không tính vào 23 class.

- Thực thể lưu thông tin của một đối tượng, kiểm tra dữ liệu của nó và tính tiền khi phù hợp.
- Mỗi danh sách có mảng `ds` và biến `soLuong`, cung cấp thêm/sửa/xóa/tìm kiếm. Mở rộng bằng `Arrays.copyOf` như bài giảng, xóa bằng vòng lặp dồn mảng.
- `QuanLyCuaHang` giữ các danh sách, chạy menu và điều phối nghiệp vụ. Các hàm nhập/menu được tách nhỏ và gọi trực tiếp, không truyền hàm qua tham số.
- `Kho` dùng chung danh sách sản phẩm. `LuuTruFileText` chỉ đọc/ghi. `Main` chỉ tạo lớp quản lý và khởi chạy.

## Menu

| Mục | Chức năng |
| --- | --- |
| 1–5 | Quản lý sản phẩm, loại SP, khách hàng, nhân viên, nhà cung cấp |
| 6–7 | Quản lý hóa đơn, phiếu nhập và các dòng chi tiết |
| 8 | Ghi dữ liệu ra file |
| 9 | Thống kê tổng tiền hóa đơn/phiếu nhập: toàn bộ hoặc theo khoảng ngày |
| 10 | Nhập n/thêm k sản phẩm; kiểm tra cả lô trước khi thêm |
| 11 | Lọc sản phẩm theo loại hoặc khoảng giá |
| 0 | Ghi file và thoát |

Menu con: `1` thêm, `2` sửa, `3` xóa, `4` tìm gần đúng mã/tên, `5` xem tất cả, `6` tìm chính xác mã, `0` quay lại. Chứng từ tìm theo mã chứng từ hoặc mã đối tác. Tìm kiếm không phân biệt hoa/thường; nhập từ khóa có dấu nếu tên được lưu có dấu.

Thay đổi được giữ trong bộ nhớ. Chọn **8** để lưu trong khi đang chạy; chọn **0 ở menu chính** để lưu và thoát. Nếu hết đầu vào giữa thao tác thì bỏ bản đang nhập dở, lưu các thao tác đã hoàn tất. Không tự ghi sau từng setter hoặc mỗi lần nhập một dòng chi tiết.

Ví dụ từ dữ liệu rỗng: thêm loại SP → thêm nhân viên/khách/nhà cung cấp → thêm SP (tồn 0) → lập phiếu nhập 10 → lập hóa đơn bán 3 → tồn còn 7. Sửa hóa đơn thành bán 5 chỉ trừ thêm 2. Xóa hóa đơn hoàn lại lượng đã bán.

## Quy tắc cần hiểu khi bảo vệ

- Mã không đổi khi sửa; thêm trùng mã trả `false`. Dữ liệu giá/số lượng/ngày không hợp lệ báo lỗi.
- Giày, quần áo, dụng cụ cùng nằm trong `SanPham[]`; khi xuất, gọi `hienThiThongTin()` của đúng lớp con.
- Thêm sản phẩm có tồn 0. Sửa thông tin sản phẩm giữ nguyên tồn. Chỉ `Kho` và lớp đọc file gọi `capNhatTon()` theo quy ước của đồ án.
- Chứng từ mới phải có chi tiết; một mã SP chỉ có một dòng. Nhập/sửa dòng không tự đổi kho.
- Sửa chứng từ dùng constructor sao chép từng dòng để giữ được bản cũ. Kho dùng hai vòng lặp: kiểm tra tất cả tồn mới trước, rồi cập nhật tất cả sau.
- Chặn xóa loại/người/SP đang được tham chiếu; chặn xóa SP còn tồn hoặc hủy phiếu nhập làm âm tồn.
- Tiền dùng `BigDecimal`, ngày dùng `LocalDate`, theo yêu cầu rõ trong `To_Chuc_Class_Sport_Store.txt`. Chỉ dùng các phép tính tiền/các thao tác ngày cần thiết.
- Đơn giá nằm trong chi tiết nên sửa giá sản phẩm không đổi tiền chứng từ cũ. Tổng tiền được tính, không có setter tổng tiền.
- Mảng trả về là bản sao cấu trúc mảng. Các thực thể loại/người/sản phẩm vẫn dùng tham chiếu chung; sửa chúng tại chỗ để các liên kết không bị cũ. Riêng chứng từ và chi tiết được sao chép trước khi sửa.

## File text

Có 9 file UTF-8: `loaisp.txt`, `sanpham.txt`, `khachhang.txt`, `nhanvien.txt`, `nhacungcap.txt`, `hoadon.txt`, `chitiethoadon.txt`, `phieunhap.txt`, `chitietphieunhap.txt`.

Mỗi file có dòng tiêu đề, sau đó là các bản ghi tách bằng `|`. Không nhận ký tự `|`, tab hoặc xuống dòng bên trong một trường. Đọc/ghi bằng `FileInputStream`/`FileOutputStream` kết hợp bộ đọc/ghi text, với các bước mở → đọc/ghi → đóng. Tài liệu `BinaryFile` giúp hiểu luồng và đóng file; yêu cầu đồ án vẫn là **file text**, vì vậy không thay bằng file nhị phân.

Đọc theo thứ tự loại/người → SP → chứng từ → chi tiết; nối lại đối tượng bằng mã. Tồn đã có trong file SP nên đọc chứng từ không cập nhật Kho lần nữa. File sai định dạng, mã trùng hoặc thiếu liên kết báo tên file và dòng lỗi. Một bộ đã có dữ liệu phải đủ 9 file.

Trước khi thay file đang dùng, chương trình ghi đủ bộ file vào `data/tam`, sao lưu bộ cũ ở `data/sao_luu`, rồi chép bộ mới. Nếu chép báo lỗi, chương trình cố phục hồi bộ cũ. Đây là các thao tác file và vòng lặp thông thường, không có cơ sở dữ liệu hoặc cơ chế giao dịch phức tạp. Không chạy nhiều tiến trình cùng sửa một thư mục dữ liệu.

Dữ liệu mẫu đã đổi sang định dạng dễ đọc này. Bản gốc được giữ trong `data/du_lieu_cu`, gồm cả cột địa chỉ nhân viên cũ. Hai chứng từ mẫu chưa có chi tiết vẫn là bản nháp để có thể sửa/xóa, tổng tiền 0.

Nếu bạn đã có **`sport-store.txt` từ bản trước** trên máy, hãy giữ bản sao trước khi chuyển đổi. Bản mới báo rõ khi phát hiện file đó để không bỏ qua dữ liệu cá nhân. Bản này không tự chuyển định dạng snapshot cũ. Khi dùng dữ liệu mẫu mới, đặt file cũ sang một thư mục khác và giữ nguyên làm bản sao.

## Tài liệu và kiểm tra

[docs/THIET_KE_HIEN_TAI.md](docs/THIET_KE_HIEN_TAI.md) ghi rõ phần nào ứng với chương nào, cách đọc code và ví dụ cập nhật tồn. UML trong `artifacts/Sport_Store.drawio.xml` thể hiện 23 class + interface. Các báo cáo task/audit cũ là tài liệu lịch sử.

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File .\test.ps1
```

```bash
bash test.sh
```

Bốn nhóm kiểm tra: mảng/model, nghiệp vụ/kho, đọc ghi file, thao tác qua console thật. Các lớp trong `tests/` chỉ hỗ trợ kiểm tra, không tính vào số class của đồ án. Không cần thư viện kiểm thử ngoài.
