package quanly;

import models.LoaiSP;

public class QuanLyLoaiSP extends QuanLyCuaHang {

    @Override
    public void menuChinh() {
        int chon;
        do {
            System.out.println("\n=== QUAN LY LOAI SAN PHAM ===");
            System.out.println("1. Xem danh sach");
            System.out.println("2. Them");
            System.out.println("3. Sua");
            System.out.println("4. Xoa");
            System.out.println("5. Tim kiem");
            System.out.println("0. Quay lai");
            System.out.print("Chon: ");
            chon = nhapSoNguyen("Chon: ", 0, 5);
            switch (chon) {
                case 1 -> dsLoaiSP.xuat();
                case 2 -> themLoaiSP();
                case 3 -> suaLoaiSP();
                case 4 -> xoaLoaiSP();
                case 5 -> timLoaiSP();
                case 0 -> System.out.println("Quay lai...");
                default -> System.out.println("Lua chon khong hop le.");
            }
        } while (chon != 0);
    }

    private void themLoaiSP() {
        int n = nhapSoNguyen("Nhap so luong can them (1-100): ", 1, 100);
        for (int i = 0; i < n; i++) {
            if (n > 1) System.out.println("\n-- Nhap phan tu thu " + (i + 1) + " --");
            String ma = nhapChuoi("Ma moi: ");
            boolean ok = dsLoaiSP.them(new LoaiSP(ma, nhapChuoi("Ten loai: "), nhapChuoi("Mo ta: ")));
            System.out.println(ok ? "Them thanh cong!" : "Them that bai (ma da ton tai).");
        }
    }

    private void suaLoaiSP() {
        String ma = nhapChuoi("Ma can sua: ");
        boolean ok = dsLoaiSP.sua(ma, new LoaiSP(ma, nhapChuoi("Ten loai moi: "), nhapChuoi("Mo ta moi: ")));
        System.out.println(ok ? "Sua thanh cong!" : "Khong tim thay ma can sua!");
    }

    private void xoaLoaiSP() {
        String ma = nhapChuoi("Ma can xoa: ");
        boolean ok = dsLoaiSP.xoa(ma);
        System.out.println(ok ? "Xoa thanh cong!" : "Khong tim thay ma can xoa!");
    }

    private void timLoaiSP() {
        LoaiSP[] kq = dsLoaiSP.timKiem(nhapChuoi("Ma/ten loai can tim: "));
        for (LoaiSP x : kq) {
            System.out.println(x.hienThiThongTin());
        }
        System.out.println("Tim thay: " + kq.length);
    }

    private String nhapChuoi(String loiNhac) {
        System.out.print(loiNhac);
        String giaTri = sc.nextLine().trim();
        if (giaTri.isEmpty() || giaTri.contains("|") || giaTri.contains("\n") || giaTri.contains("\r"))
            throw new IllegalArgumentException("Chuoi khong duoc rong, chua dau | hay xuong dong.");
        return giaTri;
    }

    private int nhapSoNguyen(String loiNhac, int min, int max) {
        int giaTri = Integer.parseInt(nhapChuoi(loiNhac));
        if (giaTri < min || giaTri > max)
            throw new IllegalArgumentException("Nhap so nguyen tu " + min + " den " + max + ".");
        return giaTri;
    }
}
