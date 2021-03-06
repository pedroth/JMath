package fields;

import utils.Copyable;

import java.util.function.Function;

public interface AlgebraicField<T> extends Copyable<T> {
    T add(T a);

    T sub(T a);

    T prod(T a);

    T div(T a);

    T symmetric();

    T conj();

    T sumIdentity();

    T prodIdentity();

    T reciprocal();

    T scale(double r);

    default <R> R map(Function<T, R> mapFunction) {
        return mapFunction.apply(this.copy());
    }

    double squareNorm();

    default double norm() {
        return Math.sqrt(this.squareNorm());
    }
}
