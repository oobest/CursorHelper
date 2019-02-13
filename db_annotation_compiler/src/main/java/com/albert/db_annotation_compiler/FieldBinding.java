package com.albert.db_annotation_compiler;

import javax.lang.model.type.TypeMirror;

public final class FieldBinding {
    private final String name;

    private final TypeMirror type;

    private final String colsName;

    public FieldBinding(String name, TypeMirror type, String colsName) {
        this.name = name;
        this.type = type;
        this.colsName = colsName;
    }

    public String getName() {
        return name;
    }

    public TypeMirror getType() {
        return type;
    }

    public String getColsName() {
        return colsName;
    }
}
