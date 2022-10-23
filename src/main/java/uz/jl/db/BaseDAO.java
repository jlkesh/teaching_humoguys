package uz.jl.db;

import com.google.gson.Gson;
import lombok.NonNull;
import uz.jl.db.mapper.RowMapper;
import uz.jl.domains.Domain;
import uz.jl.properties.DatabaseProperties;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
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

    // @SuppressWarnings({"unchecked", "rawtypes"})
    protected <E extends Domain, T extends RowMapper<E>> E mapTo(@NonNull ResultSet resultSet,
                                                                 @NonNull Class<T> clazz) {
        try {
            T t = clazz.getConstructor().newInstance();
            return t.map(resultSet);
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

}
