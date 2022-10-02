package uz.jl.ui;

import uz.jl.db.UserDAO;
import uz.jl.dto.UserCreateDTO;

import java.sql.*;


public class UIWEB {
    public static void main(String[] args) {

        try {
            UserDAO userDAO = new UserDAO();
            Long userID = userDAO.createUser(UserCreateDTO
                    .builder()
                    .username("john")
                    .password("q123456789")
                    .build());
            System.out.println("userID = " + userID);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/humoguystodo",
                "postgres",
                "213");
        return connection;
    }
}
