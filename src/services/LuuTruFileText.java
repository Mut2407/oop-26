package services;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import models.*;

public class LuuTruFileText {
    private File thuMuc;
    private static final String[] TEN_FILE = {"loaisp.txt", "khachhang.txt", "nhanvien.txt", "nhacungcap.txt",
            "sanpham.txt", "hoadon.txt", "chitiethoadon.txt", "phieunhap.txt", "chitietphieunhap.txt"};
    private static final String[] TIEU_DE = {"LOAI_SP", "KHACH_HANG", "NHAN_VIEN", "NHA_CUNG_CAP",
            "SAN_PHAM", "HOA_DON", "CHI_TIET_HOA_DON", "PHIEU_NHAP", "CHI_TIET_PHIEU_NHAP"};

    public LuuTruFileText() {
        this("data");
    }

    public LuuTruFileText(String thuMuc) {
        if (thuMuc == null || thuMuc.trim().isEmpty()) {
            throw new IllegalArgumentException("Thu muc du lieu khong duoc rong.");
        }
        this.thuMuc = new File(thuMuc);
    }

    public QuanLyCuaHang docDuLieu() throws IOException {
        kiemTraBoFile();
        QuanLyCuaHang moi = new QuanLyCuaHang(thuMuc.getPath());
        docLoaiSP(moi);
        docKhachHang(moi);
        docNhanVien(moi);
        docNhaCungCap(moi);
        docSanPham(moi);
        docHoaDon(moi);
        docPhieuNhap(moi);
        docChiTietHoaDon(moi);
        docChiTietPhieuNhap(moi);
        return moi;
    }

    private void kiemTraBoFile() throws IOException {
        if (new File(thuMuc, "sport-store.txt").exists()) {
            throw new IOException("Con sport-store.txt cua ban truoc. Can sao luu/chuyen doi file nay truoc khi dung bo 9 file moi.");
        }
        int soFile = 0;
        for (int i = 0; i < TEN_FILE.length; i++) {
            if (new File(thuMuc, TEN_FILE[i]).exists()) {
                soFile++;
            }
        }
        if (soFile > 0 && soFile < TEN_FILE.length) {
            throw new IOException("Bo du lieu phai co du 9 file; khong doc bo file dang thieu.");
        }
    }

