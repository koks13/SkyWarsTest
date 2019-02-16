package koks.utility;

import koks.SkyWars;

public class ChatUtil {

    public static String format(String string) {
        return string.replace('&', '§');
    }

    public static String clearColor(String string) {
        if(!string.contains("§"))return string;
        StringBuilder builder = new StringBuilder();
        String[] values = string.split("§");
        for(int i = 1; i < values.length; i++){
            builder.append(values[i].substring(1));
        }
        return builder.toString();
    }
}
