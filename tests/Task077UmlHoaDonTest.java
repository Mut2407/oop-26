import java.lang.reflect.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.nio.file.Path;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import models.*;
import services.*;
public class Task077UmlHoaDonTest {
    public static void main(String[] args) throws Exception {
        var factory=DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true);
        var doc=factory.newDocumentBuilder().parse(Path.of("artifacts/Sport_Store.drawio.xml").toFile());
        var page=(org.w3c.dom.Element) doc.getElementsByTagName("diagram").item(0);
        assert page.getAttribute("name").equals("01 - Tổng quan (20 class)");
        var hdUml=nhom(page,"+HoaDon(");
        var ctUml=nhom(page,"+ChiTietHoaDon(");
        var dsUml=nhom(page,"+DanhSachHoaDon(");
        var khoUml=nhom(page,"+DichVuKho(");
        var fileUml=nhom(page,"+LuuTruFileText(");
        for (Class<?> type : List.of(HoaDon.class, ChiTietHoaDon.class, DanhSachHoaDon.class, DichVuKho.class)) {
            var uml = type==HoaDon.class ? hdUml : type==ChiTietHoaDon.class ? ctUml : type==DanhSachHoaDon.class ? dsUml : khoUml;
            for (Field field : type.getDeclaredFields()) {
                String name=field.getGenericType().getTypeName().replace("java.util.", "").replace("java.time.", "")
                        .replace("java.math.", "").replace("java.lang.", "").replace("models.", "").replace("services.", "");
                assert uml.contains("-" + name + " " + field.getName()) : field;
                assert Modifier.isPrivate(field.getModifiers());
            }
        }
        api(hdUml,"+tinhTongTien() BigDecimal",HoaDon.class,"tinhTongTien",BigDecimal.class);
        api(hdUml,"+getDanhSachChiTiet() List<ChiTietHoaDon>",HoaDon.class,"getDanhSachChiTiet",List.class);
        api(hdUml,"+getNgayLap() LocalDate",HoaDon.class,"getNgayLap",LocalDate.class);
        api(hdUml,"+coTheChinhSua() boolean",HoaDon.class,"coTheChinhSua",boolean.class);
        api(hdUml,"+themChiTiet(ChiTietHoaDon ct) boolean",HoaDon.class,"themChiTiet",boolean.class,ChiTietHoaDon.class);
        api(hdUml,"+xoaChiTiet(String maSP) boolean",HoaDon.class,"xoaChiTiet",boolean.class,String.class);
        api(ctUml,"+thanhTien() BigDecimal",ChiTietHoaDon.class,"thanhTien",BigDecimal.class);
        api(dsUml,"+tongDoanhThu(LocalDate tuNgay, LocalDate denNgay) BigDecimal",DanhSachHoaDon.class,"tongDoanhThu",BigDecimal.class,LocalDate.class,LocalDate.class);
        api(khoUml,"+xacNhanBanHang(HoaDon hd) void",DichVuKho.class,"xacNhanBanHang",void.class,HoaDon.class);
        api(fileUml,"+ghiHoaDon(String path, DanhSachHoaDon ds) void",LuuTruFileText.class,"ghiHoaDon",void.class,String.class,DanhSachHoaDon.class);
        api(fileUml,"+docHoaDon(String path, DanhSachSanPham dsSP, DanhSachKhachHang dsKH, List<NhanVien> dsNV) DanhSachHoaDon",LuuTruFileText.class,"docHoaDon",DanhSachHoaDon.class,String.class,DanhSachSanPham.class,DanhSachKhachHang.class,List.class);
        String xml=java.nio.file.Files.readString(Path.of("artifacts/Sport_Store.drawio.xml"));
        assert xml.contains("01 - Tổng quan (20 class)");
        HoaDon.class.getConstructor(String.class,LocalDate.class,KhachHang.class,NhanVien.class);
        ChiTietHoaDon.class.getConstructor(SanPham.class,int.class,BigDecimal.class);
        for(String name:new String[]{"getMaHoaDon","getNgayLap","getTrangThai","getDanhSachChiTiet","tinhTongTien","coTheChinhSua","taoMaTuDong"}) HoaDon.class.getMethod(name);
        assert Modifier.isStatic(HoaDon.class.getDeclaredField("soThuTuTiepTheo").getModifiers());
        assert Modifier.isStatic(HoaDon.class.getMethod("taoMaTuDong").getModifiers());
        int visibility=HoaDon.class.getDeclaredMethod("danhDauDaXacNhan").getModifiers();
        assert !Modifier.isPublic(visibility) && !Modifier.isPrivate(visibility) && !Modifier.isProtected(visibility);
        assert HoaDon.class.getDeclaredFields().length==7;
        assert ChiTietHoaDon.class.getDeclaredFields().length==3;
        for(Field f:HoaDon.class.getDeclaredFields()) assert f.getType()!=double.class && f.getType()!=java.util.Date.class;
        assert HoaDon.class.getDeclaredField("danhSachChiTiet").getGenericType().getTypeName().equals("java.util.List<models.ChiTietHoaDon>");
        assert DanhSachHoaDon.class.getDeclaredField("ds").getGenericType().getTypeName().equals("java.util.List<models.HoaDon>");
        HoaDon.class.getMethod("themChiTiet",ChiTietHoaDon.class);
        HoaDon.class.getMethod("xoaChiTiet",String.class);
        for(String n:new String[]{"getSanPham","getSoLuong","getDonGiaBan","thanhTien"}) ChiTietHoaDon.class.getMethod(n);
        DanhSachHoaDon.class.getMethod("them",HoaDon.class);
        DanhSachHoaDon.class.getMethod("timTheoMa",String.class);
        DanhSachHoaDon.class.getMethod("timTheoKhachHang",String.class);
        DanhSachHoaDon.class.getMethod("locTheoNgay",LocalDate.class,LocalDate.class);
        DanhSachHoaDon.class.getMethod("locTheoTrangThai",TrangThaiHoaDon.class);
        DanhSachHoaDon.class.getMethod("tongDoanhThu",LocalDate.class,LocalDate.class);
        DichVuKho.class.getConstructor(DanhSachSanPham.class);
        DichVuKho.class.getMethod("kiemTraTonKho",HoaDon.class);
        DichVuKho.class.getMethod("xacNhanBanHang",HoaDon.class);
        DichVuKho.class.getMethod("xacNhanNhapHang",PhieuNhap.class);
        LuuTruFileText.class.getMethod("ghiHoaDon",String.class,DanhSachHoaDon.class);
        LuuTruFileText.class.getMethod("docHoaDon",String.class,DanhSachSanPham.class,DanhSachKhachHang.class,List.class);
        assert TrangThaiHoaDon.values().length==2;
        System.out.println("Task077UmlHoaDonTest OK");
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
