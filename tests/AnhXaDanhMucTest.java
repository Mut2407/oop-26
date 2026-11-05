import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import models.DanhMuc;
import models.DuLieu;
import models.SanPham;
import services.DanhSachSanPham;

public class AnhXaDanhMucTest {
    public static void main(String[] args) throws IOException {
        DuLieu.khoiTaoDanhMuc();
        DanhMuc dc = DuLieu.timDanhMuc("dc");
        DanhMuc giay = DuLieu.timDanhMuc("GiAy");
        check(dc != null && dc.getTenDanhMuc().equals("Dụng cụ thể thao"));
        check(giay != null && giay.getTenDanhMuc().equals("Giày thể thao"));
        check(DuLieu.getDanhMuc().size() == 2 && DuLieu.timDanhMuc("AO") == null);
        expectError(() -> DuLieu.dangKyDanhMuc(new DanhMuc("dC", "Trung", "")));

        SanPham[] mau = DuLieu.taoDanhSachSanPham();
        check(mau[0].getDanhMuc() == mau[1].getDanhMuc());
        check(mau[2].getDanhMuc() == mau[3].getDanhMuc());
        check(mau[0].getDanhMuc() == giay && mau[2].getDanhMuc() == dc);

        Path tep = Files.createTempFile("sanpham-cu-", ".txt");
        try {
            Files.writeString(tep, "SAN_PHAM_V1\n"
                    + "GIAY\tG1\tGiay\t100.25\tNike\t2\tGIAY\t42.0\tDen\tDe\n"
                    + "GIAY\tG2\tGiay khac\t50\tNike\t3\tGIAY\t41\tTrang\tDe\n"
                    + "DUNG_CU\tD1\tVot\t20.50\tYonex\t4\tDC\tCau long\tCarbon\t0.1\n",
                    StandardCharsets.UTF_8);
            byte[] truoc = Files.readAllBytes(tep);
            DanhSachSanPham ds = new DanhSachSanPham(new SanPham[0]);
            ds.docFile(tep.toString());
            check(ds.timTheoMa("G1").getDanhMuc() == ds.timTheoMa("G2").getDanhMuc());
            check(ds.timTheoMa("G1").getDanhMuc() == giay && ds.timTheoMa("D1").getDanhMuc() == dc);
            check(ds.timTheoMa("G1").getGiaBan().equals(new BigDecimal("100.25")));
            check(ds.timTheoMa("D1").getSoLuongTon() == 4);
            check(ds.timTheoMa("D1").layLoaiSanPham().equals("DUNG_CU"));
            check(Arrays.equals(truoc, Files.readAllBytes(tep)));

            Files.writeString(tep, "SAN_PHAM_V1\n"
                    + "GIAY\tG3\tKhong ro\t10\tNike\t1\tCHUA_CO\t42\tDen\tDe\n",
                    StandardCharsets.UTF_8);
            byte[] loi = Files.readAllBytes(tep);
            try {
                ds.docFile(tep.toString());
                throw new AssertionError("Phai bao ma chua anh xa");
            } catch (IOException e) {
                check(e.getMessage().contains("Dong 2") && e.getMessage().contains("CHUA_CO"));
                check(ds.timTheoMa("G1") != null);
                check(Arrays.equals(loi, Files.readAllBytes(tep)));
            }
        } finally {
            Files.deleteIfExists(tep);
        }
        System.out.println("AnhXaDanhMucTest OK");
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
        throw new AssertionError("Phai tu choi ma danh muc trung");
    }
}
