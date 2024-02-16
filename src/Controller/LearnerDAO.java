/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Helper.JdbcHelper;
import Model.Learner;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Quang
 */
public class LearnerDAO {

    public void insert(Learner model) throws SQLException {
        String sql = "insert into nguoihoc values (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        JdbcHelper.executeUpdate(sql, model.getMaNH(), model.getHoTen(), model.getNgaySinh(), model.getGioiTinh(), model.getDienThoai(), model.getEmail(), model.getGhiChu(), model.getMaNV(), model.getHinhAnh());
    }

    public void update(Learner model) throws SQLException {
        String sql = "update nguoihoc set hovaten = ?, ngaysinh = ?, gioitinh = ?, sodienthoai = ?, email = ?, ghichu = ?, manhanvien = ?, hinhanh = ? where manguoihoc = ?";
        JdbcHelper.executeUpdate(sql, model.getHoTen(), model.getNgaySinh(), model.getGioiTinh(), model.getDienThoai(), model.getEmail(), model.getGhiChu(), model.getMaNV(), model.getHinhAnh(), model.getMaNH());
    }

    public void delete(String maNH) throws SQLException {
        String sql = "delete from nguoihoc where manguoihoc = ?";
        JdbcHelper.executeUpdate(sql, maNH);
    }

    private Learner readFromResultSet(ResultSet rs) throws SQLException {
        Learner model = new Learner();

        model.setMaNH(rs.getString("manguoihoc"));
        model.setHoTen(rs.getString("hovaten"));
        model.setNgaySinh(rs.getDate("ngaysinh"));
        model.setGioiTinh(rs.getBoolean("gioitinh"));
        model.setDienThoai(rs.getString("sodienthoai"));
        model.setEmail(rs.getString("email"));
        model.setGhiChu(rs.getString("ghichu"));
        model.setMaNV(rs.getString("manhanvien"));
        model.setNgayDK(rs.getDate("ngaydangky"));
        model.setHinhAnh(rs.getString("hinhanh"));

        return model;
    }

    private List<Learner> select(String sql, Object... args) {
        List<Learner> list = new ArrayList<>();

        try {
            ResultSet rs = null;

            try {
                rs = JdbcHelper.executeQuery(sql, args);

                while (rs.next()) {
                    Learner model = readFromResultSet(rs);
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

    public List<Learner> select() {
        String sql = "select * from nguoihoc";

        return select(sql);
    }

    public List<Learner> selectByKeyword(String keyword) {
        if (keyword.equalsIgnoreCase("Nam") || keyword.equalsIgnoreCase("N") || keyword.equalsIgnoreCase("Na") || keyword.equalsIgnoreCase("a") || keyword.equalsIgnoreCase("m") || keyword.equalsIgnoreCase("am")) {
            String sql = "select * from nguoihoc where gioitinh like ?;";

            return select(sql, 1);
        }

        if (keyword.equalsIgnoreCase("Nữ") || keyword.equalsIgnoreCase("N") || keyword.equalsIgnoreCase("ữ")) {
            String sql = "select * from nguoihoc where gioitinh like ?;";

            return select(sql, 0);
        }

        String sql = "select * from nguoihoc_format where hovaten like ? or manguoihoc like ? or sodienthoai like ? or email like ? or manhanvien like ? or ngaysinh_format like ? or ngaydangky_format like ?;";

        return select(sql, "%" + keyword + "%", "%" + keyword + "%", "%" + keyword + "%", "%" + keyword + "%", "%" + keyword + "%", "%" + keyword + "%", "%" + keyword + "%");
    }

    public List<Learner> selectByCourse(int maKH, String keyword) {
//        String sql = "select * from nguoihoc where hovaten like ? and manguoihoc not in (select manguoihoc from hocvien where makhoahoc = ?)";
//
//        return select(sql, "%" + keyword + "%", maKH);

        if (keyword.equalsIgnoreCase("Nam") || keyword.equalsIgnoreCase("N") || keyword.equalsIgnoreCase("Na") || keyword.equalsIgnoreCase("a") || keyword.equalsIgnoreCase("m") || keyword.equalsIgnoreCase("am")) {
            String sql = "select * from nguoihoc where gioitinh like ? and manguoihoc not in (select manguoihoc from hocvien where makhoahoc = ?);";

            return select(sql, 1, maKH);
        }

        if (keyword.equalsIgnoreCase("Nữ") || keyword.equalsIgnoreCase("N") || keyword.equalsIgnoreCase("ữ")) {
            String sql = "select * from nguoihoc where gioitinh like ? and manguoihoc not in (select manguoihoc from hocvien where makhoahoc = ?);";

            return select(sql, 0, maKH);
        }

//        String sql = "select * from nguoihoc_format where (hovaten like ? or manguoihoc like ? or sodienthoai like ? or email like ? or manhanvien like ? or ngaysinh_format like ? or ngaydangky_format like ?) and manguoihoc not in (select manguoihoc from hocvien where makhoahoc = ?);";
        String sql = "select * from nguoihoc_format where (hovaten like ? or manguoihoc like ? or sodienthoai like ? or email like ? or manhanvien like ? or ngaysinh_format like ? or ngaydangky_format like ?) and manguoihoc not in (select manguoihoc from hocvien where makhoahoc = ?);";

        return select(sql, "%" + keyword + "%", "%" + keyword + "%", "%" + keyword + "%", "%" + keyword + "%", "%" + keyword + "%", "%" + keyword + "%", "%" + keyword + "%", maKH);
//        return select(sql, "%" + keyword + "%", maKH);
    }

    public Learner findById(String maNH) {
        String sql = "select * from nguoihoc where manguoihoc like ?;";
        List<Learner> list = select(sql, maNH);

        return list.size() > 0 ? list.get(0) : null;
    }
}
