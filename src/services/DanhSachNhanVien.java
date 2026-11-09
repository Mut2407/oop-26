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
import models.NhanVien;
import services.DanhSachKhachHang;
import services.FileText;
import services.VanBanConsole;

public final class DanhSachNhanVien {
    private static final int SUC_CHUA_BAN_DAU = 10;
    static final String TIEU_DE = "maNhanVien\thoTen\tcaLamViec\tsoDienThoai\tdiaChi";
    static final String TIEU_DE_MOI = "maNhanVien\thoTen\tsoDienThoai\tcaLamViec\tdiaChiCu";
    static final String TIEU_DE_UML = "maNhanVien\thoTen\tsoDienThoai\tcaLamViec";
    private NhanVien[] danhSach = new NhanVien[SUC_CHUA_BAN_DAU];
    private int soLuong;
    // Compatibility data belongs to this list, never to NhanVien or a global registry.
    private Map<String, String> diaChiCuTheoMa = new LinkedHashMap<>();

    public DanhSachNhanVien() {
    }

    public DanhSachNhanVien(NhanVien[] nhanVienArray) {
        this();
        this.nhapDanhSach(nhanVienArray);
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

    public NhanVien[] getDanhSach() {
        NhanVien[] nhanVienArray = new NhanVien[this.soLuong];
        for (int i = 0; i < this.soLuong; ++i) {
            nhanVienArray[i] = DanhSachNhanVien.saoChep(this.danhSach[i]);
        }
        return nhanVienArray;
    }

    private void nhapDanhSach(NhanVien[] nhanVienArray) {
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

    public boolean sua(String ma, String ten, String ca, String sdt) {
        int n = this.chiSoTheoMa(ma);
        if (n < 0) {
            return false;
        }
        this.danhSach[n] = new NhanVien(this.danhSach[n].getMaNhanVien(), ten, sdt, ca);
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

    public NhanVien CheckMaTrung(String string) {
        int n = this.chiSoTheoMa(string);
        return n < 0 ? null : DanhSachNhanVien.saoChep(this.danhSach[n]);
    }

    public NhanVien[] TimKiemTheoMaHoacTen(String tuKhoa) {
        if (tuKhoa == null || tuKhoa.isBlank()) {
            throw new IllegalArgumentException("Tu khoa khong duoc rong.");
        }
        List<NhanVien> result = new ArrayList<>();
        for (int i = 0; i < soLuong; i++) {
            NhanVien nv = danhSach[i];
            if (khop(nv.getMaNhanVien(), tuKhoa, false) || khop(nv.getHoTen(), tuKhoa, false)) {
                result.add(saoChep(nv));
            }
        }
        return result.toArray(new NhanVien[0]);
    }

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

    public void docFile(String string) throws IOException {
        DanhSachNhanVien danhSachNhanVien = new DanhSachNhanVien();
        FileText.Bang bang = FileText.docBang(string, TIEU_DE, TIEU_DE_MOI, TIEU_DE_UML);
        List<String[]> list = bang.dong();
        boolean cu = bang.tieuDe().equals(TIEU_DE);
        int soCot = bang.tieuDe().equals(TIEU_DE_UML) ? 4 : 5;
        for (int i = 0; i < list.size(); ++i) {
            String[] stringArray = list.get(i);
            try {
                if (stringArray.length != soCot) {
                    throw new IllegalArgumentException("Phai co " + soCot + " cot.");
                }
                NhanVien nhanVien = new NhanVien(stringArray[0], stringArray[1],
                        stringArray[cu ? 3 : 2], stringArray[cu ? 2 : 3]);
                if (soCot == 4 || stringArray[4].isEmpty()) {
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
        String string3 = VanBanConsole.khongDau(string).toLowerCase(Locale.ROOT);
        String string4 = VanBanConsole.khongDau(string2.trim()).toLowerCase(Locale.ROOT);
        return bl ? string3.equals(string4) : string3.contains(string4);
    }
}
