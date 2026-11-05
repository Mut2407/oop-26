/*
 * Decompiled with CFR 0.152.
 */
package services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import models.KhachHang;
import services.FileText;
import services.LuuTruFile;

public final class DanhSachKhachHang
implements LuuTruFile {
    private static final int SUC_CHUA_BAN_DAU = 10;
    private static final String TIEU_DE_CU = "KHACH_HANG_V1";
    private static final String TIEU_DE_FILE = "maKhachHang\thoTen\tsoDienThoai\tdiaChi";
    private KhachHang[] danhSach = new KhachHang[10];
    private int soLuong = 0;

    public DanhSachKhachHang() {
    }

    public DanhSachKhachHang(KhachHang[] khachHangArray) {
        this();
        this.nhapDanhSach(khachHangArray);
    }

    public int getSoLuong() {
        return this.soLuong;
    }

    public KhachHang[] getDanhSach() {
        KhachHang[] khachHangArray = new KhachHang[this.soLuong];
        for (int i = 0; i < this.soLuong; ++i) {
            khachHangArray[i] = DanhSachKhachHang.saoChep(this.danhSach[i]);
        }
        return khachHangArray;
    }

    public void nhapDanhSach(KhachHang[] khachHangArray) {
        if (khachHangArray == null) {
            throw new IllegalArgumentException("Danh sach khong duoc null.");
        }
        DanhSachKhachHang danhSachKhachHang = new DanhSachKhachHang();
        for (KhachHang khachHang : khachHangArray) {
            danhSachKhachHang.them(khachHang);
        }
        this.danhSach = danhSachKhachHang.danhSach;
        this.soLuong = danhSachKhachHang.soLuong;
    }

    public void them(KhachHang khachHang) {
        KhachHang khachHang2 = DanhSachKhachHang.kiemTraVaSaoChep(khachHang);
        if (this.chiSoTheoMa(khachHang2.getMaKhachHang()) >= 0) {
            throw new IllegalArgumentException("Ma khach hang da ton tai!");
        }
        if (this.soLuong == this.danhSach.length) {
            this.danhSach = Arrays.copyOf(this.danhSach, Math.max(1, this.danhSach.length * 2));
        }
        this.danhSach[this.soLuong++] = khachHang2;
    }

    public void themNhieu(KhachHang[] khachHangArray) {
        if (khachHangArray == null) {
            throw new IllegalArgumentException("Danh sach khong duoc null.");
        }
        DanhSachKhachHang danhSachKhachHang = new DanhSachKhachHang(this.getDanhSach());
        for (KhachHang khachHang : khachHangArray) {
            danhSachKhachHang.them(khachHang);
        }
        this.danhSach = danhSachKhachHang.danhSach;
        this.soLuong = danhSachKhachHang.soLuong;
    }

    public boolean sua(String string, String string2, String string3, String string4) {
        int n = this.chiSoTheoMa(string);
        if (n < 0) {
            return false;
        }
        this.danhSach[n] = DanhSachKhachHang.kiemTraVaSaoChep(new KhachHang(this.danhSach[n].getMaKhachHang(), string2, string3, string4));
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

    public KhachHang timTheoMa(String string) {
        int n = this.chiSoTheoMa(string);
        return n < 0 ? null : DanhSachKhachHang.saoChep(this.danhSach[n]);
    }

    public KhachHang[] timKiem(String string, String string2, String string3, String string4, boolean bl) {
        KhachHang[] khachHangArray = new KhachHang[this.soLuong];
        int n = 0;
        for (int i = 0; i < this.soLuong; ++i) {
            KhachHang khachHang = this.danhSach[i];
            if (!DanhSachKhachHang.khop(khachHang.getMaKhachHang(), string, bl) || !DanhSachKhachHang.khop(khachHang.getHoTen(), string2, bl) || !DanhSachKhachHang.khop(khachHang.getSoDienThoai(), string3, bl) || !DanhSachKhachHang.khop(khachHang.getDiaChi(), string4, bl)) continue;
            khachHangArray[n++] = DanhSachKhachHang.saoChep(khachHang);
        }
        return Arrays.copyOf(khachHangArray, n);
    }

    public String thongKe() {
        return "Tong so khach hang: " + this.soLuong + "\nTheo dia chi:\n" + this.thongKeTheoNhom(true) + "Theo dau so dien thoai (3 chu so dau, bo dau +):\n" + this.thongKeTheoNhom(false);
    }

    private String thongKeTheoNhom(boolean bl) {
        String[] stringArray = new String[this.soLuong];
        int[] nArray = new int[this.soLuong];
        int n = 0;
        for (int i = 0; i < this.soLuong; ++i) {
            int n2;
            String string = this.danhSach[i].getDiaChi();
            if (!bl) {
                string = this.danhSach[i].getSoDienThoai().replace("+", "").substring(0, 3);
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
            stringBuilder.append("  ").append(stringArray[i]).append(": ").append(nArray[i]).append(" khach hang\n");
        }
        return stringBuilder.toString();
    }

    @Override
    public void ghiFile(String string) throws IOException {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add(TIEU_DE_FILE);
        for (int i = 0; i < this.soLuong; ++i) {
            KhachHang khachHang = this.danhSach[i];
            arrayList.add(FileText.dong(khachHang.getMaKhachHang(), khachHang.getHoTen(), khachHang.getSoDienThoai(), khachHang.getDiaChi()));
        }
        FileText.ghi(string, arrayList);
    }

    @Override
    public void docFile(String string) throws IOException {
        DanhSachKhachHang danhSachKhachHang = new DanhSachKhachHang();
        List<String[]> list = FileText.doc(string, TIEU_DE_FILE, TIEU_DE_CU);
        for (int i = 0; i < list.size(); ++i) {
            String[] stringArray = list.get(i);
            try {
                if (stringArray.length != 4) {
                    throw new IllegalArgumentException("Phai co 4 cot.");
                }
                danhSachKhachHang.them(new KhachHang(stringArray[0], stringArray[1], stringArray[2], stringArray[3]));
                continue;
            }
            catch (IllegalArgumentException illegalArgumentException) {
                throw new IOException("Dong " + (i + 2) + ": " + illegalArgumentException.getMessage(), illegalArgumentException);
            }
        }
        this.danhSach = danhSachKhachHang.danhSach;
        this.soLuong = danhSachKhachHang.soLuong;
    }

    public static boolean maHopLe(String string) {
        return string != null && string.trim().matches("[A-Za-z0-9_-]+");
    }

    public static boolean soDienThoaiHopLe(String string) {
        return string != null && string.trim().matches("\\+?[0-9]{9,15}");
    }

    private int chiSoTheoMa(String string) {
        if (string != null) {
            for (int i = 0; i < this.soLuong; ++i) {
                if (!this.danhSach[i].getMaKhachHang().equalsIgnoreCase(string.trim())) continue;
                return i;
            }
        }
        return -1;
    }

    private static KhachHang kiemTraVaSaoChep(KhachHang khachHang) {
        if (khachHang == null || !DanhSachKhachHang.maHopLe(khachHang.getMaKhachHang())) {
            throw new IllegalArgumentException("Ma chi gom chu, so, dau _ hoac -.");
        }
        if (khachHang.getHoTen() == null || khachHang.getHoTen().trim().isEmpty() || khachHang.getDiaChi() == null || khachHang.getDiaChi().trim().isEmpty()) {
            throw new IllegalArgumentException("Ho ten va dia chi khong duoc de trong.");
        }
        if (!DanhSachKhachHang.soDienThoaiHopLe(khachHang.getSoDienThoai())) {
            throw new IllegalArgumentException("So dien thoai phai co 9-15 chu so, co the bat dau bang +.");
        }
        return new KhachHang(khachHang.getMaKhachHang().trim(), khachHang.getHoTen().trim(), khachHang.getSoDienThoai().trim(), khachHang.getDiaChi().trim());
    }

    private static KhachHang saoChep(KhachHang khachHang) {
        return new KhachHang(khachHang.getMaKhachHang(), khachHang.getHoTen(), khachHang.getSoDienThoai(), khachHang.getDiaChi());
    }

    private static boolean khop(String string, String string2, boolean bl) {
        if (string2 == null || string2.trim().isEmpty()) {
            return true;
        }
        String string3 = string.toLowerCase(Locale.ROOT);
        String string4 = string2.trim().toLowerCase(Locale.ROOT);
        return bl ? string3.equals(string4) : string3.contains(string4);
    }
}

