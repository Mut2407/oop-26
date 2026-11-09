package services;

import models.*;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.DateTimeException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

// Doc cac file text theo thu tu de noi lai tham chieu bang ma.
public class LuuTruFileText {
    private String thuMuc;
    private String tenFile;
    private int soDong;
    private static final String[] CAC_FILE = {"loaisp.txt", "khachhang.txt", "nhanvien.txt",
            "nhacungcap.txt", "sanpham.txt", "hoadon.txt", "chitiethoadon.txt",
            "phieunhap.txt", "chitietphieunhap.txt"};
    private static final String[] CAC_NHAN = {"LOAI_SP", "KHACH_HANG", "NHAN_VIEN",
            "NHA_CUNG_CAP", "SAN_PHAM", "HOA_DON", "CHI_TIET_HOA_DON",
            "PHIEU_NHAP", "CHI_TIET_PHIEU_NHAP"};

    public LuuTruFileText() {
        this("data");
    }

    public LuuTruFileText(String thuMuc) {
        if (thuMuc == null || thuMuc.trim().isEmpty())
            throw new IllegalArgumentException("Thu muc khong hop le.");
        this.thuMuc = thuMuc;
    }

    public void docDuLieu(QuanLyCuaHang quanLy) throws IOException {
        QuanLyCuaHang tam = new QuanLyCuaHang(thuMuc);
        try {
            docCacFile(tam);
        } catch (IllegalArgumentException | DateTimeException e) {
            throw new IOException(viTri() + e.getMessage(), e);
        }
        quanLy.thayDuLieu(tam);
    }

    private String viTri() {
        return tenFile + ": dong " + soDong + ": ";
    }

    private void docCacFile(QuanLyCuaHang tam) throws IOException {
        for (int i = 0; i < 5; i++)
            docDanhSach(i, tam);
        docHoaDonRieng(tam);
        docPhieuNhapRieng(tam);
    }

    private BufferedReader moDoc(File tep) throws IOException {
        return new BufferedReader(new InputStreamReader(new FileInputStream(tep), "UTF-8"));
    }

    private String[] docCacDong(int loai) throws IOException {
        File tep = new File(thuMuc, CAC_FILE[loai]);
        if (!tep.exists())
            return new String[0];
        tenFile = tep.getName();
        soDong = 1;
        try (BufferedReader in = moDoc(tep)) {
            String dau = in.readLine();
            yeuCau(dau != null && dau.replace("\uFEFF", "").equals(CAC_NHAN[loai]), "Sai dau file.");
            return docPhanConLai(in);
        }
    }

    private String[] docPhanConLai(BufferedReader in) throws IOException {
        String[] dong = new String[10];
        int n = 0;
        String s;
        while ((s = in.readLine()) != null) {
            if (n == dong.length)
                dong = Arrays.copyOf(dong, n + 10);
            dong[n++] = s;
        }
        return Arrays.copyOf(dong, n);
    }

    private String[] tachDong(String dong, int loai, int viTri, int soTruong) throws IOException {
        tenFile = CAC_FILE[loai];
        soDong = viTri + 2;
        String[] f = dong.split("\\|", -1);
        for (int i = 0; i < f.length; i++)
            f[i] = f[i].trim();
        yeuCau(soTruong == 0 || f.length == soTruong, "Sai so truong.");
        return f;
    }

    private void docDanhSach(int loai, QuanLyCuaHang q) throws IOException {
        String[] dong = docCacDong(loai);
        for (int i = 0; i < dong.length; i++) {
            if (dong[i].trim().isEmpty())
                continue;
            String[] f = tachDong(dong[i], loai, i, loai == 0 ? 3 : (loai == 4 ? 0 : 4));
            yeuCau(themDongDanhSach(loai, f, q), "Trung ma hoac du lieu khong hop le.");
        }
    }

