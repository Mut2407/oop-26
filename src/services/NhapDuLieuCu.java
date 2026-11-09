package services;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.function.Consumer;
import models.*;

/** Chỉ nhập định dạng đang có trong repo; không sửa hoặc xóa các file gốc. */
final class NhapDuLieuCu {
    private final Path thuMuc;

    NhapDuLieuCu(Path thuMuc) {
        this.thuMuc = thuMuc;
    }

    QuanLyCuaHang doc() throws IOException {
        QuanLyCuaHang ql = new QuanLyCuaHang();
        docDoiTac(ql);
        docTep("sanpham.txt", this::kiemTraSanPham, cot -> ql.khoiPhucSanPham(sanPhamCu(cot, ql)),
                "kieu\tmaSanPham\ttenSanPham\tgiaBan\tthuongHieu\tsoLuongTon\tmaLoai\tkichCoGiay\tmauSac\tloaiDeGiay\tmonTheThao\tvatLieu\tkhoiLuongKg\tkichCoQuanAo\tmauSacChung\tchatLieuChung");
        docChungTu(ql);
        return ql;
    }

    private void docDoiTac(QuanLyCuaHang ql) throws IOException {
        docTep("danhmuc.txt", cot -> FileText.soCot(cot, 3),
                cot -> ql.themLoaiSP(new LoaiSP(cot[0], cot[1], cot[2])), "maDanhMuc\ttenDanhMuc\tmoTa");
        docTep("khachhang.txt", cot -> FileText.soCot(cot, 4),
                cot -> ql.themKhachHang(new KhachHang(cot[0], cot[1], cot[2], cot[3])),
                "maKhachHang\thoTen\tsoDienThoai\tdiaChi");
        docTep("nhanvien.txt", cot -> FileText.soCot(cot, 5),
                cot -> ql.themNhanVien(new NhanVien(cot[0], cot[1], cot[3], cot[2])),
                "maNhanVien\thoTen\tcaLamViec\tsoDienThoai\tdiaChi");
        docTep("nhacungcap.txt", cot -> FileText.soCot(cot, 4),
                cot -> ql.themNhaCungCap(new NhaCungCap(cot[0], cot[1], cot[2], cot[3])),
                "maNhaCungCap\ttenNhaCungCap\tsoDienThoai\tdiaChi");
    }

    private void docChungTu(QuanLyCuaHang ql) throws IOException {
        docTep("hoadon.txt", this::kiemTraBanNhap,
                cot -> ql.khoiPhucHoaDon(new HoaDon(cot[1], LocalDate.parse(cot[2]),
                        ql.getKhachHang().timTheoMa(cot[3]), ql.getNhanVien().timTheoMa(cot[4]))),
                "HOA_DON_V1");
        docTep("phieunhap.txt", this::kiemTraBanNhap,
                cot -> ql.khoiPhucPhieuNhap(new PhieuNhap(cot[1], LocalDate.parse(cot[2]),
                        ql.getNhaCungCap().timTheoMa(cot[3]), ql.getNhanVien().timTheoMa(cot[4]))),
                "PHIEU_NHAP_V1");
    }

    private void kiemTraBanNhap(String[] cot) {
        FileText.soCot(cot, 7);
        if (!cot[0].equals("H") || !cot[5].equals("CHO_XAC_NHAN") || !cot[6].equals("0")) {
            throw new IllegalArgumentException("Chi ho tro ban nhap V1 rong. Hay giu file goc de chuyen doi rieng.");
        }
    }

    private void kiemTraSanPham(String[] cot) {
        FileText.soCot(cot, 16);
    }

    private SanPham sanPhamCu(String[] cot, QuanLyCuaHang ql) {
        LoaiSP loai = ql.getLoaiSP().timTheoMa(cot[6]);
        BigDecimal gia = new BigDecimal(cot[3]);
        SanPham sp = switch (cot[0]) {
            case "GIAY" -> new GiayTheThao(cot[1], cot[2], cot[4], gia, loai,
                    Double.parseDouble(cot[7]), cot[8], cot[15], cot[9]);
            case "QUAN_AO" -> new QuanAoTheThao(cot[1], cot[2], cot[4], gia, loai,
                    cot[13], cot[14], cot[15]);
            case "DUNG_CU" -> new DungCuTheThao(cot[1], cot[2], cot[4], gia, loai,
                    cot[10], cot[11], Double.parseDouble(cot[12]));
            default -> throw new IllegalArgumentException("Kieu san pham cu khong hop le.");
        };
        return sp.voiTonKho(Integer.parseInt(cot[5]));
    }

    private void docTep(String ten, Consumer<String[]> kiemTra, Consumer<String[]> xuLy,
            String... tieuDe) throws IOException {
        Path tep = thuMuc.resolve(ten);
        if (!Files.exists(tep)) {
            return;
        }
        String[][] dong = FileText.doc(tep, tieuDe);
        for (int i = 0; i < dong.length; i++) {
            try {
                kiemTra.accept(dong[i]);
                xuLy.accept(dong[i]);
            } catch (RuntimeException e) {
                throw FileText.loiDong(tep, i + 2, e);
            }
        }
    }
}
