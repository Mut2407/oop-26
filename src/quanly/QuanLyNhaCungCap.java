package quanly;

import models.NhaCungCap;

public class QuanLyNhaCungCap extends QuanLyCuaHang {

    @Override
    public void menuChinh() {
        int chon;
        do {
            System.out.println("\n=== QUAN LY NHA CUNG CAP ===");
            System.out.println("1. Xem danh sach");
            System.out.println("2. Them");
            System.out.println("3. Sua");
            System.out.println("4. Xoa");
            System.out.println("5. Tim kiem");
            System.out.println("0. Quay lai");
            System.out.print("Chon: ");
            chon = nhapSoNguyen("Chon: ", 0, 5);
            switch (chon) {
                case 1 -> dsNhaCungCap.xuat();
                case 2 -> themNhaCungCap();
                case 3 -> suaNhaCungCap();
                case 4 -> xoaNhaCungCap();
                case 5 -> timNhaCungCap();
                case 0 -> System.out.println("Quay lai...");
                default -> System.out.println("Lua chon khong hop le.");
            }
        } while (chon != 0);
    }

    private void themNhaCungCap() {
        int n = nhapSoNguyen("Nhap so luong can them (1-100): ", 1, 100);
        for (int i = 0; i < n; i++) {
            if (n > 1) System.out.println("\n-- Nhap phan tu thu " + (i + 1) + " --");
            String ma = nhapChuoi("Ma moi: ");
            boolean ok = dsNhaCungCap.them(new NhaCungCap(ma, nhapChuoi("Ten NCC: "),
                    nhapChuoi("SDT: "), nhapChuoi("Dia chi: ")));
            System.out.println(ok ? "Them thanh cong!" : "Them that bai (ma da ton tai).");
        }
    }

    private void suaNhaCungCap() {
        String ma = nhapChuoi("Ma can sua: ");
        boolean ok = dsNhaCungCap.sua(ma, new NhaCungCap(ma, nhapChuoi("Ten NCC moi: "),
                nhapChuoi("SDT moi: "), nhapChuoi("Dia chi moi: ")));
        System.out.println(ok ? "Sua thanh cong!" : "Khong tim thay ma can sua!");
    }

    private void xoaNhaCungCap() {
        String ma = nhapChuoi("Ma can xoa: ");
        boolean ok = dsNhaCungCap.xoa(ma);
        System.out.println(ok ? "Xoa thanh cong!" : "Khong tim thay ma can xoa!");
    }

    private void timNhaCungCap() {
        NhaCungCap[] kq = dsNhaCungCap.timKiem(nhapChuoi("Ma/ten/SDT can tim: "));
        for (NhaCungCap ncc : kq) {
            System.out.println(ncc.hienThiThongTin());
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
