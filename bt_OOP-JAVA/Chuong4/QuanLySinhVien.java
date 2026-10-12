import java.util.Scanner;
public class QuanLySinhVien {
    public static void main(String[] args) {
        int n;
        Scanner sc = new Scanner(System.in);
        System.out.println("Nhap so luong sinh vien: ");
        n = sc.nextInt();
        SinhVien[] sv = new SinhVien[n];
        for(int i = 0; i < n; i++){
            int chon;
            System.out.println("Nhap loai sinh vien(1. Sinh vien Chinh Quy, 2. Sinh vien Lien Thong): ");
            chon = sc.nextInt();
            if(chon == 1){
                sv[i] = new SVCQ();
                sv[i].Nhap();
            }else{
                sv[i] = new SVLT();
                sv[i].Nhap();
            }
        }   
        for(int i = 0; i < n; i++){
            sv[i].Xuat();
        }
    }
}
class SinhVien {
    String mssv;
    String Ho;
    String Ten;
    String ngaySinh;
    String gioiTinh;

    public SinhVien() {
    }

    public SinhVien(String mssv, String Ho, String Ten, String ngaySinh, String gioiTinh) {
        this.mssv = mssv;
        this.Ho = Ho;
        this.Ten = Ten;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
    }

    public String getMssv() {
        return mssv;
    }

    public void setMssv(String mssv) {
        this.mssv = mssv;
    }

    public String getHo() {
        return Ho;
    }

    public void setHo(String Ho) {
        this.Ho = Ho;
    }

    public String getTen() {
        return Ten;
    }

    public void setTen(String Ten) {
        this.Ten = Ten;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }
    public void Nhap(){
        Scanner sc = new Scanner(System.in);
        System.out.println("===================NHAP THONG TIN SINH VIEN===============");
        System.out.println("Nhap ma so sinh vien: ");
        mssv = sc.nextLine();
        System.out.println("\nNhap ho: ");
        Ho = sc.nextLine();
        System.out.println("\nNhap ten: ");
        Ten = sc.nextLine();
        System.out.println("\nNhap ngay/thang/nam sinh: ");
        ngaySinh = sc.nextLine();
        System.out.println("\nNhap gioi tinh: ");
        gioiTinh = sc.nextLine();
    }
    public void Xuat(){
        System.out.println("===================THONG TIN SINH VIEN===============");
        System.out.println("\nMa so sinh vien: " + mssv );
        System.out.println("\nHo ten sinh vien: "+ Ho +" "+Ten );
        System.out.println("\nNgay/thang/nam sinh: "+ ngaySinh);
        System.out.println("\nGioi tinh: "+gioiTinh);
    }
}

class SVCQ extends SinhVien{
    int drl;
    public SVCQ(){
        drl = 0;
    }
    public SVCQ (String mssv, String Ho, String Ten, String ngaySinh, String gioiTinh, int drl){
        super(mssv, Ho, Ten, ngaySinh, gioiTinh);
        this.drl = drl;
    }
    @Override public void Nhap(){
        Scanner sc = new Scanner(System.in);
        super.Nhap();
        System.out.println("\nNhap diem rèn luyện: ");
        drl = sc.nextInt();
    }
    @Override public void Xuat(){
        super.Xuat();
        System.out.println("\nDiem rèn luyện: " + drl);
    }
}

class SVLT extends SinhVien{
    String nganhCaoDang;
    int namTotNghiep;
    public SVLT(){
        nganhCaoDang = "";
        namTotNghiep = 0;
    }
    public SVLT(String mssv, String Ho, String Ten, String ngaySinh, String gioiTinh, String nghanhCaoDang, int namTotNghiep){
        super(mssv, Ho, Ten, ngaySinh, gioiTinh);
        this.nganhCaoDang = nghanhCaoDang;
        this.namTotNghiep = namTotNghiep;
    }
    public String getNganhCaoDang() {
        return nganhCaoDang;
    }
    public void setNganhCaoDang(String nghanhCaoDang) {
        this.nganhCaoDang = nghanhCaoDang;
    }
    public int getNamTotNghiep() {
        return namTotNghiep;
    }
    public void setNamTotNghiep(int namTotNghiep) {
        this.namTotNghiep = namTotNghiep;
    }
    @Override public void Nhap(){
        Scanner sc = new Scanner(System.in);
        super.Nhap();
        System.out.println("\nNhap nganh cao dang: ");
        nganhCaoDang = sc.nextLine();
        System.out.println("\nNhap nam tot nghiep: ");
        namTotNghiep = sc.nextInt();
    }
    @Override public void Xuat(){
        super.Xuat();
        System.out.println("\nNganh cao dang: " + nganhCaoDang);
        System.out.println("\nNam tot nghiep: " + namTotNghiep);
    }
}