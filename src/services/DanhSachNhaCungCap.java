package services;

import java.util.Arrays;
import models.NhaCungCap;
import models.KiemTra;

/** Mảng đối tượng có mở rộng, không lộ dữ liệu nội bộ cho bên gọi. */
public final class DanhSachNhaCungCap implements IQuanLyDanhSach<NhaCungCap> {
    private NhaCungCap[] ds;
    private int soLuong;

    public DanhSachNhaCungCap() {
        this(KiemTra.SUC_CHUA_MAC_DINH);
    }

    public DanhSachNhaCungCap(int sucChua) {
        if (sucChua < 0) {
            throw new IllegalArgumentException("Suc chua phai >= 0.");
        }
        ds = new NhaCungCap[sucChua];
    }

    public DanhSachNhaCungCap(NhaCungCap[] banDau) {
        this(KiemTra.khacNull(banDau, "Mang ban dau").length);
        themNhieu(banDau);
    }

    @Override
    public int getSoLuong() {
        return soLuong;
    }

    @Override
    public NhaCungCap[] getTatCa() {
        NhaCungCap[] ketQua = new NhaCungCap[soLuong];
        for (int i = 0; i < soLuong; i++) {
            ketQua[i] = ds[i].saoChep();
        }
        return ketQua;
    }

    @Override
    public boolean them(NhaCungCap doiTuong) {
        NhaCungCap hopLe = KiemTra.khacNull(doiTuong, "Doi tuong").saoChep();
        if (timViTri(hopLe.getMaNhaCungCap()) >= 0) {
            throw new IllegalArgumentException("Ma da ton tai: " + hopLe.getMaNhaCungCap());
        }
        if (soLuong == ds.length) {
            ds = Arrays.copyOf(ds, Math.max(1, ds.length * 2));
        }
        ds[soLuong++] = hopLe;
        return true;
    }

    /** Kiểm tra toàn bộ trước khi thay đổi mảng, kể cả mã trùng trong lô. */
    public void themNhieu(NhaCungCap[] doiTuong) {
        KiemTra.khacNull(doiTuong, "Mang can them");
        DanhSachNhaCungCap moi = new DanhSachNhaCungCap(soLuong + doiTuong.length);
        for (NhaCungCap cu : getTatCa()) {
            moi.them(cu);
        }
        for (NhaCungCap item : doiTuong) {
            moi.them(item);
        }
        ds = moi.ds;
        soLuong = moi.soLuong;
    }

    @Override
    public boolean sua(String ma, NhaCungCap doiTuongMoi) {
        int viTri = timViTri(ma);
        if (viTri < 0) {
            return false;
        }
        NhaCungCap hopLe = KiemTra.khacNull(doiTuongMoi, "Doi tuong").saoChep();
        if (!ds[viTri].getMaNhaCungCap().equals(hopLe.getMaNhaCungCap())) {
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
    public NhaCungCap timTheoMa(String ma) {
        int viTri = timViTri(ma);
        return viTri < 0 ? null : ds[viTri].saoChep();
    }

    private int timViTri(String ma) {
        String maChuan = KiemTra.ma(ma);
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getMaNhaCungCap().equals(maChuan)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public NhaCungCap[] timKiem(String tuKhoa) {
        NhaCungCap[] ketQua = new NhaCungCap[soLuong];
        int soKetQua = 0;
        for (int i = 0; i < soLuong; i++) {
            if (KiemTra.chua(ds[i].getMaNhaCungCap(), tuKhoa)
                    || KiemTra.chua(ds[i].getTenNhaCungCap(), tuKhoa)) {
                ketQua[soKetQua++] = ds[i].saoChep();
            }
        }
        return Arrays.copyOf(ketQua, soKetQua);
    }
}
