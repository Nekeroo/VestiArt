package com.project.vestiart.utils;

import com.project.vestiart.models.enums.TypeEnum;

public class PromptUtils {

    public static String formatPromptRequest(String person, String reference, int type ) {
        return String.format("Do me a pub for a clothes (the piece you want that can be shoes, t-shirt, short, complet outfit, tie...) that represent %s from %s %s.\n" +
                "In this ad I need to know : the texture, the inspiration, the price, the size and all colors used (with location)", person, reference, TypeEnum.findTypeEnumById(type));
    }

    public static String formatPromptImage(String person, String reference, int type ) {
        return "";
    }
}
