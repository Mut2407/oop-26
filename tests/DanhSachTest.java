import models.*;
import services.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class DanhSachTest {
    public static void main(String[] args) throws IOException {
        DuLieu.dangKyDanhMuc(new DanhMuc("GIAY", "Giay test", ""));
        DuLieu.dangKyDanhMuc(new DanhMuc("AO", "Ao test", ""));
        DuLieu.dangKyDanhMuc(new DanhMuc("DC", "Dung cu test", ""));
        DanhSachSanPham sanPham = new DanhSachSanPham(new SanPham[0]);
        SanPham giay = new GiayTheThao("SP1", "Giay chay", "Nike", new BigDecimal("100"), 2, "Den", null, DuLieu.timDanhMuc("GIAY"), "42", "Cao su");
        sanPham.them(giay);
        check(sanPham.timTheoMa("sp1") != giay);
        check(sanPham.timTheoMa("sp1").getMaSanPham().equals("SP1"));
        check(sanPham.locTheoLoai("giay").size() == 1);
        check(sanPham.timGanDung("chay").size() == 1);
        expectError(() -> sanPham.them(giay));
        expectError(() -> sanPham.them(new GiayTheThao("SP2", "Giay", "Nike", new BigDecimal("-1"), 1, "Den", null, DuLieu.timDanhMuc("GIAY"), "42", "De")));
        check(sanPham.suaTheoMa("SP1", new QuanAoTheThao("SP1", "Ao", "Adidas", new BigDecimal("50"), 3, null, null, DuLieu.timDanhMuc("AO"), "M")));
        check(sanPham.timTheoMa("SP1") instanceof QuanAoTheThao);
        check(sanPham.xoaTheoMa("SP1") && sanPham.getDanhSach().isEmpty());

        DanhSachNhaCungCap nhaCungCap = new DanhSachNhaCungCap(new NhaCungCap[0]);
        nhaCungCap.them(new NhaCungCap("NCC1", "An Phat", "0902000001", "TP.HCM"));
        check(nhaCungCap.timTheoMa("ncc1").getSoDienThoai().startsWith("0"));
        expectError(() -> nhaCungCap.them(new NhaCungCap("ncc1", "Khac", "0902000002", "TP.HCM")));
        expectError(() -> nhaCungCap.them(new NhaCungCap("NCC2", "Khac", "123", "TP.HCM")));
        check(nhaCungCap.suaTheoMa("NCC1", new NhaCungCap("NCC1", "Binh Minh", "0902000003", "Ha Noi")));
        check(nhaCungCap.timGanDung("binh").size() == 1);
        check(nhaCungCap.xoaTheoMa("NCC1") && nhaCungCap.getDanhSach().isEmpty());
        kiemTraBaoVeDanhSach();
        kiemTraCrudKhachNhanVien();
        kiemTraMain();
        kiemTraLuuFile();
        System.out.println("DanhSachTest OK");
    }

    private static void kiemTraBaoVeDanhSach() {
        SanPham dauVao = new GiayTheThao("S1", "Giay", "Hang", new BigDecimal("100"), 2, "Den", null, DuLieu.timDanhMuc("GIAY"), "42", "De");
        DanhSachSanPham ds = new DanhSachSanPham(new SanPham[] { dauVao });
        expectError(() -> dauVao.giamTon(3));
        expectError(() -> dauVao.capNhatGiaBan(new BigDecimal("-1")));
        dauVao.tangTon(1);
        check(ds.timTheoMa("S1").getSoLuongTon() == 2 && ds.timTheoMa("S1").getGiaBan().compareTo(new BigDecimal("100")) == 0);
        List<SanPham> banSao = ds.getDanhSach();
        banSao.get(0).tangTon(1);
        expectError(() -> ds.timTheoMa("S1").giamTon(3));
        expectError(() -> ds.timGanDung("Giay").get(0).capNhatGiaBan(new BigDecimal("-1")));
        check(ds.timTheoMa("S1") != null && ds.timTheoMa("TRUNG") == null);
        check(ds.timTheoMa("S1").getSoLuongTon() == 2 && ds.timTheoMa("S1").getGiaBan().compareTo(new BigDecimal("100")) == 0);
        SanPham moi = new QuanAoTheThao("S2", "Ao", "Hang", new BigDecimal("50"), 1, null, null, DuLieu.timDanhMuc("AO"), "M");
        ds.them(moi);
        moi.tangTon(1);
        check(ds.timTheoMa("S2") != null);
        check(ds.timTheoMa("S2").getSoLuongTon() == 1);
        expectError(() -> ds.them(new QuanAoTheThao("S1", "Trung", "Hang", new BigDecimal("50"), 1, null, null, DuLieu.timDanhMuc("AO"), "M")));
        expectError(() -> ds.suaTheoMa("S1", new GiayTheThao("S1", "Loi", "Hang", new BigDecimal("-1"), 1, "Den", null, DuLieu.timDanhMuc("GIAY"), "42", "De")));
        SanPham sua = new QuanAoTheThao("S2", "Ao moi", "Hang", new BigDecimal("60"), 3, null, null, DuLieu.timDanhMuc("AO"), "L");
        check(ds.suaTheoMa("S2", sua));
        sua.tangTon(1);
        check(ds.timTheoMa("S2").getSoLuongTon() == 3);
        check(ds.xoaTheoMa("S2") && ds.timTheoMa("S2") == null);

        NhaCungCap ncc = new NhaCungCap("N1", "Nha cung cap", "0902000001", "Dia chi");
        DanhSachNhaCungCap nhaCungCap = new DanhSachNhaCungCap(new NhaCungCap[] { ncc });
        ncc.capNhatThongTin("Ten khac", "0902000002", "Dia chi khac");
        nhaCungCap.timTheoMa("N1").capNhatThongTin("Ten khac", "0902000002", "Dia chi khac");
        nhaCungCap.getDanhSach().get(0).capNhatThongTin("Ten khac", "0902000002", "Dia chi khac");
        nhaCungCap.timGanDung("Nha").get(0).capNhatThongTin("Ten khac", "0902000002", "Dia chi khac");
        check(nhaCungCap.timTheoMa("N1") != null && nhaCungCap.timTheoMa("N2") == null);
        check(nhaCungCap.timTheoMa("N1").getSoDienThoai().equals("0902000001"));
        NhaCungCap suaNcc = new NhaCungCap("N1", "Nha moi", "0902000002", "Dia chi");
        check(nhaCungCap.suaTheoMa("N1", suaNcc));
        suaNcc.capNhatThongTin("Ten khac", "0902000003", "Dia chi khac");
        check(nhaCungCap.timTheoMa("N1").getTenNhaCungCap().equals("Nha moi"));
        check(nhaCungCap.xoaTheoMa("N1") && nhaCungCap.getDanhSach().isEmpty());
    }

    private static void kiemTraCrudKhachNhanVien() {
        DanhSachKhachHang kh = new DanhSachKhachHang();
        kh.them(new KhachHang("K1", "Khach", "0901000001", "Ha Noi"));
        kh.themNhieu(new KhachHang[] { new KhachHang("K2", "Khach hai", "0901000002", "Hue") });
        check(kh.timTheoMa("k1") != null && kh.timKiem(null, "hai", null, null, false).length == 1);
        check(kh.sua("K1", "Khach moi", "0901000003", "Ha Noi"));
        check(kh.timTheoMa("K1").getHoTen().equals("Khach moi"));
        expectError(() -> kh.them(new KhachHang("K1", "Trung", "0901000004", "Hue")));
        check(kh.xoa("K2") && kh.getSoLuong() == 1);

        DanhSachNhanVien nv = new DanhSachNhanVien();
        nv.them(new NhanVien("V1", "Nhan vien", "0901000001", "Sang"), "Ha Noi");
        nv.themNhieu(new NhanVien[] { new NhanVien("V2", "Nhan vien hai", "0901000002", "Chieu") });
        check(nv.timTheoMa("v1") != null && nv.timKiem(null, "hai", null, null, null, false).length == 1);
        check(nv.sua("V1", "Nhan vien moi", "Toi", "0901000003", "Ha Noi"));
        check(nv.timTheoMa("V1").getCaLamViec().equals("Toi"));
        expectError(() -> nv.them(new NhanVien("V1", "Trung", "0901000004", "Sang"), "Hue"));
        check(nv.xoa("V2") && nv.getSoLuong() == 1);
    }

    private static void kiemTraMain() throws IOException {
        List<String> dong = Files.readAllLines(Path.of("src/Main.java"), StandardCharsets.UTF_8);
        int batDau = -1;
        for (int i = 0; i < dong.size(); i++) {
            if (dong.get(i).contains("public static void main(String[] args)")) batDau = i;
            if (batDau >= 0 && i > batDau && dong.get(i).equals("    }")) {
                check(i - batDau + 1 <= 10);
                return;
            }
        }
        throw new AssertionError("Khong tim thay main() trong src/Main.java");
    }

    private static void kiemTraLuuFile() throws IOException {
        Path thuMuc = Files.createTempDirectory("sport-store-test-");
        Path tepSanPham = thuMuc.resolve("sanpham.txt");
        Path tepNhaCungCap = thuMuc.resolve("nhacungcap.txt");
        Path tepKhachHang = thuMuc.resolve("khachhang.txt");
        Path tepNhanVien = thuMuc.resolve("nhanvien.txt");
        try {
            DanhSachSanPham sanPham = new DanhSachSanPham(new SanPham[] {
                new GiayTheThao("G1", "Giay\tchay", "Nike", new BigDecimal("100.25"), 2, "Den", "Vai", DuLieu.timDanhMuc("GIAY"), "42", "Cao su"),
                new DungCuTheThao("D1", "Vot", "Yonex", new BigDecimal("50"), 1, "Den", "Carbon", DuLieu.timDanhMuc("DC"), 0.1),
                new QuanAoTheThao("A1", "Ao", "Adidas", new BigDecimal("30"), 3, null, null, DuLieu.timDanhMuc("AO"), "M")
            });
            sanPham.ghiFile(tepSanPham.toString());
            check(Files.readAllLines(tepSanPham).get(0).startsWith("kieu\tmaSanPham\t"));
            DanhSachSanPham docLai = new DanhSachSanPham(new SanPham[0]);
            docLai.docFile(tepSanPham.toString());
            check(docLai.getDanhSach().size() == 3);
            check(docLai.timTheoMa("G1").getTenSanPham().equals("Giay\tchay"));
            check(docLai.timTheoMa("G1").getGiaBan().equals(new BigDecimal("100.25")));
            check(docLai.timTheoMa("G1").getDanhMuc() == DuLieu.timDanhMuc("GIAY"));
            check(docLai.timTheoMa("G1").getChatLieu().equals("Vai"));
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
            check(((GiayTheThao) sanPham.timTheoMa("G2")).getKichCoGiay().equals("42.0"));
            Files.writeString(tepSanPham,
                    "SAN_PHAM_V1\nDUNG_CU\tD2\tVot cu\t50.0\tYonex\t1\tDC\tCau long\tCarbon\t0.1\n",
                    StandardCharsets.UTF_8);
            sanPham.docFile(tepSanPham.toString());
            sanPham.ghiFile(tepSanPham.toString());
            check(Files.readAllLines(tepSanPham).get(1).split("\t", -1)[10].equals("Cau long"));
            Files.writeString(tepSanPham,
                    "kieu\tmaSanPham\ttenSanPham\tgiaBan\tthuongHieu\tsoLuongTon\tmaLoai\tkichCoGiay\tmauSac\tloaiDeGiay\tmonTheThao\tvatLieu\tkhoiLuongKg\tkichCoQuanAo\n"
                    + "DUNG_CU\tD3\tVot cu\t50.25\tYonex\t1\tDC\t\t\t\tCau long\tCarbon\t0.1\t\n",
                    StandardCharsets.UTF_8);
            sanPham.docFile(tepSanPham.toString());
            sanPham.ghiFile(tepSanPham.toString());
            check(Files.readAllLines(tepSanPham).get(1).split("\t", -1)[10].equals("Cau long"));
            check(sanPham.timTheoMa("D3").getGiaBan().equals(new BigDecimal("50.25")));
            Files.writeString(tepSanPham,
                    "SAN_PHAM_V1\nGIAY\tG3\tKhong ro\t10\tNike\t1\tCHUA_CO\t42\tDen\tDe\n",
                    StandardCharsets.UTF_8);
            try {
                sanPham.docFile(tepSanPham.toString());
                throw new AssertionError("Ma danh muc chua anh xa phai bi tu choi");
            } catch (IOException e) {
                check(e.getMessage().contains("CHUA_CO"));
                check(sanPham.timTheoMa("D3") != null);
            }

            DanhSachNhaCungCap ncc = new DanhSachNhaCungCap(new NhaCungCap[] {
                new NhaCungCap("N1", "An\nPhat", "0902000001", "TP.HCM")
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

            DanhSachNhanVien nv = new DanhSachNhanVien();
            nv.them(new NhanVien("NV1", "Nhan vien", "0901000001", "Sang"), "Ha Noi");
            nv.ghiFile(tepNhanVien.toString());
            nv.docFile(tepNhanVien.toString());
            check(nv.getDanhSach()[0].getSoDienThoai().equals("0901000001"));
            check(nv.getDiaChiCu("NV1").equals("Ha Noi"));
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
