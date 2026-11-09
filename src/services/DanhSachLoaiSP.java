package services;

import java.util.Arrays;
import models.LoaiSP;
import models.KiemTra;

/** Mảng đối tượng có mở rộng, không lộ dữ liệu nội bộ cho bên gọi. */
public final class DanhSachLoaiSP implements IQuanLyDanhSach<LoaiSP> {
    private LoaiSP[] ds;
    private int soLuong;

    public DanhSachLoaiSP() {
        this(KiemTra.SUC_CHUA_MAC_DINH);
    }

    public DanhSachLoaiSP(int sucChua) {
        if (sucChua < 0) {
            throw new IllegalArgumentException("Suc chua phai >= 0.");
        }
        ds = new LoaiSP[sucChua];
    }

    public DanhSachLoaiSP(LoaiSP[] banDau) {
        this(KiemTra.khacNull(banDau, "Mang ban dau").length);
        themNhieu(banDau);
    }

    @Override
    public int getSoLuong() {
        return soLuong;
    }

    @Override
    public LoaiSP[] getTatCa() {
        LoaiSP[] ketQua = new LoaiSP[soLuong];
        for (int i = 0; i < soLuong; i++) {
            ketQua[i] = ds[i].saoChep();
        }
        return ketQua;
    }

    @Override
    public boolean them(LoaiSP doiTuong) {
        LoaiSP hopLe = KiemTra.khacNull(doiTuong, "Doi tuong").saoChep();
        if (timViTri(hopLe.getMaLoaiSP()) >= 0) {
            throw new IllegalArgumentException("Ma da ton tai: " + hopLe.getMaLoaiSP());
        }
        if (soLuong == ds.length) {
            ds = Arrays.copyOf(ds, Math.max(1, ds.length * 2));
        }
        ds[soLuong++] = hopLe;
        return true;
    }

    /** Kiểm tra toàn bộ trước khi thay đổi mảng, kể cả mã trùng trong lô. */
    public void themNhieu(LoaiSP[] doiTuong) {
        KiemTra.khacNull(doiTuong, "Mang can them");
        DanhSachLoaiSP moi = new DanhSachLoaiSP(soLuong + doiTuong.length);
        for (LoaiSP cu : getTatCa()) {
            moi.them(cu);
        }
        for (LoaiSP item : doiTuong) {
            moi.them(item);
        }
        ds = moi.ds;
        soLuong = moi.soLuong;
    }

    @Override
    public boolean sua(String ma, LoaiSP doiTuongMoi) {
        int viTri = timViTri(ma);
        if (viTri < 0) {
            return false;
        }
        LoaiSP hopLe = KiemTra.khacNull(doiTuongMoi, "Doi tuong").saoChep();
        if (!ds[viTri].getMaLoaiSP().equals(hopLe.getMaLoaiSP())) {
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
    public LoaiSP timTheoMa(String ma) {
        int viTri = timViTri(ma);
        return viTri < 0 ? null : ds[viTri].saoChep();
    }

    private int timViTri(String ma) {
        String maChuan = KiemTra.ma(ma);
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getMaLoaiSP().equals(maChuan)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public LoaiSP[] timKiem(String tuKhoa) {
        LoaiSP[] ketQua = new LoaiSP[soLuong];
        int soKetQua = 0;
        for (int i = 0; i < soLuong; i++) {
            if (KiemTra.chua(ds[i].getMaLoaiSP(), tuKhoa)
                    || KiemTra.chua(ds[i].getTenLoaiSP(), tuKhoa)) {
                ketQua[soKetQua++] = ds[i].saoChep();
            }
        }
        return Arrays.copyOf(ketQua, soKetQua);
    }
}
