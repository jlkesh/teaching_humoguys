package uz.jl.db;

import lombok.NonNull;
import uz.jl.dto.UserCreateDTO;
import uz.jl.dto.UserDTO;
import uz.jl.dto.UserLoginDTO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO extends BaseDAO {


    public Long createUser(@NonNull UserCreateDTO dto) throws SQLException {
        Connection connection = getPostgresConnection();
        CallableStatement callableStatement = connection.prepareCall("select todo.user_register(?)");
        String jsonDATA = gson.toJson(dto);
        callableStatement.setString(1, jsonDATA);
        ResultSet resultSet = callableStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getLong(1);
        }
        throw new SQLException("QUERY did not return id");
    }

    public UserDTO login(@NonNull UserLoginDTO dto) throws SQLException {
        Connection connection = getPostgresConnection();
        CallableStatement callableStatement = connection.prepareCall("select todo.user_login(?, ?)");
        callableStatement.setString(1, dto.getUsername());
        callableStatement.setString(2, dto.getPassword());
        ResultSet resultSet = callableStatement.executeQuery();
        if (resultSet.next()) {
            return gson.fromJson(resultSet.getString(1), UserDTO.class);
        }
        throw new SQLException("QUERY did not return user details");
    }
}
