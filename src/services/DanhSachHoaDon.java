package services;

import models.*;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Scanner;

public class DanhSachHoaDon implements IQuanLyDanhSach<HoaDon> {
    private ArrayList<HoaDon> ds = new ArrayList<>();

    public int getSoLuong() {
        return ds.size();
    }

    public HoaDon[] getTatCa() {
        HoaDon[] kq = new HoaDon[ds.size()];
        for (int i = 0; i < ds.size(); i++)
            kq[i] = new HoaDon(ds.get(i));
        return kq;
    }

    public boolean kiemTraMaDuyNhat(String ma) {
        return timTheoMa(ma) == null;
    }

    public HoaDon timTheoMa(String ma) {
        if (ma == null) return null;
        for (HoaDon hd : ds)
            if (hd.getMaHoaDon().equalsIgnoreCase(ma.trim()))
                return new HoaDon(hd);
        return null;
    }

    @Override
    public boolean them(HoaDon doiTuong) {
        if (doiTuong == null || !doiTuong.kiemTraHopLe()) return false;
        if (!kiemTraMaDuyNhat(doiTuong.getMaHoaDon())) return false;
        return ds.add(new HoaDon(doiTuong));
    }

    public int themNhieu(HoaDon[] cacDoiTuong) {
        int daThem = 0;
        for (HoaDon hd : cacDoiTuong)
            if (them(hd)) daThem++;
        return daThem;
    }

