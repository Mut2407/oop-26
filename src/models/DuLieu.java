package models;

public class DuLieu {
    public static SanPham[] taoDanhSachSanPham() {
        return new SanPham[] {
            new DungCuTheThao(
                "SP01", "Vot cau long", 350000, "Yonex",
                10, "DC", "cau long", "Carbon", 0.085
            ),
            new DungCuTheThao(
                "SP02", "Bong da", 250000, "Dong Luc",
                20, "DC", "Bong da", "Da tong hop", 0.45
            )
        };
    }
}
