package com.project.vestiart.enums;

import lombok.Getter;

@Getter
public enum TypeEnum {

    MOVIE(0, "MOVIE"),
    SERIES(1, "SERIES"),
    ANIME(2, "ANIME");

    private final int id;
    private final String type;

    TypeEnum(int id, String type) {
        this.id = id;
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }

    public static TypeEnum findTypeEnumById(int id) {
        for (TypeEnum type : TypeEnum.values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        throw new IllegalArgumentException("No enum constant with id: " + id);
    }

    public static TypeEnum findTypeEnumByType(String type) {
        for (TypeEnum typeEnum : TypeEnum.values()) {
            if (typeEnum.getType().equalsIgnoreCase(type)) {
                return typeEnum;
            }
        }
        throw new IllegalArgumentException("No enum constant with type: " + type);
    }
}
