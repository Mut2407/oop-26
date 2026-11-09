package ui;

import java.math.BigDecimal;
import models.*;
import services.QuanLyCuaHang;

public final class NhapSanPham {
    private final QuanLyCuaHang cuaHang;
    private final NhapLieuConsole nhap;

    public NhapSanPham(QuanLyCuaHang cuaHang, NhapLieuConsole nhap) {
        this.cuaHang = cuaHang;
        this.nhap = nhap;
    }

    public SanPham nhap(String maCu) {
        SanPham cu = maCu == null ? null : cuaHang.getSanPham().timTheoMa(maCu);
        String ma = maCu == null ? nhap.ma("Ma san pham: ") : maCu;
        String ten = nhap.batBuoc("Ten san pham: ");
        String thuongHieu = nhap.batBuoc("Thuong hieu: ");
        BigDecimal gia = nhap.tien("Gia ban: ");
        LoaiSP loai = chonLoai();
        String kieu = cu == null ? nhap.docDong("Kieu (1=giay, 2=quan ao, 3=dung cu): ") : kieu(cu);
        SanPham moi = taoSanPham(kieu, ma, ten, thuongHieu, gia, loai);
        return moi.voiTonKho(cu == null ? 0 : cu.getSoLuongTon());
    }

    private LoaiSP chonLoai() {
        for (LoaiSP loai : cuaHang.getLoaiSP().getTatCa()) {
            System.out.println(loai.hienThiThongTin());
        }
        LoaiSP loai = cuaHang.getLoaiSP().timTheoMa(nhap.ma("Ma loai SP: "));
        return KiemTra.khacNull(loai, "Loai san pham chua ton tai");
    }

    private String kieu(SanPham sp) {
        if (sp instanceof GiayTheThao) {
            return "1";
        }
        return sp instanceof QuanAoTheThao ? "2" : "3";
    }

    private SanPham taoSanPham(String kieu, String ma, String ten, String hieu,
            BigDecimal gia, LoaiSP loai) {
        return switch (kieu) {
            case "1" -> new GiayTheThao(ma, ten, hieu, gia, loai,
                    nhap.soDuong("Co giay: "), nhap.batBuoc("Mau sac: "),
                    nhap.docDong("Chat lieu (co the de trong): "), nhap.batBuoc("Loai de giay: "));
            case "2" -> new QuanAoTheThao(ma, ten, hieu, gia, loai,
                    nhap.batBuoc("Co quan ao (S/M/L/XL...): "), nhap.batBuoc("Mau sac: "),
                    nhap.docDong("Chat lieu (co the de trong): "));
            case "3" -> new DungCuTheThao(ma, ten, hieu, gia, loai,
                    nhap.batBuoc("Mon the thao: "), nhap.batBuoc("Vat lieu: "), nhap.soDuong("Khoi luong kg: "));
            default -> throw new IllegalArgumentException("Kieu san pham khong hop le.");
        };
    }

    public void themNhieu() {
        int soLuong = nhap.soNguyen("So san pham can nhap ban dau/them: ", 1);
        SanPham[] moi = new SanPham[soLuong];
        for (int i = 0; i < soLuong; i++) {
            System.out.println("San pham " + (i + 1) + "/" + soLuong);
            moi[i] = nhap(null);
        }
        cuaHang.themNhieuSanPham(moi);
    }

    public void loc() {
        String chon = nhap.docDong("1. Loc theo loai SP  2. Loc khoang gia: ");
        SanPham[] ketQua = switch (chon) {
            case "1" -> cuaHang.getSanPham().locTheoLoaiSP(nhap.ma("Ma loai: "));
            case "2" -> cuaHang.getSanPham().locTheoKhoangGia(nhap.tien("Tu gia: "), nhap.tien("Den gia: "));
            default -> throw new IllegalArgumentException("Lua chon khong hop le.");
        };
        System.out.println("So ket qua: " + ketQua.length);
        for (SanPham sp : ketQua) {
            System.out.println(sp.hienThiThongTin());
        }
    }
}
