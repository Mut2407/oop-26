# Hướng dẫn đọc và bảo vệ đồ án theo các chương đã học

Bản này triển khai theo `To_Chuc_Do_An.txt`, `To_Chuc_UML.txt`, `To_Chuc_Class_Sport_Store.txt`, `Yeu_Cau_Do_An.txt` và yêu cầu gốc trong `yeucau_doan_OOP.docx`. Mục tiêu là tự đọc, gõ lại và giải thích được code ở trình độ sinh viên năm 2.

## Đối chiếu bài giảng

| Nguồn học | Kiến thức trong nguồn | Áp dụng trong code |
| --- | --- | --- |
| Chương 1, slide 6–9 | Thuộc tính, phương thức, đóng gói, kế thừa, đa hình | Tách một đối tượng thực thể với mảng danh sách và lớp quản lý |
| Chương 2, slide 13–19 | Constructor mặc định/có tham số/sao chép, `this`, get/set | Constructor các thực thể; `new HoaDon(cu)` và `new ChiTietHoaDon(cu)` khi sửa |
| Chương 2, slide 22–28 | Thuộc tính/hàm static, overload | `SanPham.SUC_CHUA_BAN_DAU`, `main`, hàm kiểm tra static; hai hàm `thongKeTongTien` |
| Chương 2, slide 30–39 | Mảng đối tượng, `Arrays.copyOf`, lớp danh sách, lớp quản lý và menu | Mảng `ds`, `soLuong`, thêm/sửa/xóa/tìm, menu `switch-case` trong `QuanLyCuaHang` |
| Chương 3, slide 8–15 | `extends`, `super`, phạm vi truy xuất | Ba lớp sản phẩm gọi constructor và hàm của `SanPham` |
| Chương 3, slide 19–24 | Tham chiếu, chuyển kiểu, has-a, mảng | SP có một loại, chứng từ có đối tác và chi tiết; chuyển kiểu khi đọc/ghi đúng lớp con |
| Chương 4, slide 7–15 | Override, mảng đa hình, `instanceof` | Gọi `hienThiThongTin()` qua `SanPham[]`; kiểm tra kiểu tại nhập liệu/ghi file |
| Chương 4, slide 22–25 | Abstract class/method | `SanPham` và các hàm `hienThiThongTin`, `kiemTraHopLe` |
| Chương 5, trang 8, 13–15 | Interface, tham chiếu interface, generic type `T` | Bảy danh sách implements `IQuanLyDanhSach<T>`; ví dụ dùng interface ở phần dưới |
| BinaryFile, trang 3–4, 8–11 | Mở/đọc hoặc ghi/đóng, stream, `try/catch/finally` | `LuuTruFileText` dùng luồng file, đóng trong `finally`; ghi text theo yêu cầu đồ án |
| File tổ chức class, mục 3 và 9 | `BigDecimal`, `LocalDate` | Tiền tính bằng `add`/`multiply`, ngày đọc bằng `parse` và so sánh khoảng ngày |

`BigDecimal` và `LocalDate` là thư viện để thực hiện yêu cầu cụ thể trong file tổ chức, không phải một kiến trúc mới. Bài giảng BinaryFile mô tả file nhị phân, còn đề yêu cầu file text; phần dùng lại là các bước làm việc với luồng và cách đóng file.

## Đúng 23 class + 1 interface

| Nhóm | Class |
| --- | --- |
| Thực thể, 12 class | SanPham, GiayTheThao, QuanAoTheThao, DungCuTheThao, LoaiSP, KhachHang, NhanVien, NhaCungCap, HoaDon, ChiTietHoaDon, PhieuNhap, ChiTietPhieuNhap |
| Danh sách, 7 class | DanhSachSanPham, DanhSachLoaiSP, DanhSachKhachHang, DanhSachNhanVien, DanhSachNhaCungCap, DanhSachHoaDon, DanhSachPhieuNhap |
| Quản lý/hỗ trợ, 4 class | QuanLyCuaHang, Kho, LuuTruFileText, Main |
| Interface | IQuanLyDanhSach<T> |

Không có lớp `KiemTra`, menu generic, bộ mã sản phẩm hoặc bộ nhập định dạng cũ riêng như bản trước. Các việc kiểm tra giá trị thuộc thực thể, mã trùng thuộc danh sách, liên kết thuộc lớp quản lý. Các thao tác file nằm trong `LuuTruFileText`.

