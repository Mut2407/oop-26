import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import models.*;
import services.*;

public class LuuTruFileTextTest {
    public static void main(String[] args) throws IOException {
        DuLieu.dangKyDanhMuc(new DanhMuc("DC", "Dụng cụ thể thao", ""));
        DuLieu.dangKyDanhMuc(new DanhMuc("GIAY", "Giày thể thao", ""));
        Path thuMuc = Files.createTempDirectory("luu-tru-oop-");
        Path tepDanhMuc = thuMuc.resolve("danhmuc.txt");
        Path tepSanPham = thuMuc.resolve("sanpham.txt");
        Path tepNcc = thuMuc.resolve("nhacungcap.txt");
        LuuTruFileText luuTru = new LuuTruFileText(thuMuc.toString());
        try {
            luuTru.ghiDanhMuc("danhmuc.txt", DuLieu.getDanhMuc());
            List<DanhMuc> docDanhMuc = luuTru.docDanhMuc("danhmuc.txt");
            check(docDanhMuc.size() == 2);
            check(docDanhMuc.get(0).getTenDanhMuc().equals("Dụng cụ thể thao"));
            Files.writeString(tepDanhMuc,
                    "maDanhMuc\ttenDanhMuc\tmoTa\nDC\tDung cu\t\ndc\tTrung\t\n",
                    StandardCharsets.UTF_8);
            try {
                luuTru.docDanhMuc("danhmuc.txt");
                throw new AssertionError("Phai tu choi danh muc trung");
            } catch (IOException e) {
                check(e.getMessage().contains("Dong 3"));
            }

            String sanPhamCu = "SAN_PHAM_V1\n"
                    + "GIAY\tG1\tGiay cu\t100.25\tNike\t2\tGIAY\t42.0\tDen\tDe\n"
                    + "DUNG_CU\tD1\tVot cu\t50.50\tYonex\t4\tDC\tCau long\tCarbon\t0.1\n";
            Files.writeString(tepSanPham, sanPhamCu, StandardCharsets.UTF_8);
            DanhSachSanPham ds = luuTru.docSanPham("sanpham.txt", DuLieu.getDanhMuc());
            check(ds.getSoLuong() == 2);
            check(ds.timTheoMa("G1").getDanhMuc() == DuLieu.timDanhMuc("GIAY"));
            check(ds.timTheoMa("G1").getGiaBan().equals(new BigDecimal("100.25")));
            check(ds.timTheoMa("G1").getSoLuongTon() == 2);
            check(((GiayTheThao) ds.timTheoMa("G1")).getKichCoGiay().equals("42.0"));
            luuTru.ghiSanPham("sanpham.txt", ds);
            check(Files.readString(thuMuc.resolve("sanpham.txt.bak")).equals(sanPhamCu));
            check(Files.readAllLines(tepSanPham).get(0).contains("mauSacChung"));
            check(Files.readAllLines(tepSanPham).get(2).split("\t", -1)[10].equals("Cau long"));
            DanhSachSanPham docLai = luuTru.docSanPham("sanpham.txt", DuLieu.getDanhMuc());
            check(docLai.timTheoMa("D1").getSoLuongTon() == 4);
            check(docLai.timTheoMa("D1").getGiaBan().equals(new BigDecimal("50.50")));
            check(docLai.timTheoMa("D1").layLoaiSanPham().equals("DUNG_CU"));

            String loiMoi = Files.readAllLines(tepSanPham).get(0) + "\n"
                    + "GIAY\tG2\tLoi\t10\tNike\t1\tKHONG_CO\t42\tDen\tDe\n";
            Files.writeString(tepSanPham, loiMoi, StandardCharsets.UTF_8);
            try {
                luuTru.ghiSanPham("sanpham.txt", ds);
                throw new AssertionError("Khong duoc ghi de file moi bi loi");
            } catch (IOException e) {
                check(e.getMessage().contains("KHONG_CO"));
                check(Files.readString(tepSanPham).equals(loiMoi));
            }

            String loi = "SAN_PHAM_V1\nGIAY\tG2\tLoi\t10\tNike\t1\tKHONG_CO\t42\tDen\tDe\n";
            Files.writeString(tepSanPham, loi, StandardCharsets.UTF_8);
            try {
                luuTru.ghiSanPham("sanpham.txt", ds);
                throw new AssertionError("Khong duoc ghi de file loi");
            } catch (IOException e) {
                check(e.getMessage().contains("Dong 2") && e.getMessage().contains("KHONG_CO"));
                check(Files.readString(tepSanPham).equals(loi));
            }

            String nccCu = "NHA_CUNG_CAP_V1\nN1\tAn Phat\t0902000001\tHa Noi\n";
            Files.writeString(tepNcc, nccCu, StandardCharsets.UTF_8);
            DanhSachNhaCungCap ncc = luuTru.docNhaCungCap("nhacungcap.txt");
            check(ncc.timTheoMa("N1").getSoDienThoai().equals("0902000001"));
            luuTru.ghiNhaCungCap("nhacungcap.txt", ncc);
            check(Files.readString(thuMuc.resolve("nhacungcap.txt.bak")).equals(nccCu));
            check(luuTru.docNhaCungCap("nhacungcap.txt").timTheoMa("N1").getDiaChi().equals("Ha Noi"));
        } finally {
            for (String ten : List.of("danhmuc.txt", "sanpham.txt", "sanpham.txt.bak",
                    "nhacungcap.txt", "nhacungcap.txt.bak"))
                Files.deleteIfExists(thuMuc.resolve(ten));
            Files.deleteIfExists(thuMuc);
        }
        System.out.println("LuuTruFileTextTest OK");
    }

    private static void check(boolean dung) {
        if (!dung) throw new AssertionError();
    }
}
