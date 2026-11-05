import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import models.DuLieu;
import models.NhanVien;
import services.DanhSachNhanVien;

public class NhanVienDiaChiCuTest {
    private static final String HEADER = "maNhanVien\thoTen\tcaLamViec\tsoDienThoai\tdiaChi\n";
    private static final String DIA_CHI = "  Ngõ 1\tHà Nội\nTầng 2\\A  ";
    private static final String CU = HEADER
            + "Nv01\tNguyễn Ánh\tSáng\t0901234567\t  Ngõ 1\\tHà Nội\\nTầng 2\\\\A  \n"
            + "NV02\tTrần Bình\tChiều\t0912345678\tHuế\n";
    private static int soCa;

    public static void main(String[] args) throws Exception {
        Path dir = Files.createTempDirectory("task061-dia-chi-");
        try {
            docGhiNguyenVen(dir);
            suaVaThem(dir);
            nhapLoNguyenKhoi(dir);
            saoChepMetadata(dir);
            xoaVaNhapLai(dir);
            docLoiKhongThayDuLieu(dir);
            chanGhiThieuMetadata(dir);
            tachBietNguonDuLieu(dir);
            duLieuMau();
        } finally {
            try (var paths = Files.walk(dir)) {
                for (Path path : paths.sorted(Comparator.reverseOrder()).toList()) {
                    Files.delete(path);
                }
            }
        }
        System.out.println("NhanVienDiaChiCuTest OK (" + soCa + " cases)");
    }

    private static DanhSachNhanVien nap(Path path) throws IOException {
        Files.writeString(path, CU);
        DanhSachNhanVien ds = new DanhSachNhanVien();
        ds.docFile(path.toString());
        return ds;
    }

    private static void docGhiNguyenVen(Path dir) throws Exception {
        Path path = dir.resolve("roundtrip.txt");
        DanhSachNhanVien ds = nap(path);
        check(ds.getSoLuong() == 2);
        check(ds.timTheoMa("nv01").getMaNhanVien().equals("Nv01"));
        check(ds.timTheoMa("nv01").getSoDienThoai().equals("0901234567"));
        check(ds.timTheoMa("nv01").getCaLamViec().equals("Sáng"));
        check(ds.getDiaChiCu(" nv01 ").equals(DIA_CHI));
        check(ds.getDiaChiCu("NV02").equals("Huế"));
        ds.ghiFile(path.toString());
        check(Files.readString(path).equals(CU));
        DanhSachNhanVien moLai = new DanhSachNhanVien();
        moLai.docFile(path.toString());
        check(moLai.getDiaChiCuTheoMa().equals(ds.getDiaChiCuTheoMa()));
        check(moLai.timTheoMa("NV01").getHoTen().equals("Nguyễn Ánh"));
        check(ds.timKiem(null, "ánh", "sáng", "090", "hà nội", false).length == 1);
        check(ds.thongKe().contains("Huế: 1 nhan vien"));
        soCa++;
    }

    private static void suaVaThem(Path dir) throws Exception {
        Path path = dir.resolve("edit.txt");
        DanhSachNhanVien ds = nap(path);
        check(ds.sua("nv01", "Ánh mới", "Tối", "+84901234567"));
        check(ds.timTheoMa("NV01").getMaNhanVien().equals("Nv01"));
        check(ds.getDiaChiCu("NV01").equals(DIA_CHI));
        String truoc = ds.timTheoMa("NV01").hienThiThongTin();
        loi(() -> ds.sua("NV01", "Sai", "Sáng", "bad", "Địa chỉ khác"));
        loi(() -> ds.sua("NV01", "Sai", "Sáng", "0901234567", ""));
        check(ds.timTheoMa("NV01").hienThiThongTin().equals(truoc));
        check(ds.getDiaChiCu("NV01").equals(DIA_CHI));
        check(!ds.sua("UNKNOWN", "Tên", "Sáng", "0901234567"));
        check(ds.sua("NV02", "Bình mới", "Sáng", "0912345678", "Đà Nẵng"));
        ds.them(new NhanVien("NV03", "Mới", "0923456789", "Chiều"));
        ds.ghiFile(path.toString());
        ds.docFile(path.toString());
        check(ds.getDiaChiCu("NV01").equals(DIA_CHI));
        check(ds.getDiaChiCu("NV02").equals("Đà Nẵng"));
        check(ds.getDiaChiCu("NV03") == null);
        check(ds.timTheoMa("NV03").getCaLamViec().equals("Chiều"));
        check(ds.thongKe().contains("Metadata dia chi chua day du"));
        check(ds.timKiem(null, null, null, null, "Đà Nẵng", true).length == 1);
        soCa++;
    }

