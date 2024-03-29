package lk.ijse.projectharbourmaster.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private final static String URL = "jdbc:mysql://localhost:3306/ProjectHarbourmaster";
    private final static String user = "root";
    private final static String password = "1234";

    private static DBConnection dbConnection;
    private Connection con;

    private DBConnection() throws SQLException {
        con = DriverManager.getConnection(URL , user , password);
    }

    public static DBConnection getInstance() throws SQLException {
        if (dbConnection == null){
            return dbConnection = new DBConnection();
        }else {
            return dbConnection;
        }

    }

    public Connection getConnection(){
        return con;
    }

}
