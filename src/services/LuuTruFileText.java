package services;

import models.*;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.nio.charset.StandardCharsets;

public class LuuTruFileText {
    private String thuMuc;

    public LuuTruFileText() {
        this("data");
    }

    public LuuTruFileText(String thuMuc) {
        this.thuMuc = (thuMuc == null || thuMuc.trim().isEmpty()) ? "data" : thuMuc.trim();
    }

    public void docDuLieu(QuanLyCuaHang q) throws IOException {
        File folder = new File(thuMuc);
        if (!folder.exists()) {
            folder.mkdirs();
            return;
        }
        docLoaiSP(q);
        docKhachHang(q);
        docNhanVien(q);
        docNhaCungCap(q);
        docSanPham(q);
        docHoaDon(q);
        docPhieuNhap(q);
    }

    public void ghiDuLieu(QuanLyCuaHang q) throws IOException {
        File folder = new File(thuMuc);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        ghiLoaiSP(q);
        ghiKhachHang(q);
        ghiNhanVien(q);
        ghiNhaCungCap(q);
        ghiSanPham(q);
        ghiHoaDon(q);
        ghiPhieuNhap(q);
    }

    private BufferedReader moDoc(String tenFile) throws IOException {
        File f = new File(thuMuc, tenFile);
        if (!f.exists()) return null;
        return new BufferedReader(new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8));
    }

    private BufferedWriter moGhi(String tenFile) throws IOException {
        File f = new File(thuMuc, tenFile);
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), StandardCharsets.UTF_8));
    }

    //loại sp
    private void docLoaiSP(QuanLyCuaHang q) throws IOException {
        try (BufferedReader br = moDoc("loaisp.txt")) {
            if (br == null) return;
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replace("\uFEFF", "").trim();
                if (line.isEmpty() || line.equals("LOAI_SP")) continue;
                String[] f = line.split("\\|");
                if (f.length >= 3) {
                    q.getDanhSachLoaiSP().them(new LoaiSP(f[0].trim(), f[1].trim(), f[2].trim()));
                }
            }
        }
    }

    private void ghiLoaiSP(QuanLyCuaHang q) throws IOException {
        try (BufferedWriter bw = moGhi("loaisp.txt")) {
            bw.write("LOAI_SP");
            bw.newLine();
            for (LoaiSP x : q.getDanhSachLoaiSP().getTatCa()) {
                bw.write(x.getMaLoaiSP() + "|" + x.getTenLoaiSP() + "|" + x.getMoTa());
                bw.newLine();
            }
        }
    }

    //khách hàng
    private void docKhachHang(QuanLyCuaHang q) throws IOException {
        try (BufferedReader br = moDoc("khachhang.txt")) {
            if (br == null) return;
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replace("\uFEFF", "").trim();
                if (line.isEmpty() || line.equals("KHACH_HANG")) continue;
                String[] f = line.split("\\|");
                if (f.length >= 4) {
                    q.getDanhSachKhachHang().them(new KhachHang(f[0].trim(), f[1].trim(), f[2].trim(), f[3].trim()));
                }
            }
        }
    }

    private void ghiKhachHang(QuanLyCuaHang q) throws IOException {
        try (BufferedWriter bw = moGhi("khachhang.txt")) {
            bw.write("KHACH_HANG");
            bw.newLine();
            for (KhachHang x : q.getDanhSachKhachHang().getTatCa()) {
                bw.write(x.getMaKhachHang() + "|" + x.getHoTen() + "|" + x.getSoDienThoai() + "|" + x.getDiaChi());
                bw.newLine();
            }
        }
    }

    //nhân viên
    private void docNhanVien(QuanLyCuaHang q) throws IOException {
        try (BufferedReader br = moDoc("nhanvien.txt")) {
            if (br == null) return;
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replace("\uFEFF", "").trim();
                if (line.isEmpty() || line.equals("NHAN_VIEN")) continue;
                String[] f = line.split("\\|");
                if (f.length >= 4) {
                    q.getDanhSachNhanVien().them(new NhanVien(f[0].trim(), f[1].trim(), f[2].trim(), f[3].trim()));
                }
            }
        }
    }

    private void ghiNhanVien(QuanLyCuaHang q) throws IOException {
        try (BufferedWriter bw = moGhi("nhanvien.txt")) {
            bw.write("NHAN_VIEN");
            bw.newLine();
            for (NhanVien x : q.getDanhSachNhanVien().getTatCa()) {
                bw.write(x.getMaNhanVien() + "|" + x.getHoTen() + "|" + x.getSoDienThoai() + "|" + x.getCaLamViec());
                bw.newLine();
            }
        }
    }

    //nhà cung cấp
    private void docNhaCungCap(QuanLyCuaHang q) throws IOException {
        try (BufferedReader br = moDoc("nhacungcap.txt")) {
            if (br == null) return;
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replace("\uFEFF", "").trim();
                if (line.isEmpty() || line.equals("NHA_CUNG_CAP")) continue;
                String[] f = line.split("\\|");
                if (f.length >= 4) {
                    q.getDanhSachNhaCungCap().them(new NhaCungCap(f[0].trim(), f[1].trim(), f[2].trim(), f[3].trim()));
                }
            }
        }
    }

    private void ghiNhaCungCap(QuanLyCuaHang q) throws IOException {
        try (BufferedWriter bw = moGhi("nhacungcap.txt")) {
            bw.write("NHA_CUNG_CAP");
            bw.newLine();
            for (NhaCungCap x : q.getDanhSachNhaCungCap().getTatCa()) {
                bw.write(x.getMaNhaCungCap() + "|" + x.getTenNhaCungCap() + "|" + x.getSoDienThoai() + "|" + x.getDiaChi());
                bw.newLine();
            }
        }
    }
    //sản phẩm
    private void docSanPham(QuanLyCuaHang q) throws IOException {
        try (BufferedReader br = moDoc("sanpham.txt")) {
            if (br == null) return;
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replace("\uFEFF", "").trim();
                if (line.isEmpty() || line.equals("SAN_PHAM")) continue;
                String[] f = line.split("\\|");
                if (f.length < 7) continue;

                LoaiSP loai = q.getDanhSachLoaiSP().timTheoMa(f[6].trim());
                SanPham sp = null;

                if (f[0].trim().equalsIgnoreCase("GIAY") && f.length >= 11) {
                    sp = new GiayTheThao(f[1].trim(), f[2].trim(), f[3].trim(),
                            new BigDecimal(f[4].trim()), loai,
                            Double.parseDouble(f[7].trim()), f[8].trim(), f[9].trim(), f[10].trim());
                } else if ((f[0].trim().equalsIgnoreCase("DUNG_CU") || f[0].trim().equalsIgnoreCase("DUNGCU")) && f.length >= 10) {
                    sp = new DungCuTheThao(f[1].trim(), f[2].trim(), f[3].trim(),
                            new BigDecimal(f[4].trim()), loai,
                            f[7].trim(), f[8].trim(), Double.parseDouble(f[9].trim()));
                }

                if (sp != null) {
                    sp.capNhatTon(Integer.parseInt(f[5].trim()));
                    q.getDanhSachSanPham().them(sp);
                }
            }
        }
    }

    private void ghiSanPham(QuanLyCuaHang q) throws IOException {
        try (BufferedWriter bw = moGhi("sanpham.txt")) {
            bw.write("SAN_PHAM");
            bw.newLine();
            for (SanPham sp : q.getDanhSachSanPham().getTatCa()) {
                if (sp instanceof GiayTheThao) {
                    GiayTheThao g = (GiayTheThao) sp;
                    bw.write("GIAY|" + g.getMaSanPham() + "|" + g.getTenSanPham() + "|" + g.getThuongHieu() + "|"
                            + g.getGiaBan().toPlainString() + "|" + g.getSoLuongTon() + "|" + g.getLoaiSP().getMaLoaiSP() + "|"
                            + g.getKichCoGiay() + "|" + g.getMauSac() + "|" + g.getChatLieu() + "|" + g.getLoaiDeGiay());
                } else if (sp instanceof DungCuTheThao) {
                    DungCuTheThao d = (DungCuTheThao) sp;
                    bw.write("DUNG_CU|" + d.getMaSanPham() + "|" + d.getTenSanPham() + "|" + d.getThuongHieu() + "|"
                            + d.getGiaBan().toPlainString() + "|" + d.getSoLuongTon() + "|" + d.getLoaiSP().getMaLoaiSP() + "|"
                            + d.getMonTheThao() + "|" + d.getVatLieu() + "|" + d.getKhoiLuongKg());
                }
                bw.newLine();
            }
        }
    }

    //hóa đơn và chi tiết hóa đơn
    private void docHoaDon(QuanLyCuaHang q) throws IOException {
        // Đọc trước các chi tiết hóa đơn vào danh sách tạm
        java.util.ArrayList<String> dsChiTiet = new java.util.ArrayList<>();
        try (BufferedReader br = moDoc("chitiethoadon.txt")) {
            if (br != null) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.replace("\uFEFF", "").trim();
                    if (!line.isEmpty() && !line.equals("CHI_TIET_HOA_DON")) {
                        dsChiTiet.add(line);
                    }
                }
            }
        }

        // Đọc từng hóa đơn, gắn chi tiết vào trước rồi mới thêm vào danh sách
        try (BufferedReader br = moDoc("hoadon.txt")) {
            if (br == null) return;
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replace("\uFEFF", "").trim();
                if (line.isEmpty() || line.equals("HOA_DON")) continue;
                String[] f = line.split("\\|");
                if (f.length >= 4) {
                    KhachHang kh = q.getDanhSachKhachHang().timTheoMa(f[2].trim());
                    NhanVien nv = q.getDanhSachNhanVien().timTheoMa(f[3].trim());
                    HoaDon hd = new HoaDon(f[0].trim(), LocalDate.parse(f[1].trim()), kh, nv);

                    for (String ctLine : dsChiTiet) {
                        String[] ct = ctLine.split("\\|");
                        if (ct.length >= 4 && ct[0].trim().equalsIgnoreCase(hd.getMaHoaDon())) {
                            SanPham sp = q.getDanhSachSanPham().timTheoMa(ct[1].trim());
                            if (sp != null) {
                                hd.themChiTiet(new ChiTietHoaDon(sp, Integer.parseInt(ct[2].trim()), new BigDecimal(ct[3].trim())));
                            }
                        }
                    }
                    q.getDanhSachHoaDon().them(hd);
                }
            }
        }
    }

    private void ghiHoaDon(QuanLyCuaHang q) throws IOException {
        // 1. Ghi hóa đơn
        try (BufferedWriter bw = moGhi("hoadon.txt")) {
            bw.write("HOA_DON");
            bw.newLine();
            for (HoaDon hd : q.getDanhSachHoaDon().getTatCa()) {
                bw.write(hd.getMaHoaDon() + "|" + hd.getNgayLap() + "|"
                        + hd.getKhachHang().getMaKhachHang() + "|" + hd.getNhanVien().getMaNhanVien());
                bw.newLine();
            }
        }

        // 2. Ghi chi tiết hóa đơn
        try (BufferedWriter bw = moGhi("chitiethoadon.txt")) {
            bw.write("CHI_TIET_HOA_DON");
            bw.newLine();
            for (HoaDon hd : q.getDanhSachHoaDon().getTatCa()) {
                for (ChiTietHoaDon ct : hd.getChiTiet()) {
                    bw.write(hd.getMaHoaDon() + "|" + ct.getSanPham().getMaSanPham() + "|"
                            + ct.getSoLuong() + "|" + ct.getDonGiaBan().toPlainString());
                    bw.newLine();
                }
            }
        }
    }

    //phiếu nhập và chi tiết phiếu nhập
    private void docPhieuNhap(QuanLyCuaHang q) throws IOException {
        // Đọc trước các chi tiết phiếu nhập vào danh sách tạm
        java.util.ArrayList<String> dsChiTiet = new java.util.ArrayList<>();
        try (BufferedReader br = moDoc("chitietphieunhap.txt")) {
            if (br != null) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.replace("\uFEFF", "").trim();
                    if (!line.isEmpty() && !line.equals("CHI_TIET_PHIEU_NHAP")) {
                        dsChiTiet.add(line);
                    }
                }
            }
        }

        // Đọc từng phiếu nhập, gắn chi tiết vào trước rồi mới thêm vào danh sách
        try (BufferedReader br = moDoc("phieunhap.txt")) {
            if (br == null) return;
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replace("\uFEFF", "").trim();
                if (line.isEmpty() || line.equals("PHIEU_NHAP")) continue;
                String[] f = line.split("\\|");
                if (f.length >= 4) {
                    NhaCungCap ncc = q.getDanhSachNhaCungCap().timTheoMa(f[2].trim());
                    NhanVien nv = q.getDanhSachNhanVien().timTheoMa(f[3].trim());
                    PhieuNhap pn = new PhieuNhap(f[0].trim(), LocalDate.parse(f[1].trim()), ncc, nv);

                    for (String ctLine : dsChiTiet) {
                        String[] ct = ctLine.split("\\|");
                        if (ct.length >= 4 && ct[0].trim().equalsIgnoreCase(pn.getMaPhieuNhap())) {
                            SanPham sp = q.getDanhSachSanPham().timTheoMa(ct[1].trim());
                            if (sp != null) {
                                pn.themChiTiet(new ChiTietPhieuNhap(sp, Integer.parseInt(ct[2].trim()), new BigDecimal(ct[3].trim())));
                            }
                        }
                    }
                    q.getDanhSachPhieuNhap().them(pn);
                }
            }
        }
    }

    private void ghiPhieuNhap(QuanLyCuaHang q) throws IOException {
        // 1. Ghi phiếu nhập
        try (BufferedWriter bw = moGhi("phieunhap.txt")) {
            bw.write("PHIEU_NHAP");
            bw.newLine();
            for (PhieuNhap pn : q.getDanhSachPhieuNhap().getTatCa()) {
                bw.write(pn.getMaPhieuNhap() + "|" + pn.getNgayLap() + "|"
                        + pn.getNhaCungCap().getMaNhaCungCap() + "|" + pn.getNhanVien().getMaNhanVien());
                bw.newLine();
            }
        }

        // 2. Ghi chi tiết phiếu nhập
        try (BufferedWriter bw = moGhi("chitietphieunhap.txt")) {
            bw.write("CHI_TIET_PHIEU_NHAP");
            bw.newLine();
            for (PhieuNhap pn : q.getDanhSachPhieuNhap().getTatCa()) {
                for (ChiTietPhieuNhap ct : pn.getChiTiet()) {
                    bw.write(pn.getMaPhieuNhap() + "|" + ct.getSanPham().getMaSanPham() + "|"
                            + ct.getSoLuong() + "|" + ct.getDonGiaNhap().toPlainString());
                    bw.newLine();
                }
            }
        }
    }
}
