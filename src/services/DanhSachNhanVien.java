/*
 * Decompiled with CFR 0.152.
 */
package services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
    // Compatibility data belongs to this list, never to NhanVien or a global registry.
    private Map<String, String> diaChiCuTheoMa = new LinkedHashMap<>();

    public DanhSachNhanVien() {
    }

    /** Model-only input. To copy legacy data, use the copy constructor or pass its metadata. */
    public DanhSachNhanVien(NhanVien[] nhanVienArray) {
        this();
        this.nhapDanhSach(nhanVienArray);
    }

    /** Pass metadata explicitly when importing an old array-based snapshot. */
    public DanhSachNhanVien(NhanVien[] nhanVienArray, Map<String, String> diaChiCu) {
        this(nhanVienArray);
        if (diaChiCu == null) {
            throw new IllegalArgumentException("Metadata dia chi khong duoc null.");
        }
        for (Map.Entry<String, String> entry : diaChiCu.entrySet()) {
            if (chiSoTheoMa(entry.getKey()) < 0) {
                throw new IllegalArgumentException("Metadata khong co nhan vien: " + entry.getKey());
            }
            String ma = khoaMa(entry.getKey());
            if (diaChiCuTheoMa.putIfAbsent(ma, kiemTraDiaChiCu(entry.getValue())) != null) {
                throw new IllegalArgumentException("Trung ma metadata: " + ma);
            }
        }
    }

    /** Copies both employees and compatibility metadata. */
    public DanhSachNhanVien(DanhSachNhanVien nguon) {
        this(nguon.getDanhSach(), nguon.getDiaChiCuTheoMa());
    }

    public String getDiaChiCu(String ma) {
        return ma == null ? null : diaChiCuTheoMa.get(khoaMa(ma));
    }

    public Map<String, String> getDiaChiCuTheoMa() {
        return Map.copyOf(diaChiCuTheoMa);
    }

    private static String khoaMa(String ma) {
        return ma.trim().toUpperCase(Locale.ROOT);
    }

    private static String kiemTraDiaChiCu(String diaChi) {
        if (diaChi == null || diaChi.isBlank()) {
            throw new IllegalArgumentException("Dia chi chuyen tiep khong duoc rong.");
        }
        // Preserve the exact legacy value, including escaped whitespace.
        return diaChi;
    }

    private void thayBang(DanhSachNhanVien moi) {
        this.danhSach = moi.danhSach;
        this.soLuong = moi.soLuong;
        this.diaChiCuTheoMa = moi.diaChiCuTheoMa;
    }

    public int getSoLuong() {
        return this.soLuong;
    }

    /** Defensive model copies; use getDiaChiCuTheoMa() to transfer legacy addresses too. */
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
            String diaChi = getDiaChiCu(nhanVien.getMaNhanVien());
            if (diaChi != null) {
                danhSachNhanVien.diaChiCuTheoMa.put(khoaMa(nhanVien.getMaNhanVien()), diaChi);
            }
        }
        thayBang(danhSachNhanVien);
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

    /** Legacy import adapter; the address is stored separately from the model. */
    public void them(NhanVien nhanVien, String diaChiCu) {
        String diaChi = kiemTraDiaChiCu(diaChiCu);
        them(nhanVien);
        diaChiCuTheoMa.put(khoaMa(nhanVien.getMaNhanVien()), diaChi);
    }

    public void themNhieu(NhanVien[] nhanVienArray) {
        if (nhanVienArray == null) {
            throw new IllegalArgumentException("Danh sach khong duoc null.");
        }
        DanhSachNhanVien danhSachNhanVien = new DanhSachNhanVien(this);
        for (NhanVien nhanVien : nhanVienArray) {
            danhSachNhanVien.them(nhanVien);
        }
        thayBang(danhSachNhanVien);
    }

    public boolean sua(String ma, String ten, String ca, String sdt) {
        int n = this.chiSoTheoMa(ma);
        if (n < 0) {
            return false;
        }
        this.danhSach[n] = new NhanVien(this.danhSach[n].getMaNhanVien(), ten, sdt, ca);
        return true;
    }

    /** Retains the old list API order: code, name, shift, phone, legacy address. */
    public boolean sua(String ma, String ten, String ca, String sdt, String diaChiCu) {
        if (chiSoTheoMa(ma) < 0) {
            return false;
        }
        String diaChi = kiemTraDiaChiCu(diaChiCu);
        sua(ma, ten, ca, sdt);
        diaChiCuTheoMa.put(khoaMa(ma), diaChi);
        return true;
    }

    public boolean xoa(String string) {
        int n = this.chiSoTheoMa(string);
        if (n < 0) {
            return false;
        }
        diaChiCuTheoMa.remove(khoaMa(this.danhSach[n].getMaNhanVien()));
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
            if (!DanhSachNhanVien.khop(nhanVien.getMaNhanVien(), string, bl) || !DanhSachNhanVien.khop(nhanVien.getHoTen(), string2, bl) || !DanhSachNhanVien.khop(nhanVien.getCaLamViec(), string3, bl) || !DanhSachNhanVien.khop(nhanVien.getSoDienThoai(), string4, bl) || !DanhSachNhanVien.khop(getDiaChiCu(nhanVien.getMaNhanVien()), string5, bl)) continue;
            nhanVienArray[n++] = DanhSachNhanVien.saoChep(nhanVien);
        }
        return Arrays.copyOf(nhanVienArray, n);
    }

    public String thongKe() {
        return "Tong so nhan vien: " + this.soLuong + "\nTheo ca lam viec:\n" + this.thongKeTheoNhom(true) + "Theo dia chi (du lieu chuyen tiep):\n" + this.thongKeTheoNhom(false);
    }

    private String thongKeTheoNhom(boolean bl) {
        String[] stringArray = new String[this.soLuong];
        int[] nArray = new int[this.soLuong];
        int n = 0;
        for (int i = 0; i < this.soLuong; ++i) {
            int n2;
            String string = bl ? this.danhSach[i].getCaLamViec() : getDiaChiCu(this.danhSach[i].getMaNhanVien());
            if (string == null) {
                return "  Metadata dia chi chua day du; khong the thong ke chinh xac.\n";
            }
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
        // A model-only snapshot must never erase addresses from an existing file.
        if (Files.exists(Path.of(string))) {
            DanhSachNhanVien trenDia = new DanhSachNhanVien();
            trenDia.docFile(string);
            for (NhanVien nhanVien : getDanhSach()) {
                String ma = nhanVien.getMaNhanVien();
                if (trenDia.getDiaChiCu(ma) != null && getDiaChiCu(ma) == null) {
                    throw new IOException("Khong the ghi: thieu metadata dia chi cu cua nhan vien " + ma);
                }
            }
        }
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add(TIEU_DE);
        for (int i = 0; i < this.soLuong; ++i) {
            NhanVien nhanVien = this.danhSach[i];
            String diaChi = getDiaChiCu(nhanVien.getMaNhanVien());
            // Empty only means this record has no legacy address, never a fabricated address.
            arrayList.add(FileText.dong(nhanVien.getMaNhanVien(), nhanVien.getHoTen(), nhanVien.getCaLamViec(), nhanVien.getSoDienThoai(), diaChi == null ? "" : diaChi));
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
                NhanVien nhanVien = new NhanVien(stringArray[0], stringArray[1], stringArray[3], stringArray[2]);
                if (stringArray[4].isEmpty()) {
                    danhSachNhanVien.them(nhanVien);
                } else {
                    danhSachNhanVien.them(nhanVien, stringArray[4]);
                }
                continue;
            }
            catch (IllegalArgumentException illegalArgumentException) {
                throw new IOException("Dong " + (i + 2) + ": " + illegalArgumentException.getMessage(), illegalArgumentException);
            }
        }
        thayBang(danhSachNhanVien);
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
        if (nhanVien == null) {
            throw new IllegalArgumentException("Nhan vien khong duoc null.");
        }
        return saoChep(nhanVien);
    }

    private static boolean rong(String string) {
        return string == null || string.trim().isEmpty();
    }

    private static NhanVien saoChep(NhanVien nhanVien) {
        return new NhanVien(nhanVien.getMaNhanVien(), nhanVien.getHoTen(), nhanVien.getSoDienThoai(), nhanVien.getCaLamViec());
    }

    private static boolean khop(String string, String string2, boolean bl) {
        if (DanhSachNhanVien.rong(string2)) {
            return true;
        }
        if (string == null) {
            return false;
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
                        this.hienThi(this.getDanhSach());
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
        DanhSachNhanVien danhSachNhanVien = bl ? new DanhSachNhanVien() : new DanhSachNhanVien(this);
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
            String string5 = DanhSachNhanVien.docDong(scanner, "Dia chi chuyen tiep (Enter neu khong co): ");
            if (string5 == null) {
                return false;
            }
            NhanVien nhanVien = new NhanVien(string, string2, string4, string3);
            String diaChiCu = string5.isEmpty() ? getDiaChiCu(string) : string5;
            if (diaChiCu == null) {
                danhSachNhanVien.them(nhanVien);
            } else {
                danhSachNhanVien.them(nhanVien, diaChiCu);
            }
        }
        thayBang(danhSachNhanVien);
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
        this.hienThi(new NhanVien[]{nhanVien});
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
        String string5 = DanhSachNhanVien.docDong(scanner, "Dia chi chuyen tiep moi (Enter de giu nguyen): ");
        if (string5 == null) {
            return false;
        }
        String ten = string2.isEmpty() ? nhanVien.getHoTen() : string2;
        String ca = string3.isEmpty() ? nhanVien.getCaLamViec() : string3;
        String sdt = string4.isEmpty() ? nhanVien.getSoDienThoai() : string4;
        if (string5.isEmpty()) {
            this.sua(string, ten, ca, sdt);
        } else {
            this.sua(string, ten, ca, sdt, string5);
        }
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
        String[] stringArray = new String[]{"Ma nhan vien: ", "Ho ten: ", "Ca lam viec: ", "So dien thoai: ", "Dia chi chuyen tiep: "};
        String[] stringArray2 = new String[5];
        for (int i = 0; i < stringArray2.length; ++i) {
            stringArray2[i] = DanhSachNhanVien.docDong(scanner, stringArray[i]);
            if (stringArray2[i] != null) continue;
            return;
        }
        this.hienThi(this.timKiem(stringArray2[0], stringArray2[1], stringArray2[2], stringArray2[3], stringArray2[4], string.equals("1")));
    }

    private void hienThi(NhanVien[] nhanVienArray) {
        System.out.println("===== DANH SACH NHAN VIEN =====");
        if (nhanVienArray.length == 0) {
            System.out.println("Danh sach nhan vien dang trong.");
        }
        for (NhanVien nhanVien : nhanVienArray) {
            System.out.println(VanBanConsole.khongDau(nhanVien.hienThiThongTin()));
            String diaChi = getDiaChiCu(nhanVien.getMaNhanVien());
            if (diaChi != null) {
                System.out.println(VanBanConsole.khongDau("Dia chi (du lieu chuyen tiep): " + diaChi));
            }
            System.out.println("------------------------------");
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

