package com.project.vestiart.models.enums;

import lombok.Getter;

@Getter
public enum TypeEnum {

    MOVIE(0, "movie"),
    SERIES(1, "series"),
    ANIME(2, "anime");

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
}
