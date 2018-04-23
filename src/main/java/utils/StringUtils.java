package utils;


import java.util.Arrays;
import java.util.Collection;

public final class StringUtils {
    private StringUtils() { }

    public static String interp(String str, Object ... list) {
        return String.format(str.replace("{}", "%s"), list);
    }

    public static String mult(String s, int n) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < n; i++) {
            stringBuffer.append(s);
        }
        return stringBuffer.toString();
    }
}
