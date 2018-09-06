package general_matrix;

import array.DenseNDArray;
import exceptions.MatrixRunTimeException;
import fields.AlgebraicField;
import fields.Real;
import lombok.NonNull;
import tuple.TypedTuple;
import utils.Copyable;
import utils.Printable;
import utils.StringUtils;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.DoubleStream;

/**
 * Imutable dense general_matrix implementation
 *
 * @param <T>
 */
public class DMatrix<T extends AlgebraicField> implements Matrix<T>, Printable, Copyable<Matrix<T>> {
    private DenseNDArray<T> matrix;

    public DMatrix(T[][] array) {
        Optional.ofNullable(array).orElseThrow(() -> new MatrixRunTimeException("null array as input of Matrix constructor"));
        this.matrix = new DenseNDArray<>(new int[]{array.length, array[0].length});
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                this.matrix.set(new int[]{i, j}, array[i][j]);
            }
        }
    }

    public DMatrix(T[] array, int lines, int columns) {
        if (array.length != lines * columns) {
            throw new MatrixRunTimeException("Array size incompatible with general_matrix size");
        }
        this.matrix = new DenseNDArray<>(array, new int[]{lines, columns});
    }

    public DMatrix(DenseNDArray<T> array) {
        Optional.ofNullable(array).orElseThrow(() -> new MatrixRunTimeException("null array as input of Matrix constructor"));
        int[] dim = array.getDim();
        assert dim.length <= 2 : StringUtils.interp("A general_matrix is a 2-dimensional array, given {}-dim array", dim.length);
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
    public Matrix<T> mult(Matrix<T> b) {
        return null;
    }

    @Override
    public Matrix<T> div(Matrix<T> b) {
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
        return this.map(x -> x.scale(r));
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

    public  static <K extends AlgebraicField> Matrix<K> of(K[][] matrix) {
        return new DMatrix<>(matrix);
    }

    public  static Matrix<Real> of(@NonNull double[][] matrix) {
        assert matrix.length > 0 && matrix[0].length > 0 : new MatrixRunTimeException("empty matrix");
        Real[][] realMatrix = new Real[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                realMatrix[i][j] = Real.of(matrix[i][j]);
            }
        }
        return DMatrix.of(realMatrix);
    }




}
