import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String URL = "jdbc:postgresql://localhost:5432/hospital";
    private static final String USER = "postgres"; // Replace with your PostgreSQL username
    private static final String PASSWORD = "Aimankh.7"; // Replace with your PostgreSQL password

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver"); // Ensure PostgreSQL driver is loaded
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}