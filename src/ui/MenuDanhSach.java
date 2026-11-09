package ui;

import java.io.UncheckedIOException;
import java.util.function.*;
import services.IQuanLyDanhSach;

/** Menu CRUD dùng chung qua interface; nhập từng loại vẫn ở các lớp riêng. */
public final class MenuDanhSach<T> {
    private final String ten;
    private final Supplier<? extends IQuanLyDanhSach<T>> docDanhSach;
    private final Function<String, T> nhapDoiTuong;
    private final Function<T, String> hienThi;
    private final Consumer<T> them;
    private final BiPredicate<String, T> sua;
    private final Predicate<String> xoa;
    private final NhapLieuConsole nhap;
    private final Runnable luu;

    public MenuDanhSach(String ten, Supplier<? extends IQuanLyDanhSach<T>> docDanhSach,
            Function<String, T> nhapDoiTuong, Function<T, String> hienThi,
            Consumer<T> them, BiPredicate<String, T> sua, Predicate<String> xoa,
            NhapLieuConsole nhap, Runnable luu) {
        this.ten = ten;
        this.docDanhSach = docDanhSach;
        this.nhapDoiTuong = nhapDoiTuong;
        this.hienThi = hienThi;
        this.them = them;
        this.sua = sua;
        this.xoa = xoa;
        this.nhap = nhap;
        this.luu = luu;
    }

    public void chay() {
        while (true) {
            System.out.println("\n===== " + ten + " =====");
            System.out.println("1. Them  2. Sua  3. Xoa  4. Tim gan dung  5. Xem tat ca  6. Tim dung ma  0. Quay lai");
            String chon = nhap.docDong("Lua chon: ");
            if (chon.equals("0")) {
                return;
            }
            try {
                xuLy(chon);
            } catch (IllegalArgumentException e) {
                System.out.println("Khong thuc hien: " + e.getMessage());
            } catch (UncheckedIOException e) {
                System.out.println("CHUA LUU DUOC: " + e.getMessage() + ". Du lieu van o RAM; chon Luu de thu lai.");
            }
        }
    }

    private void xuLy(String chon) {
        switch (chon) {
            case "1":
                them.accept(nhapDoiTuong.apply(null));
                daThayDoi();
                break;
            case "2":
                suaDoiTuong();
                break;
            case "3":
                baoKetQua(xoa.test(nhap.ma("Ma can xoa: ")));
                break;
            case "4":
                hienThiMang(docDanhSach.get().timKiem(nhap.docDong("Tu khoa ma/ten/doi tac: ")));
                break;
            case "5":
                hienThiMang(docDanhSach.get().getTatCa());
                break;
            case "6":
                T ketQua = docDanhSach.get().timTheoMa(nhap.ma("Ma: "));
                System.out.println(ketQua == null ? "Khong tim thay." : hienThi.apply(ketQua));
                break;
            default:
                System.out.println("Lua chon khong hop le.");
        }
    }

    private void suaDoiTuong() {
        String ma = nhap.ma("Ma can sua: ");
        if (docDanhSach.get().timTheoMa(ma) == null) {
            System.out.println("Khong tim thay.");
            return;
        }
        baoKetQua(sua.test(ma, nhapDoiTuong.apply(ma)));
    }

    private void baoKetQua(boolean thanhCong) {
        if (thanhCong) {
            daThayDoi();
        } else {
            System.out.println("Khong tim thay.");
        }
    }

    private void daThayDoi() {
        luu.run();
        System.out.println("Da cap nhat va luu.");
    }

    private void hienThiMang(T[] mang) {
        if (mang.length == 0) {
            System.out.println("Danh sach trong.");
        }
        for (T doiTuong : mang) {
            System.out.println(hienThi.apply(doiTuong));
        }
    }
}
