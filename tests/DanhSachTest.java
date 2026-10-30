import models.*;
import services.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class DanhSachTest {
    public static void main(String[] args) throws IOException {
        DanhSachSanPham sanPham = new DanhSachSanPham(new SanPham[0]);
        SanPham giay = new GiayTheThao("SP1", "Giay chay", 100, "Nike", 2, "GIAY", 42, "Den", "Cao su");
        sanPham.them(giay);
        check(sanPham.timTheoMa("sp1") == giay);
        check(sanPham.locTheoLoai("giay").size() == 1);
        check(sanPham.timGanDung("chay").size() == 1);
        expectError(() -> sanPham.them(giay));
        expectError(() -> sanPham.them(new GiayTheThao("SP2", "Giay", -1, "Nike", 1, "GIAY", 42, "Den", "De")));
        check(sanPham.suaTheoMa("SP1", new QuanAoTheThao("SP1", "Ao", 50, "Adidas", 3, "AO", "M")));
        check(sanPham.timTheoMa("SP1") instanceof QuanAoTheThao);
        check(sanPham.xoaTheoMa("SP1") && sanPham.getDanhSach().isEmpty());

        DanhSachNhaCungCap nhaCungCap = new DanhSachNhaCungCap(new NhaCungCap[0]);
        nhaCungCap.them(new NhaCungCap("NCC1", "An Phat", "TP.HCM", "0902000001"));
        check(nhaCungCap.timTheoMa("ncc1").getSoDienThoai().startsWith("0"));
        expectError(() -> nhaCungCap.them(new NhaCungCap("ncc1", "Khac", "TP.HCM", "0902000002")));
        expectError(() -> nhaCungCap.them(new NhaCungCap("NCC2", "Khac", "TP.HCM", "123")));
        check(nhaCungCap.suaTheoMa("NCC1", new NhaCungCap("NCC1", "Binh Minh", "Ha Noi", "0902000003")));
        check(nhaCungCap.timGanDung("binh").size() == 1);
        check(nhaCungCap.xoaTheoMa("NCC1") && nhaCungCap.getDanhSach().isEmpty());
        kiemTraLuuFile();
        System.out.println("DanhSachTest OK");
    }

    private static void kiemTraLuuFile() throws IOException {
        Path thuMuc = Files.createTempDirectory("sport-store-test-");
        Path tepSanPham = thuMuc.resolve("sanpham.txt");
        Path tepNhaCungCap = thuMuc.resolve("nhacungcap.txt");
        Path tepKhachHang = thuMuc.resolve("khachhang.txt");
        Path tepNhanVien = thuMuc.resolve("nhanvien.txt");
        try {
            DanhSachSanPham sanPham = new DanhSachSanPham(new SanPham[] {
                new GiayTheThao("G1", "Giay\tchay", 100, "Nike", 2, "GIAY", 42, "Den", "Cao su"),
                new DungCuTheThao("D1", "Vot", 50, "Yonex", 1, "DC", "Cau long", "Carbon", 0.1),
                new QuanAoTheThao("A1", "Ao", 30, "Adidas", 3, "AO", "M")
            });
            sanPham.ghiFile(tepSanPham.toString());
            check(Files.readAllLines(tepSanPham).get(0).startsWith("kieu\tmaSanPham\t"));
            DanhSachSanPham docLai = new DanhSachSanPham(new SanPham[0]);
            docLai.docFile(tepSanPham.toString());
            check(docLai.getDanhSach().size() == 3);
            check(docLai.timTheoMa("G1").getTenSanPham().equals("Giay\tchay"));
            check(docLai.timTheoMa("D1") instanceof DungCuTheThao);
            check(docLai.timTheoMa("A1") instanceof QuanAoTheThao);
            docLai.xoaTheoMa("G1");
            docLai.ghiFile(tepSanPham.toString());
            sanPham.docFile(tepSanPham.toString());
            check(sanPham.timTheoMa("G1") == null);
            Files.writeString(tepSanPham,
                    "SAN_PHAM_V1\nGIAY\tG2\tGiay cu\t100.0\tNike\t2\tGIAY\t42.0\tDen\tCao su\n",
                    StandardCharsets.UTF_8);
            sanPham.docFile(tepSanPham.toString());
            check(sanPham.timTheoMa("G2") instanceof GiayTheThao);

            DanhSachNhaCungCap ncc = new DanhSachNhaCungCap(new NhaCungCap[] {
                new NhaCungCap("N1", "An\nPhat", "TP.HCM", "0902000001")
            });
            ncc.ghiFile(tepNhaCungCap.toString());
            check(Files.readAllLines(tepNhaCungCap).get(0).equals(
                    "maNhaCungCap\ttenNhaCungCap\tsoDienThoai\tdiaChi"));
            ncc.xoaTheoMa("N1");
            ncc.docFile(tepNhaCungCap.toString());
            check(ncc.timTheoMa("N1").getTenNhaCungCap().equals("An\nPhat"));
            Files.writeString(tepNhaCungCap, "NHA_CUNG_CAP_V1\nN2\tLoi\t123\tDia chi\n", StandardCharsets.UTF_8);
            try {
                ncc.docFile(tepNhaCungCap.toString());
                throw new AssertionError("File loi phai bi tu choi");
            } catch (IOException e) {
                check(ncc.timTheoMa("N1") != null);
            }
            DanhSachKhachHang kh = new DanhSachKhachHang(new KhachHang[0]);
            Files.writeString(tepKhachHang,
                    "KHACH_HANG_V1\nKH1\tKhach cu\t0901234567\tHa Noi\n",
                    StandardCharsets.UTF_8);
            kh.docFile(tepKhachHang.toString());
            kh.ghiFile(tepKhachHang.toString());
            check(Files.readAllLines(tepKhachHang).get(0).startsWith("maKhachHang\t"));
            check(kh.timTheoMa("KH1").getSoDienThoai().equals("0901234567"));

            DanhSachNhanVien nv = new DanhSachNhanVien(new NhanVien[] {
                new NhanVien("NV1", "Nhan vien", "Sang", "0901000001", "Ha Noi")
            });
            nv.ghiFile(tepNhanVien.toString());
            nv.docFile(tepNhanVien.toString());
            check(nv.getDanhSach().get(0).getSoDienThoai().equals("0901000001"));
        } finally {
            Files.deleteIfExists(tepSanPham);
            Files.deleteIfExists(tepNhaCungCap);
            Files.deleteIfExists(tepKhachHang);
            Files.deleteIfExists(tepNhanVien);
            Files.deleteIfExists(thuMuc);
        }
    }

    private static void check(boolean dung) {
        if (!dung) throw new AssertionError();
    }

    private static void expectError(Runnable viec) {
        try {
            viec.run();
        } catch (IllegalArgumentException e) {
            return;
        }
        throw new AssertionError("Phai bao loi du lieu khong hop le");
    }
}
