package services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import models.*;

public class DanhSachSanPham implements LuuTruFile {
    private static final String TIEU_DE_CU = "SAN_PHAM_V1";
    private static final String TIEU_DE = "kieu\tmaSanPham\ttenSanPham\tgiaBan\tthuongHieu\tsoLuongTon\tmaLoai\tkichCoGiay\tmauSac\tloaiDeGiay\tmonTheThao\tvatLieu\tkhoiLuongKg\tkichCoQuanAo";
    private final List<SanPham> danhSach = new ArrayList<>();

    public DanhSachSanPham(SanPham[] banDau) {
        for (SanPham sp : banDau) them(sp);
    }

    public List<SanPham> getDanhSach() {
        return List.copyOf(danhSach);
    }

    public void them(SanPham sp) {
        kiemTra(sp);
        if (timTheoMa(sp.getMaSanPham()) != null)
            throw new IllegalArgumentException("Ma san pham da ton tai.");
        danhSach.add(sp);
    }

    public boolean suaTheoMa(String ma, SanPham moi) {
        int i = chiSo(ma);
        if (i < 0) return false;
        kiemTra(moi);
        if (!danhSach.get(i).getMaSanPham().equalsIgnoreCase(moi.getMaSanPham()))
            throw new IllegalArgumentException("Khong duoc doi ma san pham.");
        danhSach.set(i, moi);
        return true;
    }

    public boolean xoaTheoMa(String ma) {
        int i = chiSo(ma);
        if (i < 0) return false;
        danhSach.remove(i);
        return true;
    }

    public SanPham timTheoMa(String ma) {
        int i = chiSo(ma);
        return i < 0 ? null : danhSach.get(i);
    }

    public List<SanPham> timGanDung(String tuKhoa) {
        String khoa = tuKhoa.toLowerCase(Locale.ROOT);
        List<SanPham> ketQua = new ArrayList<>();
        for (SanPham sp : danhSach)
            if (sp.getMaSanPham().toLowerCase(Locale.ROOT).contains(khoa)
                    || sp.getTenSanPham().toLowerCase(Locale.ROOT).contains(khoa))
                ketQua.add(sp);
        return ketQua;
    }

    public List<SanPham> locTheoLoai(String maLoai) {
        List<SanPham> ketQua = new ArrayList<>();
        for (SanPham sp : danhSach)
            if (sp.getMaLoai().equalsIgnoreCase(maLoai)) ketQua.add(sp);
        return ketQua;
    }

    @Override
    public void ghiFile(String duongDan) throws IOException {
        List<String> dong = new ArrayList<>();
        dong.add(TIEU_DE);
        for (SanPham sp : danhSach) {
            kiemTra(sp);
            String[] chung = { sp.getMaSanPham(), sp.getTenSanPham(),
                    Double.toString(sp.getGiaban()), sp.getThuongHieu(),
                    Integer.toString(sp.getSoLuongTon()), sp.getMaLoai() };
            if (sp instanceof GiayTheThao) {
                GiayTheThao giay = (GiayTheThao) sp;
                dong.add(FileText.dong("GIAY", chung[0], chung[1], chung[2], chung[3],
                        chung[4], chung[5], Double.toString(giay.getKichCoGiay()),
                        giay.getMauSac(), giay.getLoaiDeGiay(), "", "", "", ""));
            } else if (sp instanceof DungCuTheThao) {
                DungCuTheThao dungCu = (DungCuTheThao) sp;
                dong.add(FileText.dong("DUNG_CU", chung[0], chung[1], chung[2], chung[3],
                        chung[4], chung[5], "", "", "", dungCu.getMonTheThao(),
                        dungCu.getVatLieu(), Double.toString(dungCu.getKhoiLuong()), ""));
            } else if (sp instanceof QuanAoTheThao) {
                QuanAoTheThao quanAo = (QuanAoTheThao) sp;
                dong.add(FileText.dong("QUAN_AO", chung[0], chung[1], chung[2], chung[3],
                        chung[4], chung[5], "", "", "", "", "", "", quanAo.getKichCoQuanAo()));
            }
        }
        FileText.ghi(duongDan, dong);
    }

