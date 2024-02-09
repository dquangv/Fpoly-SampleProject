/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Helper.JdbcHelper;
import Model.Thematic;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Quang
 */
public class ThematicDAO {

    public void insert(Thematic model) throws SQLException {
        String sql = "insert into chuyende values (?, ?, ?, ?, ?, ?);";
        JdbcHelper.executeUpdate(sql, model.getMaCD(), model.getTenCD(), model.getHocPhi(), model.getThoiLuong(), model.getHinh(), model.getMoTa());
    }

    public void update(Thematic model) throws SQLException {
        String sql = "update chuyende set tenchuyende = ?, hocphi = ?, thoiluong = ?, hinhlogo = ?, motachuyende = ? where machuyende = ?;";
        JdbcHelper.executeUpdate(sql, model.getTenCD(), model.getHocPhi(), model.getThoiLuong(), model.getHinh(), model.getMoTa(), model.getMaCD());
    }

    public void delete(String maCD) throws SQLException {
        String sql = "delete from chuyende where machuyende = ?";
        JdbcHelper.executeUpdate(sql, maCD);
    }

    private Thematic readFromResultSet(ResultSet rs) throws SQLException {
        Thematic model = new Thematic();

        model.setMaCD(rs.getString("machuyende"));
        model.setTenCD(rs.getString("tenchuyende"));
        model.setHocPhi(rs.getDouble("hocphi"));
        model.setThoiLuong(rs.getInt("thoiluong"));
        model.setHinh(rs.getString("hinhlogo"));
        model.setMoTa(rs.getString("motachuyende"));

        return model;
    }

    private List<Thematic> select(String sql, Object... args) {
        List<Thematic> list = new ArrayList<>();

        try {
            ResultSet rs = null;

            try {
                rs = JdbcHelper.executeQuery(sql, args);

                while (rs.next()) {
                    Thematic model = readFromResultSet(rs);
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

    public List<Thematic> select() {
        String sql = "select * from chuyende";

        return select(sql);
    }

    public Thematic findById(String maCD) {
        String sql = "select * from chuyende where machuyende like ?";
        List<Thematic> list = select(sql, maCD);

        return list.size() > 0 ? list.get(0) : null;
    }
}
