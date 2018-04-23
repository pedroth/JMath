package matrix;

import fields.AlgebraicField;
import tuple.TypedTuple;
import utils.Copyable;

import java.util.function.Function;

/**
 * Matrix interface
 * @param <T>
 */
public interface Matrix<T extends AlgebraicField<T>> extends Copyable<Matrix<T>> {
    T get();
    T get(TypedTuple<Integer> x);
    T get(int[] x);
    Matrix<T> get(String s);
    Matrix<T> add(Matrix<T> b);
    Matrix<T> sub(Matrix<T> b);
    Matrix<T> prod(Matrix<T> b);
    Matrix<T> solve(Matrix<T> b);
    Matrix<T> scale(double r);
    <R extends AlgebraicField<R>> Matrix<R> apply(Function<T,R> mapFunction);
    <U> U map(Function<Matrix<T>, U> mapFunction);
    double innerProd(Matrix<T> b);
}
