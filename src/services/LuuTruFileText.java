package services;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import models.*;

/** Một snapshot chứa cả chứng từ và tồn: không có tình trạng lưu lệch hai file. */
public final class LuuTruFileText {
    public static final String TEN_FILE = "sport-store.txt";
    public static final String TIEU_DE = "SPORT_STORE_V2";
    private final Path thuMuc;

    public LuuTruFileText() {
        this("data");
    }

    public LuuTruFileText(String thuMuc) {
        this.thuMuc = Path.of(KiemTra.batBuoc(thuMuc, "Thu muc")).toAbsolutePath().normalize();
    }

    public QuanLyCuaHang docDuLieu() throws IOException {
        Path tep = thuMuc.resolve(TEN_FILE);
        if (!Files.exists(tep)) {
            return new NhapDuLieuCu(thuMuc).doc();
        }
        String[][] dong = FileText.doc(tep, TIEU_DE);
        QuanLyCuaHang moi = new QuanLyCuaHang();
        docDanhMucVaDoiTac(moi, dong, tep);
        docSanPham(moi, dong, tep);
        docChungTu(moi, dong, tep);
        return moi;
    }

    public void ghiDuLieu(QuanLyCuaHang cuaHang) throws IOException {
        KiemTra.khacNull(cuaHang, "Cua hang");
        List<String> dong = new ArrayList<>();
        dong.add(TIEU_DE);
        ghiDanhMucVaDoiTac(cuaHang, dong);
        for (SanPham sp : cuaHang.getSanPham().getTatCa()) {
            dong.add(BoMaSanPham.ghi(sp));
        }
        ghiChungTu(cuaHang, dong);
        FileText.ghi(thuMuc.resolve(TEN_FILE), dong);
    }

    private void ghiDanhMucVaDoiTac(QuanLyCuaHang ql, List<String> dong) {
        for (LoaiSP loai : ql.getLoaiSP().getTatCa()) {
            dong.add(FileText.dong("LOAI", loai.getMaLoaiSP(), loai.getTenLoaiSP(), loai.getMoTa()));
        }
        for (KhachHang kh : ql.getKhachHang().getTatCa()) {
            dong.add(FileText.dong("KH", kh.getMaKhachHang(), kh.getHoTen(), kh.getSoDienThoai(), kh.getDiaChi()));
        }
        for (NhanVien nv : ql.getNhanVien().getTatCa()) {
            dong.add(FileText.dong("NV", nv.getMaNhanVien(), nv.getHoTen(), nv.getSoDienThoai(), nv.getCaLamViec()));
        }
        for (NhaCungCap ncc : ql.getNhaCungCap().getTatCa()) {
            dong.add(FileText.dong("NCC", ncc.getMaNhaCungCap(), ncc.getTenNhaCungCap(), ncc.getSoDienThoai(), ncc.getDiaChi()));
        }
    }

    private void ghiChungTu(QuanLyCuaHang ql, List<String> dong) {
        for (HoaDon hd : ql.getHoaDon().getTatCa()) {
            dong.add(FileText.dong("HD", hd.getMaHoaDon(), hd.getNgayLap().toString(),
                    hd.getKhachHang().getMaKhachHang(), hd.getNhanVien().getMaNhanVien()));
            for (ChiTietHoaDon ct : hd.getChiTiet()) {
                dong.add(FileText.dong("CTHD", hd.getMaHoaDon(), ct.getSanPham().getMaSanPham(),
                        Integer.toString(ct.getSoLuong()), ct.getDonGiaBan().toPlainString()));
            }
        }
        for (PhieuNhap pn : ql.getPhieuNhap().getTatCa()) {
            dong.add(FileText.dong("PN", pn.getMaPhieuNhap(), pn.getNgayLap().toString(),
                    pn.getNhaCungCap().getMaNhaCungCap(), pn.getNhanVien().getMaNhanVien()));
            for (ChiTietPhieuNhap ct : pn.getChiTiet()) {
                dong.add(FileText.dong("CTPN", pn.getMaPhieuNhap(), ct.getSanPham().getMaSanPham(),
                        Integer.toString(ct.getSoLuong()), ct.getDonGiaNhap().toPlainString()));
            }
        }
    }

    private void docDanhMucVaDoiTac(QuanLyCuaHang ql, String[][] dong, Path tep) throws IOException {
        for (int i = 0; i < dong.length; i++) {
            String[] cot = dong[i];
            try {
                docDoiTuong(ql, cot);
            } catch (RuntimeException e) {
                throw FileText.loiDong(tep, i + 2, e);
            }
        }
    }

