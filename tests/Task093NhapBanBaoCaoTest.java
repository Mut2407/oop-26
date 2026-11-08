import java.nio.file.*;
import java.math.BigDecimal;
import java.util.*;
import models.*;
import services.*;
public class Task093NhapBanBaoCaoTest {
    public static void main(String[] args) throws Exception {
        Path dir=Files.createTempDirectory("full-flow-");
        try {
            Path data=Files.createDirectory(dir.resolve("data"));
            try(var ps=Files.list(Path.of("data"))) {
                for(Path p:ps.filter(Files::isRegularFile).toList()) Files.copy(p,data.resolve(p.getFileName()));
            }
            DuLieu.khoiTaoDanhMuc();
            LuuTruFileText store=new LuuTruFileText(data.toString());
            var sp=store.docSanPham("sanpham.txt",DuLieu.getDanhMuc());
            SanPham a=sp.timTheoMa("SP01");
            if(a.getSoLuongTon()>10) a.giamTon(a.getSoLuongTon()-10);
            else if(a.getSoLuongTon()<10) a.tangTon(10-a.getSoLuongTon());
            sp.suaTheoMa("SP01",a); store.ghiSanPham("sanpham.txt",sp);
            String out=ConsoleTestSupport.run(dir,ConsoleTestSupport.lines(
                "7","2","PN_FLOW","NCC01","NV01","2026-11-08","4","PN_FLOW","SP01","5","2",
                "10","7","PN_FLOW","Y","7","PN_FLOW","Y","11","0",
                "6","2","HD_FLOW","KH01","NV01","2026-11-08","4","HD_FLOW","SP01","3","4",
                "7","HD_FLOW","Y","11","0",
                "8","1","2026-11-08","2026-11-08","2","2026-11-08","2026-11-08","3","5","4","12","5","0","0"),0);
            for(String key:new String[]{"Da xac nhan va luu nhap kho.","Da xac nhan va luu ban hang.","Doanh thu da xac nhan: 12","Gia tri da nhap kho: 10","Da ban: 3","Ton: 12"}) assert out.contains(key):out;
            sp=store.docSanPham("sanpham.txt",DuLieu.getDanhMuc());
            assert sp.timTheoMa("SP01").getSoLuongTon()==12;
            var kh=store.docKhachHang("khachhang.txt");
            var nv=store.docNhanVien("nhanvien.txt");
            var ncc=store.docNhaCungCap("nhacungcap.txt");
            var hd=store.docHoaDon("hoadon.txt",sp,kh,nv);
            var pn=store.docPhieuNhap("phieunhap.txt",sp,ncc,nv);
            var bc=new BaoCaoThongKe(sp,hd,pn);
            BigDecimal value=bc.giaTriTonKhoTheoGiaBan();
            // Changing today's price affects stock valuation, never historic document prices.
            var changed=sp.timTheoMa("SP01"); changed.capNhatGiaBan(changed.getGiaBan().add(BigDecimal.ONE));
            sp.suaTheoMa("SP01",changed);
            assert bc.giaTriTonKhoTheoGiaBan().subtract(value).compareTo(BigDecimal.valueOf(12))==0;
            assert hd.timTheoMa("HD_FLOW").tinhTongTien().compareTo(BigDecimal.valueOf(12))==0;
            assert pn.timTheoMa("PN_FLOW").tinhTongTienNhap().compareTo(BigDecimal.TEN)==0;
            Map<Path,byte[]> before=new HashMap<>();
            try(var ps=Files.list(data)) { for(Path p:ps.filter(Files::isRegularFile).toList()) before.put(p,Files.readAllBytes(p)); }
            out=ConsoleTestSupport.run(dir,"7\n3\nPN_FLOW\n11\n0\n6\n3\nHD_FLOW\n11\n0\n8\n1\n2026-11-08\n2026-11-08\n2\n2026-11-08\n2026-11-08\n0\n0\n",0);
            assert out.contains("DA_NHAP_KHO") && out.contains("DA_XAC_NHAN"):out;
            for(var entry:before.entrySet()) assert Arrays.equals(entry.getValue(),Files.readAllBytes(entry.getKey()));
            out=ConsoleTestSupport.run(dir,"5\n4\nNCC01\n0\n4\n4\nNV01\n0\n1\n6\nSP01\n0\n0\n",0);
            assert out.contains("phieu nhap tham chieu"):out;
            assert store.docNhaCungCap("nhacungcap.txt").timTheoMa("NCC01")!=null;
            Files.writeString(data.resolve("phieunhap.txt"),"WRONG\n");
            out=ConsoleTestSupport.run(dir,"0\n",1);
            assert out.contains("Khong the khoi dong"):out;
            assert Files.readString(data.resolve("phieunhap.txt")).equals("WRONG\n");
            System.out.println("Task093NhapBanBaoCaoTest OK (10+5-3=12, reports 12/10, restart/read-only, snapshots, references, bad file)");
        } finally { ConsoleTestSupport.deleteTree(dir); }
    }
}