Các constructor mặc định tạo đối tượng chưa nhập đủ dữ liệu. Constructor có tham số kiểm tra và gán dữ liệu. Constructor sao chép thể hiện đúng cách được dạy ở chương 2 và chương 3.

## Thứ tự đọc code

1. `LoaiSP`, `KhachHang`, `NhanVien`, `NhaCungCap`: thuộc tính, constructor, get/set.
2. `SanPham` và ba lớp con: hiểu `super`, phần chung/phần riêng, override.
3. `DanhSachLoaiSP` hoặc `DanhSachKhachHang`: hiểu `ds.length` khác `soLuong`; `timViTri`, kiểm tra mã, mở rộng và dồn mảng.
4. `ChiTietHoaDon` và `HoaDon`: một dòng tính thành tiền, nhiều dòng tính tổng tiền.
5. `Kho`: tính chênh lệch, kiểm tra trước rồi mới cập nhật.
6. `QuanLyCuaHang`: theo một hàm như `themHoaDon` rồi xem hàm nhập/menu gọi nó.
7. `LuuTruFileText`: đọc từng file theo thứ tự liên kết, sau đó xem ghi file.
8. `Main`: tạo lớp quản lý và gọi `chay`, không chứa toàn bộ chương trình.

`QuanLyCuaHang` dài vì giữ đầy đủ menu và nhập liệu của bảy nhóm như tài liệu yêu cầu. Mỗi nhóm có hàm riêng, gọi trực tiếp. `LuuTruFileText` cũng chia hàm đọc/ghi từng nhóm. Clean Code ở đây là tên dễ hiểu và hàm có nhiệm vụ rõ ràng; không dùng một hàm tổng quát khó giải thích để giảm số dòng.

## Mảng và interface

Ví dụ sử dụng qua kiểu interface, đúng chương 5:

```java
IQuanLyDanhSach<KhachHang> ds = new DanhSachKhachHang();
ds.them(new KhachHang("KH01", "Nguyen Van An", "0901234567", "TP.HCM"));
KhachHang kh = ds.timTheoMa("KH01");
System.out.println(kh.hienThiThongTin());
```

`T` được thay bằng `KhachHang`, `SanPham`, `HoaDon`... Interface chỉ quy định các tên hàm, không viết menu hay xử lý Kho. Bảy lớp tự cài phần thân hàm theo kiểu của mình. Hàm `nhapSanPham` dùng một biến kiểu interface trỏ đến `DanhSachSanPham` để tra cứu. Bài kiểm tra các danh sách cũng gọi thông qua interface.

Thêm vào mảng: kiểm tra đối tượng và mã → mở rộng nếu đầy → gán vào `ds[soLuong]` → tăng `soLuong`. Xóa: tìm vị trí → dịch các phần tử sau sang trái → giảm số lượng và bỏ tham chiếu cuối. Tìm kiếm: duyệt mảng, đưa kết quả phù hợp vào mảng tạm và trả phần đã sử dụng.

## Tham chiếu và sao chép

Mỗi sản phẩm trỏ đến một `LoaiSP` trong danh sách. Hóa đơn trỏ đến khách/nhân viên; chi tiết trỏ đến sản phẩm. Khi sửa loại hoặc người, danh sách cập nhật chính đối tượng cũ, nên những nơi tham chiếu nó thấy thông tin mới.

`getTatCa()` trả mảng mới, không trả thẳng mảng nội bộ. Mảng của thực thể thông thường chứa cùng tham chiếu đối tượng; đây là sao chép mảng, không phải sao chép sâu cả hệ thống. Quy ước sử dụng là menu thay đổi dữ liệu thông qua `QuanLyCuaHang` để kiểm tra các liên kết.

Chứng từ cần cách riêng vì phải giữ bản cũ trước khi tính chênh lệch. Constructor `HoaDon(HoaDon cu)` tạo mảng chi tiết mới và sao chép từng dòng. Vì thế sửa số lượng trên bản đang soạn không thay số lượng của bản đang lưu. Đối tượng sản phẩm được tham chiếu chung, nhưng đơn giá lịch sử là thuộc tính riêng của dòng chi tiết.

