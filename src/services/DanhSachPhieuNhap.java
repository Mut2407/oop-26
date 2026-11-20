package services;

import models.*;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Scanner;

public class DanhSachPhieuNhap implements IQuanLyDanhSach<PhieuNhap> {
    private ArrayList<PhieuNhap> ds = new ArrayList<>();

    public int getSoLuong() {
        return ds.size();
    }

    public PhieuNhap[] getTatCa() {
        PhieuNhap[] kq = new PhieuNhap[ds.size()];
        for (int i = 0; i < ds.size(); i++)
            kq[i] = new PhieuNhap(ds.get(i));
        return kq;
    }

    public boolean kiemTraMaDuyNhat(String ma) {
        return timTheoMa(ma) == null;
    }

    public PhieuNhap timTheoMa(String ma) {
        if (ma == null) return null;
        for (PhieuNhap pn : ds)
            if (pn.getMaPhieuNhap().equalsIgnoreCase(ma.trim()))
                return new PhieuNhap(pn);
        return null;
    }

    @Override
    public boolean them(PhieuNhap doiTuong) {
        if (doiTuong == null || !doiTuong.kiemTraHopLe()) return false;
        if (!kiemTraMaDuyNhat(doiTuong.getMaPhieuNhap())) return false;
        return ds.add(new PhieuNhap(doiTuong));
    }

    public int themNhieu(PhieuNhap[] cacDoiTuong) {
        int daThem = 0;
        for (PhieuNhap pn : cacDoiTuong)
            if (them(pn)) daThem++;
        return daThem;
    }

