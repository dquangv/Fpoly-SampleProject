/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Helper.JdbcHelper;
import Model.Course;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Quang
 */
public class CourseDAO {

    public void insert(Course model) throws SQLException {
        String sql = "insert into khoahoc values (?, ?, ?, ?, ?, ?);";
        JdbcHelper.executeUpdate(sql, model.getMaCD(), model.getHocPhi(), model.getThoiLuong(), model.getNgayKG(), model.getGhiChu(), model.getMaNV());
    }

    public void update(Course model) throws SQLException {
        String sql = "update khoahoc set machuyende = ?, hocphi = ?, thoiluong = ?, ngaykhaigiang = ?, ghichu = ?, manhanvien = ? where makhoahoc = ?;";
        JdbcHelper.executeUpdate(sql, model.getMaCD(), model.getHocPhi(), model.getThoiLuong(), model.getNgayKG(), model.getGhiChu(), model.getMaNV(), model.getMaKH());
    }

    public void delete(int maKH) throws SQLException {
        String sql = "delete from khoahoc where makhoahoc = ?;";
        JdbcHelper.executeUpdate(sql, maKH);
    }

    private Course readFromResultSet(ResultSet rs) throws SQLException {
        Course model = new Course();

        model.setMaKH(rs.getInt("makhoahoc"));
        model.setMaCD(rs.getString("machuyende"));
        model.setHocPhi(rs.getDouble("hocphi"));
        model.setThoiLuong(rs.getInt("thoiluong"));
        model.setNgayKG(rs.getDate("ngaykhaigiang"));
        model.setGhiChu(rs.getString("ghichu"));
        model.setMaNV(rs.getString("manhanvien"));
        model.setNgayTao(rs.getDate("ngaytao"));

        return model;
    }

    private List<Course> select(String sql, Object... args) {
        List<Course> list = new ArrayList<>();

        try {
            ResultSet rs = null;

            try {
                rs = JdbcHelper.executeQuery(sql, args);

                while (rs.next()) {
                    Course model = readFromResultSet(rs);
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

    public List<Course> select() {
        String sql = "select * from khoahoc";
        
        return select(sql);
    }
    
    public Course findById(Integer maKH) {
        String sql = "select * from khoahoc where makhoahoc = ?;";
        List<Course> list = select(sql, maKH);
        
        return list.size() > 0 ? list.get(0) : null;
    }
}
