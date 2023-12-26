/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author Quang
 */
public class Employee {

    private String maNV, matKhau, hoTen;
    private boolean vaiTro = false;

    public Employee() {
    }

    public Employee(String maNV, String matKhau, String hoTen) {
        this.maNV = maNV;
        this.matKhau = matKhau;
        this.hoTen = hoTen;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public boolean isVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(boolean vaiTro) {
        this.vaiTro = vaiTro;
    }

    public String toString() {
        return this.hoTen;
    }

    public int getVaiTro() {
        if (this.vaiTro) {
            return 0;
        }
        return 1;
    }
}
