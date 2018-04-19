package utils;

public final class ArrayUtils {
    private ArrayUtils() { }

    public static <T> T[] concat(T[] first, T... toAdd) {
        if(first == null && toAdd == null) {
            return null;
        }
        if(first == null) {
            return toAdd;
        }
        if(toAdd == null) {
            return first;
        }
        Object[] ans = new Object[first.length + toAdd.length];
        System.arraycopy(first, 0, ans, 0, first.length);
        System.arraycopy(toAdd, 0, ans, first.length, toAdd.length);
        return (T[]) ans;
    }

    public static int[] concat(int[] first, int ... toAdd) {
        if(first == null && toAdd == null) {
            return null;
        }
        if(first == null) {
            return toAdd;
        }
        if(toAdd == null) {
            return first;
        }
        int[] ans = new int[first.length + toAdd.length];
        System.arraycopy(first, 0, ans, 0, first.length);
        System.arraycopy(toAdd, 0, ans, first.length, toAdd.length);
        return ans;
    }

    public static double[] concat(double[] first, double ... toAdd) {
        if(first == null && toAdd == null) {
            return null;
        }
        if(first == null) {
            return toAdd;
        }
        if(toAdd == null) {
            return first;
        }
        double[] ans = new double[first.length + toAdd.length];
        System.arraycopy(first, 0, ans, 0, first.length);
        System.arraycopy(toAdd, 0, ans, first.length, toAdd.length);
        return ans;
    }
}
