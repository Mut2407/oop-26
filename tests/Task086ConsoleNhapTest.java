import java.nio.file.*;
import services.*;
import models.*;
public class Task086ConsoleNhapTest {
    private static boolean money(String output, String label, String expected) {
        return output.lines().filter(line -> line.contains(label)).anyMatch(line -> {
            String number = line.substring(line.indexOf(label) + label.length()).trim();
            return new java.math.BigDecimal(number).compareTo(new java.math.BigDecimal(expected)) == 0;
        });
    }
    public static void main(String[] args) throws Exception {
        Path dir = Files.createTempDirectory("console-hd-");
        try {
            Files.createDirectory(dir.resolve("data"));
            try (var paths=Files.list(Path.of("data"))) {
                for (Path p:paths.filter(Files::isRegularFile).toList()) Files.copy(p,dir.resolve("data").resolve(p.getFileName()));
            }
            String output=ConsoleTestSupport.run(dir,ConsoleTestSupport.lines(
                "7","2","PNTEST","NCC01","NV01","2026-11-01",
                "4","PNTEST","SP01","2","0.10","5","PNTEST","SP01","3","0.10",
                "8","PN","NCC01","NV01","2026-11-01","2026-11-01","CHO_XAC_NHAN",
                "10","0","0"),0);
            assert output.contains("Da luu phieu nhap.") && money(output,"Tong tien: ","0.30") : output;
            DuLieu.khoiTaoDanhMuc();
            LuuTruFileText store=new LuuTruFileText(dir.resolve("data").toString());
            DanhSachSanPham sp=store.docSanPham("sanpham.txt",DuLieu.getDanhMuc());
            int before=sp.timTheoMa("SP01").getSoLuongTon();
            output=ConsoleTestSupport.run(dir,ConsoleTestSupport.lines("7","3","PNTEST","7","PNTEST","Y",
                "7","PNTEST","Y","4","PNTEST","9","2026-11-01","2026-11-01","11","3","PNTEST","0","0"),0);
            assert output.contains("Da xac nhan va luu nhap kho.") && money(output,"Gia tri nhap da xac nhan: ","0.30") : output;
            sp=store.docSanPham("sanpham.txt",DuLieu.getDanhMuc());
            assert sp.timTheoMa("SP01").getSoLuongTon()==before+3;
            output=ConsoleTestSupport.run(dir,"7\n3\nPNTEST\n0\n0\n",0);
            assert output.contains("DA_NHAP_KHO") : output;
            assert store.docSanPham("sanpham.txt",DuLieu.getDanhMuc()).timTheoMa("SP01").getSoLuongTon()==before+3;
            System.out.println("Task086ConsoleNhapTest OK");
        } finally { ConsoleTestSupport.deleteTree(dir); }
    }
}
