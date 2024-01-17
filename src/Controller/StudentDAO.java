/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Helper.JdbcHelper;
import Model.Student;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Quang
 */
public class StudentDAO {

    public void insert(Student model) throws SQLException {
        String sql = "insert into hocvien values (?, ?, ?);";
        JdbcHelper.executeUpdate(sql, model.getMaKH(), model.getMaNH(), model.getDiem());
    }

    public void update(Student model) throws SQLException {
        String sql = "update hocvien set makhoahoc = ?, manguoihoc = ?, diemtrungbinh = ? where mahocvien = ?;";
        JdbcHelper.executeUpdate(sql, model.getMaKH(), model.getMaNH(), model.getDiem(), model.getMaHV());
    }

    public void delete(Integer maHV) throws SQLException {
        String sql = "delete from hocvien where mahocvien = ?;";
        JdbcHelper.executeUpdate(sql, maHV);
    }

    private Student readFromResultSet(ResultSet rs) throws SQLException {
        Student model = new Student();

        model.setMaHV(rs.getInt("mahocvien"));
        model.setMaKH(rs.getInt("makhoahoc"));
        model.setMaNH(rs.getString("manguoihoc"));
        model.setDiem(rs.getDouble("diemtrungbinh"));

        return model;
    }

    private List<Student> select(String sql, Object... args) {
        List<Student> list = new ArrayList<>();

        try {
            ResultSet rs = null;

            try {
                rs = JdbcHelper.executeQuery(sql, args);

                while (rs.next()) {
                    Student model = readFromResultSet(rs);
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
    
    public List<Student> select() {
        String sql = "select * from hocvien;";
        
        return select(sql);
    }
    
    public Student findById(Integer maHV) {
        String sql = "select * from hocvien where mahocvien like ?;";
        List<Student> list = select(sql, maHV);
        
        return list.size() > 0 ? list.get(0) : null;
    }
    
    public List<Student> findByKhoaHoc(int maKH) {
        String sql = "select * from hocvien where makhoahoc = ?;";
        
        return this.select(sql, maKH);
    }
}
