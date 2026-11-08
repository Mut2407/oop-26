import java.lang.reflect.*;
import java.nio.file.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import javax.xml.parsers.DocumentBuilderFactory;
import services.*;
public class Task093UmlBaoCaoTest {
    public static void main(String[] args) throws Exception {
        var factory=DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true);
        var doc=factory.newDocumentBuilder().parse(Path.of("artifacts/Sport_Store.drawio.xml").toFile());
        var page=(org.w3c.dom.Element)doc.getElementsByTagName("diagram").item(0);
        var cells=page.getElementsByTagName("mxCell");
        String parent=null;
        for(int i=0;i<cells.getLength();i++) {
            var c=(org.w3c.dom.Element)cells.item(i);
            if(c.getAttribute("value").startsWith("+BaoCaoThongKe(")) parent=c.getAttribute("parent");
        }
        assert parent!=null;
        Set<String> values=new HashSet<>();
        for(int i=0;i<cells.getLength();i++) {
            var c=(org.w3c.dom.Element)cells.item(i);
            if(c.getAttribute("parent").equals(parent)) values.add(c.getAttribute("value"));
        }
        assert BaoCaoThongKe.class.getDeclaredFields().length==3;
        for(Field f:BaoCaoThongKe.class.getDeclaredFields()) {
            assert Modifier.isPrivate(f.getModifiers());
            assert values.contains("-"+f.getType().getSimpleName()+" "+f.getName());
        }
        BaoCaoThongKe.class.getConstructor(DanhSachSanPham.class,DanhSachHoaDon.class,DanhSachPhieuNhap.class);
        check(values,"+doanhThuTheoKhoang(LocalDate tuNgay, LocalDate denNgay) BigDecimal","doanhThuTheoKhoang",BigDecimal.class,LocalDate.class,LocalDate.class);
        check(values,"+giaTriNhapTheoKhoang(LocalDate tuNgay, LocalDate denNgay) BigDecimal","giaTriNhapTheoKhoang",BigDecimal.class,LocalDate.class,LocalDate.class);
        check(values,"+sanPhamBanChay(int topN) List<SanPham>","sanPhamBanChay",List.class,int.class);
        check(values,"+canhBaoTonKho(int nguong) List<SanPham>","canhBaoTonKho",List.class,int.class);
        check(values,"+giaTriTonKhoTheoGiaBan() BigDecimal","giaTriTonKhoTheoGiaBan",BigDecimal.class);
        System.out.println("Task093UmlBaoCaoTest OK");
    }
    static void check(Set<String> uml,String signature,String name,Class<?> result,Class<?>... args) throws Exception {
        assert uml.contains(signature):signature;
        assert BaoCaoThongKe.class.getMethod(name,args).getReturnType()==result;
    }
}
