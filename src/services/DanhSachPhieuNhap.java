package services;

import java.util.Arrays;
import models.PhieuNhap;
import models.KiemTra;
import java.math.BigDecimal;
import java.time.LocalDate;

/** Mảng đối tượng có mở rộng, không lộ dữ liệu nội bộ cho bên gọi. */
public final class DanhSachPhieuNhap implements IQuanLyDanhSach<PhieuNhap> {
    private PhieuNhap[] ds;
    private int soLuong;

    public DanhSachPhieuNhap() {
        this(KiemTra.SUC_CHUA_MAC_DINH);
    }

    public DanhSachPhieuNhap(int sucChua) {
        if (sucChua < 0) {
            throw new IllegalArgumentException("Suc chua phai >= 0.");
        }
        ds = new PhieuNhap[sucChua];
    }

    public DanhSachPhieuNhap(PhieuNhap[] banDau) {
        this(KiemTra.khacNull(banDau, "Mang ban dau").length);
        themNhieu(banDau);
    }

    @Override
    public int getSoLuong() {
        return soLuong;
    }

    @Override
    public PhieuNhap[] getTatCa() {
        PhieuNhap[] ketQua = new PhieuNhap[soLuong];
        for (int i = 0; i < soLuong; i++) {
            ketQua[i] = ds[i].saoChep();
        }
        return ketQua;
    }

    @Override
    public boolean them(PhieuNhap doiTuong) {
        PhieuNhap hopLe = KiemTra.khacNull(doiTuong, "Doi tuong").saoChep();
        if (timViTri(hopLe.getMaPhieuNhap()) >= 0) {
            throw new IllegalArgumentException("Ma da ton tai: " + hopLe.getMaPhieuNhap());
        }
        if (soLuong == ds.length) {
            ds = Arrays.copyOf(ds, Math.max(1, ds.length * 2));
        }
        ds[soLuong++] = hopLe;
        return true;
    }

    /** Kiểm tra toàn bộ trước khi thay đổi mảng, kể cả mã trùng trong lô. */
    public void themNhieu(PhieuNhap[] doiTuong) {
        KiemTra.khacNull(doiTuong, "Mang can them");
        DanhSachPhieuNhap moi = new DanhSachPhieuNhap(soLuong + doiTuong.length);
        for (PhieuNhap cu : getTatCa()) {
            moi.them(cu);
        }
        for (PhieuNhap item : doiTuong) {
            moi.them(item);
        }
        ds = moi.ds;
        soLuong = moi.soLuong;
    }

    @Override
    public boolean sua(String ma, PhieuNhap doiTuongMoi) {
        int viTri = timViTri(ma);
        if (viTri < 0) {
            return false;
        }
        PhieuNhap hopLe = KiemTra.khacNull(doiTuongMoi, "Doi tuong").saoChep();
        if (!ds[viTri].getMaPhieuNhap().equals(hopLe.getMaPhieuNhap())) {
            throw new IllegalArgumentException("Khong duoc doi ma dinh danh.");
        }
        ds[viTri] = hopLe;
        return true;
    }

    @Override
    public boolean xoa(String ma) {
        int viTri = timViTri(ma);
        if (viTri < 0) {
            return false;
        }
        System.arraycopy(ds, viTri + 1, ds, viTri, soLuong - viTri - 1);
        ds[--soLuong] = null;
        return true;
    }

    @Override
    public PhieuNhap timTheoMa(String ma) {
        int viTri = timViTri(ma);
        return viTri < 0 ? null : ds[viTri].saoChep();
    }

    private int timViTri(String ma) {
        String maChuan = KiemTra.ma(ma);
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getMaPhieuNhap().equals(maChuan)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public PhieuNhap[] timKiem(String tuKhoa) {
        PhieuNhap[] ketQua = new PhieuNhap[soLuong];
        int soKetQua = 0;
        for (int i = 0; i < soLuong; i++) {
            if (KiemTra.chua(ds[i].getMaPhieuNhap(), tuKhoa)
                    || KiemTra.chua(ds[i].getNhaCungCap().getMaNhaCungCap(), tuKhoa)) {
                ketQua[soKetQua++] = ds[i].saoChep();
            }
        }
        return Arrays.copyOf(ketQua, soKetQua);
    }

    public BigDecimal thongKeTongTien() {
        BigDecimal tong = BigDecimal.ZERO;
        for (int i = 0; i < soLuong; i++) {
            tong = tong.add(ds[i].tinhTongTien());
        }
        return tong;
    }

    public BigDecimal thongKeTongTien(LocalDate tu, LocalDate den) {
        KiemTra.khacNull(tu, "Tu ngay");
        KiemTra.khacNull(den, "Den ngay");
        if (tu.isAfter(den)) {
            throw new IllegalArgumentException("Tu ngay phai <= den ngay.");
        }
        BigDecimal tong = BigDecimal.ZERO;
        for (int i = 0; i < soLuong; i++) {
            LocalDate ngay = ds[i].getNgayLap();
            if (!ngay.isBefore(tu) && !ngay.isAfter(den)) {
                tong = tong.add(ds[i].tinhTongTien());
            }
        }
        return tong;
    }
}
