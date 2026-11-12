package services;

import models.*;
import java.util.ArrayList;

public class DanhSachNhaCungCap implements IQuanLyDanhSach<NhaCungCap> {
    private ArrayList<NhaCungCap> ds = new ArrayList<>();

    public int getSoLuong() {
        return ds.size();
    }

    public NhaCungCap[] getTatCa() {
        return ds.toArray(new NhaCungCap[0]);
    }

    public boolean kiemTraMaDuyNhat(String ma) {
        return timTheoMa(ma) == null;
    }

    public NhaCungCap timTheoMa(String ma) {
        if (ma == null) return null;
        for (NhaCungCap ncc : ds)
            if (ncc.getMaNhaCungCap().equalsIgnoreCase(ma.trim()))
                return ncc;
        return null;
    }

    @Override
    public boolean them(NhaCungCap doiTuong) {
        if (doiTuong == null || !doiTuong.kiemTraHopLe()) return false;
        if (!kiemTraMaDuyNhat(doiTuong.getMaNhaCungCap())) return false;
        return ds.add(doiTuong);
    }

    public int themNhieu(NhaCungCap[] cacDoiTuong) {
        int daThem = 0;
        for (NhaCungCap ncc : cacDoiTuong)
            if (them(ncc)) daThem++;
        return daThem;
    }

    @Override
    public boolean sua(String ma, NhaCungCap moi) {
        NhaCungCap cu = timTheoMa(ma);
        if (cu == null || moi == null || !moi.kiemTraHopLe()) return false;
        if (!cu.getMaNhaCungCap().equalsIgnoreCase(moi.getMaNhaCungCap())) return false;
        cu.capNhatTu(moi);
        return true;
    }

    @Override
    public boolean xoa(String ma) {
        NhaCungCap ncc = timTheoMa(ma);
        if (ncc == null) return false;
        return ds.remove(ncc);
    }

    @Override
    public NhaCungCap[] timKiem(String tuKhoa) {
        String key = tuKhoa == null ? "" : tuKhoa.trim().toLowerCase();
        ArrayList<NhaCungCap> kq = new ArrayList<>();
        for (NhaCungCap ncc : ds)
            if (phuHop(ncc, key)) kq.add(ncc);
        return kq.toArray(new NhaCungCap[0]);
    }

    private boolean phuHop(NhaCungCap x, String key) {
        return x.getMaNhaCungCap().toLowerCase().contains(key)
                || x.getTenNhaCungCap().toLowerCase().contains(key)
                || x.getSoDienThoai().contains(key);
    }

    public void hienThiDanhSach() {
        if (ds.isEmpty()) { System.out.println("Danh sach rong."); return; }
        for (NhaCungCap ncc : ds)
            System.out.println(ncc.hienThiThongTin());
    }

}
