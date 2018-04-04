package utils;

public final class StringUtils {
    private StringUtils() { }

    public static String interp(String str, Object ... list) {
        return String.format(str.replace("{}", "%s"), list);
    }
}
