package services;

import models.*;
import java.util.ArrayList;
import java.math.BigDecimal;

public class DanhSachSanPham implements IQuanLyDanhSach<SanPham> {
    private ArrayList<SanPham> ds = new ArrayList<>();

    public int getSoLuong() {
        return ds.size();
    }

    public SanPham[] getTatCa() {
        return ds.toArray(new SanPham[0]);
    }

    public boolean kiemTraMaDuyNhat(String ma) {
        return timTheoMa(ma) == null;
    }

    public SanPham timTheoMa(String ma) {
        if (ma == null) return null;
        for (SanPham sp : ds)
            if (sp.getMaSanPham().equalsIgnoreCase(ma.trim()))
                return sp;
        return null;
    }

    @Override
    public boolean them(SanPham doiTuong) {
        if (doiTuong == null || !doiTuong.kiemTraHopLe()) return false;
        if (!kiemTraMaDuyNhat(doiTuong.getMaSanPham())) return false;
        return ds.add(doiTuong);
    }

    public int themNhieu(SanPham[] cacDoiTuong) {
        int daThem = 0;
        for (SanPham sp : cacDoiTuong)
            if (them(sp)) daThem++;
        return daThem;
    }

    @Override
    public boolean sua(String ma, SanPham moi) {
        SanPham cu = timTheoMa(ma);
        if (cu == null || moi == null || !moi.kiemTraHopLe()) return false;
        if (!cu.getMaSanPham().equalsIgnoreCase(moi.getMaSanPham())) return false;
        if (cu.getClass() != moi.getClass()) return false;
        cu.capNhatTu(moi);
        return true;
    }

    @Override
    public boolean xoa(String ma) {
        SanPham sp = timTheoMa(ma);
        if (sp == null) return false;
        return ds.remove(sp);
    }

    @Override
    public SanPham[] timKiem(String tuKhoa) {
        String key = tuKhoa == null ? "" : tuKhoa.trim().toLowerCase();
        ArrayList<SanPham> kq = new ArrayList<>();
        for (SanPham sp : ds)
            if (phuHop(sp, key)) kq.add(sp);
        return kq.toArray(new SanPham[0]);
    }

    private boolean phuHop(SanPham x, String key) {
        return x.getMaSanPham().toLowerCase().contains(key)
                || x.getTenSanPham().toLowerCase().contains(key);
    }

    public void hienThiDanhSach() {
        if (ds.isEmpty()) { System.out.println("Danh sach rong."); return; }
        for (SanPham sp : ds)
            System.out.println(sp.hienThiThongTin());
    }

    public SanPham[] locTheoLoaiSP(String maLoai) {
        maLoai = maLoai == null ? "" : maLoai.trim();
        ArrayList<SanPham> kq = new ArrayList<>();
        for (SanPham sp : ds)
            if (sp.getLoaiSP().getMaLoaiSP().equalsIgnoreCase(maLoai))
                kq.add(sp);
        return kq.toArray(new SanPham[0]);
    }

    public SanPham[] locTheoKhoangGia(BigDecimal tu, BigDecimal den) {
        if (tu == null || den == null || tu.signum() < 0 || tu.compareTo(den) > 0)
            throw new IllegalArgumentException("Khoang gia khong hop le.");
        ArrayList<SanPham> kq = new ArrayList<>();
        for (SanPham sp : ds)
            if (sp.getGiaBan().compareTo(tu) >= 0 && sp.getGiaBan().compareTo(den) <= 0)
                kq.add(sp);
        return kq.toArray(new SanPham[0]);
    }

}