    private boolean themDongDanhSach(int loai, String[] f, QuanLyCuaHang q) throws IOException {
        if (loai == 0) return q.getDanhSachLoaiSP().them(new LoaiSP(f[0], f[1], f[2]));
        if (loai == 1) return q.getDanhSachKhachHang().them(new KhachHang(f[0], f[1], f[2], f[3]));
        if (loai == 2) return q.getDanhSachNhanVien().them(new NhanVien(f[0], f[1], f[2], f[3]));
        if (loai == 3) return q.getDanhSachNhaCungCap().them(new NhaCungCap(f[0], f[1], f[2], f[3]));
        SanPham sp = taoSanPham(f, q);
        sp.capNhatTon(Integer.parseInt(f[5]));
        return q.getDanhSachSanPham().them(sp);
    }

    private void docHoaDonRieng(QuanLyCuaHang q) throws IOException {
        String[] dong = docCacDong(5);
        String[] chiTiet = docCacDong(6);
        boolean[] daDung = new boolean[chiTiet.length];
        for (int i = 0; i < dong.length; i++)
            if (!dong[i].trim().isEmpty())
                docMotHoaDon(dong[i], i, chiTiet, daDung, q);
        kiemTraChiTiet(chiTiet, daDung, 6);
    }

    private void docMotHoaDon(String dong, int i, String[] ct, boolean[] daDung, QuanLyCuaHang q) throws IOException {
        String[] f = tachDong(dong, 5, i, 4);
        HoaDon phieu = new HoaDon(f[0], LocalDate.parse(f[1]),
                q.getDanhSachKhachHang().timTheoMa(f[2]), q.getDanhSachNhanVien().timTheoMa(f[3]));
        docChiTietHoaDon(phieu, ct, daDung, q);
        tachDong(dong, 5, i, 4);
        yeuCau(q.getDanhSachHoaDon().them(phieu), "Hoa don rong chi tiet hoac trung ma.");
    }

    private void docChiTietHoaDon(HoaDon phieu, String[] dong, boolean[] daDung, QuanLyCuaHang q) throws IOException {
        for (int i = 0; i < dong.length; i++) {
            if (dong[i].trim().isEmpty()) continue;
            String[] f = tachDong(dong[i], 6, i, 4);
            if (!f[0].equalsIgnoreCase(phieu.getMaHoaDon())) continue;
            ChiTietHoaDon ct = new ChiTietHoaDon(q.getDanhSachSanPham().timTheoMa(f[1]),
                    Integer.parseInt(f[2]), new BigDecimal(f[3]));
            yeuCau(phieu.themChiTiet(ct), "Chi tiet trung san pham hoac khong hop le.");
            daDung[i] = true;
        }
    }

    private void docPhieuNhapRieng(QuanLyCuaHang q) throws IOException {
        String[] dong = docCacDong(7);
        String[] chiTiet = docCacDong(8);
        boolean[] daDung = new boolean[chiTiet.length];
        for (int i = 0; i < dong.length; i++)
            if (!dong[i].trim().isEmpty())
                docMotPhieuNhap(dong[i], i, chiTiet, daDung, q);
        kiemTraChiTiet(chiTiet, daDung, 8);
    }

    private void docMotPhieuNhap(String dong, int i, String[] ct, boolean[] daDung, QuanLyCuaHang q) throws IOException {
        String[] f = tachDong(dong, 7, i, 4);
        PhieuNhap phieu = new PhieuNhap(f[0], LocalDate.parse(f[1]),
                q.getDanhSachNhaCungCap().timTheoMa(f[2]), q.getDanhSachNhanVien().timTheoMa(f[3]));
        docChiTietPhieuNhap(phieu, ct, daDung, q);
        tachDong(dong, 7, i, 4);
        yeuCau(q.getDanhSachPhieuNhap().them(phieu), "Phieu nhap rong chi tiet hoac trung ma.");
    }

