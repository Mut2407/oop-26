import java.io.File;
import java.nio.file.*;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import models.*;
import services.*;
public class HoaDonRecoveryTest {
    public static void main(String[] args) throws Exception {
        Path dir=Files.createTempDirectory("recovery-hd-");
        try {
            Path data=Files.createDirectory(dir.resolve("data"));
            try(var paths=Files.list(Path.of("data"))) {
                for(Path p:paths.filter(Files::isRegularFile).toList()) Files.copy(p,data.resolve(p.getFileName()));
            }
            DuLieu.khoiTaoDanhMuc();
            LuuTruFileText store=new LuuTruFileText(data.toString());
            DanhSachSanPham sp=store.docSanPham("sanpham.txt",DuLieu.getDanhMuc());
            DanhSachKhachHang kh=store.docKhachHang("khachhang.txt");
            DanhSachNhanVien nv=DanhSachNhanVien.tuDanhSach(store.docNhanVien("nhanvien.txt"));
            HoaDon h=new HoaDon("CRASH",LocalDate.of(2026,11,7),kh.timTheoMa("KH01"),nv.timTheoMa("NV01"));
            h.themChiTiet(new ChiTietHoaDon(sp.timTheoMa("SP01"),1,new BigDecimal("12.345")));
            DanhSachHoaDon hd=new DanhSachHoaDon(); hd.them(h);
            store.ghiHoaDon("hoadon.txt",hd);
            String originalSP=Files.readString(data.resolve("sanpham.txt"));
            String originalHD=Files.readString(data.resolve("hoadon.txt"));
            for(String point:new String[]{"first","second"}) {
                crash(data,point);
                assert Files.exists(data.resolve("banhang.undo"));
                String out=ConsoleTestSupport.run(dir,"6\n3\nCRASH\n0\n0\n",0);
                assert out.contains("CHO_XAC_NHAN") : out;
                assert Files.readString(data.resolve("sanpham.txt")).equals(originalSP);
                assert Files.readString(data.resolve("hoadon.txt")).equals(originalHD);
                assert !Files.exists(data.resolve("banhang.undo"));
            }
            // A broken journal must block startup without touching good records.
            Files.writeString(data.resolve("banhang.undo"),"BAN_HANG_UNDO_V1\nbroken\n");
            String out=ConsoleTestSupport.run(dir,"0\n",1);
            assert out.contains("Khong the khoi dong") : out;
            assert Files.readString(data.resolve("hoadon.txt")).equals(originalHD);
            Files.delete(data.resolve("banhang.undo"));
            // Invalid input, canceled/EOF batches and failed confirmation never persist partial drafts.
            out=ConsoleTestSupport.run(dir,"6\n2\nCANCEL\nKH01\n",0);
            assert Files.readString(data.resolve("hoadon.txt")).equals(originalHD);
            out=ConsoleTestSupport.run(dir,"6\n2\nBAD\nKH01\nNV01\nnot-a-date\n7\nCRASH\nY\n0\n0\n",0);
            assert out.contains("Khong the thuc hien") : out;
            // The successful confirmation above persisted CRASH, not the failed BAD draft.
            hd=store.docHoaDon("hoadon.txt",sp,kh,nv.getTatCa());
            assert hd.timTheoMa("BAD")==null && !hd.timTheoMa("CRASH").coTheChinhSua();
            out=ConsoleTestSupport.run(dir,"6\n2\nCHUA_LUU\nKH01\nNV01\n2026-11-07\n11\nN\n3\nCHUA_LUU\n0\n0\n",0);
            assert out.contains("CHUA_LUU") && out.contains("Thay doi chua luu") : out;
            // Confirmed records cannot disappear through a reload.
            DanhSachHoaDon existing=hd;
            try { existing.napLai(new DanhSachHoaDon()); throw new AssertionError(); } catch(IllegalArgumentException expected) { }
            System.out.println("HoaDonRecoveryTest OK (real JVM halts after each file, restart rollback, corrupt journal, EOF/input, reload protection)");
        } finally { ConsoleTestSupport.deleteTree(dir); }
    }
    private static void crash(Path data,String point) throws Exception {
        String cp=Arrays.stream(System.getProperty("java.class.path").split(File.pathSeparator))
                .map(s->Path.of(s).toAbsolutePath().toString()).collect(Collectors.joining(File.pathSeparator));
        Path log=data.getParent().resolve("crash.log");
        Process p=new ProcessBuilder(Path.of(System.getProperty("java.home"),"bin","java").toString(),"-cp",cp,
                "HoaDonCrashWorker",data.toString(),point).redirectErrorStream(true).redirectOutput(log.toFile()).start();
        try {
            if(!p.waitFor(30,TimeUnit.SECONDS)) throw new AssertionError("Crash worker timed out");
            assert p.exitValue()==(point.equals("first")?77:78) : Files.readString(log);
        } finally { if(p.isAlive()) p.destroyForcibly().waitFor(); }
    }
}