    @Override
    public boolean sua(String ma, HoaDon moi) {
        if (moi == null || !moi.kiemTraHopLe()) return false;
        for (int i = 0; i < ds.size(); i++) {
            if (ds.get(i).getMaHoaDon().equalsIgnoreCase(ma == null ? "" : ma.trim())) {
                if (!ds.get(i).getMaHoaDon().equalsIgnoreCase(moi.getMaHoaDon())) return false;
                ds.set(i, new HoaDon(moi));
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean xoa(String ma) {
        if (ma == null) return false;
        for (int i = 0; i < ds.size(); i++) {
            if (ds.get(i).getMaHoaDon().equalsIgnoreCase(ma.trim())) {
                ds.remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public HoaDon[] timKiem(String tuKhoa) {
        String key = tuKhoa == null ? "" : tuKhoa.trim().toLowerCase();
        ArrayList<HoaDon> kq = new ArrayList<>();
        for (HoaDon hd : ds)
            if (phuHop(hd, key)) kq.add(new HoaDon(hd));
        return kq.toArray(new HoaDon[0]);
    }

    private boolean phuHop(HoaDon x, String key) {
        return x.getMaHoaDon().toLowerCase().contains(key)
                || x.getKhachHang().getMaKhachHang().toLowerCase().contains(key);
    }

    public void hienThiDanhSach() {
        if (ds.isEmpty()) { System.out.println("Danh sach rong."); return; }
        for (HoaDon hd : ds)
            System.out.println(hd.hienThiThongTin());
    }

    public BigDecimal thongKeTongTien() {
        BigDecimal tong = BigDecimal.ZERO;
        for (HoaDon hd : ds)
            tong = tong.add(hd.tinhTongTien());
        return tong;
    }

    public BigDecimal thongKeTongTien(LocalDate tu, LocalDate den) {
        if (tu == null || den == null || tu.isAfter(den))
            throw new IllegalArgumentException("Khoang ngay khong hop le.");
        BigDecimal tong = BigDecimal.ZERO;
        for (HoaDon hd : ds)
            if (!hd.getNgayLap().isBefore(tu) && !hd.getNgayLap().isAfter(den))
                tong = tong.add(hd.tinhTongTien());
        return tong;
    }

    // ================= MENU & NHẬP XUẤT =================
    public void menu(Scanner banPhim, DanhSachSanPham dsSP, DanhSachKhachHang dsKH, DanhSachNhanVien dsNV, Kho kho) {
        int chon;
        do {
            System.out.println("\n--- Quan ly Hoa don ---");
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
                case 2: themTuBanPhim(banPhim, dsSP, dsKH, dsNV, kho); break;
                case 3: suaTuBanPhim(banPhim, dsSP, dsKH, dsNV, kho); break;
                case 4: xoaHoaDonTuBanPhim(banPhim, kho); break;
                case 5: timTuBanPhim(banPhim); break;
                case 6: xemChiTiet(banPhim); break;
            }
        } while (chon != 0);
    }

    public boolean themHoaDon(HoaDon phieu, Kho kho) {
        if (phieu == null || !phieu.kiemTraHopLe()) return false;
        if (!them(phieu)) return false;
        if (kho != null) kho.capNhatTheoHoaDon(null, phieu);
        return true;
    }

    public boolean suaHoaDon(String ma, HoaDon moi, Kho kho) {
        HoaDon cu = timTheoMa(ma);
        if (cu == null || moi == null || !moi.kiemTraHopLe()) return false;
        if (!sua(ma, moi)) return false;
        if (kho != null) kho.capNhatTheoHoaDon(cu, moi);
        return true;
    }

    public boolean xoaHoaDon(String ma, Kho kho) {
        HoaDon cu = timTheoMa(ma);
        if (cu == null) return false;
        if (kho != null) kho.capNhatTheoHoaDon(cu, null);
        return xoa(ma);
    }

    private void themTuBanPhim(Scanner banPhim, DanhSachSanPham dsSP, DanhSachKhachHang dsKH, DanhSachNhanVien dsNV, Kho kho) {
        HoaDon phieu = nhapHoaDon(banPhim, dsKH, dsNV, nhapChuoi(banPhim, "Ma moi: "));
        soanHoaDon(banPhim, dsSP, phieu);
        thongBao(themHoaDon(phieu, kho));
    }

    private void suaTuBanPhim(Scanner banPhim, DanhSachSanPham dsSP, DanhSachKhachHang dsKH, DanhSachNhanVien dsNV, Kho kho) {
        HoaDon moi = chonHoaDon(banPhim);
        moi.setNgayLap(nhapNgay(banPhim, "Ngay lap moi"));
        moi.setKhachHang(chonKhachHang(banPhim, dsKH));
        moi.setNhanVien(chonNhanVien(banPhim, dsNV));
        soanHoaDon(banPhim, dsSP, moi);
        thongBao(suaHoaDon(moi.getMaHoaDon(), moi, kho));
    }

    private void xoaHoaDonTuBanPhim(Scanner banPhim, Kho kho) {
        thongBao(xoaHoaDon(nhapChuoi(banPhim, "Ma can xoa: "), kho));
    }

    private void timTuBanPhim(Scanner banPhim) {
        HoaDon[] kq = timKiem(nhapChuoi(banPhim, "Ma chung tu/ma doi tac: "));
        for (int i = 0; i < kq.length; i++)
            hienThiChiTietHD(kq[i]);
        System.out.println("Tim thay: " + kq.length);
    }

    private void xemChiTiet(Scanner banPhim) {
        hienThiChiTietHD(chonHoaDon(banPhim));
    }

    private HoaDon nhapHoaDon(Scanner banPhim, DanhSachKhachHang dsKH, DanhSachNhanVien dsNV, String ma) {
        return new HoaDon(ma, nhapNgay(banPhim, "Ngay lap"), chonKhachHang(banPhim, dsKH), chonNhanVien(banPhim, dsNV));
    }

    private HoaDon chonHoaDon(Scanner banPhim) {
        HoaDon phieu = timTheoMa(nhapChuoi(banPhim, "Ma chung tu: "));
        if (phieu == null)
            throw new IllegalArgumentException("Khong tim thay chung tu.");
        return phieu;
    }

    private void soanHoaDon(Scanner banPhim, DanhSachSanPham dsSP, HoaDon phieu) {
        int chon;
        do {
            System.out.println("Chi tiet: 1. Xem   2. Them   3. Sua   4. Xoa   0. Luu ban soan");
            chon = nhapSoNguyen(banPhim, "Chon: ", 0, 4);
            switch (chon) {
                case 1: hienThiChiTietHD(phieu); break;
                case 2: thongBao(phieu.themChiTiet(nhapChiTietHoaDon(banPhim, chonSanPham(banPhim, dsSP)))); break;
                case 3: suaDongHoaDon(banPhim, dsSP, phieu); break;
                case 4: thongBao(phieu.xoaChiTiet(nhapChuoi(banPhim, "Ma SP can xoa: "))); break;
            }
        } while (chon != 0);
    }

    private ChiTietHoaDon nhapChiTietHoaDon(Scanner banPhim, SanPham sp) {
        return new ChiTietHoaDon(sp, nhapSoNguyen(banPhim, "So luong: ", 1, Integer.MAX_VALUE),
                nhapTien(banPhim, "Don gia ban (gia hien tai: " + sp.getGiaBan().toPlainString() + "): "));
    }

    private void suaDongHoaDon(Scanner banPhim, DanhSachSanPham dsSP, HoaDon phieu) {
        SanPham sp = chonSanPham(banPhim, dsSP);
        thongBao(phieu.suaChiTiet(sp.getMaSanPham(), nhapChiTietHoaDon(banPhim, sp)));
    }

    private void hienThiChiTietHD(HoaDon phieu) {
        System.out.println(phieu.hienThiThongTin());
        ChiTietHoaDon[] chitiet = phieu.getChiTiet();
        for (int i = 0; i < chitiet.length; i++)
            System.out.println("  " + chitiet[i].hienThiThongTin());
    }

    private KhachHang chonKhachHang(Scanner banPhim, DanhSachKhachHang dsKH) {
        KhachHang x = dsKH.timTheoMa(nhapChuoi(banPhim, "Ma KhachHang: "));
        if (x == null)
            throw new IllegalArgumentException("Khong tim thay KhachHang.");
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
