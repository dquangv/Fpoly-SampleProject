/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Helper.JdbcHelper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Quang
 */
public class StatisticsDAO {

    public List<Object[]> getNguoiHoc() {
        List<Object[]> list = new ArrayList<>();

        try {
            ResultSet rs = null;

            try {
                String sql = "{call sp_ThongKeNguoiHoc}";
                rs = JdbcHelper.executeQuery(sql);

                while (rs.next()) {
                    Object[] model = {rs.getInt("Nam"), rs.getInt("SoLuong"), rs.getDate("DauTien"), rs.getDate("CuoiCung")};
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

    public List<Object[]> getBangDiem(Integer maKH) {
        List<Object[]> list = new ArrayList<>();

        try {
            ResultSet rs = null;
            try {
                String sql = "{call sp_BangDiem(?)}";
                rs = JdbcHelper.executeQuery(sql, maKH);

                while (rs.next()) {
                    double diem = rs.getDouble("diemtrungbinh");
                    String xepLoai = "Xuất sắc";

                    if (diem < 0) {
                        xepLoai = "Chưa nhập";
                    } else if (diem < 3) {
                        xepLoai = "Kém";
                    } else if (diem < 5) {
                        xepLoai = "Yếu";
                    } else if (diem < 6.5) {
                        xepLoai = "Trung bình";
                    } else if (diem < 7.5) {
                        xepLoai = "Khá";
                    } else if (diem < 9) {
                        xepLoai = "Giỏi";
                    }

                    Object[] model = {rs.getString("manguoihoc"), rs.getString("hovaten"), diem, xepLoai};
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

    public List<Object[]> getDiemTheoChuyenDe() {
        List<Object[]> list = new ArrayList<>();

        try {
            ResultSet rs = null;

            try {
                String sql = "{call sp_ThongKeDiem}";
                rs = JdbcHelper.executeQuery(sql);

                while (rs.next()) {
                    Object[] model = {rs.getString("ChuyenDe"), rs.getInt("SoHocVien"), rs.getDouble("ThapNhat"), rs.getDouble("CaoNhat"), rs.getDouble("TrungBinh")};
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

    public List<Object[]> getDoanhThu(int nam) {
        List<Object[]> list = new ArrayList<>();

        try {
            ResultSet rs = null;

            try {
                String sql = "{call sp_ThongKeDoanhThu(?)}";
                rs = JdbcHelper.executeQuery(sql, nam);

                while (rs.next()) {
                    Object[] model = {rs.getString("ChuyenDe"), rs.getInt("SoKhoaHoc"), rs.getInt("SoHocVien"), rs.getDouble("DoanhThu"), rs.getDouble("ThapNhat"), rs.getDouble("CaoNhat"), rs.getDouble("TrungBinh")};
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
    
//    public List<Object[]> getDoanhThuBD(int nam) {
//        String sql = "{call "
//    }
 }
