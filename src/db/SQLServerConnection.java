package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLServerConnection {
    private static final String URL =
        "jdbc:sqlserver://localhost:1433;databaseName=ooad;encrypt=false";

    private static final String USER = "sa";
    private static final String PASS = "123456";

    static {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            System.err.println("Khong tim thay SQL Server JDBC Driver");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            System.err.println("Ket noi SQL Server that bai: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