    private void docChiTietPhieuNhap(PhieuNhap phieu, String[] dong, boolean[] daDung, QuanLyCuaHang q) throws IOException {
        for (int i = 0; i < dong.length; i++) {
            if (dong[i].trim().isEmpty()) continue;
            String[] f = tachDong(dong[i], 8, i, 4);
            if (!f[0].equalsIgnoreCase(phieu.getMaPhieuNhap())) continue;
            ChiTietPhieuNhap ct = new ChiTietPhieuNhap(q.getDanhSachSanPham().timTheoMa(f[1]),
                    Integer.parseInt(f[2]), new BigDecimal(f[3]));
            yeuCau(phieu.themChiTiet(ct), "Chi tiet trung san pham hoac khong hop le.");
            daDung[i] = true;
        }
    }

    private void kiemTraChiTiet(String[] dong, boolean[] daDung, int loai) throws IOException {
        for (int i = 0; i < dong.length; i++)
            if (!dong[i].trim().isEmpty() && !daDung[i]) {
                tachDong(dong[i], loai, i, 4);
                yeuCau(false, "Chi tiet tham chieu ma chung tu khong ton tai.");
            }
    }

    private void yeuCau(boolean dung, String thongBao) throws IOException {
        if (!dung)
            throw new IOException(viTri() + thongBao);
    }

    private SanPham taoSanPham(String[] f, QuanLyCuaHang tam) throws IOException {
        yeuCau(f.length >= 7, "Thieu truong san pham.");
        LoaiSP loai = tam.getDanhSachLoaiSP().timTheoMa(f[6]);
        if (f[0].equals("GIAY"))
            return docGiay(f, loai);
        yeuCau((f[0].equals("DUNGCU") || f[0].equals("DUNG_CU")), "Sai nhan lop san pham.");
        return docDungCu(f, loai);
    }

    private GiayTheThao docGiay(String[] f, LoaiSP loai) throws IOException {
        yeuCau(f.length == 11, "Sai so truong GIAY.");
        return new GiayTheThao(f[1], f[2], f[3], new BigDecimal(f[4]), loai,
                Double.parseDouble(f[7]), f[8], f[9], f[10]);
    }

    private DungCuTheThao docDungCu(String[] f, LoaiSP loai) throws IOException {
        yeuCau(f.length == 10, "Sai so truong DUNGCU.");
        return new DungCuTheThao(f[1], f[2], f[3], new BigDecimal(f[4]), loai,
                f[7], f[8], Double.parseDouble(f[9]));
    }

    public void ghiDuLieu(QuanLyCuaHang quanLy) throws IOException {
        File tam = new File(thuMuc, ".luu-tam");
        if (!tam.isDirectory() && !tam.mkdirs())
            throw new IOException("Khong tao duoc thu muc tam.");
        ghiCacFile(tam, quanLy);
        new LuuTruFileText(tam.getPath()).docDuLieu(new QuanLyCuaHang(tam.getPath()));
        boolean[] coFileCu = saoLuuCacFile(tam);
        thayCacFile(tam, coFileCu);
    }

