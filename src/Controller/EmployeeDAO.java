/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Helper.JdbcHelper;
import Model.Employee;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Quang
 */
public class EmployeeDAO {

    public void insert(Employee model) throws SQLException {
        String sql = "insert into nhanvien values (?, ?, ?, ?);";
        JdbcHelper.executeUpdate(sql, model.getMaNV(), model.getMatKhau(), model.getHoTen(), model.getVaiTro());
    }

    public void update(Employee model) throws SQLException {
        String sql = "update nhanvien set matkhau = ?, hovaten = ?, vaitro = ? where manhanvien = ?;";
        JdbcHelper.executeUpdate(sql, model.getMatKhau(), model.getHoTen(), model.getVaiTro(), model.getMaNV());
    }

    public void delete(String maNV) throws SQLException {
        String sql = "delete from nhanvien where manhanvien = ?;";
        JdbcHelper.executeUpdate(sql, maNV);
    }

    private Employee readFromResultSet(ResultSet rs) throws SQLException {
        Employee model = new Employee();

        model.setMaNV(rs.getString("manhanvien"));
        model.setMatKhau(rs.getString("matkhau"));
        model.setHoTen(rs.getString("hovaten"));
        model.setVaiTro(rs.getBoolean("vaitro"));

        return model;
    }

    private List<Employee> select(String sql, Object... args) {
        List<Employee> list = new ArrayList<>();

        try {
            ResultSet rs = null;

            try {
                rs = JdbcHelper.executeQuery(sql, args);

                while (rs.next()) {
                    Employee model = readFromResultSet(rs);
                    list.add(model);
                }
            } finally {
                rs.getStatement().getConnection().close();
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return list;
    }

    public List<Employee> select() {
        String sql = "select * from nhanvien";
        return select(sql);
    }
    
    public Employee findById(String maNV) {
        String sql = "select * from nhanvien where manhanvien like ?";
        List<Employee> list = select(sql, maNV);
        
        return list.size() > 0 ? list.get(0) : null;
    }
    
    public String selectPasswordByEmail(String email) {
        String sql = "select matkhau from nhanvien where email = ?;";
        
        try {
            ResultSet rs = JdbcHelper.executeQuery(sql, email);
            
            if (rs.next()) {
                return rs.getString("matkhau");
            }
            
            rs.getStatement().getConnection().close();
            
            return "Email này đã không được đăng ký trong hệ thống";
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
