package services;

import java.util.Arrays;
import models.KhachHang;
import models.KiemTra;

/** Mảng đối tượng có mở rộng, không lộ dữ liệu nội bộ cho bên gọi. */
public final class DanhSachKhachHang implements IQuanLyDanhSach<KhachHang> {
    private KhachHang[] ds;
    private int soLuong;

    public DanhSachKhachHang() {
        this(KiemTra.SUC_CHUA_MAC_DINH);
    }

    public DanhSachKhachHang(int sucChua) {
        if (sucChua < 0) {
            throw new IllegalArgumentException("Suc chua phai >= 0.");
        }
        ds = new KhachHang[sucChua];
    }

    public DanhSachKhachHang(KhachHang[] banDau) {
        this(KiemTra.khacNull(banDau, "Mang ban dau").length);
        themNhieu(banDau);
    }

    @Override
    public int getSoLuong() {
        return soLuong;
    }

    @Override
    public KhachHang[] getTatCa() {
        KhachHang[] ketQua = new KhachHang[soLuong];
        for (int i = 0; i < soLuong; i++) {
            ketQua[i] = ds[i].saoChep();
        }
        return ketQua;
    }

    @Override
    public boolean them(KhachHang doiTuong) {
        KhachHang hopLe = KiemTra.khacNull(doiTuong, "Doi tuong").saoChep();
        if (timViTri(hopLe.getMaKhachHang()) >= 0) {
            throw new IllegalArgumentException("Ma da ton tai: " + hopLe.getMaKhachHang());
        }
        if (soLuong == ds.length) {
            ds = Arrays.copyOf(ds, Math.max(1, ds.length * 2));
        }
        ds[soLuong++] = hopLe;
        return true;
    }

    /** Kiểm tra toàn bộ trước khi thay đổi mảng, kể cả mã trùng trong lô. */
    public void themNhieu(KhachHang[] doiTuong) {
        KiemTra.khacNull(doiTuong, "Mang can them");
        DanhSachKhachHang moi = new DanhSachKhachHang(soLuong + doiTuong.length);
        for (KhachHang cu : getTatCa()) {
            moi.them(cu);
        }
        for (KhachHang item : doiTuong) {
            moi.them(item);
        }
        ds = moi.ds;
        soLuong = moi.soLuong;
    }

    @Override
    public boolean sua(String ma, KhachHang doiTuongMoi) {
        int viTri = timViTri(ma);
        if (viTri < 0) {
            return false;
        }
        KhachHang hopLe = KiemTra.khacNull(doiTuongMoi, "Doi tuong").saoChep();
        if (!ds[viTri].getMaKhachHang().equals(hopLe.getMaKhachHang())) {
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
    public KhachHang timTheoMa(String ma) {
        int viTri = timViTri(ma);
        return viTri < 0 ? null : ds[viTri].saoChep();
    }

    private int timViTri(String ma) {
        String maChuan = KiemTra.ma(ma);
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getMaKhachHang().equals(maChuan)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public KhachHang[] timKiem(String tuKhoa) {
        KhachHang[] ketQua = new KhachHang[soLuong];
        int soKetQua = 0;
        for (int i = 0; i < soLuong; i++) {
            if (KiemTra.chua(ds[i].getMaKhachHang(), tuKhoa)
                    || KiemTra.chua(ds[i].getHoTen(), tuKhoa)) {
                ketQua[soKetQua++] = ds[i].saoChep();
            }
        }
        return Arrays.copyOf(ketQua, soKetQua);
    }
}
