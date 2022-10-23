package uz.jl.db;

import com.google.gson.reflect.TypeToken;
import lombok.NonNull;
import uz.jl.db.mapper.UserRowMapper;
import uz.jl.domains.UserDomain;
import uz.jl.dto.todo.TodoDTO;
import uz.jl.dto.user.UserCreateDTO;
import uz.jl.dto.user.UserDTO;

import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UserDAO extends BaseDAO {


    public static final String SELECT_USER_BY_USERNAME_QUERY = """
            select * from humoguystodo.todo.users t where t.username =  ? ;""";
    public static final String INSERT_USER_QUERY = """
            insert into humoguystodo.todo.users (username, password, uuid, "role") values (?,?,?,?) returning id;""";


    public Optional<UserDomain> findUserByUsername(@NonNull String username) throws SQLException {
        Connection postgresConnection = getPostgresConnection();
        PreparedStatement prst = postgresConnection.prepareStatement(SELECT_USER_BY_USERNAME_QUERY);
        prst.setString(1, username);
        ResultSet resultSet = prst.executeQuery();
        return Optional.of(mapTo(resultSet, UserRowMapper.class));
    }

    public List<TodoDTO> getAllTodoByUserId(Long userId) throws SQLException {
        Connection connection = getPostgresConnection();
        CallableStatement callableStatement = connection.prepareCall("select user_todos(?)");
        callableStatement.setLong(1, userId);
        ResultSet resultSet = callableStatement.executeQuery();
        if (resultSet.next()) {
            String jsonDATA = resultSet.getString(1);
            Type type = new TypeToken<List<TodoDTO>>() {
            }.getType();
            return Objects.requireNonNullElse(gson.fromJson(jsonDATA, type), new ArrayList<>());

        }
        throw new SQLException("QUERY did not return todo list");
    }

    public Long save(@NonNull UserDomain userDomain) throws SQLException {
        Connection postgresConnection = getPostgresConnection();
        PreparedStatement prst = postgresConnection.prepareStatement(INSERT_USER_QUERY);
        prst.setString(1, userDomain.getUsername());
        prst.setString(2, userDomain.getPassword());
        prst.setString(3, userDomain.getUuid());
        prst.setString(4, userDomain.getRole());
        ResultSet resultSet = prst.executeQuery();
        if (resultSet.next()) {
            return resultSet.getLong("id");
        }
        return null;
    }
}
