package com.project.vestiart.enums;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public enum TypeEnum {

    MOVIE(0, "MOVIE"),
    SERIE(1, "SERIE"),
    ANIME(2, "ANIME");

    private final int id;
    private final String type;

    private static final Logger LOGGER = LoggerFactory.getLogger(TypeEnum.class);

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

        LOGGER.error("No enum constant with type: " + type);
        return null;
    }
}
