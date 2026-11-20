package services;

import models.*;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.util.Scanner;

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

    // ================= MENU & NHẬP XUẤT =================
    public void menu(Scanner banPhim, DanhSachLoaiSP danhSachLoaiSP) {
        int chon;
        do {
            System.out.println("\n--- Quan ly San pham ---");
            System.out.println("1. Xem danh sach");
            System.out.println("2. Them");
            System.out.println("3. Sua");
            System.out.println("4. Xoa");
            System.out.println("5. Tim kiem");
            System.out.println("0. Quay lai");
            chon = nhapSoNguyen(banPhim, "Chon: ", 0, 5);
            switch (chon) {
                case 1: hienThiDanhSach(); break;
                case 2: themTuBanPhim(banPhim, danhSachLoaiSP); break;
                case 3: suaTuBanPhim(banPhim, danhSachLoaiSP); break;
                case 4: thongBao(xoa(nhapChuoi(banPhim, "Ma can xoa: "))); break;
                case 5: timTuBanPhim(banPhim); break;
            }
        } while (chon != 0);
    }

    private void themTuBanPhim(Scanner banPhim, DanhSachLoaiSP danhSachLoaiSP) {
        int n = nhapSoNguyen(banPhim, "Nhap so luong can them (1-100): ", 1, 100);
        for (int i = 0; i < n; i++) {
            if (n > 1) System.out.println("\n-- Nhap phan tu thu " + (i + 1) + " --");
            String ma = nhapChuoi(banPhim, "Ma moi: ");
            thongBao(them(nhapSanPham(banPhim, danhSachLoaiSP, ma, null)));
        }
    }

    private void suaTuBanPhim(Scanner banPhim, DanhSachLoaiSP danhSachLoaiSP) {
        String ma = nhapChuoi(banPhim, "Ma can sua: ");
        SanPham cu = timTheoMa(ma);
        if (cu == null)
            throw new IllegalArgumentException("Khong tim thay SP.");
        SanPham moi = nhapSanPham(banPhim, danhSachLoaiSP, cu.getMaSanPham(), cu);
        boolean thanhCong = sua(ma, moi);
        if (thanhCong)
            cu.capNhatTon(moi.getSoLuongTon() - cu.getSoLuongTon());
        thongBao(thanhCong);
    }

    private void timTuBanPhim(Scanner banPhim) {
        int chon = nhapSoNguyen(banPhim, "1. Ma chinh xac  2. Ma/ten gan dung  3. Loai  4. Khoang gia: ", 1, 4);
        SanPham[] kq = locSanPham(banPhim, chon);
        for (int i = 0; i < kq.length; i++)
            System.out.println(kq[i].hienThiThongTin());
        System.out.println("Tim thay: " + kq.length);
    }

    private SanPham[] locSanPham(Scanner banPhim, int chon) {
        if (chon == 1) {
            SanPham sp = timTheoMa(nhapChuoi(banPhim, "Ma: "));
            return sp == null ? new SanPham[0] : new SanPham[] {sp};
        }
        if (chon == 2)
            return timKiem(nhapChuoi(banPhim, "Ma/ten: "));
        if (chon == 3)
            return locTheoLoaiSP(nhapChuoi(banPhim, "Ma loai: "));
        return locTheoKhoangGia(nhapTien(banPhim, "Gia tu: "), nhapTien(banPhim, "Gia den: "));
    }

    private SanPham nhapSanPham(Scanner banPhim, DanhSachLoaiSP danhSachLoaiSP, String ma, SanPham cu) {
        int loai = cu == null ? nhapSoNguyen(banPhim, "1. Giay  2. Dung cu: ", 1, 2) : loaiLop(cu);
        SanPham sp = loai == 1 ? nhapGiay(banPhim, danhSachLoaiSP, ma) : nhapDungCu(banPhim, danhSachLoaiSP, ma);
        sp.capNhatTon(nhapSoNguyen(banPhim, "So luong ton kho: ", 0, Integer.MAX_VALUE));
        return sp;
    }

    private int loaiLop(SanPham sp) {
        if (sp instanceof GiayTheThao) return 1;
        return 2;
    }

    private LoaiSP chonLoaiSP(Scanner banPhim, DanhSachLoaiSP danhSachLoaiSP) {
        LoaiSP x = danhSachLoaiSP.timTheoMa(nhapChuoi(banPhim, "Ma LoaiSP: "));
        if (x == null)
            throw new IllegalArgumentException("Khong tim thay LoaiSP.");
        return x;
    }

    private GiayTheThao nhapGiay(Scanner banPhim, DanhSachLoaiSP danhSachLoaiSP, String ma) {
        return new GiayTheThao(ma, nhapChuoi(banPhim, "Ten SP: "), nhapChuoi(banPhim, "Thuong hieu: "),
                nhapTien(banPhim, "Gia ban: "), chonLoaiSP(banPhim, danhSachLoaiSP), nhapSoThuc(banPhim, "Co giay: "),
                nhapChuoi(banPhim, "Mau: "), nhapChuoi(banPhim, "Chat lieu: "), nhapChuoi(banPhim, "Loai de: "));
    }

    private DungCuTheThao nhapDungCu(Scanner banPhim, DanhSachLoaiSP danhSachLoaiSP, String ma) {
        return new DungCuTheThao(ma, nhapChuoi(banPhim, "Ten SP: "), nhapChuoi(banPhim, "Thuong hieu: "),
                nhapTien(banPhim, "Gia ban: "), chonLoaiSP(banPhim, danhSachLoaiSP), nhapChuoi(banPhim, "Mon the thao: "),
                nhapChuoi(banPhim, "Vat lieu: "), nhapSoThuc(banPhim, "Khoi luong (kg): "));
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

    private double nhapSoThuc(Scanner banPhim, String loiNhac) {
        double giaTri = Double.parseDouble(nhapChuoi(banPhim, loiNhac));
        if (!Double.isFinite(giaTri) || giaTri <= 0)
            throw new IllegalArgumentException("Nhap so duong huu han.");
        return giaTri;
    }

    private BigDecimal nhapTien(Scanner banPhim, String loiNhac) {
        BigDecimal giaTri = new BigDecimal(nhapChuoi(banPhim, loiNhac));
        if (giaTri.signum() < 0)
            throw new IllegalArgumentException("So tien phai >= 0.");
        return giaTri;
    }

    private void thongBao(boolean thanhCong) {
        System.out.println(thanhCong ? "Thao tac thanh cong!"
                : "That bai: Vui long kiem tra lai ma hoac du lieu nhap vao.");
    }
}
