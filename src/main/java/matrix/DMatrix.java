package matrix;

import array.DenseNDArray;
import exceptions.MatrixRunTimeException;
import fields.AlgebraicField;
import tuple.TypedTuple;
import utils.Printable;

import java.util.Optional;
import java.util.function.Function;

/**
 * Imutable dense matrix implementation
 * @param <T>
 */
public class DMatrix<T extends AlgebraicField<T>> implements Matrix<T>, Printable {
    private DenseNDArray<T> matrix;
    DMatrix(T[][] array) {
        Optional.ofNullable(array).orElseThrow(() -> new MatrixRunTimeException("null array as input of Matrix constructor"));
        this.matrix = new DenseNDArray<>(new int[]{array.length, array[0].length});
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                this.matrix.set(new int[]{i,j}, array[i][j]);
            }
        }
    }

    DMatrix(T[] array, int lines, int columns) {
        if(array.length  != lines * columns) {
            throw new MatrixRunTimeException("Array size incompatible with matrix size");
        }
        this.matrix = new DenseNDArray<>(new int[]{lines, columns});
    }

    @Override
    public T get() {
        return this.matrix.get();
    }

    @Override
    public T get(TypedTuple<Integer> x) {
        return this.get(x);
    }

    @Override
    public T get(int[] x) {
        return this.matrix.get(x);
    }

    @Override
    public Matrix<T> get(String s) {
        return null;
    }

    @Override
    public Matrix<T> add(Matrix<T> b) {
        return null;
    }

    @Override
    public Matrix<T> sub(Matrix<T> b) {
        return null;
    }

    @Override
    public Matrix<T> prod(Matrix<T> b) {
        return null;
    }

    @Override
    public Matrix<T> solve(Matrix<T> b) {
        return null;
    }

    @Override
    public Matrix<T> scale(double r) {
        return this.apply(x -> x.scale(r));
    }

    @Override
    public <R extends AlgebraicField<R>> Matrix<R> apply(Function<T, R> mapFunction) {
        return null;
    }

    @Override
    public <U> U map(Function<Matrix<T>, U> mapFunction) {
        return null;
    }

    @Override
    public double innerProd(Matrix<T> b) {
        return 0;
    }

    @Override
    public Matrix<T> copy() {
        return null;
    }
}
