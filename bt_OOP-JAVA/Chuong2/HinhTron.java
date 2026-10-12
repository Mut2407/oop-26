import java.util.Scanner;

public class HinhTron {
    private float banKinh;
    public void Nhap(Scanner sc)  {
        System.out.print("Nhap ban kinh: ");
        banKinh = sc.nextFloat();
    }
    public void Xuat(){
        System.out.print("\nBan kinh: " + banKinh);
    }
    public void ChuVi(){
        System.out.print("\nChu vi: " + (2 * 3.14 * banKinh));
    }
    public void DienTich(){
        System.out.print("\nDien tich: " + (3.14 * banKinh * banKinh));
    }
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        HinhTron ht = new HinhTron();
        ht.Nhap(sc);
        ht.Xuat();
        ht.ChuVi();
        ht.DienTich();
    }    
}
