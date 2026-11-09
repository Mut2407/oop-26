package ui;

import java.math.BigDecimal;
import java.util.Scanner;
import services.DanhSachKhachHang;

public final class NhapLieuConsole {
    private final Scanner sc;

    public NhapLieuConsole(Scanner sc) {
        this.sc = sc;
    }

    public String docDong(String string) {
        System.out.print(string);
        return sc.hasNextLine() ? sc.nextLine().trim() : null;
    }

    public String nhapBatBuoc(String string) {
        String string2;
        while ((string2 = docDong(string)) != null && string2.isEmpty()) {
            System.out.println("Thong tin khong duoc de trong!");
        }
        return string2;
    }

    public Integer nhapSoNguyen(String string, int n) {
        String string2;
        while ((string2 = docDong(string)) != null) {
            try {
                int n2 = Integer.parseInt(string2);
                if (n2 >= n) {
                    return n2;
                }
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
            System.out.println("Vui long nhap so nguyen >= " + n + ".");
        }
        return null;
    }

    public String nhapSoDienThoai(String string, boolean bl) {
        String string2;
        while (!((string2 = docDong(string)) == null || bl && string2.isEmpty())) {
            if (DanhSachKhachHang.soDienThoaiHopLe(string2)) {
                return string2;
            }
            System.out.println("So dien thoai phai co 9-15 chu so, co the bat dau bang +.");
        }
        return string2;
    }

    public BigDecimal nhapGia() {
        return nhapGia("Gia ban: ");
    }

    public BigDecimal nhapGia(String string) {
        String string2;
        while ((string2 = docDong(string)) != null) {
            try {
                BigDecimal bigDecimal = new BigDecimal(string2);
                if (bigDecimal.signum() >= 0) {
                    return bigDecimal;
                }
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
            System.out.println("Vui long nhap gia >= 0.");
        }
        return null;
    }

    public Double nhapSoThuc(String string, double d) {
        String string2;
        while ((string2 = docDong(string)) != null) {
            try {
                double d2 = Double.parseDouble(string2);
                if (Double.isFinite(d2) && d2 >= d) {
                    return d2;
                }
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
            System.out.println("Vui long nhap so >= " + d + ".");
        }
        return null;
    }
}
