import models.DanhMuc;
import models.DuLieu;

public class DanhMucTest {
    public static void main(String[] args) {
        DanhMuc danhMuc = new DanhMuc("GIAY", "Giay the thao", "");
        DuLieu.dangKyDanhMuc(danhMuc);
        check(DuLieu.timDanhMuc("giay") == danhMuc);
        check(DuLieu.getDanhMuc().get(0) == danhMuc);
        check(DuLieu.timDanhMuc("KHONG_CO") == null);
        expectError(() -> new DanhMuc(" ", "Giay", ""));
        expectError(() -> new DanhMuc("DM", " ", ""));
        expectError(() -> DuLieu.dangKyDanhMuc(new DanhMuc("giay", "Trung", "")));
        expectError(() -> DuLieu.dangKyDanhMuc(null));
        danhMuc.capNhatThongTin("Giay moi", "Mo ta moi");
        check(danhMuc.getMaDanhMuc().equals("GIAY"));
        check(danhMuc.getTenDanhMuc().equals("Giay moi"));
        check(danhMuc.hienThiThongTin().contains("Mo ta moi"));
        expectError(() -> danhMuc.capNhatThongTin("", "Sai"));
        check(danhMuc.getTenDanhMuc().equals("Giay moi"));

        check(DuLieu.xoaDanhMuc("giay") && DuLieu.timDanhMuc("GIAY") == null);
        check(!DuLieu.xoaDanhMuc("NONE"));
        System.out.println("DanhMucTest OK");
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
        throw new AssertionError("Phai tu choi du lieu khong hop le");
    }
}
