package services;

import java.util.Arrays;
import models.HoaDon;
import models.KiemTra;
import java.math.BigDecimal;
import java.time.LocalDate;

/** Mảng đối tượng có mở rộng, không lộ dữ liệu nội bộ cho bên gọi. */
public final class DanhSachHoaDon implements IQuanLyDanhSach<HoaDon> {
    private HoaDon[] ds;
    private int soLuong;

    public DanhSachHoaDon() {
        this(KiemTra.SUC_CHUA_MAC_DINH);
    }

    public DanhSachHoaDon(int sucChua) {
        if (sucChua < 0) {
            throw new IllegalArgumentException("Suc chua phai >= 0.");
        }
        ds = new HoaDon[sucChua];
    }

    public DanhSachHoaDon(HoaDon[] banDau) {
        this(KiemTra.khacNull(banDau, "Mang ban dau").length);
        themNhieu(banDau);
    }

    @Override
    public int getSoLuong() {
        return soLuong;
    }

    @Override
    public HoaDon[] getTatCa() {
        HoaDon[] ketQua = new HoaDon[soLuong];
        for (int i = 0; i < soLuong; i++) {
            ketQua[i] = ds[i].saoChep();
        }
        return ketQua;
    }

    @Override
    public boolean them(HoaDon doiTuong) {
        HoaDon hopLe = KiemTra.khacNull(doiTuong, "Doi tuong").saoChep();
        if (timViTri(hopLe.getMaHoaDon()) >= 0) {
            throw new IllegalArgumentException("Ma da ton tai: " + hopLe.getMaHoaDon());
        }
        if (soLuong == ds.length) {
            ds = Arrays.copyOf(ds, Math.max(1, ds.length * 2));
        }
        ds[soLuong++] = hopLe;
        return true;
    }

    /** Kiểm tra toàn bộ trước khi thay đổi mảng, kể cả mã trùng trong lô. */
    public void themNhieu(HoaDon[] doiTuong) {
        KiemTra.khacNull(doiTuong, "Mang can them");
        DanhSachHoaDon moi = new DanhSachHoaDon(soLuong + doiTuong.length);
        for (HoaDon cu : getTatCa()) {
            moi.them(cu);
        }
        for (HoaDon item : doiTuong) {
            moi.them(item);
        }
        ds = moi.ds;
        soLuong = moi.soLuong;
    }

    @Override
    public boolean sua(String ma, HoaDon doiTuongMoi) {
        int viTri = timViTri(ma);
        if (viTri < 0) {
            return false;
        }
        HoaDon hopLe = KiemTra.khacNull(doiTuongMoi, "Doi tuong").saoChep();
        if (!ds[viTri].getMaHoaDon().equals(hopLe.getMaHoaDon())) {
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
    public HoaDon timTheoMa(String ma) {
        int viTri = timViTri(ma);
        return viTri < 0 ? null : ds[viTri].saoChep();
    }

    private int timViTri(String ma) {
        String maChuan = KiemTra.ma(ma);
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getMaHoaDon().equals(maChuan)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public HoaDon[] timKiem(String tuKhoa) {
        HoaDon[] ketQua = new HoaDon[soLuong];
        int soKetQua = 0;
        for (int i = 0; i < soLuong; i++) {
            if (KiemTra.chua(ds[i].getMaHoaDon(), tuKhoa)
                    || KiemTra.chua(ds[i].getKhachHang().getMaKhachHang(), tuKhoa)) {
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
