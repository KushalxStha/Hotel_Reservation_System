import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    private static final String url="jdbc:mysql://localhost:3306/hotel_db";
    private static final String user="root";
    private static final String password="Mysql@24";

    public static void main(String[] args) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e){
            e.getStackTrace();
        }

        try{
            Connection connection = DriverManager.getConnection(url, user, password);
        }catch (SQLException e){
            e.getStackTrace();
        }
    }
}