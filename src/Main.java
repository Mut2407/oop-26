import services.QuanLyCuaHang;

public class Main {
    public Main() {
    }

    public static void main(String[] args) {
        String thuMuc = args.length == 0 ? "data" : args[0];
        QuanLyCuaHang quanLy = new QuanLyCuaHang(thuMuc);
        quanLy.chay();
    }
}
