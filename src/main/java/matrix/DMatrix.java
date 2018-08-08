package matrix;

import array.DenseNDArray;
import exceptions.MatrixRunTimeException;
import fields.AlgebraicField;
import tuple.TypedTuple;
import utils.Printable;
import utils.StringUtils;

import java.util.Optional;
import java.util.function.Function;

/**
 * Imutable dense matrix implementation
 * @param <T>
 */
public class DMatrix<T extends AlgebraicField<T>> implements Matrix<T>, Printable {
    private DenseNDArray<T> matrix;

    public DMatrix(T[][] array) {
        Optional.ofNullable(array).orElseThrow(() -> new MatrixRunTimeException("null array as input of Matrix constructor"));
        this.matrix = new DenseNDArray<>(new int[]{array.length, array[0].length});
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                this.matrix.set(new int[]{i,j}, array[i][j]);
            }
        }
    }

    public DMatrix(T[] array, int lines, int columns) {
        if(array.length  != lines * columns) {
            throw new MatrixRunTimeException("Array size incompatible with matrix size");
        }
        this.matrix = new DenseNDArray<>(array, new int[]{lines, columns});
    }

    public DMatrix(DenseNDArray<T> array) {
        Optional.ofNullable(array).orElseThrow(() -> new MatrixRunTimeException("null array as input of Matrix constructor"));
        int[] dim = array.getDim();
        assert dim.length <= 2 : StringUtils.interp("A matrix is a 2-dimensional array, given {}-dim array", dim.length);
        this.matrix = array.copy();
    }

    @Override
    public T get() {
        return this.matrix.get();
    }

    @Override
    public T get(TypedTuple<Integer> x) {
        return this.matrix.get(x);
    }

    @Override
    public T get(int[] x) {
        return this.matrix.get(x);
    }

    @Override
    public Matrix<T> get(String s) {
        return new DMatrix<>(this.matrix.get(s));
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