    @Override
    public void docFile(String duongDan) throws IOException {
        DanhSachSanPham moi = new DanhSachSanPham(new SanPham[0]);
        List<String[]> dong = FileText.doc(duongDan, TIEU_DE, TIEU_DE_CU);
        for (int i = 0; i < dong.size(); i++) {
            String[] c = dong.get(i);
            try {
                if (c.length < 8) throw new IllegalArgumentException("Thieu cot.");
                double gia = Double.parseDouble(c[3]);
                int ton = Integer.parseInt(c[5]);
                SanPham sp;
                switch (c[0]) {
                    case "GIAY":
                        if (c.length != 10 && c.length != 14) throw new IllegalArgumentException("Sai so cot.");
                        if (c.length == 14 && !rong(c, 10, 14))
                            throw new IllegalArgumentException("Cot cua loai khac phai de trong.");
                        sp = new GiayTheThao(c[1], c[2], gia, c[4], ton, c[6],
                                Double.parseDouble(c[7]), c[8], c[9]);
                        break;
                    case "DUNG_CU":
                        if (c.length != 10 && c.length != 14) throw new IllegalArgumentException("Sai so cot.");
                        if (c.length == 14 && (!rong(c, 7, 10) || !rong(c, 13, 14)))
                            throw new IllegalArgumentException("Cot cua loai khac phai de trong.");
                        sp = new DungCuTheThao(c[1], c[2], gia, c[4], ton, c[6],
                                c.length == 14 ? c[10] : c[7],
                                c.length == 14 ? c[11] : c[8],
                                Double.parseDouble(c.length == 14 ? c[12] : c[9]));
                        break;
                    case "QUAN_AO":
                        if (c.length != 8 && c.length != 14) throw new IllegalArgumentException("Sai so cot.");
                        if (c.length == 14 && !rong(c, 7, 13))
                            throw new IllegalArgumentException("Cot cua loai khac phai de trong.");
                        sp = new QuanAoTheThao(c[1], c[2], gia, c[4], ton, c[6],
                                c.length == 14 ? c[13] : c[7]);
                        break;
                    default: throw new IllegalArgumentException("Kieu san pham khong hop le.");
                }
                moi.them(sp);
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
                if (danhSach.get(i).getMaSanPham().equalsIgnoreCase(ma.trim())) return i;
        return -1;
    }

    private static boolean rong(String[] cot, int tu, int den) {
        for (int i = tu; i < den; i++) if (!cot[i].isBlank()) return false;
        return true;
    }

    private static void kiemTra(SanPham sp) {
        if (sp == null || !DanhSachKhachHang.maHopLe(sp.getMaSanPham())
                || trong(sp.getTenSanPham()) || trong(sp.getThuongHieu())
                || !DanhSachKhachHang.maHopLe(sp.getMaLoai())
                || !Double.isFinite(sp.getGiaban()) || sp.getGiaban() < 0
                || sp.getSoLuongTon() < 0
                || (sp instanceof GiayTheThao && (
                    !Double.isFinite(((GiayTheThao) sp).getKichCoGiay())
                    || ((GiayTheThao) sp).getKichCoGiay() <= 0
                    || trong(((GiayTheThao) sp).getMauSac())
                    || trong(((GiayTheThao) sp).getLoaiDeGiay())))
                || (sp instanceof DungCuTheThao && (
                    trong(((DungCuTheThao) sp).getMonTheThao())
                    || trong(((DungCuTheThao) sp).getVatLieu())
                    || !Double.isFinite(((DungCuTheThao) sp).getKhoiLuong())
                    || ((DungCuTheThao) sp).getKhoiLuong() < 0))
                || (sp instanceof QuanAoTheThao &&
                    trong(((QuanAoTheThao) sp).getKichCoQuanAo()))
                || !(sp instanceof GiayTheThao || sp instanceof DungCuTheThao
                    || sp instanceof QuanAoTheThao))
            throw new IllegalArgumentException("Thong tin san pham khong hop le.");
    }

    private static boolean trong(String s) {
        return s == null || s.isBlank();
    }
}
