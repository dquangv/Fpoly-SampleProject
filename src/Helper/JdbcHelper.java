/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Helper;

import java.sql.*;

/**
 *
 * @author Quang
 */
public class JdbcHelper {
    public static String url = "jdbc:sqlserver://localhost:1433;database=Polypro;username=sa;password=123;encrypt=false;";
    
    public static PreparedStatement preparedStatement(String sql, Object... args) throws SQLException {
        Connection connection = DriverManager.getConnection(url);
        PreparedStatement pstm = null;
        
        if (sql.trim().startsWith("{")) {
            pstm = connection.prepareCall(sql);
        } else {
            pstm = connection.prepareStatement(sql);
        }
        
        for (int i = 0; i < args.length; i++) {
            pstm.setObject(i + 1, args[i]);
        }
        
        return pstm;
    }
    
    public static void executeUpdate(String sql, Object... args) throws SQLException {
        PreparedStatement pstm = preparedStatement(sql, args);
        
        pstm.executeUpdate();
        pstm.getConnection().close();
    }
    
    public static ResultSet executeQuery(String sql, Object... args) throws SQLException {
        PreparedStatement pstm = preparedStatement(sql, args);
        
        return pstm.executeQuery();
    }
}