    private static void nhapLoNguyenKhoi(Path dir) throws Exception {
        DanhSachNhanVien ds = nap(dir.resolve("batch.txt"));
        Map<String, String> truoc = ds.getDiaChiCuTheoMa();
        NhanVien moi = new NhanVien("NV03", "Mới", "0923456789", "Sáng");
        NhanVien trung = new NhanVien("nv01", "Trùng", "0934567890", "Chiều");
        loi(() -> ds.themNhieu(new NhanVien[]{moi, trung}));
        loi(() -> ds.nhapDanhSach(new NhanVien[]{moi, moi}));
        loi(() -> ds.themNhieu(new NhanVien[]{moi, null}));
        check(ds.getSoLuong() == 2 && ds.timTheoMa("NV03") == null);
        check(ds.getDiaChiCuTheoMa().equals(truoc));
        ds.themNhieu(new NhanVien[]{moi});
        check(ds.getSoLuong() == 3 && ds.getDiaChiCuTheoMa().equals(truoc));
        ds.nhapDanhSach(ds.getDanhSach());
        check(ds.getDiaChiCuTheoMa().equals(truoc));
        soCa++;
    }

    private static void saoChepMetadata(Path dir) throws Exception {
        DanhSachNhanVien ds = nap(dir.resolve("copy.txt"));
        DanhSachNhanVien copy = new DanhSachNhanVien(ds);
        ds.timTheoMa("NV01").capNhatLienHe("0999999999");
        ds.getDanhSach()[0].doiCaLamViec("Tối");
        ds.timKiem(null, null, null, null, null, false)[0].doiCaLamViec("Tối");
        check(ds.timTheoMa("NV01").getSoDienThoai().equals("0901234567"));
        check(ds.timTheoMa("NV01").getCaLamViec().equals("Sáng"));
        loiLoai(UnsupportedOperationException.class, () -> ds.getDiaChiCuTheoMa().put("NV01", "Sai"));
        copy.sua("NV01", "Tên mới", "Sáng", "0901234567", "Huế");
        check(ds.getDiaChiCu("NV01").equals(DIA_CHI));
        Map<String, String> metadata = new LinkedHashMap<>(ds.getDiaChiCuTheoMa());
        DanhSachNhanVien copy2 = new DanhSachNhanVien(ds.getDanhSach(), metadata);
        metadata.put("NV01", "Sai");
        check(copy2.getDiaChiCu("NV01").equals(DIA_CHI));
        loi(() -> new DanhSachNhanVien(ds.getDanhSach(), Map.of("UNKNOWN", "Huế")));
        loi(() -> new DanhSachNhanVien(ds.getDanhSach(), Map.of("NV01", "")));
        loi(() -> new DanhSachNhanVien(ds.getDanhSach(), Map.of("NV01", "Huế", "nv01", "Hà Nội")));
        copy2.ghiFile(dir.resolve("copy-new-path.txt").toString());
        DanhSachNhanVien docLai = new DanhSachNhanVien();
        docLai.docFile(dir.resolve("copy-new-path.txt").toString());
        check(docLai.getDiaChiCuTheoMa().equals(ds.getDiaChiCuTheoMa()));
        soCa++;
    }

    private static void xoaVaNhapLai(Path dir) throws Exception {
        Path path = dir.resolve("delete.txt");
        DanhSachNhanVien ds = nap(path);
        check(ds.xoa("nv01"));
        check(ds.getDiaChiCu("NV01") == null && ds.getDiaChiCu("NV02").equals("Huế"));
        check(!ds.xoa("nv01"));
        ds.ghiFile(path.toString());
        ds.them(new NhanVien("NV01", "Người mới", "0934567890", "Tối"));
        ds.ghiFile(path.toString());
        ds.docFile(path.toString());
        check(ds.getDiaChiCu("NV01") == null);
        check(ds.getDiaChiCu("NV02").equals("Huế"));
        ds.nhapDanhSach(new NhanVien[]{new NhanVien("NV02", "Bình", "0912345678", "Tối")});
        check(ds.getDiaChiCu("NV02").equals("Huế") && ds.getSoLuong() == 1);
        soCa++;
    }

