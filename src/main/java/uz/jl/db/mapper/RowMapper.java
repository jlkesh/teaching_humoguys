package uz.jl.db.mapper;

import uz.jl.domains.Domain;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface RowMapper<T extends Domain> {
    T map(ResultSet resultSet) throws SQLException;
}
