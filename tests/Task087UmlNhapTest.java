import java.lang.reflect.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.nio.file.Path;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import models.*;
import services.*;
public class Task087UmlNhapTest {
    public static void main(String[] args) throws Exception {
        var factory=DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true);
        var doc=factory.newDocumentBuilder().parse(Path.of("artifacts/Sport_Store.drawio.xml").toFile());
        var page=(org.w3c.dom.Element) doc.getElementsByTagName("diagram").item(0);
        assert page.getAttribute("name").equals("01 - Tổng quan (20 class)");
        var hdUml=nhom(page,"+PhieuNhap(");
        var ctUml=nhom(page,"+ChiTietPhieuNhap(");
        var dsUml=nhom(page,"+DanhSachPhieuNhap(");
        var khoUml=nhom(page,"+DichVuKho(");
        var fileUml=nhom(page,"+LuuTruFileText(");
        for (Class<?> type : List.of(PhieuNhap.class, ChiTietPhieuNhap.class, DanhSachPhieuNhap.class, DichVuKho.class)) {
            var uml = type==PhieuNhap.class ? hdUml : type==ChiTietPhieuNhap.class ? ctUml : type==DanhSachPhieuNhap.class ? dsUml : khoUml;
            for (Field field : type.getDeclaredFields()) {
                String name=field.getGenericType().getTypeName().replace("java.util.", "").replace("java.time.", "")
                        .replace("java.math.", "").replace("java.lang.", "").replace("models.", "").replace("services.", "");
                assert uml.contains("-" + name + " " + field.getName()) : field;
                assert Modifier.isPrivate(field.getModifiers());
            }
        }
        api(hdUml,"+tinhTongTienNhap() BigDecimal",PhieuNhap.class,"tinhTongTienNhap",BigDecimal.class);
        api(hdUml,"+getDanhSachChiTiet() List<ChiTietPhieuNhap>",PhieuNhap.class,"getDanhSachChiTiet",List.class);
        api(hdUml,"+getNgayNhap() LocalDate",PhieuNhap.class,"getNgayNhap",LocalDate.class);
        api(hdUml,"+coTheChinhSua() boolean",PhieuNhap.class,"coTheChinhSua",boolean.class);
        api(hdUml,"+themChiTiet(ChiTietPhieuNhap ct) boolean",PhieuNhap.class,"themChiTiet",boolean.class,ChiTietPhieuNhap.class);
        api(hdUml,"+xoaChiTiet(String maSP) boolean",PhieuNhap.class,"xoaChiTiet",boolean.class,String.class);
        api(ctUml,"+thanhTienNhap() BigDecimal",ChiTietPhieuNhap.class,"thanhTienNhap",BigDecimal.class);
        api(dsUml,"+tongGiaTriNhap(LocalDate tuNgay, LocalDate denNgay) BigDecimal",DanhSachPhieuNhap.class,"tongGiaTriNhap",BigDecimal.class,LocalDate.class,LocalDate.class);
        api(khoUml,"+xacNhanNhapHang(PhieuNhap pn) void",DichVuKho.class,"xacNhanNhapHang",void.class,PhieuNhap.class);
        api(fileUml,"+ghiPhieuNhap(String path, DanhSachPhieuNhap ds) void",LuuTruFileText.class,"ghiPhieuNhap",void.class,String.class,DanhSachPhieuNhap.class);
        api(fileUml,"+docPhieuNhap(String path, DanhSachSanPham dsSP, DanhSachNhaCungCap dsNCC, List<NhanVien> dsNV) DanhSachPhieuNhap",LuuTruFileText.class,"docPhieuNhap",DanhSachPhieuNhap.class,String.class,DanhSachSanPham.class,DanhSachNhaCungCap.class,List.class);
        String xml=java.nio.file.Files.readString(Path.of("artifacts/Sport_Store.drawio.xml"));
        assert xml.contains("01 - Tổng quan (20 class)");
        PhieuNhap.class.getConstructor(String.class,LocalDate.class,NhaCungCap.class,NhanVien.class);
        ChiTietPhieuNhap.class.getConstructor(SanPham.class,int.class,BigDecimal.class);
        for(String name:new String[]{"getMaPhieuNhap","getNgayNhap","getTrangThai","getDanhSachChiTiet","tinhTongTienNhap","coTheChinhSua","taoMaTuDong"}) PhieuNhap.class.getMethod(name);
        assert Modifier.isStatic(PhieuNhap.class.getDeclaredField("soThuTuTiepTheo").getModifiers());
        assert Modifier.isStatic(PhieuNhap.class.getMethod("taoMaTuDong").getModifiers());
        int visibility=PhieuNhap.class.getDeclaredMethod("danhDauDaNhapKho").getModifiers();
        assert !Modifier.isPublic(visibility) && !Modifier.isPrivate(visibility) && !Modifier.isProtected(visibility);
        assert PhieuNhap.class.getDeclaredFields().length==7;
        assert ChiTietPhieuNhap.class.getDeclaredFields().length==3;
        for(Field f:PhieuNhap.class.getDeclaredFields()) assert f.getType()!=double.class && f.getType()!=java.util.Date.class;
        assert PhieuNhap.class.getDeclaredField("danhSachChiTiet").getGenericType().getTypeName().equals("java.util.List<models.ChiTietPhieuNhap>");
        assert DanhSachPhieuNhap.class.getDeclaredField("ds").getGenericType().getTypeName().equals("java.util.List<models.PhieuNhap>");
        PhieuNhap.class.getMethod("themChiTiet",ChiTietPhieuNhap.class);
        PhieuNhap.class.getMethod("xoaChiTiet",String.class);
        for(String n:new String[]{"getSanPham","getSoLuong","getDonGiaNhap","thanhTienNhap"}) ChiTietPhieuNhap.class.getMethod(n);
        DanhSachPhieuNhap.class.getMethod("them",PhieuNhap.class);
        DanhSachPhieuNhap.class.getMethod("timTheoMa",String.class);
        DanhSachPhieuNhap.class.getMethod("timTheoNhaCungCap",String.class);
        DanhSachPhieuNhap.class.getMethod("locTheoNgay",LocalDate.class,LocalDate.class);
        DanhSachPhieuNhap.class.getMethod("locTheoTrangThai",TrangThaiPhieuNhap.class);
        DanhSachPhieuNhap.class.getMethod("tongGiaTriNhap",LocalDate.class,LocalDate.class);
        DichVuKho.class.getConstructor(DanhSachSanPham.class);
        DichVuKho.class.getMethod("kiemTraTonKho",HoaDon.class);
        DichVuKho.class.getMethod("xacNhanNhapHang",PhieuNhap.class);
        DichVuKho.class.getMethod("xacNhanNhapHang",PhieuNhap.class);
        LuuTruFileText.class.getMethod("ghiPhieuNhap",String.class,DanhSachPhieuNhap.class);
        LuuTruFileText.class.getMethod("docPhieuNhap",String.class,DanhSachSanPham.class,DanhSachNhaCungCap.class,List.class);
        assert TrangThaiPhieuNhap.values().length==2;
        System.out.println("Task087UmlNhapTest OK");
    }
    private static java.util.Set<String> nhom(org.w3c.dom.Element page, String ctor) {
        var cells=page.getElementsByTagName("mxCell");
        String parent=null;
        for(int i=0;i<cells.getLength();i++) {
            var c=(org.w3c.dom.Element) cells.item(i);
            if(c.getAttribute("value").startsWith(ctor)) parent=c.getAttribute("parent");
        }
        assert parent!=null : ctor;
        var result=new java.util.HashSet<String>();
        for(int i=0;i<cells.getLength();i++) {
            var c=(org.w3c.dom.Element) cells.item(i);
            if(c.getAttribute("parent").equals(parent)) result.add(c.getAttribute("value"));
        }
        return result;
    }
    private static void api(java.util.Set<String> uml,String signature,Class<?> owner,String name,Class<?> result,Class<?>... args) throws Exception {
        assert uml.contains(signature) : signature;
        assert owner.getMethod(name,args).getReturnType()==result;
    }

}
