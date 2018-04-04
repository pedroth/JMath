package utils;

import java.util.function.Function;

public interface Printable {
    default String toString(Function<Printable, String> printer) {
        return printer.apply(this);
    }
}
