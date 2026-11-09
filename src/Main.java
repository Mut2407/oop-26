import java.io.IOException;
import java.util.Scanner;
import services.LuuTruFileText;
import ui.MenuCuaHang;

public final class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            LuuTruFileText luu = new LuuTruFileText(args.length == 0 ? "data" : args[0]);
            new MenuCuaHang(luu.docDuLieu(), scanner, luu).chay();
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Khong the doc/ghi du lieu: " + e.getMessage());
            System.exit(1);
        }
    }
}
