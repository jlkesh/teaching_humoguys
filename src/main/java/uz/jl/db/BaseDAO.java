package uz.jl.db;

import com.google.gson.Gson;
import uz.jl.properties.DatabaseProperties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class BaseDAO {
    private static Connection connection;
    protected static final Gson gson = new Gson();


    protected Connection getPostgresConnection() throws SQLException {
        if (Objects.isNull(connection) || connection.isClosed()) {
            connection = DriverManager.getConnection(
                    DatabaseProperties.JDBC_URL,
                    DatabaseProperties.USERNAME,
                    DatabaseProperties.PASSWORD
            );
        }
        return connection;
    }

    public void destroyConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
