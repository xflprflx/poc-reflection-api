package com.xflprflx.table_poc.helpers;

import com.xflprflx.table_poc.annotations.Coluna;
import com.xflprflx.table_poc.annotations.Tabela;

import java.lang.reflect.Field;

public class Helper {

    public static String getTableName(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Tabela.class)) {
            return clazz.getAnnotation(Tabela.class).name();
        } else {
            return clazz.getSimpleName().toLowerCase();
        }
    }

    public static String getFieldName(Field field) {
        return field.isAnnotationPresent(Coluna.class) && !field.getAnnotation(Coluna.class).name().isEmpty() ?
                field.getAnnotation(Coluna.class).name() : field.getName();
    }

}
