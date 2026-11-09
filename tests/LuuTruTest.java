import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.*;
import models.*;
import services.*;

public final class LuuTruTest {
    public static void main(String[] args) throws Exception {
        roundTrip();
        duLieuCu();
        fileLoi();
        System.out.println("LuuTruTest OK: " + TestSupport.assertions + " assertions");
    }

    private static void roundTrip() throws Exception {
        Path thuMuc = Files.createTempDirectory("oop-roundtrip-");
        LuuTruFileText luu = new LuuTruFileText(thuMuc.toString());
        TestSupport.bang(0, luu.docDuLieu().getSanPham().getSoLuong());
        QuanLyCuaHang ql = TestSupport.cuaHang();
        ql.suaLoaiSP("L1", new LoaiSP("L1", "Thể thao", "Mo ta\tco tab\nco dong\rva \\ slash"));
        LoaiSP loai = ql.getLoaiSP().timTheoMa("L1");
        ql.themSanPham(new QuanAoTheThao("AO1", "Áo thể thao", "Nike", new BigDecimal("0.10"), loai, "XL", "Trắng", "Cotton"));
        ql.themSanPham(new DungCuTheThao("DC1", "Vợt", "Yonex", new BigDecimal("0.20"), loai, "Cầu lông", "Carbon", 0.085));
        ql.themPhieuNhap(TestSupport.phieuNhap(ql, "PN1", "SP1", 10));
        ql.themHoaDon(TestSupport.hoaDon(ql, "HD1", "SP1", 3));
        luu.ghiDuLieu(ql);
        QuanLyCuaHang docLai = luu.docDuLieu();
        TestSupport.bang(7, docLai.traCuuTon("SP1"));
        TestSupport.bang(4, docLai.getSanPham().getSoLuong());
        TestSupport.bang(QuanAoTheThao.class, docLai.getSanPham().timTheoMa("AO1").getClass());
        TestSupport.bang(DungCuTheThao.class, docLai.getSanPham().timTheoMa("DC1").getClass());
        TestSupport.bang(ql.getLoaiSP().timTheoMa("L1").getMoTa(), docLai.getLoaiSP().timTheoMa("L1").getMoTa());
        TestSupport.tien("300.30", docLai.thongKeTienBan());
        TestSupport.tien("602.50", docLai.thongKeTienNhap());
        byte[] lanDau = Files.readAllBytes(thuMuc.resolve(LuuTruFileText.TEN_FILE));
        luu.ghiDuLieu(docLai);
        TestSupport.check(java.util.Arrays.equals(lanDau, Files.readAllBytes(thuMuc.resolve(LuuTruFileText.TEN_FILE))));
        TestSupport.check(java.util.Arrays.equals(lanDau, Files.readAllBytes(thuMuc.resolve(LuuTruFileText.TEN_FILE + ".bak"))));
        TestSupport.bang(7, luu.docDuLieu().traCuuTon("SP1"));
        Path snapshot = thuMuc.resolve(LuuTruFileText.TEN_FILE);
        Files.writeString(snapshot, "\uFEFF" + Files.readString(snapshot));
        TestSupport.bang(7, luu.docDuLieu().traCuuTon("SP1"));

        docLai.xoaHoaDon("HD1");
        TestSupport.bang(10, docLai.traCuuTon("SP1"));
        luu.ghiDuLieu(docLai);
        TestSupport.bang(10, luu.docDuLieu().traCuuTon("SP1"));
    }

    private static void duLieuCu() throws Exception {
        Path thuMuc = Files.createTempDirectory("oop-legacy-");
        try (var tep = Files.list(Path.of("data"))) {
            for (Path goc : tep.filter(p -> p.toString().endsWith(".txt")).toList()) {
                Files.copy(goc, thuMuc.resolve(goc.getFileName()));
            }
        }
        LuuTruFileText luu = new LuuTruFileText(thuMuc.toString());
        QuanLyCuaHang ql = luu.docDuLieu();
        TestSupport.bang(4, ql.getSanPham().getSoLuong());
        TestSupport.bang(10, ql.getKhachHang().getSoLuong());
        TestSupport.bang(5, ql.getNhanVien().getSoLuong());
        TestSupport.bang(5, ql.getNhaCungCap().getSoLuong());
        TestSupport.bang(8, ql.traCuuTon("SP03"));
        TestSupport.bang(1, ql.getHoaDon().getSoLuong());
        TestSupport.bang(1, ql.getPhieuNhap().getSoLuong());
        TestSupport.check(ql.getHoaDon().timTheoMa("HD000001").hienThiThongTin().contains("ban nhap"));
        luu.ghiDuLieu(ql);
        TestSupport.bang(8, luu.docDuLieu().traCuuTon("SP03"));
        TestSupport.bang("cau long", ((DungCuTheThao) luu.docDuLieu().getSanPham().timTheoMa("SP01")).getMonTheThao());
        for (String ten : new String[]{"nhanvien.txt", "sanpham.txt", "hoadon.txt", "phieunhap.txt"}) {
            TestSupport.bang(-1L, Files.mismatch(Path.of("data", ten), thuMuc.resolve(ten)));
        }
    }

    private static void fileLoi() throws Exception {
        String[] noiDungLoi = {"", "SAI_DINH_DANG\n", "SPORT_STORE_V2\nKH\tKH1\tA\tbad\tHCM\n",
                "SPORT_STORE_V2\nLOAI\tL1\tA\t\nLOAI\tl1\tB\t\n",
                "SPORT_STORE_V2\nSP\tGIAY\tS1\tGiay\tNike\t100\t0\tMISSING\t42\tDen\tVai\tCao su\n",
                "SPORT_STORE_V2\nKH\tKH1\tA\\q\t0901234567\tHCM\n",
                "SPORT_STORE_V2\nCTHD\tHD404\tSP404\t1\t100\n",
                "SPORT_STORE_V2\nUNKNOWN\t1\n", "SPORT_STORE_V2\nHD\tHD1\t2026-02-30\tKH1\tNV1\n"};
        for (String noiDung : noiDungLoi) {
            Path thuMuc = Files.createTempDirectory("oop-corrupt-");
            Path tep = thuMuc.resolve(LuuTruFileText.TEN_FILE);
            Files.writeString(tep, noiDung);
            try {
                new LuuTruFileText(thuMuc.toString()).docDuLieu();
                throw new AssertionError("Expected IOException for " + noiDung);
            } catch (IOException e) {
                TestSupport.check(e.getMessage().contains(tep.toString()));
                TestSupport.bang(noiDung, Files.readString(tep));
            }
        }
        Path thuMuc = Files.createTempDirectory("oop-write-failure-");
        Path dich = thuMuc.resolve(LuuTruFileText.TEN_FILE);
        Files.createDirectory(dich);
        Files.writeString(dich.resolve("giu-nguyen.txt"), "Giữ nguyên");
        try {
            new LuuTruFileText(thuMuc.toString()).ghiDuLieu(TestSupport.cuaHang());
            throw new AssertionError("Expected IOException for directory target");
        } catch (IOException e) {
            TestSupport.bang("Giữ nguyên", Files.readString(dich.resolve("giu-nguyen.txt")));
        }
        try (var tep = Files.list(thuMuc)) {
            TestSupport.check(tep.noneMatch(p -> p.toString().endsWith(".tmp")));
        }
    }
}
