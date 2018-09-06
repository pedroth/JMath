package general_matrix;

import fields.AlgebraicField;
import tuple.TypedTuple;
import utils.Copyable;

import java.util.function.Function;

/**
 * Matrix interface
 *
 * @param <T>
 */
public interface Matrix<T extends AlgebraicField> extends Copyable<Matrix<T>> {
    T get();

    T get(TypedTuple<Integer> x);

    T get(int[] x);

    Matrix<T> get(String s);

    Matrix<T> add(Matrix<T> b);

    Matrix<T> sub(Matrix<T> b);

    Matrix<T> mult(Matrix<T> b);

    Matrix<T> div(Matrix<T> b);

    Matrix<T> scale(double r);

    Matrix<T> prod(Matrix<T> b);

    Matrix<T> solve(Matrix<T> b);

    <U> U map(Function<Matrix<T>, U> mapFunction);

    double innerProd(Matrix<T> b);
}
