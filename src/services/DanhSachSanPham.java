package services;

import java.util.Arrays;
import models.SanPham;
import models.KiemTra;

/** Mảng đối tượng có mở rộng, không lộ dữ liệu nội bộ cho bên gọi. */
public final class DanhSachSanPham implements IQuanLyDanhSach<SanPham> {
    private SanPham[] ds;
    private int soLuong;

    public DanhSachSanPham() {
        this(KiemTra.SUC_CHUA_MAC_DINH);
    }

    public DanhSachSanPham(int sucChua) {
        if (sucChua < 0) {
            throw new IllegalArgumentException("Suc chua phai >= 0.");
        }
        ds = new SanPham[sucChua];
    }

    public DanhSachSanPham(SanPham[] banDau) {
        this(KiemTra.khacNull(banDau, "Mang ban dau").length);
        themNhieu(banDau);
    }

    @Override
    public int getSoLuong() {
        return soLuong;
    }

    @Override
    public SanPham[] getTatCa() {
        SanPham[] ketQua = new SanPham[soLuong];
        for (int i = 0; i < soLuong; i++) {
            ketQua[i] = ds[i].saoChep();
        }
        return ketQua;
    }

    @Override
    public boolean them(SanPham doiTuong) {
        SanPham hopLe = KiemTra.khacNull(doiTuong, "Doi tuong").saoChep();
        if (timViTri(hopLe.getMaSanPham()) >= 0) {
            throw new IllegalArgumentException("Ma da ton tai: " + hopLe.getMaSanPham());
        }
        if (soLuong == ds.length) {
            ds = Arrays.copyOf(ds, Math.max(1, ds.length * 2));
        }
        ds[soLuong++] = hopLe;
        return true;
    }

    /** Kiểm tra toàn bộ trước khi thay đổi mảng, kể cả mã trùng trong lô. */
    public void themNhieu(SanPham[] doiTuong) {
        KiemTra.khacNull(doiTuong, "Mang can them");
        DanhSachSanPham moi = new DanhSachSanPham(soLuong + doiTuong.length);
        for (SanPham cu : getTatCa()) {
            moi.them(cu);
        }
        for (SanPham item : doiTuong) {
            moi.them(item);
        }
        ds = moi.ds;
        soLuong = moi.soLuong;
    }

    @Override
    public boolean sua(String ma, SanPham doiTuongMoi) {
        int viTri = timViTri(ma);
        if (viTri < 0) {
            return false;
        }
        SanPham hopLe = KiemTra.khacNull(doiTuongMoi, "Doi tuong").saoChep();
        if (!ds[viTri].getMaSanPham().equals(hopLe.getMaSanPham())) {
            throw new IllegalArgumentException("Khong duoc doi ma dinh danh.");
        }
        if (ds[viTri].getSoLuongTon() != hopLe.getSoLuongTon()
                || ds[viTri].getClass() != hopLe.getClass()) {
            throw new IllegalArgumentException("Khong duoc sua ton kho hoac doi lop san pham.");
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
    public SanPham timTheoMa(String ma) {
        int viTri = timViTri(ma);
        return viTri < 0 ? null : ds[viTri].saoChep();
    }

    private int timViTri(String ma) {
        String maChuan = KiemTra.ma(ma);
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getMaSanPham().equals(maChuan)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public SanPham[] timKiem(String tuKhoa) {
        SanPham[] ketQua = new SanPham[soLuong];
        int soKetQua = 0;
        for (int i = 0; i < soLuong; i++) {
            if (KiemTra.chua(ds[i].getMaSanPham(), tuKhoa)
                    || KiemTra.chua(ds[i].getTenSanPham(), tuKhoa)) {
                ketQua[soKetQua++] = ds[i].saoChep();
            }
        }
        return Arrays.copyOf(ketQua, soKetQua);
    }

    public SanPham[] locTheoLoaiSP(String maLoai) {
        String maChuan = KiemTra.ma(maLoai);
        SanPham[] ketQua = new SanPham[soLuong];
        int soKetQua = 0;
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getLoaiSP().getMaLoaiSP().equals(maChuan)) {
                ketQua[soKetQua++] = ds[i].saoChep();
            }
        }
        return Arrays.copyOf(ketQua, soKetQua);
    }

    public SanPham[] locTheoKhoangGia(java.math.BigDecimal tu, java.math.BigDecimal den) {
        KiemTra.tien(tu);
        KiemTra.tien(den);
        if (tu.compareTo(den) > 0) {
            throw new IllegalArgumentException("Khoang gia khong hop le.");
        }
        return Arrays.stream(getTatCa()).filter(sp -> sp.getGiaBan().compareTo(tu) >= 0
                && sp.getGiaBan().compareTo(den) <= 0).toArray(SanPham[]::new);
    }

    /** Chỉ Kho gọi: dựng xong tất cả bản sao rồi mới thay mảng đang dùng. */
    void apDungTonKho(int[] tonMoi) {
        if (tonMoi.length != soLuong) {
            throw new IllegalArgumentException("So phan tu ton kho khong khop.");
        }
        SanPham[] moi = Arrays.copyOf(ds, ds.length);
        for (int i = 0; i < soLuong; i++) {
            moi[i] = ds[i].voiTonKho(tonMoi[i]);
        }
        ds = moi;
    }
}
