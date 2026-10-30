package services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import models.NhaCungCap;

public class DanhSachNhaCungCap implements LuuTruFile {
    private static final String TIEU_DE_CU = "NHA_CUNG_CAP_V1";
    private static final String TIEU_DE = "maNhaCungCap\ttenNhaCungCap\tsoDienThoai\tdiaChi";
    private final List<NhaCungCap> danhSach = new ArrayList<>();

    public DanhSachNhaCungCap(NhaCungCap[] banDau) {
        for (NhaCungCap ncc : banDau) them(ncc);
    }

    public List<NhaCungCap> getDanhSach() {
        return List.copyOf(danhSach);
    }

    public void them(NhaCungCap ncc) {
        kiemTra(ncc);
        if (timTheoMa(ncc.getMaNhaCungCap()) != null)
            throw new IllegalArgumentException("Ma nha cung cap da ton tai.");
        danhSach.add(ncc);
    }

    public boolean suaTheoMa(String ma, NhaCungCap moi) {
        int i = chiSo(ma);
        if (i < 0) return false;
        kiemTra(moi);
        if (!danhSach.get(i).getMaNhaCungCap().equalsIgnoreCase(moi.getMaNhaCungCap()))
            throw new IllegalArgumentException("Khong duoc doi ma nha cung cap.");
        danhSach.set(i, moi);
        return true;
    }

    public boolean xoaTheoMa(String ma) {
        int i = chiSo(ma);
        if (i < 0) return false;
        danhSach.remove(i);
        return true;
    }

    public NhaCungCap timTheoMa(String ma) {
        int i = chiSo(ma);
        return i < 0 ? null : danhSach.get(i);
    }

    public List<NhaCungCap> timGanDung(String tuKhoa) {
        String khoa = tuKhoa.toLowerCase(Locale.ROOT);
        List<NhaCungCap> ketQua = new ArrayList<>();
        for (NhaCungCap ncc : danhSach)
            if (ncc.getMaNhaCungCap().toLowerCase(Locale.ROOT).contains(khoa)
                    || ncc.getTenNhaCungCap().toLowerCase(Locale.ROOT).contains(khoa))
                ketQua.add(ncc);
        return ketQua;
    }

    @Override
    public void ghiFile(String duongDan) throws IOException {
        List<String> dong = new ArrayList<>();
        dong.add(TIEU_DE);
        for (NhaCungCap ncc : danhSach) {
            kiemTra(ncc);
            dong.add(FileText.dong(ncc.getMaNhaCungCap(), ncc.getTenNhaCungCap(),
                    ncc.getSoDienThoai(), ncc.getDiaChi()));
        }
        FileText.ghi(duongDan, dong);
    }

    @Override
    public void docFile(String duongDan) throws IOException {
        DanhSachNhaCungCap moi = new DanhSachNhaCungCap(new NhaCungCap[0]);
        List<String[]> dong = FileText.doc(duongDan, TIEU_DE, TIEU_DE_CU);
        for (int i = 0; i < dong.size(); i++) {
            String[] c = dong.get(i);
            try {
                if (c.length != 4) throw new IllegalArgumentException("Phai co 4 cot.");
                moi.them(new NhaCungCap(c[0], c[1], c[3], c[2]));
            } catch (IllegalArgumentException e) {
                throw new IOException("Dong " + (i + 2) + ": " + e.getMessage(), e);
            }
        }
        danhSach.clear();
        danhSach.addAll(moi.danhSach);
    }

    private int chiSo(String ma) {
        if (ma != null)
            for (int i = 0; i < danhSach.size(); i++)
                if (danhSach.get(i).getMaNhaCungCap().equalsIgnoreCase(ma.trim())) return i;
        return -1;
    }

    private static void kiemTra(NhaCungCap ncc) {
        if (ncc == null || !DanhSachKhachHang.maHopLe(ncc.getMaNhaCungCap())
                || ncc.getTenNhaCungCap() == null || ncc.getTenNhaCungCap().isBlank()
                || ncc.getDiaChi() == null || ncc.getDiaChi().isBlank()
                || !DanhSachKhachHang.soDienThoaiHopLe(ncc.getSoDienThoai()))
            throw new IllegalArgumentException("Thong tin nha cung cap khong hop le.");
    }
}
