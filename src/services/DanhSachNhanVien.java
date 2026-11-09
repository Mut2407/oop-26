package services;

import java.util.Arrays;
import models.NhanVien;
import models.KiemTra;

/** Mảng đối tượng có mở rộng, không lộ dữ liệu nội bộ cho bên gọi. */
public final class DanhSachNhanVien implements IQuanLyDanhSach<NhanVien> {
    private NhanVien[] ds;
    private int soLuong;

    public DanhSachNhanVien() {
        this(KiemTra.SUC_CHUA_MAC_DINH);
    }

    public DanhSachNhanVien(int sucChua) {
        if (sucChua < 0) {
            throw new IllegalArgumentException("Suc chua phai >= 0.");
        }
        ds = new NhanVien[sucChua];
    }

    public DanhSachNhanVien(NhanVien[] banDau) {
        this(KiemTra.khacNull(banDau, "Mang ban dau").length);
        themNhieu(banDau);
    }

    @Override
    public int getSoLuong() {
        return soLuong;
    }

    @Override
    public NhanVien[] getTatCa() {
        NhanVien[] ketQua = new NhanVien[soLuong];
        for (int i = 0; i < soLuong; i++) {
            ketQua[i] = ds[i].saoChep();
        }
        return ketQua;
    }

    @Override
    public boolean them(NhanVien doiTuong) {
        NhanVien hopLe = KiemTra.khacNull(doiTuong, "Doi tuong").saoChep();
        if (timViTri(hopLe.getMaNhanVien()) >= 0) {
            throw new IllegalArgumentException("Ma da ton tai: " + hopLe.getMaNhanVien());
        }
        if (soLuong == ds.length) {
            ds = Arrays.copyOf(ds, Math.max(1, ds.length * 2));
        }
        ds[soLuong++] = hopLe;
        return true;
    }

    /** Kiểm tra toàn bộ trước khi thay đổi mảng, kể cả mã trùng trong lô. */
    public void themNhieu(NhanVien[] doiTuong) {
        KiemTra.khacNull(doiTuong, "Mang can them");
        DanhSachNhanVien moi = new DanhSachNhanVien(soLuong + doiTuong.length);
        for (NhanVien cu : getTatCa()) {
            moi.them(cu);
        }
        for (NhanVien item : doiTuong) {
            moi.them(item);
        }
        ds = moi.ds;
        soLuong = moi.soLuong;
    }

    @Override
    public boolean sua(String ma, NhanVien doiTuongMoi) {
        int viTri = timViTri(ma);
        if (viTri < 0) {
            return false;
        }
        NhanVien hopLe = KiemTra.khacNull(doiTuongMoi, "Doi tuong").saoChep();
        if (!ds[viTri].getMaNhanVien().equals(hopLe.getMaNhanVien())) {
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
    public NhanVien timTheoMa(String ma) {
        int viTri = timViTri(ma);
        return viTri < 0 ? null : ds[viTri].saoChep();
    }

    private int timViTri(String ma) {
        String maChuan = KiemTra.ma(ma);
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getMaNhanVien().equals(maChuan)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public NhanVien[] timKiem(String tuKhoa) {
        NhanVien[] ketQua = new NhanVien[soLuong];
        int soKetQua = 0;
        for (int i = 0; i < soLuong; i++) {
            if (KiemTra.chua(ds[i].getMaNhanVien(), tuKhoa)
                    || KiemTra.chua(ds[i].getHoTen(), tuKhoa)) {
                ketQua[soKetQua++] = ds[i].saoChep();
            }
        }
        return Arrays.copyOf(ketQua, soKetQua);
    }
}
