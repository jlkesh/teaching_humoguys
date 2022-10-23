package uz.jl.db.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface RowMapper<T> extends RowMapperMarker {
    T map(ResultSet resultSet) throws SQLException;
}
