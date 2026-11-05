/*
 * Decompiled with CFR 0.152.
 */
package services;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import models.DanhMuc;
import models.DuLieu;
import models.SanPham;
import services.DanhSachNhaCungCap;
import services.DanhSachSanPham;
import services.FileText;

public class LuuTruFileText {
    private static final char KY_TU_PHAN_CACH = '\t';
    private static final String TIEU_DE_DANH_MUC = "maDanhMuc\ttenDanhMuc\tmoTa";
    private final String thuMucDuLieu;

    public LuuTruFileText(String string) {
        if (string == null || string.isBlank()) {
            throw new IllegalArgumentException("Thu muc du lieu khong hop le.");
        }
        this.thuMucDuLieu = Path.of(string, new String[0]).toAbsolutePath().normalize().toString();
    }

    public void ghiDanhMuc(String string, List<DanhMuc> list) throws IOException {
        if (list == null) {
            throw new IllegalArgumentException("Danh sach danh muc khong hop le.");
        }
        ArrayList<String> arrayList = new ArrayList<String>();
        HashSet<String> hashSet = new HashSet<String>();
        arrayList.add(TIEU_DE_DANH_MUC);
        for (DanhMuc danhMuc : list) {
            if (danhMuc == null || !hashSet.add(danhMuc.getMaDanhMuc().toUpperCase(Locale.ROOT))) {
                throw new IllegalArgumentException("Ma danh muc trung hoac khong hop le.");
            }
            arrayList.add(FileText.dong(danhMuc.getMaDanhMuc(), danhMuc.getTenDanhMuc(), danhMuc.getMoTa()));
        }
        FileText.ghi(this.duongDan(string).toString(), arrayList);
    }

    public List<DanhMuc> docDanhMuc(String string) throws IOException {
        List<String[]> list = FileText.doc(this.duongDan(string).toString(), TIEU_DE_DANH_MUC);
        ArrayList<DanhMuc> arrayList = new ArrayList<DanhMuc>();
        HashSet<String> hashSet = new HashSet<String>();
        for (int i = 0; i < list.size(); ++i) {
            String[] stringArray = list.get(i);
            try {
                if (stringArray.length != 3) {
                    throw new IllegalArgumentException("Phai co 3 cot.");
                }
                DanhMuc danhMuc = new DanhMuc(stringArray[0], stringArray[1], stringArray[2]);
                if (!hashSet.add(danhMuc.getMaDanhMuc().toUpperCase(Locale.ROOT))) {
                    throw new IllegalArgumentException("Ma danh muc trung: " + danhMuc.getMaDanhMuc());
                }
                arrayList.add(danhMuc);
                continue;
            }
            catch (IllegalArgumentException illegalArgumentException) {
                throw new IOException("Dong " + (i + 2) + ": " + illegalArgumentException.getMessage(), illegalArgumentException);
            }
        }
        return List.copyOf(arrayList);
    }

    public void ghiSanPham(String string, DanhSachSanPham danhSachSanPham) throws IOException {
        if (danhSachSanPham == null) {
            throw new IllegalArgumentException("Danh sach san pham khong hop le.");
        }
        Path path = this.duongDan(string);
        if (Files.exists(path, new LinkOption[0])) {
            new DanhSachSanPham(0).docFile(path.toString());
            if (LuuTruFileText.laFileCu(path, "SAN_PHAM_V1", "kieu\t")) {
                LuuTruFileText.saoLuuFileCu(path);
            }
        }
        danhSachSanPham.ghiFile(path.toString());
    }

    public DanhSachSanPham docSanPham(String string, List<DanhMuc> list) throws IOException {
        if (list == null) {
            throw new IllegalArgumentException("Danh muc khong hop le.");
        }
        for (DanhMuc object : list) {
            if (object != null && DuLieu.timDanhMuc(object.getMaDanhMuc()) == object) continue;
            throw new IllegalArgumentException("Danh muc phai tu nguon chuan.");
        }
        DanhSachSanPham danhSachSanPham = new DanhSachSanPham(0);
        danhSachSanPham.docFile(this.duongDan(string).toString());
        for (SanPham sanPham : danhSachSanPham.getDanhSach()) {
            if (list.contains(sanPham.getDanhMuc())) continue;
            throw new IOException("Danh muc chua co trong danh sach: " + sanPham.getDanhMuc().getMaDanhMuc());
        }
        return danhSachSanPham;
    }

    public void ghiNhaCungCap(String string, DanhSachNhaCungCap danhSachNhaCungCap) throws IOException {
        if (danhSachNhaCungCap == null) {
            throw new IllegalArgumentException("Danh sach nha cung cap khong hop le.");
        }
        Path path = this.duongDan(string);
        if (Files.exists(path, new LinkOption[0])) {
            new DanhSachNhaCungCap().docFile(path.toString());
            if (LuuTruFileText.laFileCu(path, "NHA_CUNG_CAP_V1", null)) {
                LuuTruFileText.saoLuuFileCu(path);
            }
        }
        danhSachNhaCungCap.ghiFile(path.toString());
    }

    public DanhSachNhaCungCap docNhaCungCap(String string) throws IOException {
        DanhSachNhaCungCap danhSachNhaCungCap = new DanhSachNhaCungCap();
        danhSachNhaCungCap.docFile(this.duongDan(string).toString());
        return danhSachNhaCungCap;
    }

    private Path duongDan(String string) {
        if (string == null || string.isBlank()) {
            throw new IllegalArgumentException("Duong dan khong hop le.");
        }
        Path path = Path.of(string, new String[0]);
        return (path.isAbsolute() ? path : Path.of(this.thuMucDuLieu, new String[0]).resolve(path)).normalize();
    }

    private static boolean laFileCu(Path path, String string, String string2) throws IOException {
        if (!Files.exists(path, new LinkOption[0])) {
            return false;
        }
        try (BufferedReader bufferedReader = Files.newBufferedReader(path, StandardCharsets.UTF_8);){
            String string3 = bufferedReader.readLine();
            if (string3 == null) {
                throw new IOException("File rong: " + String.valueOf(path));
            }
            boolean bl = string3.equals(string) || string2 != null && string3.startsWith(string2) && !string3.contains("mauSacChung");
            return bl;
        }
    }

    private static void saoLuuFileCu(Path path) throws IOException {
        Path path2 = path.resolveSibling(String.valueOf(path.getFileName()) + ".bak");
        if (!Files.exists(path2, new LinkOption[0])) {
            Files.copy(path, path2, new CopyOption[0]);
        }
        if (Files.mismatch(path, path2) != -1L) {
            throw new IOException("Ban sao luu khac file cu: " + String.valueOf(path2));
        }
    }
}

