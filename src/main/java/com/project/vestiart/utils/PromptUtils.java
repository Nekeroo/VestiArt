package com.project.vestiart.utils;

import com.project.vestiart.models.enums.TypeEnum;

public class PromptUtils {

    public static String formatPromptRequest(String person, String reference, int type ) {
        return String.format(
                "Do me a pub for a complet outfit that represent %s from %s %s.\\n\" +\"In this ad I need to know some informations like the following example. And please, don't use special style inside the response (surrender the \"*\" to have bold or italic text). The result must contains les than 850 characters\n" +
                "\n" +
                "Introductory Sentence\n" +
                "\n" +
                "[...]\n" +
                "\n" +
                "Outfit Details:\n" +
                "- Texture: [...]\n" +
                "- Inspiration: [...]\n" +
                "- Sizes: [...]\n" +
                "- Colors: [list of all colors used and in which part of the outfit/shoes/tie/t-shorts]\n" +
                "\n" +
                "Complete the Look:\n" +
                "[Tips with product placement for matching accessories]\n" +
                "\n" +
                "Early Sentence to make people want to buy", person, reference, TypeEnum.findTypeEnumById(type));
    }

    public static String formatPromptImage(String prompt) {
        return String.format("Create me a 3D realistic picture to represent the following ad. I want something with multiple view. Please don't put any text on the picture. %s", prompt);
    }
}
