/*
 * Decompiled with CFR 0.152.
 */
package services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import models.NhanVien;
import services.DanhSachKhachHang;
import services.FileText;
import services.LuuTruFile;
import services.VanBanConsole;

public final class DanhSachNhanVien
implements LuuTruFile {
    private static final int SUC_CHUA_BAN_DAU = 10;
    private static final String TIEU_DE = "maNhanVien\thoTen\tcaLamViec\tsoDienThoai\tdiaChi";
    private NhanVien[] danhSach = new NhanVien[10];
    private int soLuong;

    public DanhSachNhanVien() {
    }

    public DanhSachNhanVien(NhanVien[] nhanVienArray) {
        this();
        this.nhapDanhSach(nhanVienArray);
    }

    public int getSoLuong() {
        return this.soLuong;
    }

    public NhanVien[] getDanhSach() {
        NhanVien[] nhanVienArray = new NhanVien[this.soLuong];
        for (int i = 0; i < this.soLuong; ++i) {
            nhanVienArray[i] = DanhSachNhanVien.saoChep(this.danhSach[i]);
        }
        return nhanVienArray;
    }

    public void nhapDanhSach(NhanVien[] nhanVienArray) {
        if (nhanVienArray == null) {
            throw new IllegalArgumentException("Danh sach khong duoc null.");
        }
        DanhSachNhanVien danhSachNhanVien = new DanhSachNhanVien();
        for (NhanVien nhanVien : nhanVienArray) {
            danhSachNhanVien.them(nhanVien);
        }
        this.danhSach = danhSachNhanVien.danhSach;
        this.soLuong = danhSachNhanVien.soLuong;
    }

    public void them(NhanVien nhanVien) {
        NhanVien nhanVien2 = DanhSachNhanVien.kiemTraVaSaoChep(nhanVien);
        if (this.chiSoTheoMa(nhanVien2.getMaNhanVien()) >= 0) {
            throw new IllegalArgumentException("Ma nhan vien da ton tai!");
        }
        if (this.soLuong == this.danhSach.length) {
            this.danhSach = Arrays.copyOf(this.danhSach, Math.max(1, this.danhSach.length * 2));
        }
        this.danhSach[this.soLuong++] = nhanVien2;
    }

    public void themNhieu(NhanVien[] nhanVienArray) {
        if (nhanVienArray == null) {
            throw new IllegalArgumentException("Danh sach khong duoc null.");
        }
        DanhSachNhanVien danhSachNhanVien = new DanhSachNhanVien(this.getDanhSach());
        for (NhanVien nhanVien : nhanVienArray) {
            danhSachNhanVien.them(nhanVien);
        }
        this.danhSach = danhSachNhanVien.danhSach;
        this.soLuong = danhSachNhanVien.soLuong;
    }

    public boolean sua(String string, String string2, String string3, String string4, String string5) {
        int n = this.chiSoTheoMa(string);
        if (n < 0) {
            return false;
        }
        this.danhSach[n] = DanhSachNhanVien.kiemTraVaSaoChep(new NhanVien(this.danhSach[n].getMaNhanVien(), string2, string3, string4, string5));
        return true;
    }

    public boolean xoa(String string) {
        int n = this.chiSoTheoMa(string);
        if (n < 0) {
            return false;
        }
        for (int i = n; i < this.soLuong - 1; ++i) {
            this.danhSach[i] = this.danhSach[i + 1];
        }
        this.danhSach[--this.soLuong] = null;
        return true;
    }

    public NhanVien timTheoMa(String string) {
        int n = this.chiSoTheoMa(string);
        return n < 0 ? null : DanhSachNhanVien.saoChep(this.danhSach[n]);
    }

    public NhanVien[] timKiem(String string, String string2, String string3, String string4, String string5, boolean bl) {
        NhanVien[] nhanVienArray = new NhanVien[this.soLuong];
        int n = 0;
        for (int i = 0; i < this.soLuong; ++i) {
            NhanVien nhanVien = this.danhSach[i];
            if (!DanhSachNhanVien.khop(nhanVien.getMaNhanVien(), string, bl) || !DanhSachNhanVien.khop(nhanVien.getHoTen(), string2, bl) || !DanhSachNhanVien.khop(nhanVien.getCaLamViec(), string3, bl) || !DanhSachNhanVien.khop(nhanVien.getSoDienThoai(), string4, bl) || !DanhSachNhanVien.khop(nhanVien.getDiaChi(), string5, bl)) continue;
            nhanVienArray[n++] = DanhSachNhanVien.saoChep(nhanVien);
        }
        return Arrays.copyOf(nhanVienArray, n);
    }

    public String thongKe() {
        return "Tong so nhan vien: " + this.soLuong + "\nTheo ca lam viec:\n" + this.thongKeTheoNhom(true) + "Theo dia chi:\n" + this.thongKeTheoNhom(false);
    }

    private String thongKeTheoNhom(boolean bl) {
        String[] stringArray = new String[this.soLuong];
        int[] nArray = new int[this.soLuong];
        int n = 0;
        for (int i = 0; i < this.soLuong; ++i) {
            int n2;
            String string = bl ? this.danhSach[i].getCaLamViec() : this.danhSach[i].getDiaChi();
            for (n2 = 0; n2 < n && !stringArray[n2].equalsIgnoreCase(string); ++n2) {
            }
            if (n2 == n) {
                stringArray[n++] = string;
            }
            int n3 = n2;
            nArray[n3] = nArray[n3] + 1;
        }
        if (n == 0) {
            return "  Chua co du lieu.\n";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < n; ++i) {
            stringBuilder.append("  ").append(stringArray[i]).append(": ").append(nArray[i]).append(" nhan vien\n");
        }
        return stringBuilder.toString();
    }

    @Override
    public void ghiFile(String string) throws IOException {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add(TIEU_DE);
        for (int i = 0; i < this.soLuong; ++i) {
            NhanVien nhanVien = this.danhSach[i];
            arrayList.add(FileText.dong(nhanVien.getMaNhanVien(), nhanVien.getHoTen(), nhanVien.getCaLamViec(), nhanVien.getSoDienThoai(), nhanVien.getDiaChi()));
        }
        FileText.ghi(string, arrayList);
    }

    @Override
    public void docFile(String string) throws IOException {
        DanhSachNhanVien danhSachNhanVien = new DanhSachNhanVien();
        List<String[]> list = FileText.doc(string, TIEU_DE);
        for (int i = 0; i < list.size(); ++i) {
            String[] stringArray = list.get(i);
            try {
                if (stringArray.length != 5) {
                    throw new IllegalArgumentException("Phai co 5 cot.");
                }
                danhSachNhanVien.them(new NhanVien(stringArray[0], stringArray[1], stringArray[2], stringArray[3], stringArray[4]));
                continue;
            }
            catch (IllegalArgumentException illegalArgumentException) {
                throw new IOException("Dong " + (i + 2) + ": " + illegalArgumentException.getMessage(), illegalArgumentException);
            }
        }
        this.danhSach = danhSachNhanVien.danhSach;
        this.soLuong = danhSachNhanVien.soLuong;
    }

    private int chiSoTheoMa(String string) {
        if (string != null) {
            for (int i = 0; i < this.soLuong; ++i) {
                if (!this.danhSach[i].getMaNhanVien().equalsIgnoreCase(string.trim())) continue;
                return i;
            }
        }
        return -1;
    }

    private static NhanVien kiemTraVaSaoChep(NhanVien nhanVien) {
        if (nhanVien == null || !DanhSachKhachHang.maHopLe(nhanVien.getMaNhanVien())) {
            throw new IllegalArgumentException("Ma chi gom chu, so, dau _ hoac -.");
        }
        if (DanhSachNhanVien.rong(nhanVien.getHoTen()) || DanhSachNhanVien.rong(nhanVien.getCaLamViec()) || DanhSachNhanVien.rong(nhanVien.getDiaChi())) {
            throw new IllegalArgumentException("Ho ten, ca lam viec va dia chi khong duoc rong.");
        }
        if (!DanhSachKhachHang.soDienThoaiHopLe(nhanVien.getSoDienThoai())) {
            throw new IllegalArgumentException("So dien thoai phai co 9-15 chu so, co the bat dau bang +.");
        }
        return new NhanVien(nhanVien.getMaNhanVien().trim(), nhanVien.getHoTen().trim(), nhanVien.getCaLamViec().trim(), nhanVien.getSoDienThoai().trim(), nhanVien.getDiaChi().trim());
    }

    private static boolean rong(String string) {
        return string == null || string.trim().isEmpty();
    }

    private static NhanVien saoChep(NhanVien nhanVien) {
        return new NhanVien(nhanVien.getMaNhanVien(), nhanVien.getHoTen(), nhanVien.getCaLamViec(), nhanVien.getSoDienThoai(), nhanVien.getDiaChi());
    }

    private static boolean khop(String string, String string2, boolean bl) {
        if (DanhSachNhanVien.rong(string2)) {
            return true;
        }
        String string3 = string.toLowerCase(Locale.ROOT);
        String string4 = string2.trim().toLowerCase(Locale.ROOT);
        return bl ? string3.equals(string4) : string3.contains(string4);
    }

    public void quanLy(Scanner scanner, String string) {
        block26: while (true) {
            System.out.println("\n1. Xem danh sach nhan vien");
            System.out.println("2. Them 1 nhan vien");
            System.out.println("3. Sua theo ma");
            System.out.println("4. Xoa theo ma");
            System.out.println("5. Nhap danh sach moi gom n nhan vien");
            System.out.println("6. Them k nhan vien");
            System.out.println("7. Tim kiem theo nhieu khoa");
            System.out.println("8. Thong ke");
            System.out.println("9. Doc lai tu file (thay danh sach hien tai)");
            System.out.println("10. Luu vao file");
            System.out.println("0. Quay lai");
            System.out.println("===== QUAN LY NHAN VIEN =====");
            String string2 = DanhSachNhanVien.docDong(scanner, "Nhap lua chon: ");
            if (string2 == null || string2.equals("0")) {
                return;
            }
            try {
                switch (string2) {
                    case "1": {
                        DanhSachNhanVien.hienThi(this.getDanhSach());
                        break;
                    }
                    case "2": {
                        if (!this.nhapTuBanPhim(scanner, false, 1)) continue block26;
                        this.luu(string);
                        break;
                    }
                    case "3": {
                        if (!this.suaTuBanPhim(scanner)) continue block26;
                        this.luu(string);
                        break;
                    }
                    case "4": {
                        String string3 = DanhSachNhanVien.batBuoc(scanner, "Ma nhan vien can xoa: ");
                        if (string3 == null) {
                            return;
                        }
                        if (this.xoa(string3)) {
                            System.out.println("Da xoa nhan vien.");
                            this.luu(string);
                            break;
                        }
                        System.out.println("Khong tim thay nhan vien!");
                        break;
                    }
                    case "5": {
                        if (!this.nhapTuBanPhim(scanner, true)) continue block26;
                        this.luu(string);
                        break;
                    }
                    case "6": {
                        if (!this.nhapTuBanPhim(scanner, false)) continue block26;
                        this.luu(string);
                        break;
                    }
                    case "7": {
                        this.timTuBanPhim(scanner);
                        break;
                    }
                    case "8": {
                        System.out.println(VanBanConsole.khongDau(this.thongKe()));
                        break;
                    }
                    case "9": {
                        this.docFile(string);
                        System.out.println("Da doc file nhan vien.");
                        break;
                    }
                    case "10": {
                        this.luu(string);
                        break;
                    }
                    default: {
                        System.out.println("Lua chon khong hop le!");
                        break;
                    }
                }
                continue;
            }
            catch (IOException | IllegalArgumentException exception) {
                System.out.println(exception.getMessage());
                continue;
            }
        }
    }

    private boolean nhapTuBanPhim(Scanner scanner, boolean bl) {
        Integer n;
        if (bl) {
            System.out.println("Danh sach moi se thay the danh sach hien tai sau khi nhap xong.");
        }
        if ((n = DanhSachNhanVien.nhapSoLuong(scanner, bl ? "Nhap so luong nhan vien: " : "Nhap so luong nhan vien muon them (0 de huy): ")) == null || !bl && n == 0) {
            return false;
        }
        return this.nhapTuBanPhim(scanner, bl, n);
    }

    private boolean nhapTuBanPhim(Scanner scanner, boolean bl, int n) {
        DanhSachNhanVien danhSachNhanVien = bl ? new DanhSachNhanVien() : new DanhSachNhanVien(this.getDanhSach());
        for (int i = 0; i < n; ++i) {
            String string;
            System.out.println("--- Nhan vien " + (i + 1) + "/" + n + " ---");
            while (true) {
                if ((string = DanhSachNhanVien.batBuoc(scanner, "Ma nhan vien: ")) == null) {
                    return false;
                }
                if (!DanhSachKhachHang.maHopLe(string)) {
                    System.out.println("Ma khong hop le.");
                    continue;
                }
                if (danhSachNhanVien.timTheoMa(string) == null) break;
                System.out.println("Ma nhan vien da ton tai!");
            }
            String string2 = DanhSachNhanVien.batBuoc(scanner, "Ho ten: ");
            if (string2 == null) {
                return false;
            }
            String string3 = DanhSachNhanVien.batBuoc(scanner, "Ca lam viec: ");
            if (string3 == null) {
                return false;
            }
            String string4 = DanhSachNhanVien.nhapDienThoai(scanner, "So dien thoai: ", false);
            if (string4 == null) {
                return false;
            }
            String string5 = DanhSachNhanVien.batBuoc(scanner, "Dia chi: ");
            if (string5 == null) {
                return false;
            }
            danhSachNhanVien.them(new NhanVien(string, string2, string3, string4, string5));
        }
        this.danhSach = danhSachNhanVien.danhSach;
        this.soLuong = danhSachNhanVien.soLuong;
        System.out.println("Da " + (bl ? "nhap moi " : "them ") + n + " nhan vien.");
        return true;
    }

    private boolean suaTuBanPhim(Scanner scanner) {
        String string = DanhSachNhanVien.batBuoc(scanner, "Ma nhan vien can sua: ");
        if (string == null) {
            return false;
        }
        NhanVien nhanVien = this.timTheoMa(string);
        if (nhanVien == null) {
            System.out.println("Khong tim thay nhan vien!");
            return false;
        }
        DanhSachNhanVien.hienThi(new NhanVien[]{nhanVien});
        System.out.println("Nhan Enter de giu gia tri cu. Ma nhan vien duoc giu nguyen.");
        String string2 = DanhSachNhanVien.docDong(scanner, "Ho ten moi: ");
        if (string2 == null) {
            return false;
        }
        String string3 = DanhSachNhanVien.docDong(scanner, "Ca lam viec moi: ");
        if (string3 == null) {
            return false;
        }
        String string4 = DanhSachNhanVien.nhapDienThoai(scanner, "So dien thoai moi: ", true);
        if (string4 == null) {
            return false;
        }
        String string5 = DanhSachNhanVien.docDong(scanner, "Dia chi moi: ");
        if (string5 == null) {
            return false;
        }
        this.sua(string, string2.isEmpty() ? nhanVien.getHoTen() : string2, string3.isEmpty() ? nhanVien.getCaLamViec() : string3, string4.isEmpty() ? nhanVien.getSoDienThoai() : string4, string5.isEmpty() ? nhanVien.getDiaChi() : string5);
        System.out.println("Da cap nhat nhan vien.");
        return true;
    }

    private void timTuBanPhim(Scanner scanner) {
        String string = DanhSachNhanVien.docDong(scanner, "Tim 1=chinh xac, 2=chua tu khoa: ");
        if (string == null) {
            return;
        }
        if (!string.equals("1") && !string.equals("2")) {
            System.out.println("Lua chon khong hop le!");
            return;
        }
        System.out.println("Bo trong khoa khong can loc; cac khoa duoc ket hop bang AND.");
        String[] stringArray = new String[]{"Ma nhan vien: ", "Ho ten: ", "Ca lam viec: ", "So dien thoai: ", "Dia chi: "};
        String[] stringArray2 = new String[5];
        for (int i = 0; i < stringArray2.length; ++i) {
            stringArray2[i] = DanhSachNhanVien.docDong(scanner, stringArray[i]);
            if (stringArray2[i] != null) continue;
            return;
        }
        DanhSachNhanVien.hienThi(this.timKiem(stringArray2[0], stringArray2[1], stringArray2[2], stringArray2[3], stringArray2[4], string.equals("1")));
    }

    private static void hienThi(NhanVien[] nhanVienArray) {
        System.out.println("===== DANH SACH NHAN VIEN =====");
        if (nhanVienArray.length == 0) {
            System.out.println("Danh sach nhan vien dang trong.");
        }
        for (NhanVien nhanVien : nhanVienArray) {
            System.out.println(VanBanConsole.khongDau(nhanVien.thongTinNhanVien() + "\nCa lam viec: " + nhanVien.getCaLamViec()) + "\n------------------------------");
        }
        System.out.println("Tong so nhan vien: " + nhanVienArray.length);
    }

    private void luu(String string) {
        try {
            this.ghiFile(string);
            System.out.println("Da luu file nhan vien.");
        }
        catch (IOException iOException) {
            System.out.println("CHUA LUU DUOC " + string + ": " + iOException.getMessage());
            System.out.println("Du lieu moi chi dang trong bo nho. Chon 10 de thu luu lai.");
        }
    }

    private static String docDong(Scanner scanner, String string) {
        System.out.print(string);
        return scanner.hasNextLine() ? scanner.nextLine().trim() : null;
    }

    private static String batBuoc(Scanner scanner, String string) {
        String string2;
        while ((string2 = DanhSachNhanVien.docDong(scanner, string)) != null && string2.isEmpty()) {
            System.out.println("Thong tin khong duoc rong!");
        }
        return string2;
    }

    private static Integer nhapSoLuong(Scanner scanner, String string) {
        String string2;
        while ((string2 = DanhSachNhanVien.docDong(scanner, string)) != null) {
            try {
                int n = Integer.parseInt(string2);
                if (n >= 0) {
                    return n;
                }
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
            System.out.println("Nhap so nguyen >= 0.");
        }
        return null;
    }

    private static String nhapDienThoai(Scanner scanner, String string, boolean bl) {
        String string2;
        while (!((string2 = DanhSachNhanVien.docDong(scanner, string)) == null || bl && string2.isEmpty())) {
            if (DanhSachKhachHang.soDienThoaiHopLe(string2)) {
                return string2;
            }
            System.out.println("So dien thoai phai co 9-15 chu so, co the bat dau bang +.");
        }
        return string2;
    }
}

