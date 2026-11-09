package models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

/** Chứng từ đang soạn; chỉ QuanLyCuaHang ghi nhận vào sổ và cập nhật Kho. */
public final class PhieuNhap {
    private final String maPhieuNhap;
    private LocalDate ngayLap;
    private NhaCungCap nhaCungCap;
    private NhanVien nhanVien;
    private ChiTietPhieuNhap[] chiTiet = new ChiTietPhieuNhap[KiemTra.SUC_CHUA_MAC_DINH];
    private int soLuongChiTiet;

    public PhieuNhap() {
        maPhieuNhap = "";
        ngayLap = LocalDate.now();
    }

    public PhieuNhap(String ma, LocalDate ngayLap, NhaCungCap nhaCungCap, NhanVien nhanVien) {
        this.maPhieuNhap = KiemTra.ma(ma);
        setNgayLap(ngayLap);
        setNhaCungCap(nhaCungCap);
        setNhanVien(nhanVien);
    }

    public String getMaPhieuNhap() {
        return maPhieuNhap;
    }

    public LocalDate getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(LocalDate ngayLap) {
        this.ngayLap = KiemTra.khacNull(ngayLap, "Ngay lap");
    }

    public NhaCungCap getNhaCungCap() {
        return nhaCungCap == null ? null : nhaCungCap.saoChep();
    }

    public void setNhaCungCap(NhaCungCap nhaCungCap) {
        this.nhaCungCap = KiemTra.khacNull(nhaCungCap, "NhaCungCap").saoChep();
    }

    public NhanVien getNhanVien() {
        return nhanVien == null ? null : nhanVien.saoChep();
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = KiemTra.khacNull(nhanVien, "Nhan vien").saoChep();
    }

    public int getSoLuongChiTiet() {
        return soLuongChiTiet;
    }

    public ChiTietPhieuNhap[] getChiTiet() {
        ChiTietPhieuNhap[] ketQua = new ChiTietPhieuNhap[soLuongChiTiet];
        for (int i = 0; i < soLuongChiTiet; i++) {
            ketQua[i] = chiTiet[i].saoChep();
        }
        return ketQua;
    }

    public void themChiTiet(ChiTietPhieuNhap moi) {
        ChiTietPhieuNhap hopLe = KiemTra.khacNull(moi, "Chi tiet").saoChep();
        if (timViTri(hopLe.getSanPham().getMaSanPham()) >= 0) {
            throw new IllegalArgumentException("San pham da co trong chung tu; hay sua dong cu.");
        }
        if (soLuongChiTiet == chiTiet.length) {
            chiTiet = Arrays.copyOf(chiTiet, Math.max(1, chiTiet.length * 2));
        }
        chiTiet[soLuongChiTiet++] = hopLe;
    }

    public boolean suaChiTiet(String maSP, ChiTietPhieuNhap moi) {
        int viTri = timViTri(maSP);
        if (viTri < 0) {
            return false;
        }
        ChiTietPhieuNhap hopLe = KiemTra.khacNull(moi, "Chi tiet").saoChep();
        if (!KiemTra.ma(maSP).equals(hopLe.getSanPham().getMaSanPham())) {
            throw new IllegalArgumentException("Khong duoc doi ma san pham cua dong chi tiet.");
        }
        chiTiet[viTri] = hopLe;
        return true;
    }

    public boolean xoaChiTiet(String maSP) {
        int viTri = timViTri(maSP);
        if (viTri < 0) {
            return false;
        }
        System.arraycopy(chiTiet, viTri + 1, chiTiet, viTri, soLuongChiTiet - viTri - 1);
        chiTiet[--soLuongChiTiet] = null;
        return true;
    }

    public ChiTietPhieuNhap timChiTiet(String maSP) {
        int viTri = timViTri(maSP);
        return viTri < 0 ? null : chiTiet[viTri].saoChep();
    }

    private int timViTri(String maSP) {
        for (int i = 0; i < soLuongChiTiet; i++) {
            if (chiTiet[i].getSanPham().getMaSanPham().equals(KiemTra.ma(maSP))) {
                return i;
            }
        }
        return -1;
    }

    public BigDecimal tinhTongTien() {
        BigDecimal tongTien = BigDecimal.ZERO;
        for (int i = 0; i < soLuongChiTiet; i++) {
            tongTien = tongTien.add(chiTiet[i].tinhThanhTien());
        }
        return tongTien;
    }

    public PhieuNhap saoChep() {
        PhieuNhap banSao = new PhieuNhap(maPhieuNhap, ngayLap, nhaCungCap, nhanVien);
        for (ChiTietPhieuNhap dong : getChiTiet()) {
            banSao.themChiTiet(dong);
        }
        return banSao;
    }

    public String hienThiThongTin() {
        return maPhieuNhap + " | " + ngayLap + " | " + (nhaCungCap == null ? "" : nhaCungCap.getMaNhaCungCap())
                + " | " + (nhanVien == null ? "" : nhanVien.getMaNhanVien()) + " | Tong: " + tinhTongTien().toPlainString()
                + (soLuongChiTiet == 0 ? " (ban nhap cu chua co chi tiet)" : "");
    }
}
