package com.xflprflx.table_poc.core;

import com.xflprflx.table_poc.annotations.*;
import com.xflprflx.table_poc.helpers.Helper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class SimpleEntityManager {

    private final DataSource dataSource;

    public SimpleEntityManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public <T> T persist(T entity) {
        Class<?> clazz = entity.getClass();
        if (!clazz.isAnnotationPresent(Entidade.class)) {
            throw new IllegalArgumentException("A classe " + clazz.getSimpleName() + " não é uma @Entidade.");
        }

        String tableName = Helper.getTableName(clazz);
        List<String> columnNames = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        Field idField = null;

        try {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Coluna.class)) {
                    if (field.isAnnotationPresent(ValorGerado.class)) {
                        idField = field;
                        continue;
                    }
                    field.setAccessible(true);
                    columnNames.add(Helper.getFieldName(field));
                    values.add(field.get(entity));
                }
            }

            String columns = String.join(", ", columnNames);
            String placeholders = String.join(", ", Collections.nCopies(values.size(), "?"));
            String sql = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + placeholders + ")";

            System.out.println("SQL de Persistência Gerado: " + sql);

            try (Connection connection = dataSource.getConnection();
                 PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                for (int i = 0; i < values.size(); i++) {
                    stmt.setObject(i + 1, values.get(i));
                }
                stmt.executeUpdate();

                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next() && idField != null) {
                    String idColumnName = idField.getAnnotation(Coluna.class).name();
                    Object generatedId = generatedKeys.getObject(idColumnName);

                    idField.setAccessible(true);
                    idField.set(entity, generatedId);
                }
            }

        } catch (IllegalAccessException | SQLException e) {
            throw new RuntimeException("Falha ao persistir a entidade", e);
        }

        return entity;
    }

}
