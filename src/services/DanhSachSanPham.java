/*
 * Decompiled with CFR 0.152.
 */
package services;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import models.DanhMuc;
import models.DuLieu;
import models.DungCuTheThao;
import models.GiayTheThao;
import models.QuanAoTheThao;
import models.SanPham;
import services.FileText;
import services.IQuanLyDanhSach;

public class DanhSachSanPham
implements IQuanLyDanhSach<SanPham> {
    private static final String TIEU_DE_CU = "SAN_PHAM_V1";
    private static final String TIEU_DE = "kieu\tmaSanPham\ttenSanPham\tgiaBan\tthuongHieu\tsoLuongTon\tmaLoai\tkichCoGiay\tmauSac\tloaiDeGiay\tmonTheThao\tvatLieu\tkhoiLuongKg\tkichCoQuanAo";
    private static final String TIEU_DE_MOI = "kieu\tmaSanPham\ttenSanPham\tgiaBan\tthuongHieu\tsoLuongTon\tmaLoai\tkichCoGiay\tmauSac\tloaiDeGiay\tmonTheThao\tvatLieu\tkhoiLuongKg\tkichCoQuanAo\tmauSacChung\tchatLieuChung";
    private SanPham[] ds;
    private int soLuong;
    private final Map<String, String> monTheThaoCu = new HashMap<String, String>();

    public DanhSachSanPham(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Suc chua khong duoc am.");
        }
        this.ds = new SanPham[n];
    }

    public DanhSachSanPham(SanPham[] sanPhamArray) {
        this(sanPhamArray == null ? 0 : sanPhamArray.length);
        this.nhapBanDau(sanPhamArray);
    }

    public int getSoLuong() {
        return this.soLuong;
    }

    private void nhapBanDau(SanPham[] sanPhamArray) {
        if (sanPhamArray == null) {
            throw new IllegalArgumentException("Danh sach ban dau khong hop le.");
        }
        DanhSachSanPham danhSachSanPham = new DanhSachSanPham(sanPhamArray.length);
        for (SanPham sanPham : sanPhamArray) {
            danhSachSanPham.them(sanPham);
        }
        this.ds = danhSachSanPham.ds;
        this.soLuong = danhSachSanPham.soLuong;
        this.monTheThaoCu.keySet().removeIf(string -> danhSachSanPham.chiSo((String)string) < 0);
    }

    public List<SanPham> getDanhSach() {
        ArrayList<SanPham> arrayList = new ArrayList<SanPham>();
        for (int i = 0; i < this.soLuong; ++i) {
            arrayList.add(DanhSachSanPham.saoChep(this.ds[i]));
        }
        return List.copyOf(arrayList);
    }

    @Override
    public boolean them(SanPham sanPham) {
        DanhSachSanPham.kiemTra(sanPham);
        if (this.chiSo(sanPham.getMaSanPham()) >= 0) {
            throw new IllegalArgumentException("Ma san pham da ton tai.");
        }
        if (this.soLuong == this.ds.length) {
            this.moRongMang();
        }
        this.ds[this.soLuong++] = DanhSachSanPham.saoChep(sanPham);
        return true;
    }

    public boolean suaTheoMa(String string, SanPham sanPham) {
        int n = this.chiSo(string);
        if (n < 0) {
            return false;
        }
        DanhSachSanPham.kiemTra(sanPham);
        if (!this.ds[n].getMaSanPham().equalsIgnoreCase(sanPham.getMaSanPham())) {
            throw new IllegalArgumentException("Khong duoc doi ma san pham.");
        }
        this.ds[n] = DanhSachSanPham.saoChep(sanPham);
        return true;
    }

    @Override
    public boolean xoaTheoMa(String string) {
        int n = this.chiSo(string);
        if (n < 0) {
            return false;
        }
        this.monTheThaoCu.remove(this.ds[n].getMaSanPham().toUpperCase(Locale.ROOT));
        System.arraycopy(this.ds, n + 1, this.ds, n, this.soLuong - n - 1);
        this.ds[--this.soLuong] = null;
        return true;
    }

    @Override
    public SanPham CheckMaTrung(String string) {
        int n = this.chiSo(string);
        return n < 0 ? null : DanhSachSanPham.saoChep(this.ds[n]);
    }

    @Override
    public List<SanPham> TimKiemTheoMaHoacTen(String string) {
        if (string == null || string.isBlank()) {
            throw new IllegalArgumentException("Tu khoa khong duoc rong.");
        }
        String string2 = DanhSachSanPham.chuanHoaTimKiem(string.trim());
        ArrayList<SanPham> arrayList = new ArrayList<SanPham>();
        for (int i = 0; i < this.soLuong; ++i) {
            if (!DanhSachSanPham.chuanHoaTimKiem(this.ds[i].getMaSanPham()).contains(string2) && !DanhSachSanPham.chuanHoaTimKiem(this.ds[i].getTenSanPham()).contains(string2)) continue;
            arrayList.add(DanhSachSanPham.saoChep(this.ds[i]));
        }
        return List.copyOf(arrayList);
    }

    public String thongKeTonKho() {
        StringBuilder ketQua = new StringBuilder();
        long tongSoLuong = 0;
        for (int i = 0; i < soLuong; i++) {
            SanPham sp = ds[i];
            ketQua.append(sp.getMaSanPham()).append(" - ").append(sp.getTenSanPham())
                    .append(": ").append(sp.getSoLuongTon()).append("\n");
            tongSoLuong += sp.getSoLuongTon();
        }
        return ketQua + "Tong so mat hang: " + soLuong + "\nTong so luong ton: " + tongSoLuong;
    }

    public void ghiFile(String string) throws IOException {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add(TIEU_DE_MOI);
        for (int i = 0; i < this.soLuong; ++i) {
            SanPham sanPham;
            SanPham sanPham2 = this.ds[i];
            DanhSachSanPham.kiemTra(sanPham2);
            String string2 = sanPham2.getMaSanPham();
            String string3 = this.monTheThaoCu.getOrDefault(string2.toUpperCase(Locale.ROOT), "");
            String[] stringArray = new String[]{string2, sanPham2.getTenSanPham(), sanPham2.getGiaBan().toPlainString(), sanPham2.getThuongHieu(), Integer.toString(sanPham2.getSoLuongTon()), sanPham2.getDanhMuc().getMaDanhMuc()};
            if (sanPham2 instanceof GiayTheThao) {
                sanPham = (GiayTheThao)sanPham2;
                arrayList.add(FileText.dong("GIAY", stringArray[0], stringArray[1], stringArray[2], stringArray[3], stringArray[4], stringArray[5], ((GiayTheThao)sanPham).getKichCoGiay(), DanhSachSanPham.text(sanPham2.getMauSac()), ((GiayTheThao)sanPham).getLoaiDeGiay(), string3, "", "", "", DanhSachSanPham.text(sanPham2.getMauSac()), DanhSachSanPham.text(sanPham2.getChatLieu())));
                continue;
            }
            if (sanPham2 instanceof DungCuTheThao) {
                sanPham = (DungCuTheThao)sanPham2;
                arrayList.add(FileText.dong("DUNG_CU", stringArray[0], stringArray[1], stringArray[2], stringArray[3], stringArray[4], stringArray[5], "", "", "", string3, DanhSachSanPham.text(sanPham2.getChatLieu()), Double.toString(((DungCuTheThao)sanPham).getKhoiLuongKg()), "", DanhSachSanPham.text(sanPham2.getMauSac()), DanhSachSanPham.text(sanPham2.getChatLieu())));
                continue;
            }
            if (!(sanPham2 instanceof QuanAoTheThao)) continue;
            sanPham = (QuanAoTheThao)sanPham2;
            arrayList.add(FileText.dong("QUAN_AO", stringArray[0], stringArray[1], stringArray[2], stringArray[3], stringArray[4], stringArray[5], "", "", "", string3, "", "", ((QuanAoTheThao)sanPham).getKichCoQuanAo(), DanhSachSanPham.text(sanPham2.getMauSac()), DanhSachSanPham.text(sanPham2.getChatLieu())));
        }
        FileText.ghi(string, arrayList);
    }

    public void docFile(String string) throws IOException {
        DanhSachSanPham danhSachSanPham = new DanhSachSanPham(0);
        List<String[]> list = FileText.doc(string, TIEU_DE_MOI, TIEU_DE, TIEU_DE_CU);
        for (int i = 0; i < list.size(); ++i) {
            String[] stringArray = list.get(i);
            try {
                String string2;
                if (stringArray.length < 8) {
                    throw new IllegalArgumentException("Thieu cot.");
                }
                DanhMuc danhMuc = DuLieu.timDanhMuc(stringArray[6]);
                if (danhMuc == null) {
                    throw new IllegalArgumentException("Ma danh muc chua anh xa: " + stringArray[6]);
                }
                BigDecimal bigDecimal = new BigDecimal(stringArray[3]);
                int n = Integer.parseInt(stringArray[5]);
                SanPham sanPham = switch (stringArray[0]) {
                    case "GIAY" -> {
                        if (stringArray.length != 10 && stringArray.length != 14 && stringArray.length != 16) {
                            throw new IllegalArgumentException("Sai so cot.");
                        }
                        yield new GiayTheThao(stringArray[1], stringArray[2], stringArray[4], bigDecimal, n, stringArray[8], stringArray.length == 16 ? DanhSachSanPham.unknown(stringArray[15]) : null, danhMuc, stringArray[7], stringArray[9]);
                    }
                    case "DUNG_CU" -> {
                        if (stringArray.length != 10 && stringArray.length != 14 && stringArray.length != 16) {
                            throw new IllegalArgumentException("Sai so cot.");
                        }
                        yield new DungCuTheThao(stringArray[1], stringArray[2], stringArray[4], bigDecimal, n, stringArray.length == 16 ? DanhSachSanPham.unknown(stringArray[14]) : null, stringArray.length == 10 ? DanhSachSanPham.unknown(stringArray[8]) : DanhSachSanPham.unknown(stringArray[11]), danhMuc, Double.parseDouble(stringArray.length == 10 ? stringArray[9] : stringArray[12]));
                    }
                    case "QUAN_AO" -> {
                        if (stringArray.length != 8 && stringArray.length != 14 && stringArray.length != 16) {
                            throw new IllegalArgumentException("Sai so cot.");
                        }
                        yield new QuanAoTheThao(stringArray[1], stringArray[2], stringArray[4], bigDecimal, n, stringArray.length == 16 ? DanhSachSanPham.unknown(stringArray[14]) : null, stringArray.length == 16 ? DanhSachSanPham.unknown(stringArray[15]) : null, danhMuc, stringArray.length == 8 ? stringArray[7] : stringArray[13]);
                    }
                    default -> throw new IllegalArgumentException("Kieu san pham khong hop le.");
                };
                danhSachSanPham.them(sanPham);
                string2 = stringArray[0].equals("DUNG_CU") && stringArray.length == 10 ? stringArray[7] : (stringArray.length >= 14 ? stringArray[10] : "");
                if (string2.isEmpty()) continue;
                danhSachSanPham.monTheThaoCu.put(sanPham.getMaSanPham().toUpperCase(Locale.ROOT), string2);
                continue;
            }
            catch (IllegalArgumentException illegalArgumentException) {
                throw new IOException("Dong " + (i + 2) + ": " + illegalArgumentException.getMessage(), illegalArgumentException);
            }
        }
        this.ds = danhSachSanPham.ds;
        this.soLuong = danhSachSanPham.soLuong;
        this.monTheThaoCu.clear();
        this.monTheThaoCu.putAll(danhSachSanPham.monTheThaoCu);
    }

    private int chiSo(String string) {
        if (string != null) {
            for (int i = 0; i < this.soLuong; ++i) {
                if (!this.ds[i].getMaSanPham().equalsIgnoreCase(string.trim())) continue;
                return i;
            }
        }
        return -1;
    }

    private void moRongMang() {
        int n = this.ds.length == 0 ? 1 : Math.max(this.ds.length + 1, this.ds.length * 2);
        SanPham[] sanPhamArray = new SanPham[n];
        System.arraycopy(this.ds, 0, sanPhamArray, 0, this.soLuong);
        this.ds = sanPhamArray;
    }

    private static String chuanHoaTimKiem(String string) {
        return Normalizer.normalize(string, Normalizer.Form.NFD).replaceAll("\\p{M}+", "").replace('\u0111', 'd').replace('\u0110', 'D').toLowerCase(Locale.ROOT);
    }

    private static SanPham saoChep(SanPham sanPham) {
        if (sanPham instanceof GiayTheThao) {
            GiayTheThao giayTheThao = (GiayTheThao)sanPham;
            return new GiayTheThao(sanPham.getMaSanPham(), sanPham.getTenSanPham(), sanPham.getThuongHieu(), sanPham.getGiaBan(), sanPham.getSoLuongTon(), sanPham.getMauSac(), sanPham.getChatLieu(), sanPham.getDanhMuc(), giayTheThao.getKichCoGiay(), giayTheThao.getLoaiDeGiay());
        }
        if (sanPham instanceof DungCuTheThao) {
            DungCuTheThao dungCuTheThao = (DungCuTheThao)sanPham;
            return new DungCuTheThao(sanPham.getMaSanPham(), sanPham.getTenSanPham(), sanPham.getThuongHieu(), sanPham.getGiaBan(), sanPham.getSoLuongTon(), sanPham.getMauSac(), sanPham.getChatLieu(), sanPham.getDanhMuc(), dungCuTheThao.getKhoiLuongKg());
        }
        QuanAoTheThao quanAoTheThao = (QuanAoTheThao)sanPham;
        return new QuanAoTheThao(sanPham.getMaSanPham(), sanPham.getTenSanPham(), sanPham.getThuongHieu(), sanPham.getGiaBan(), sanPham.getSoLuongTon(), sanPham.getMauSac(), sanPham.getChatLieu(), sanPham.getDanhMuc(), quanAoTheThao.getKichCoQuanAo());
    }

    private static void kiemTra(SanPham sanPham) {
        if (sanPham == null || DuLieu.timDanhMuc(sanPham.getDanhMuc().getMaDanhMuc()) != sanPham.getDanhMuc() || !(sanPham instanceof GiayTheThao) && !(sanPham instanceof DungCuTheThao) && !(sanPham instanceof QuanAoTheThao)) {
            throw new IllegalArgumentException("Thong tin san pham khong hop le.");
        }
    }

    private static String text(String string) {
        return string == null ? "" : string;
    }

    private static String unknown(String string) {
        return string.isBlank() ? null : string;
    }
}