    private void ghiCacFile(File thuMucTam, QuanLyCuaHang q) throws IOException {
        for (int i = 0; i < CAC_FILE.length; i++) {
            File tep = new File(thuMucTam, CAC_FILE[i]);
            try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tep), "UTF-8"))) {
                ghiDong(out, new String[] {CAC_NHAN[i]});
                if (i < 5) ghiDanhSach(out, i, q);
                else ghiChungTu(out, i, q);
            }
        }
    }

    private void ghiDanhSach(BufferedWriter out, int loai, QuanLyCuaHang q) throws IOException {
        if (loai == 0) ghiLoaiSP(out, q);
        else if (loai == 1) ghiKhachHang(out, q);
        else if (loai == 2) ghiNhanVien(out, q);
        else if (loai == 3) ghiNhaCungCap(out, q);
        else ghiSanPham(out, q);
    }

    private void ghiChungTu(BufferedWriter out, int loai, QuanLyCuaHang q) throws IOException {
        if (loai == 5) ghiHoaDon(out, q);
        else if (loai == 6) ghiChiTietHoaDon(out, q);
        else if (loai == 7) ghiPhieuNhap(out, q);
        else ghiChiTietPhieuNhap(out, q);
    }

    private boolean[] saoLuuCacFile(File tam) throws IOException {
        boolean[] coFileCu = new boolean[CAC_FILE.length];
        for (int i = 0; i < CAC_FILE.length; i++) {
            File cu = new File(thuMuc, CAC_FILE[i]);
            coFileCu[i] = cu.exists();
            if (cu.exists() && !cu.isFile())
                throw new IOException("Khong phai file: " + cu.getPath());
            if (coFileCu[i])
                Files.copy(cu.toPath(), new File(tam, CAC_FILE[i] + ".bak").toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        return coFileCu;
    }

    private void thayCacFile(File tam, boolean[] coFileCu) throws IOException {
        int daThay = 0;
        try {
            for (String ten : CAC_FILE) {
                Files.move(new File(tam, ten).toPath(), new File(thuMuc, ten).toPath(), StandardCopyOption.REPLACE_EXISTING);
                daThay++;
            }
        } catch (IOException e) {
            phucHoiCacFile(tam, coFileCu, daThay, e);
            throw e;
        }
    }

    private void phucHoiCacFile(File tam, boolean[] coFileCu, int daThay, IOException loi) {
        for (int i = 0; i < daThay; i++)
            try {
                File dich = new File(thuMuc, CAC_FILE[i]);
                if (coFileCu[i])
                    Files.copy(new File(tam, CAC_FILE[i] + ".bak").toPath(), dich.toPath(), StandardCopyOption.REPLACE_EXISTING);
                else
                    Files.deleteIfExists(dich.toPath());
            } catch (IOException e) {
                loi.addSuppressed(e);
            }
    }

    private void ghiDong(BufferedWriter out, String[] f) throws IOException {
        for (int i = 0; i < f.length; i++)
            if (f[i] == null || f[i].contains("|") || f[i].contains("\n") || f[i].contains("\r"))
                throw new IOException("Truong du lieu chua ky tu phan cach.");
        out.write(String.join("|", f));
        out.newLine();
    }

    private void ghiLoaiSP(BufferedWriter out, QuanLyCuaHang q) throws IOException {
        LoaiSP[] ds = q.getDanhSachLoaiSP().getTatCa();
        for (int i = 0; i < ds.length; i++) {
            LoaiSP x = ds[i];
            ghiDong(out, new String[] {x.getMaLoaiSP(), x.getTenLoaiSP(), x.getMoTa()});
        }
    }

    private void ghiKhachHang(BufferedWriter out, QuanLyCuaHang q) throws IOException {
        KhachHang[] ds = q.getDanhSachKhachHang().getTatCa();
        for (int i = 0; i < ds.length; i++) {
            KhachHang x = ds[i];
            ghiDong(out, new String[] {x.getMaKhachHang(), x.getHoTen(), x.getSoDienThoai(), x.getDiaChi()});
        }
    }

    private void ghiNhanVien(BufferedWriter out, QuanLyCuaHang q) throws IOException {
        NhanVien[] ds = q.getDanhSachNhanVien().getTatCa();
        for (int i = 0; i < ds.length; i++) {
            NhanVien x = ds[i];
            ghiDong(out, new String[] {x.getMaNhanVien(), x.getHoTen(), x.getSoDienThoai(), x.getCaLamViec()});
        }
    }

    private void ghiNhaCungCap(BufferedWriter out, QuanLyCuaHang q) throws IOException {
        NhaCungCap[] ds = q.getDanhSachNhaCungCap().getTatCa();
        for (int i = 0; i < ds.length; i++) {
            NhaCungCap x = ds[i];
            ghiDong(out, new String[] {x.getMaNhaCungCap(), x.getTenNhaCungCap(), x.getSoDienThoai(), x.getDiaChi()});
        }
    }

    private void ghiSanPham(BufferedWriter out, QuanLyCuaHang q) throws IOException {
        SanPham[] ds = q.getDanhSachSanPham().getTatCa();
        for (int i = 0; i < ds.length; i++)
            ghiMotSanPham(out, ds[i]);
    }

    private void ghiMotSanPham(BufferedWriter out, SanPham sp) throws IOException {
        if (sp instanceof GiayTheThao)
            ghiGiay(out, (GiayTheThao) sp);
        else if (sp instanceof DungCuTheThao)
            ghiDungCu(out, (DungCuTheThao) sp);
        else
            throw new IOException("Lop SP khong duoc ho tro.");
    }

    private String[] truongChung(SanPham sp, String nhan, int soTruong) {
        String[] f = new String[soTruong];
        f[0] = nhan;
        f[1] = sp.getMaSanPham();
        f[2] = sp.getTenSanPham();
        f[3] = sp.getThuongHieu();
        f[4] = sp.getGiaBan().toPlainString();
        f[5] = String.valueOf(sp.getSoLuongTon());
        f[6] = sp.getLoaiSP().getMaLoaiSP();
        return f;
    }

    private void ghiGiay(BufferedWriter out, GiayTheThao sp) throws IOException {
        String[] f = truongChung(sp, "GIAY", 11);
        f[7] = String.valueOf(sp.getKichCoGiay());
        f[8] = sp.getMauSac();
        f[9] = sp.getChatLieu();
        f[10] = sp.getLoaiDeGiay();
        ghiDong(out, f);
    }

    private void ghiDungCu(BufferedWriter out, DungCuTheThao sp) throws IOException {
        String[] f = truongChung(sp, "DUNG_CU", 10);
        f[7] = sp.getMonTheThao();
        f[8] = sp.getVatLieu();
        f[9] = String.valueOf(sp.getKhoiLuongKg());
        ghiDong(out, f);
    }

    private void ghiHoaDon(BufferedWriter out, QuanLyCuaHang q) throws IOException {
        HoaDon[] ds = q.getDanhSachHoaDon().getTatCa();
        for (int i = 0; i < ds.length; i++)
            ghiMotHoaDon(out, ds[i]);
    }

    private void ghiMotHoaDon(BufferedWriter out, HoaDon phieu) throws IOException {
        ghiDong(out, new String[] {phieu.getMaHoaDon(), phieu.getNgayLap().toString(),
                phieu.getKhachHang().getMaKhachHang(), phieu.getNhanVien().getMaNhanVien()});
    }

    private void ghiChiTietHoaDon(BufferedWriter out, QuanLyCuaHang q) throws IOException {
        for (HoaDon phieu : q.getDanhSachHoaDon().getTatCa())
            for (ChiTietHoaDon ct : phieu.getChiTiet())
                ghiDong(out, new String[] {phieu.getMaHoaDon(), ct.getSanPham().getMaSanPham(),
                        String.valueOf(ct.getSoLuong()), ct.getDonGiaBan().toPlainString()});
    }

    private void ghiPhieuNhap(BufferedWriter out, QuanLyCuaHang q) throws IOException {
        PhieuNhap[] ds = q.getDanhSachPhieuNhap().getTatCa();
        for (int i = 0; i < ds.length; i++)
            ghiMotPhieuNhap(out, ds[i]);
    }

    private void ghiMotPhieuNhap(BufferedWriter out, PhieuNhap phieu) throws IOException {
        ghiDong(out, new String[] {phieu.getMaPhieuNhap(), phieu.getNgayLap().toString(),
                phieu.getNhaCungCap().getMaNhaCungCap(), phieu.getNhanVien().getMaNhanVien()});
    }

    private void ghiChiTietPhieuNhap(BufferedWriter out, QuanLyCuaHang q) throws IOException {
        for (PhieuNhap phieu : q.getDanhSachPhieuNhap().getTatCa())
            for (ChiTietPhieuNhap ct : phieu.getChiTiet())
                ghiDong(out, new String[] {phieu.getMaPhieuNhap(), ct.getSanPham().getMaSanPham(),
                        String.valueOf(ct.getSoLuong()), ct.getDonGiaNhap().toPlainString()});
    }

}
