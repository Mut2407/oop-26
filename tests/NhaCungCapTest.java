import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import models.NhaCungCap;
import services.DanhSachNhaCungCap;
import services.IQuanLyDanhSach;

public class NhaCungCapTest {
    public static void main(String[] args) throws IOException {
        NhaCungCap ncc = new NhaCungCap("N1", "Cửa hàng An Phát", "0902000001", "Hà Nội");
        check(ncc.getMaNhaCungCap().equals("N1"));
        check(ncc.getSoDienThoai().equals("0902000001") && ncc.getDiaChi().equals("Hà Nội"));
        check(ncc.hienThiThongTin().contains("So dien thoai: 0902000001"));
        expectError(() -> new NhaCungCap(" ", "Ten", "0902000001", "Dia chi"));
        expectError(() -> new NhaCungCap("N2", " ", "0902000001", "Dia chi"));
        expectError(() -> new NhaCungCap("N2", "Ten", "Dia chi", "0902000001"));
        expectError(() -> new NhaCungCap("N2", "Ten", "123", "Dia chi"));
        expectError(() -> ncc.capNhatThongTin("Ten moi", "123", "Dia chi moi"));
        check(ncc.getTenNhaCungCap().equals("Cửa hàng An Phát"));
        ncc.capNhatThongTin("Tên mới", "0902000002", "Địa chỉ mới");
        check(ncc.getMaNhaCungCap().equals("N1") && ncc.getDiaChi().equals("Địa chỉ mới"));

        IQuanLyDanhSach<NhaCungCap> quanLy = new DanhSachNhaCungCap();
        DanhSachNhaCungCap ds = (DanhSachNhaCungCap) quanLy;
        check(quanLy.them(ncc));
        check(quanLy.timTheoMa("n1").getSoDienThoai().equals("0902000002"));
        check(quanLy.timGanDung("ten moi").size() == 1);
        check(quanLy.timGanDung("n1").size() == 1);
        expectError(() -> quanLy.them(new NhaCungCap("n1", "Trung", "0902000003", "Hue")));
        ncc.capNhatThongTin("Ngoài danh sách", "0902000003", "Hue");
        check(quanLy.timTheoMa("N1").getTenNhaCungCap().equals("Tên mới"));
        NhaCungCap banSao = quanLy.timTheoMa("N1");
        banSao.capNhatThongTin("Bản sao", "0902000004", "Hue");
        ds.getTatCa().get(0).capNhatThongTin("Bản sao", "0902000004", "Hue");
        expectUnsupported(() -> ds.getTatCa().clear());
        check(quanLy.timTheoMa("N1").getTenNhaCungCap().equals("Tên mới"));
        expectError(() -> quanLy.suaTheoMa("N1", new NhaCungCap("N2", "Sai ma", "0902000005", "Hue")));
        check(quanLy.suaTheoMa("N1", new NhaCungCap("N1", "Nhà cung cấp mới", "0902000005", "Huế")));
        check(!quanLy.suaTheoMa("KHONG_CO", ncc));
        check(quanLy.timGanDung("nha cung cap").size() == 1);

        Path tep = Files.createTempFile("ncc-test-", ".txt");
        try {
            ds.ghiFile(tep.toString());
            List<String> dong = Files.readAllLines(tep, StandardCharsets.UTF_8);
            check(dong.get(0).equals("maNhaCungCap\ttenNhaCungCap\tsoDienThoai\tdiaChi"));
            check(dong.get(1).split("\t", -1)[2].equals("0902000005"));
            DanhSachNhaCungCap docLai = new DanhSachNhaCungCap();
            docLai.docFile(tep.toString());
            check(docLai.timTheoMa("N1").getDiaChi().equals("Huế"));
            Files.writeString(tep, "NHA_CUNG_CAP_V1\nN3\tTen cu\t0902000006\tDia chi cu\n",
                    StandardCharsets.UTF_8);
            docLai.docFile(tep.toString());
            check(docLai.timTheoMa("N3").getSoDienThoai().equals("0902000006"));
            Files.writeString(tep, "NHA_CUNG_CAP_V1\nN4\tLoi\t123\tDia chi\n",
                    StandardCharsets.UTF_8);
            try {
                docLai.docFile(tep.toString());
                throw new AssertionError("Phai tu choi file loi");
            } catch (IOException e) {
                check(e.getMessage().contains("Dong 2"));
                check(docLai.timTheoMa("N3") != null);
            }
        } finally {
            Files.deleteIfExists(tep);
        }
        check(quanLy.xoaTheoMa("N1") && !quanLy.xoaTheoMa("N1"));
        check(ds.getTatCa().isEmpty());
        System.out.println("NhaCungCapTest OK");
    }

    private static void check(boolean dung) {
        if (!dung) throw new AssertionError();
    }

    private static void expectError(Runnable viec) {
        try { viec.run(); } catch (IllegalArgumentException e) { return; }
        throw new AssertionError("Phai tu choi du lieu khong hop le");
    }

    private static void expectUnsupported(Runnable viec) {
        try { viec.run(); } catch (UnsupportedOperationException e) { return; }
        throw new AssertionError("Khong duoc sua danh sach tra ve");
    }
}