    @Override
    public boolean sua(String ma, PhieuNhap moi) {
        if (moi == null || !moi.kiemTraHopLe()) return false;
        for (int i = 0; i < ds.size(); i++) {
            if (ds.get(i).getMaPhieuNhap().equalsIgnoreCase(ma == null ? "" : ma.trim())) {
                if (!ds.get(i).getMaPhieuNhap().equalsIgnoreCase(moi.getMaPhieuNhap())) return false;
                ds.set(i, new PhieuNhap(moi));
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean xoa(String ma) {
        if (ma == null) return false;
        for (int i = 0; i < ds.size(); i++) {
            if (ds.get(i).getMaPhieuNhap().equalsIgnoreCase(ma.trim())) {
                ds.remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public PhieuNhap[] timKiem(String tuKhoa) {
        String key = tuKhoa == null ? "" : tuKhoa.trim().toLowerCase();
        ArrayList<PhieuNhap> kq = new ArrayList<>();
        for (PhieuNhap pn : ds)
            if (phuHop(pn, key)) kq.add(new PhieuNhap(pn));
        return kq.toArray(new PhieuNhap[0]);
    }

    private boolean phuHop(PhieuNhap x, String key) {
        return x.getMaPhieuNhap().toLowerCase().contains(key)
                || x.getNhaCungCap().getMaNhaCungCap().toLowerCase().contains(key);
    }

    public void hienThiDanhSach() {
        if (ds.isEmpty()) { System.out.println("Danh sach rong."); return; }
        for (PhieuNhap pn : ds)
            System.out.println(pn.hienThiThongTin());
    }

    public BigDecimal thongKeTongTien() {
        BigDecimal tong = BigDecimal.ZERO;
        for (PhieuNhap pn : ds)
            tong = tong.add(pn.tinhTongTien());
        return tong;
    }

    public BigDecimal thongKeTongTien(LocalDate tu, LocalDate den) {
        if (tu == null || den == null || tu.isAfter(den))
            throw new IllegalArgumentException("Khoang ngay khong hop le.");
        BigDecimal tong = BigDecimal.ZERO;
        for (PhieuNhap pn : ds)
            if (!pn.getNgayLap().isBefore(tu) && !pn.getNgayLap().isAfter(den))
                tong = tong.add(pn.tinhTongTien());
        return tong;
    }

    // ================= MENU & NHẬP XUẤT =================
    public void menu(Scanner banPhim, DanhSachSanPham dsSP, DanhSachNhaCungCap dsNCC, DanhSachNhanVien dsNV, Kho kho) {
        int chon;
        do {
            System.out.println("\n--- Quan ly Phieu nhap ---");
            System.out.println("1. Xem danh sach");
            System.out.println("2. Them");
            System.out.println("3. Sua");
            System.out.println("4. Xoa");
            System.out.println("5. Tim kiem");
            System.out.println("6. Xem chi tiet");
            System.out.println("0. Quay lai");
            chon = nhapSoNguyen(banPhim, "Chon: ", 0, 6);
            switch (chon) {
                case 1: hienThiDanhSach(); break;
                case 2: themTuBanPhim(banPhim, dsSP, dsNCC, dsNV, kho); break;
                case 3: suaTuBanPhim(banPhim, dsSP, dsNCC, dsNV, kho); break;
                case 4: xoaPhieuNhapTuBanPhim(banPhim, kho); break;
                case 5: timTuBanPhim(banPhim); break;
                case 6: xemChiTiet(banPhim); break;
            }
        } while (chon != 0);
    }

    public boolean themPhieuNhap(PhieuNhap phieu, Kho kho) {
        if (phieu == null || !phieu.kiemTraHopLe()) return false;
        if (!them(phieu)) return false;
        if (kho != null) kho.capNhatTheoPhieuNhap(null, phieu);
        return true;
    }

    public boolean suaPhieuNhap(String ma, PhieuNhap moi, Kho kho) {
        PhieuNhap cu = timTheoMa(ma);
        if (cu == null || moi == null || !moi.kiemTraHopLe()) return false;
        if (!sua(ma, moi)) return false;
        if (kho != null) kho.capNhatTheoPhieuNhap(cu, moi);
        return true;
    }

    public boolean xoaPhieuNhap(String ma, Kho kho) {
        PhieuNhap cu = timTheoMa(ma);
        if (cu == null) return false;
        if (kho != null) kho.capNhatTheoPhieuNhap(cu, null);
        return xoa(ma);
    }

    private void themTuBanPhim(Scanner banPhim, DanhSachSanPham dsSP, DanhSachNhaCungCap dsNCC, DanhSachNhanVien dsNV, Kho kho) {
        PhieuNhap phieu = nhapPhieuNhap(banPhim, dsNCC, dsNV, nhapChuoi(banPhim, "Ma moi: "));
        soanPhieuNhap(banPhim, dsSP, phieu);
        thongBao(themPhieuNhap(phieu, kho));
    }

    private void suaTuBanPhim(Scanner banPhim, DanhSachSanPham dsSP, DanhSachNhaCungCap dsNCC, DanhSachNhanVien dsNV, Kho kho) {
        PhieuNhap moi = chonPhieuNhap(banPhim);
        moi.setNgayLap(nhapNgay(banPhim, "Ngay lap moi"));
        moi.setNhaCungCap(chonNhaCungCap(banPhim, dsNCC));
        moi.setNhanVien(chonNhanVien(banPhim, dsNV));
        soanPhieuNhap(banPhim, dsSP, moi);
        thongBao(suaPhieuNhap(moi.getMaPhieuNhap(), moi, kho));
    }

    private void xoaPhieuNhapTuBanPhim(Scanner banPhim, Kho kho) {
        thongBao(xoaPhieuNhap(nhapChuoi(banPhim, "Ma can xoa: "), kho));
    }

    private void timTuBanPhim(Scanner banPhim) {
        PhieuNhap[] kq = timKiem(nhapChuoi(banPhim, "Ma chung tu/ma doi tac: "));
        for (int i = 0; i < kq.length; i++)
            hienThiChiTietPN(kq[i]);
        System.out.println("Tim thay: " + kq.length);
    }

    private void xemChiTiet(Scanner banPhim) {
        hienThiChiTietPN(chonPhieuNhap(banPhim));
    }

    private PhieuNhap nhapPhieuNhap(Scanner banPhim, DanhSachNhaCungCap dsNCC, DanhSachNhanVien dsNV, String ma) {
        return new PhieuNhap(ma, nhapNgay(banPhim, "Ngay lap"), chonNhaCungCap(banPhim, dsNCC), chonNhanVien(banPhim, dsNV));
    }

    private PhieuNhap chonPhieuNhap(Scanner banPhim) {
        PhieuNhap phieu = timTheoMa(nhapChuoi(banPhim, "Ma chung tu: "));
        if (phieu == null)
            throw new IllegalArgumentException("Khong tim thay chung tu.");
        return phieu;
    }

    private void soanPhieuNhap(Scanner banPhim, DanhSachSanPham dsSP, PhieuNhap phieu) {
        int chon;
        do {
            System.out.println("Chi tiet: 1. Xem   2. Them   3. Sua   4. Xoa   0. Luu ban soan");
            chon = nhapSoNguyen(banPhim, "Chon: ", 0, 4);
            switch (chon) {
                case 1: hienThiChiTietPN(phieu); break;
                case 2: thongBao(phieu.themChiTiet(nhapChiTietPhieuNhap(banPhim, chonSanPham(banPhim, dsSP)))); break;
                case 3: suaDongPhieuNhap(banPhim, dsSP, phieu); break;
                case 4: thongBao(phieu.xoaChiTiet(nhapChuoi(banPhim, "Ma SP can xoa: "))); break;
            }
        } while (chon != 0);
    }

    private ChiTietPhieuNhap nhapChiTietPhieuNhap(Scanner banPhim, SanPham sp) {
        return new ChiTietPhieuNhap(sp, nhapSoNguyen(banPhim, "So luong: ", 1, Integer.MAX_VALUE),
                nhapTien(banPhim, "Don gia nhap: "));
    }

    private void suaDongPhieuNhap(Scanner banPhim, DanhSachSanPham dsSP, PhieuNhap phieu) {
        SanPham sp = chonSanPham(banPhim, dsSP);
        thongBao(phieu.suaChiTiet(sp.getMaSanPham(), nhapChiTietPhieuNhap(banPhim, sp)));
    }

    private void hienThiChiTietPN(PhieuNhap phieu) {
        System.out.println(phieu.hienThiThongTin());
        ChiTietPhieuNhap[] chitiet = phieu.getChiTiet();
        for (int i = 0; i < chitiet.length; i++)
            System.out.println("  " + chitiet[i].hienThiThongTin());
    }

    private NhaCungCap chonNhaCungCap(Scanner banPhim, DanhSachNhaCungCap dsNCC) {
        NhaCungCap x = dsNCC.timTheoMa(nhapChuoi(banPhim, "Ma NhaCungCap: "));
        if (x == null)
            throw new IllegalArgumentException("Khong tim thay NhaCungCap.");
        return x;
    }

    private NhanVien chonNhanVien(Scanner banPhim, DanhSachNhanVien dsNV) {
        NhanVien x = dsNV.timTheoMa(nhapChuoi(banPhim, "Ma NhanVien: "));
        if (x == null)
            throw new IllegalArgumentException("Khong tim thay NhanVien.");
        return x;
    }

    private SanPham chonSanPham(Scanner banPhim, DanhSachSanPham dsSP) {
        SanPham x = dsSP.timTheoMa(nhapChuoi(banPhim, "Ma SP: "));
        if (x == null)
            throw new IllegalArgumentException("Khong tim thay san pham.");
        return x;
    }

    private String nhapChuoi(Scanner banPhim, String loiNhac) {
        System.out.print(loiNhac);
        String giaTri = banPhim.nextLine().trim();
        if (giaTri.isEmpty() || giaTri.contains("|") || giaTri.contains("\n") || giaTri.contains("\r"))
            throw new IllegalArgumentException("Chuoi khong duoc rong, chua dau | hay xuong dong.");
        return giaTri;
    }

    private int nhapSoNguyen(Scanner banPhim, String loiNhac, int min, int max) {
        int giaTri = Integer.parseInt(nhapChuoi(banPhim, loiNhac));
        if (giaTri < min || giaTri > max)
            throw new IllegalArgumentException("Nhap so nguyen tu " + min + " den " + max + ".");
        return giaTri;
    }

    private BigDecimal nhapTien(Scanner banPhim, String loiNhac) {
        BigDecimal giaTri = new BigDecimal(nhapChuoi(banPhim, loiNhac));
        if (giaTri.signum() < 0)
            throw new IllegalArgumentException("So tien phai >= 0.");
        return giaTri;
    }

    private LocalDate nhapNgay(Scanner banPhim, String loiNhac) {
        return LocalDate.parse(nhapChuoi(banPhim, loiNhac + " (yyyy-MM-dd): "));
    }

    private void thongBao(boolean thanhCong) {
        System.out.println(thanhCong ? "Thao tac thanh cong!"
                : "That bai: Vui long kiem tra lai ma hoac du lieu nhap vao.");
    }
}
