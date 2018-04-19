package tuple;

import utils.StringUtils;

public class Pair<K, V> {
    private K first;
    private V second;

    public Pair(K first, V second) {
        this.first = first;
        this.second = second;
    }

    public K getFirst() {
        return first;
    }

    public V getSecond() {
        return second;
    }

    public static <T, U> Pair<T, U> of(T first, U second) {
        return new Pair<>(first, second);
    }

    @Override
    public String toString() {
        return StringUtils.interp("< {}, {} >", this.getFirst(), this.getSecond());
    }
}