    private static void docLoiKhongThayDuLieu(Path dir) throws Exception {
        Path path = dir.resolve("invalid.txt");
        DanhSachNhanVien ds = nap(path);
        Map<String, String> truoc = ds.getDiaChiCuTheoMa();
        String[] sai = {
            "not a header\n",
            HEADER + "N1\tAn\tSáng\tbad\tHuế\n",
            HEADER + "N1\tAn\tSáng\t0901234567\n",
            HEADER + "N1\tAn\tSáng\t0901234567\tHuế\\q\n",
            HEADER + "N1\tAn\tSáng\t0901234567\tHuế\n" + "n1\tBình\tTối\t0912345678\tHà Nội\n",
            HEADER + "N1\t\tSáng\t0901234567\tHuế\n"
        };
        for (String noiDung : sai) {
            Files.writeString(path, noiDung);
            loiLoai(IOException.class, () -> ds.docFile(path.toString()));
            check(ds.getSoLuong() == 2 && ds.getDiaChiCuTheoMa().equals(truoc));
            loiLoai(IOException.class, () -> ds.ghiFile(path.toString()));
            check(Files.readString(path).equals(noiDung));
        }
        soCa++;
    }

    private static void chanGhiThieuMetadata(Path dir) throws Exception {
        Path path = dir.resolve("missing-metadata.txt");
        DanhSachNhanVien ds = nap(path);
        DanhSachNhanVien chiModel = new DanhSachNhanVien(ds.getDanhSach());
        try {
            chiModel.ghiFile(path.toString());
            throw new AssertionError("Phai chan ghi mat dia chi");
        } catch (IOException expected) {
            check(expected.getMessage().contains("thieu metadata dia chi cu"));
            check(expected.getMessage().contains("Nv01"));
        }
        check(Files.readString(path).equals(CU));
        soCa++;
    }

    private static void tachBietNguonDuLieu(Path dir) throws Exception {
        DanhSachNhanVien a = nap(dir.resolve("a.txt"));
        DanhSachNhanVien b = new DanhSachNhanVien();
        b.them(new NhanVien("NV01", "Khác", "0987654321", "Tối"), "Địa chỉ B");
        b.ghiFile(dir.resolve("b.txt").toString());
        b.docFile(dir.resolve("b.txt").toString());
        a.ghiFile(dir.resolve("a.txt").toString());
        a.docFile(dir.resolve("a.txt").toString());
        check(a.getDiaChiCu("NV01").equals(DIA_CHI));
        check(b.getDiaChiCu("NV01").equals("Địa chỉ B"));
        soCa++;
    }

    private static void duLieuMau() {
        DanhSachNhanVien ds = new DanhSachNhanVien(DuLieu.taoDanhSachNhanVien(), DuLieu.taoDiaChiNhanVienCu());
        check(ds.getSoLuong() == 5 && ds.getDiaChiCuTheoMa().size() == 5);
        check(ds.getDiaChiCu("NV01").equals("Quan 1, TP.HCM"));
        check(ds.getDiaChiCu("NV05").equals("Thu Duc, TP.HCM"));
        check(ds.timTheoMa("NV01").getCaLamViec().equals("Sang"));
        check(Arrays.stream(ds.getDanhSach()).allMatch(nv -> nv.getSoDienThoai().startsWith("0901")));
        soCa++;
    }

    private interface Action {
        void run() throws Exception;
    }

    private static void loi(Action action) throws Exception {
        loiLoai(IllegalArgumentException.class, action);
    }

    private static void loiLoai(Class<? extends Exception> type, Action action) throws Exception {
        try {
            action.run();
        } catch (Exception e) {
            if (type.isInstance(e)) {
                return;
            }
            throw e;
        }
        throw new AssertionError("Phai bao loi " + type.getSimpleName());
    }

    private static void check(boolean condition) {
        if (!condition) {
            throw new AssertionError();
        }
    }
}