## Hai vòng lặp trong Kho

Ví dụ tồn 10, thêm hóa đơn bán 3:

- Số cũ 0, số mới 3, chênh lệch `0 - 3 = -3`.
- Tồn sau `10 + (-3) = 7`.

Sửa hóa đơn đó thành bán 5, tồn hiện tại 7:

- Chênh lệch `3 - 5 = -2`.
- Tồn sau `7 + (-2) = 5`.

| Chứng từ | Công thức chênh lệch |
| --- | --- |
| Hóa đơn | Số lượng cũ − số lượng mới |
| Phiếu nhập | Số lượng mới − số lượng cũ |

Với thao tác thêm, bản cũ là `null` nên số cũ bằng 0. Với thao tác xóa, bản mới là `null` nên số mới bằng 0. Duyệt mọi sản phẩm để tính cả mã chỉ xuất hiện ở một bản.

Vòng 1 tính tồn sau và kiểm tra tất cả. Nếu có một SP không đủ hàng thì báo lỗi khi chưa SP nào bị đổi tồn. Vòng 2 mới cập nhật tồn. Lớp quản lý đã kiểm tra mã, liên kết, chi tiết trước khi gọi Kho; sau đó thêm/sửa/xóa chứng từ trong danh sách. Không tạo một bộ danh sách mới cho toàn hệ thống mỗi lần bán hàng.

## Đọc và ghi text

Ví dụ file hóa đơn:

```text
HOA_DON
HD01|2026-11-09|KH01|NV01
```

Ví dụ file chi tiết:

```text
CHI_TIET_HOA_DON
HD01|SP01|2|500000
```

Lưu mã để đọc lại và tra đúng đối tượng, không lưu địa chỉ bộ nhớ. Đọc loại và đối tác trước, tiếp theo SP, đầu chứng từ, cuối cùng chi tiết. Đọc chứng từ chỉ khôi phục dữ liệu, không gọi hàm cộng/trừ Kho. Nếu đọc lỗi, bộ dữ liệu mới bị bỏ và không ghi đè bằng bộ rỗng.

Ghi cả bộ vào thư mục tạm trước. Khi ghi đủ thì sao lưu file cũ và chép bộ mới. Nếu có lỗi sao chép, dùng các file sao lưu để phục hồi. Đó là các thao tác `FileInputStream`, `FileOutputStream`, mảng tên file và vòng lặp. Khi chương trình bị tắt đột ngột giữa lúc chép file, người dùng có thể phục hồi từ thư mục sao lưu; đồ án không giải quyết nhiều người ghi đồng thời.

## Giới hạn vừa với phạm vi đồ án

- Console một tiến trình, không web/database/đăng nhập/phân quyền.
- Chứng từ mới có hiệu lực khi lưu vào danh sách; không thêm luồng phê duyệt.
- Chỉ kiểm tra tồn hiện tại, không dựng lại tồn theo từng ngày trong quá khứ.
- Thống kê tổng tiền bán và nhập; không gọi phần chênh hai số đó là lợi nhuận.
- Tìm kiếm dựa trên `equalsIgnoreCase`/`toLowerCase`/`contains`; không có xử lý bỏ dấu phức tạp.
- Bản snapshot cũ không tự chuyển đổi; nếu phát hiện thì báo để giữ dữ liệu, không âm thầm bỏ qua.

## Kiểm tra đã thực hiện

Bốn chương trình test chạy không cần thư viện ngoài. Các tình huống gồm mở rộng mảng từ sức chứa 0, xóa đầu/giữa/cuối, mã trùng, đổi mã, CRUD, đa hình, bản sao chứng từ, thiếu hàng nhiều dòng, sửa đổi tập SP, hủy nhập làm âm tồn, tràn số, chặn xóa liên kết, giá lịch sử, thống kê theo ngày, ghi/đọc lại Unicode và đúng lớp con, kiểm tra liên kết, không cập nhật tồn lần nữa, file sai ngày/thiếu bộ file, lỗi ghi và thao tác menu qua tiến trình Java thật.

Code sản phẩm và test đều được biên dịch kiểm tra ở mức Java 8 trên JDK 17. Các test tiến trình console chỉ là hỗ trợ kiểm tra tự động, không phải chức năng nghiệp vụ cần trình bày trong đồ án.
