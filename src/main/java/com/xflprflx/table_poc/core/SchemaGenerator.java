package com.xflprflx.table_poc.core;

import com.xflprflx.table_poc.annotations.*;
import com.xflprflx.table_poc.helpers.Helper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class SchemaGenerator {

    private final DataSource dataSource;

    public SchemaGenerator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void generateTable(Class<?> clazz) throws SQLException {
        if (!clazz.isAnnotationPresent(Entidade.class)) {
            System.err.println("A classe " + clazz.getSimpleName() + " não é uma @Entidade e será ignorada.");
            return;
        }

        String tableName = Helper.getTableName(clazz);
        StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tableName + " (\n");

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Coluna.class)) {
                String columnName = Helper.getFieldName(field);
                String columnType = mapJavaTypeToSql(field);

                sql.append("    ").append(columnName).append(" ").append(columnType);

                if (field.isAnnotationPresent(ChavePrimaria.class)) {
                    sql.append(" PRIMARY KEY");
                }
                sql.append(",\n");
            }
        }
        sql.setLength(sql.length() - 2);
        sql.append("\n);");

        System.out.println("SQL Gerado para PostgreSQL:\n" + sql);

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(sql.toString());
            System.out.println("Tabela '" + tableName + "' criada com sucesso no PostgreSQL!");
        }
    }

    private String mapJavaTypeToSql(Field field) {
        Class<?> javaType = field.getType();

        if (field.isAnnotationPresent(ChavePrimaria.class) && field.isAnnotationPresent(ValorGerado.class)) {
            if (javaType == Long.class || javaType == long.class) return "BIGSERIAL";
            if (javaType == Integer.class || javaType == int.class) return "SERIAL";
        }

        if (javaType == String.class) return "VARCHAR(255)";
        if (javaType == Long.class || javaType == long.class) return "BIGINT";
        if (javaType == Integer.class || javaType == int.class) return "INTEGER";
        if (javaType == Double.class || javaType == double.class) return "DECIMAL(10, 2)";
        if (javaType == Float.class || javaType == float.class) return "REAL";

        throw new IllegalArgumentException("Tipo Java não suportado: " + javaType.getSimpleName());
    }
}