    private void docDoiTuong(QuanLyCuaHang ql, String[] cot) {
        switch (cot[0]) {
            case "LOAI":
                FileText.soCot(cot, 4);
                ql.themLoaiSP(new LoaiSP(cot[1], cot[2], cot[3]));
                break;
            case "KH":
                FileText.soCot(cot, 5);
                ql.themKhachHang(new KhachHang(cot[1], cot[2], cot[3], cot[4]));
                break;
            case "NV":
                FileText.soCot(cot, 5);
                ql.themNhanVien(new NhanVien(cot[1], cot[2], cot[3], cot[4]));
                break;
            case "NCC":
                FileText.soCot(cot, 5);
                ql.themNhaCungCap(new NhaCungCap(cot[1], cot[2], cot[3], cot[4]));
                break;
            case "SP", "HD", "PN", "CTHD", "CTPN":
                break;
            default:
                throw new IllegalArgumentException("Loai dong khong hop le: " + cot[0]);
        }
    }

    private void docSanPham(QuanLyCuaHang ql, String[][] dong, Path tep) throws IOException {
        for (int i = 0; i < dong.length; i++) {
            if (!dong[i][0].equals("SP")) {
                continue;
            }
            try {
                ql.khoiPhucSanPham(BoMaSanPham.doc(dong[i], ql));
            } catch (RuntimeException e) {
                throw FileText.loiDong(tep, i + 2, e);
            }
        }
    }

    private void docChungTu(QuanLyCuaHang ql, String[][] dong, Path tep) throws IOException {
        DanhSachHoaDon hoaDon = new DanhSachHoaDon();
        DanhSachPhieuNhap phieuNhap = new DanhSachPhieuNhap();
        docDauChungTu(ql, hoaDon, phieuNhap, dong, tep);
        docChiTiet(ql, hoaDon, phieuNhap, dong, tep);
        for (HoaDon hd : hoaDon.getTatCa()) {
            ql.khoiPhucHoaDon(hd);
        }
        for (PhieuNhap pn : phieuNhap.getTatCa()) {
            ql.khoiPhucPhieuNhap(pn);
        }
    }

    private void docDauChungTu(QuanLyCuaHang ql, DanhSachHoaDon hd, DanhSachPhieuNhap pn,
            String[][] dong, Path tep) throws IOException {
        for (int i = 0; i < dong.length; i++) {
            String[] cot = dong[i];
            try {
                if (cot[0].equals("HD")) {
                    FileText.soCot(cot, 5);
                    hd.them(new HoaDon(cot[1], LocalDate.parse(cot[2]),
                            ql.getKhachHang().timTheoMa(cot[3]), ql.getNhanVien().timTheoMa(cot[4])));
                } else if (cot[0].equals("PN")) {
                    FileText.soCot(cot, 5);
                    pn.them(new PhieuNhap(cot[1], LocalDate.parse(cot[2]),
                            ql.getNhaCungCap().timTheoMa(cot[3]), ql.getNhanVien().timTheoMa(cot[4])));
                }
            } catch (RuntimeException e) {
                throw FileText.loiDong(tep, i + 2, e);
            }
        }
    }

    private void docChiTiet(QuanLyCuaHang ql, DanhSachHoaDon hd, DanhSachPhieuNhap pn,
            String[][] dong, Path tep) throws IOException {
        for (int i = 0; i < dong.length; i++) {
            String[] cot = dong[i];
            try {
                if (cot[0].equals("CTHD")) {
                    docChiTietHoaDon(ql, hd, cot);
                } else if (cot[0].equals("CTPN")) {
                    docChiTietPhieuNhap(ql, pn, cot);
                }
            } catch (RuntimeException e) {
                throw FileText.loiDong(tep, i + 2, e);
            }
        }
    }

    private void docChiTietHoaDon(QuanLyCuaHang ql, DanhSachHoaDon danhSach, String[] cot) {
        FileText.soCot(cot, 5);
        HoaDon hd = KiemTra.khacNull(danhSach.timTheoMa(cot[1]), "Hoa don cua chi tiet");
        hd.themChiTiet(new ChiTietHoaDon(ql.getSanPham().timTheoMa(cot[2]),
                Integer.parseInt(cot[3]), new BigDecimal(cot[4])));
        danhSach.sua(cot[1], hd);
    }

    private void docChiTietPhieuNhap(QuanLyCuaHang ql, DanhSachPhieuNhap danhSach, String[] cot) {
        FileText.soCot(cot, 5);
        PhieuNhap pn = KiemTra.khacNull(danhSach.timTheoMa(cot[1]), "Phieu nhap cua chi tiet");
        pn.themChiTiet(new ChiTietPhieuNhap(ql.getSanPham().timTheoMa(cot[2]),
                Integer.parseInt(cot[3]), new BigDecimal(cot[4])));
        danhSach.sua(cot[1], pn);
    }
}
