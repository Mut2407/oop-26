package services;

import models.*;

/** Tính và kiểm tra toàn bộ chênh lệch trước khi ghi bất kỳ tồn nào. */
public final class Kho {
    private final DanhSachSanPham sanPham;

    public Kho(DanhSachSanPham sanPham) {
        this.sanPham = KiemTra.khacNull(sanPham, "Danh sach san pham");
    }

    public int traCuuTon(String maSP) {
        SanPham timThay = sanPham.timTheoMa(maSP);
        if (timThay == null) {
            throw new IllegalArgumentException("Khong tim thay san pham: " + maSP);
        }
        return timThay.getSoLuongTon();
    }

    void capNhatTheoHoaDon(HoaDon cu, HoaDon moi) {
        KiemTra.khacNull(cu == null ? moi : cu, "Chung tu");
        SanPham[] tatCa = sanPham.getTatCa();
        long[] chenhLech = new long[tatCa.length];
        congHoaDon(tatCa, chenhLech, cu, 1);
        congHoaDon(tatCa, chenhLech, moi, -1);
        apDung(tatCa, chenhLech);
    }

    void capNhatTheoPhieuNhap(PhieuNhap cu, PhieuNhap moi) {
        KiemTra.khacNull(cu == null ? moi : cu, "Chung tu");
        SanPham[] tatCa = sanPham.getTatCa();
        long[] chenhLech = new long[tatCa.length];
        congPhieuNhap(tatCa, chenhLech, cu, -1);
        congPhieuNhap(tatCa, chenhLech, moi, 1);
        apDung(tatCa, chenhLech);
    }

    private void congHoaDon(SanPham[] tatCa, long[] chenhLech, HoaDon hoaDon, int dau) {
        if (hoaDon == null) {
            return;
        }
        for (ChiTietHoaDon dong : hoaDon.getChiTiet()) {
            int viTri = timViTri(tatCa, dong.getSanPham().getMaSanPham());
            chenhLech[viTri] += (long) dau * dong.getSoLuong();
        }
    }

    private void congPhieuNhap(SanPham[] tatCa, long[] chenhLech, PhieuNhap phieu, int dau) {
        if (phieu == null) {
            return;
        }
        for (ChiTietPhieuNhap dong : phieu.getChiTiet()) {
            int viTri = timViTri(tatCa, dong.getSanPham().getMaSanPham());
            chenhLech[viTri] += (long) dau * dong.getSoLuong();
        }
    }

    private int timViTri(SanPham[] tatCa, String maSP) {
        for (int i = 0; i < tatCa.length; i++) {
            if (tatCa[i].getMaSanPham().equals(maSP)) {
                return i;
            }
        }
        throw new IllegalArgumentException("San pham khong ton tai: " + maSP);
    }

    private void apDung(SanPham[] tatCa, long[] chenhLech) {
        int[] tonMoi = new int[tatCa.length];
        for (int i = 0; i < tatCa.length; i++) {
            long soLuong = tatCa[i].getSoLuongTon() + chenhLech[i];
            if (soLuong < 0 || soLuong > Integer.MAX_VALUE) {
                throw new IllegalArgumentException("Ton kho khong hop le cho " + tatCa[i].getMaSanPham());
            }
            tonMoi[i] = (int) soLuong;
        }
        sanPham.apDungTonKho(tonMoi);
    }
}