    private String[] docFile(int loaiFile) throws IOException {
        File tep = new File(thuMuc, TEN_FILE[loaiFile]);
        if (!tep.exists()) {
            return new String[0];
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(tep), "UTF-8"));
        try {
            String tieuDe = in.readLine();
            if (tieuDe != null && tieuDe.startsWith("\uFEFF")) {
                tieuDe = tieuDe.substring(1);
            }
            if (!TIEU_DE[loaiFile].equals(tieuDe)) {
                throw new IOException("Sai tieu de file: " + tep);
            }
            return docCacDong(in);
        } finally {
            in.close();
        }
    }

    private String[] docCacDong(BufferedReader in) throws IOException {
        String[] ketQua = new String[0];
        String dong = in.readLine();
        while (dong != null) {
            ketQua = Arrays.copyOf(ketQua, ketQua.length + 1);
            ketQua[ketQua.length - 1] = dong;
            dong = in.readLine();
        }
        return ketQua;
    }

    private String[] tachCot(String dong, int soCot) {
        String[] cot = dong.split("\\|", -1);
        if (cot.length != soCot) {
            throw new IllegalArgumentException("Can " + soCot + " cot, nhan " + cot.length + ".");
        }
        return cot;
    }

    private IOException loiDong(int loaiFile, int viTri, RuntimeException e) {
        return new IOException(TEN_FILE[loaiFile] + " - dong " + (viTri + 2) + ": " + e.getMessage(), e);
    }

    private void docLoaiSP(QuanLyCuaHang ql) throws IOException {
        String[] dong = docFile(0);
        for (int i = 0; i < dong.length; i++) {
            try {
                String[] cot = tachCot(dong[i], 3);
                LoaiSP moi = new LoaiSP(cot[0], cot[1], cot[2]);
                if (!ql.themLoaiSP(moi)) {
                    throw new IllegalArgumentException("Ma trung.");
                }
            } catch (RuntimeException e) {
                throw loiDong(0, i, e);
            }
        }
    }

    private void docKhachHang(QuanLyCuaHang ql) throws IOException {
        String[] dong = docFile(1);
        for (int i = 0; i < dong.length; i++) {
            try {
                String[] cot = tachCot(dong[i], 4);
                KhachHang moi = new KhachHang(cot[0], cot[1], cot[2], cot[3]);
                if (!ql.themKhachHang(moi)) {
                    throw new IllegalArgumentException("Ma trung.");
                }
            } catch (RuntimeException e) {
                throw loiDong(1, i, e);
            }
        }
    }

    private void docNhanVien(QuanLyCuaHang ql) throws IOException {
        String[] dong = docFile(2);
        for (int i = 0; i < dong.length; i++) {
            try {
                String[] cot = tachCot(dong[i], 4);
                NhanVien moi = new NhanVien(cot[0], cot[1], cot[2], cot[3]);
                if (!ql.themNhanVien(moi)) {
                    throw new IllegalArgumentException("Ma trung.");
                }
            } catch (RuntimeException e) {
                throw loiDong(2, i, e);
            }
        }
    }

    private void docNhaCungCap(QuanLyCuaHang ql) throws IOException {
        String[] dong = docFile(3);
        for (int i = 0; i < dong.length; i++) {
            try {
                String[] cot = tachCot(dong[i], 4);
                NhaCungCap moi = new NhaCungCap(cot[0], cot[1], cot[2], cot[3]);
                if (!ql.themNhaCungCap(moi)) {
                    throw new IllegalArgumentException("Ma trung.");
                }
            } catch (RuntimeException e) {
                throw loiDong(3, i, e);
            }
        }
    }

    private void docSanPham(QuanLyCuaHang ql) throws IOException {
        String[] dong = docFile(4);
        for (int i = 0; i < dong.length; i++) {
            try {
                String[] cot = dong[i].split("\\|", -1);
                if (cot.length < 7) {
                    throw new IllegalArgumentException("Thieu cot san pham.");
                }
                SanPham sp = taoSanPham(cot, ql.getLoaiSP().timTheoMa(cot[6]));
                sp.capNhatTon(Integer.parseInt(cot[5]));
                ql.khoiPhucSanPham(sp);
            } catch (RuntimeException e) {
                throw loiDong(4, i, e);
            }
        }
    }

    private SanPham taoSanPham(String[] cot, LoaiSP loai) {
        if (cot[0].equals("GIAY") && cot.length == 11) {
            return new GiayTheThao(cot[1], cot[2], cot[3], new BigDecimal(cot[4]), loai,
                    Double.parseDouble(cot[7]), cot[8], cot[9], cot[10]);
        }
        if (cot[0].equals("QUAN_AO") && cot.length == 10) {
            return new QuanAoTheThao(cot[1], cot[2], cot[3], new BigDecimal(cot[4]), loai, cot[7], cot[8], cot[9]);
        }
        if (cot[0].equals("DUNG_CU") && cot.length == 10) {
            return new DungCuTheThao(cot[1], cot[2], cot[3], new BigDecimal(cot[4]), loai,
                    cot[7], cot[8], Double.parseDouble(cot[9]));
        }
        throw new IllegalArgumentException("Kieu hoac so cot san pham khong hop le.");
    }

    private void docHoaDon(QuanLyCuaHang ql) throws IOException {
        String[] dong = docFile(5);
        for (int i = 0; i < dong.length; i++) {
            try {
                String[] cot = tachCot(dong[i], 4);
                HoaDon moi = new HoaDon(cot[0], LocalDate.parse(cot[1]),
                        ql.getKhachHang().timTheoMa(cot[2]), ql.getNhanVien().timTheoMa(cot[3]));
                ql.khoiPhucHoaDon(moi);
            } catch (RuntimeException e) {
                throw loiDong(5, i, e);
            }
        }
    }

    private void docChiTietHoaDon(QuanLyCuaHang ql) throws IOException {
        String[] dong = docFile(6);
        for (int i = 0; i < dong.length; i++) {
            try {
                String[] cot = tachCot(dong[i], 4);
                HoaDon chungTu = ql.getHoaDon().timTheoMa(cot[0]);
                if (chungTu == null) {
                    throw new IllegalArgumentException("Ma chung tu khong ton tai.");
                }
                chungTu.themChiTiet(new ChiTietHoaDon(ql.getSanPham().timTheoMa(cot[1]),
                        Integer.parseInt(cot[2]), new BigDecimal(cot[3])));
                ql.getHoaDon().sua(cot[0], chungTu);
            } catch (RuntimeException e) {
                throw loiDong(6, i, e);
            }
        }
    }

    private void docPhieuNhap(QuanLyCuaHang ql) throws IOException {
        String[] dong = docFile(7);
        for (int i = 0; i < dong.length; i++) {
            try {
                String[] cot = tachCot(dong[i], 4);
                PhieuNhap moi = new PhieuNhap(cot[0], LocalDate.parse(cot[1]),
                        ql.getNhaCungCap().timTheoMa(cot[2]), ql.getNhanVien().timTheoMa(cot[3]));
                ql.khoiPhucPhieuNhap(moi);
            } catch (RuntimeException e) {
                throw loiDong(7, i, e);
            }
        }
    }

    private void docChiTietPhieuNhap(QuanLyCuaHang ql) throws IOException {
        String[] dong = docFile(8);
        for (int i = 0; i < dong.length; i++) {
            try {
                String[] cot = tachCot(dong[i], 4);
                PhieuNhap chungTu = ql.getPhieuNhap().timTheoMa(cot[0]);
                if (chungTu == null) {
                    throw new IllegalArgumentException("Ma chung tu khong ton tai.");
                }
                chungTu.themChiTiet(new ChiTietPhieuNhap(ql.getSanPham().timTheoMa(cot[1]),
                        Integer.parseInt(cot[2]), new BigDecimal(cot[3])));
                ql.getPhieuNhap().sua(cot[0], chungTu);
            } catch (RuntimeException e) {
                throw loiDong(8, i, e);
            }
        }
    }

    public void ghiDuLieu(QuanLyCuaHang ql) throws IOException {
        taoThuMuc(thuMuc);
        File tam = new File(thuMuc, "tam");
        File saoLuu = new File(thuMuc, "sao_luu");
        taoThuMuc(tam);
        taoThuMuc(saoLuu);
        try {
            ghiBoFile(ql, tam);
            boolean[] daCo = saoLuuFileCu(saoLuu);
            try {
                chepBoFile(tam, thuMuc);
            } catch (IOException e) {
                phucHoiFileCu(saoLuu, daCo);
                throw e;
            }
        } finally {
            xoaFileTam(tam);
        }
    }

    private void ghiBoFile(QuanLyCuaHang ql, File dich) throws IOException {
        ghiLoaiSP(ql, dich);
        ghiKhachHang(ql, dich);
        ghiNhanVien(ql, dich);
        ghiNhaCungCap(ql, dich);
        ghiSanPham(ql, dich);
        ghiHoaDon(ql, dich);
        ghiChiTietHoaDon(ql, dich);
        ghiPhieuNhap(ql, dich);
        ghiChiTietPhieuNhap(ql, dich);
    }

    private void taoThuMuc(File thuMuc) throws IOException {
        if (!thuMuc.isDirectory() && !thuMuc.mkdirs()) {
            throw new IOException("Khong tao duoc thu muc: " + thuMuc);
        }
    }

    private BufferedWriter moGhi(File dich, int loaiFile) throws IOException {
        File tep = new File(dich, TEN_FILE[loaiFile]);
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tep), "UTF-8"));
        out.write(TIEU_DE[loaiFile]);
        out.newLine();
        return out;
    }

    private void ghiDong(BufferedWriter out, String[] cot) throws IOException {
        for (int i = 0; i < cot.length; i++) {
            if (cot[i] == null || cot[i].contains("|") || cot[i].contains("\n")
                    || cot[i].contains("\r") || cot[i].contains("\t")) {
                throw new IOException("Du lieu khong duoc chua dau |, tab hoac xuong dong.");
            }
        }
        out.write(String.join("|", cot));
        out.newLine();
    }

    private void ghiLoaiSP(QuanLyCuaHang ql, File dich) throws IOException {
        BufferedWriter out = moGhi(dich, 0);
        try {
            LoaiSP[] ds = ql.getLoaiSP().getTatCa();
            for (int i = 0; i < ds.length; i++) {
                ghiDong(out, new String[]{ds[i].getMaLoaiSP(), ds[i].getTenLoaiSP(), ds[i].getMoTa()});
            }
        } finally {
            out.close();
        }
    }

    private void ghiKhachHang(QuanLyCuaHang ql, File dich) throws IOException {
        BufferedWriter out = moGhi(dich, 1);
        try {
            KhachHang[] ds = ql.getKhachHang().getTatCa();
            for (int i = 0; i < ds.length; i++) {
                ghiDong(out, new String[]{ds[i].getMaKhachHang(), ds[i].getHoTen(), ds[i].getSoDienThoai(), ds[i].getDiaChi()});
            }
        } finally {
            out.close();
        }
    }

    private void ghiNhanVien(QuanLyCuaHang ql, File dich) throws IOException {
        BufferedWriter out = moGhi(dich, 2);
        try {
            NhanVien[] ds = ql.getNhanVien().getTatCa();
            for (int i = 0; i < ds.length; i++) {
                ghiDong(out, new String[]{ds[i].getMaNhanVien(), ds[i].getHoTen(), ds[i].getSoDienThoai(), ds[i].getCaLamViec()});
            }
        } finally {
            out.close();
        }
    }

    private void ghiNhaCungCap(QuanLyCuaHang ql, File dich) throws IOException {
        BufferedWriter out = moGhi(dich, 3);
        try {
            NhaCungCap[] ds = ql.getNhaCungCap().getTatCa();
            for (int i = 0; i < ds.length; i++) {
                ghiDong(out, new String[]{ds[i].getMaNhaCungCap(), ds[i].getTenNhaCungCap(), ds[i].getSoDienThoai(), ds[i].getDiaChi()});
            }
        } finally {
            out.close();
        }
    }

    private void ghiSanPham(QuanLyCuaHang ql, File dich) throws IOException {
        BufferedWriter out = moGhi(dich, 4);
        try {
            SanPham[] ds = ql.getSanPham().getTatCa();
            for (int i = 0; i < ds.length; i++) {
                ghiDong(out, cotSanPham(ds[i]));
            }
        } finally {
            out.close();
        }
    }

    private String[] cotSanPham(SanPham sp) {
        String[] rieng;
        String kieu;
        if (sp instanceof GiayTheThao) {
            GiayTheThao giay = (GiayTheThao) sp;
            kieu = "GIAY";
            rieng = new String[]{Double.toString(giay.getKichCoGiay()), giay.getMauSac(), giay.getChatLieu(), giay.getLoaiDeGiay()};
        } else if (sp instanceof QuanAoTheThao) {
            QuanAoTheThao ao = (QuanAoTheThao) sp;
            kieu = "QUAN_AO";
            rieng = new String[]{ao.getKichCoQuanAo(), ao.getMauSac(), ao.getChatLieu()};
        } else {
            DungCuTheThao dungCu = (DungCuTheThao) sp;
            kieu = "DUNG_CU";
            rieng = new String[]{dungCu.getMonTheThao(), dungCu.getVatLieu(), Double.toString(dungCu.getKhoiLuongKg())};
        }
        return ghepCotSanPham(sp, kieu, rieng);
    }

    private String[] ghepCotSanPham(SanPham sp, String kieu, String[] rieng) {
        String[] chung = {kieu, sp.getMaSanPham(), sp.getTenSanPham(), sp.getThuongHieu(),
                sp.getGiaBan().toPlainString(), Integer.toString(sp.getSoLuongTon()), sp.getLoaiSP().getMaLoaiSP()};
        String[] ketQua = Arrays.copyOf(chung, chung.length + rieng.length);
        for (int i = 0; i < rieng.length; i++) {
            ketQua[chung.length + i] = rieng[i];
        }
        return ketQua;
    }

    private void ghiHoaDon(QuanLyCuaHang ql, File dich) throws IOException {
        BufferedWriter out = moGhi(dich, 5);
        try {
            HoaDon[] ds = ql.getHoaDon().getTatCa();
            for (int i = 0; i < ds.length; i++) {
                ghiDong(out, new String[]{ds[i].getMaHoaDon(), ds[i].getNgayLap().toString(),
                        ds[i].getKhachHang().getMaKhachHang(), ds[i].getNhanVien().getMaNhanVien()});
            }
        } finally {
            out.close();
        }
    }

    private void ghiChiTietHoaDon(QuanLyCuaHang ql, File dich) throws IOException {
        BufferedWriter out = moGhi(dich, 6);
        try {
            HoaDon[] ds = ql.getHoaDon().getTatCa();
            for (int i = 0; i < ds.length; i++) {
                ChiTietHoaDon[] chiTiet = ds[i].getChiTiet();
                for (int j = 0; j < chiTiet.length; j++) {
                    ghiDong(out, new String[]{ds[i].getMaHoaDon(), chiTiet[j].getSanPham().getMaSanPham(),
                            Integer.toString(chiTiet[j].getSoLuong()), chiTiet[j].getDonGiaBan().toPlainString()});
                }
            }
        } finally {
            out.close();
        }
    }

    private void ghiPhieuNhap(QuanLyCuaHang ql, File dich) throws IOException {
        BufferedWriter out = moGhi(dich, 7);
        try {
            PhieuNhap[] ds = ql.getPhieuNhap().getTatCa();
            for (int i = 0; i < ds.length; i++) {
                ghiDong(out, new String[]{ds[i].getMaPhieuNhap(), ds[i].getNgayLap().toString(),
                        ds[i].getNhaCungCap().getMaNhaCungCap(), ds[i].getNhanVien().getMaNhanVien()});
            }
        } finally {
            out.close();
        }
    }

    private void ghiChiTietPhieuNhap(QuanLyCuaHang ql, File dich) throws IOException {
        BufferedWriter out = moGhi(dich, 8);
        try {
            PhieuNhap[] ds = ql.getPhieuNhap().getTatCa();
            for (int i = 0; i < ds.length; i++) {
                ChiTietPhieuNhap[] chiTiet = ds[i].getChiTiet();
                for (int j = 0; j < chiTiet.length; j++) {
                    ghiDong(out, new String[]{ds[i].getMaPhieuNhap(), chiTiet[j].getSanPham().getMaSanPham(),
                            Integer.toString(chiTiet[j].getSoLuong()), chiTiet[j].getDonGiaNhap().toPlainString()});
                }
            }
        } finally {
            out.close();
        }
    }

    private boolean[] saoLuuFileCu(File dich) throws IOException {
        boolean[] daCo = new boolean[TEN_FILE.length];
        for (int i = 0; i < TEN_FILE.length; i++) {
            File cu = new File(thuMuc, TEN_FILE[i]);
            daCo[i] = cu.exists();
            if (daCo[i]) {
                saoChepFile(cu, new File(dich, TEN_FILE[i]));
            }
        }
        return daCo;
    }

    private void chepBoFile(File nguon, File dich) throws IOException {
        for (int i = 0; i < TEN_FILE.length; i++) {
            saoChepFile(new File(nguon, TEN_FILE[i]), new File(dich, TEN_FILE[i]));
        }
    }

    private void phucHoiFileCu(File saoLuu, boolean[] daCo) throws IOException {
        for (int i = 0; i < TEN_FILE.length; i++) {
            File tep = new File(thuMuc, TEN_FILE[i]);
            if (daCo[i]) {
                saoChepFile(new File(saoLuu, TEN_FILE[i]), tep);
            } else if (tep.exists() && !tep.delete()) {
                throw new IOException("Khong phuc hoi duoc file: " + tep);
            }
        }
    }

    private void saoChepFile(File nguon, File dich) throws IOException {
        FileInputStream in = new FileInputStream(nguon);
        try {
            FileOutputStream out = new FileOutputStream(dich);
            try {
                byte[] boDem = new byte[4096];
                int soByte = in.read(boDem);
                while (soByte != -1) {
                    out.write(boDem, 0, soByte);
                    soByte = in.read(boDem);
                }
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }

    private void xoaFileTam(File tam) {
        for (int i = 0; i < TEN_FILE.length; i++) {
            new File(tam, TEN_FILE[i]).delete();
        }
        tam.delete();
    }
}
