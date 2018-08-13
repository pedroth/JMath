package array;

import exceptions.MatrixRunTimeException;
import interval.Interval;
import tuple.TypedTuple;
import utils.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * N-dimensional array implementation in column major order
 *
 * @param <T>
 */
public class DenseNDArray<T> implements Printable, Copyable<DenseNDArray<T>> {
    /*
     * column major array
     */
    private Object[] denseNDArray;

    private int[] powers;

    private int[] dim;

    public DenseNDArray(int[] dim) {
        this.dim = Optional.ofNullable(dim).orElseThrow(() -> new MatrixRunTimeException("null input exception"));
        this.powers = this.computePowers(dim);
        this.denseNDArray = new Object[this.powers[this.powers.length - 1]];
    }

    public DenseNDArray(T element) {
        this(new int[]{1});
        this.denseNDArray[0] = element;
    }

    public DenseNDArray(T[] elements, int[] dim) {
        this.dim = Optional.ofNullable(dim).orElseThrow(() -> new MatrixRunTimeException("null input exception"));
        this.powers = this.computePowers(dim);
        if (elements == null) {
            throw new MatrixRunTimeException("null input exception");
        }
        if (elements.length != this.powers[this.powers.length - 1]) {
            throw new MatrixRunTimeException("incompatible dimension size");
        }
        this.denseNDArray = Arrays.copyOf(elements, elements.length);
    }

    public DenseNDArray(DenseNDArray denseNDArray) {
        this.dim = Arrays.copyOf(denseNDArray.dim, denseNDArray.dim.length);
        this.powers = Arrays.copyOf(denseNDArray.powers, denseNDArray.powers.length);
        this.denseNDArray = Arrays.copyOf(denseNDArray.denseNDArray, denseNDArray.denseNDArray.length);
    }

    public DenseNDArray(DenseNDArray denseNDArray, int[] dim) {
        this.dim = Arrays.copyOf(dim, dim.length);
        this.powers = this.computePowers(this.dim);
        if (this.powers[this.powers.length - 1] != denseNDArray.size())
            throw new MatrixRunTimeException(StringUtils.interp("Shape not compatible, actual shape {}", TypedTuple.of(this.dim)));
        this.denseNDArray = Arrays.copyOf(denseNDArray.denseNDArray, denseNDArray.denseNDArray.length);
    }

    public static DenseNDArrayBuilder builder() {
        return new DenseNDArray.DenseNDArrayBuilder();
    }

    public T get() {
        //Assume there is at least one element
        return (T) this.denseNDArray[0];
    }

    public T get(int[] x) {
        checkIndexDimension(x.length);
        return (T) this.denseNDArray[getIndex(x)];
    }

    public T get(TypedTuple<Integer> x) {
        int[] y = Arrays.stream(x.toArray()).mapToInt(Integer::intValue).toArray();
        checkIndexDimension(y.length);
        return (T) this.denseNDArray[getIndex(y)];
    }

    public void set(int[] x, T value) {
        checkIndexDimension(x.length);
        denseNDArray[getIndex(x)] = value;
    }

    public void set(TypedTuple<Integer> x, T value) {
        int[] y = Arrays.stream(x.toArray()).mapToInt(Integer::intValue).toArray();
        checkIndexDimension(y.length);
        denseNDArray[getIndex(y)] = value;
    }

    public void forEach(Consumer<T> lambda) {
        int size = this.size();
        for (int i = 0; i < size; i++) {
            lambda.accept((T) this.denseNDArray[i]);
        }
    }

    public <K> DenseNDArray<K> map(Function<T, K> lambda) {
        DenseNDArray<K> ansArray = new DenseNDArray<>(this.dim);
        int size = this.size();
        for (int i = 0; i < size; i++) {
            ansArray.denseNDArray[i] = lambda.apply((T) this.denseNDArray[i]);
        }
        return ansArray;
    }

    /**
     *
     * @param identity identity element of the accOperator
     * @param accOperator binary operation that combines the identity with elements of the array
     * @param <K>
     * @return reduce value
     */
    public <K> K reduce(K identity, BiFunction<K, T, K> accOperator) {
        int size = this.size();
        for (int i = 0; i < size; i++) {
            identity =  accOperator.apply(identity, (T) this.denseNDArray[i]);
        }
        return identity;
    }

    public DenseNDArray<T> get(String x) {
        Interval<Integer>[] intervals = getIntervalFromStr(x);
        int[] newDim = computeNewDim(intervals);

        DenseNDArray<T> newDenseNDArray = new DenseNDArray<>(newDim);

        int size = newDenseNDArray.size();
        int[] y = new int[this.dim.length];
        int[] dx = new int[intervals.length];

        for (int i = 0; i < intervals.length; i++) {
            dx[i] = intervals[i].getXmax() - intervals[i].getXmin() + 1;
        }

        int[] powers = this.computePowers(dx);

        for (int i = 0; i < size; i++) {
            int k = 0;
            for (int j = 0; j < intervals.length; j++) {
                int index = i % powers[k + 1] / powers[k];
                y[j] = intervals[j].getXmin() + index;
                k++;
            }
            newDenseNDArray.denseNDArray[i] = this.get(y);
        }

        return newDenseNDArray;
    }

    public DenseNDArray<T> set(String x, DenseNDArray<T> vol) {
        Interval<Integer>[] intervals = getIntervalFromStr(x);

        int size = vol.size();
        int[] y = new int[this.dim.length];
        int[] dx = new int[intervals.length];

        for (int i = 0; i < intervals.length; i++) {
            dx[i] = intervals[i].getXmax() - intervals[i].getXmin() + 1;
        }

        int[] powers = this.computePowers(dx);

        for (int i = 0; i < size; i++) {
            int k = 0;
            for (int j = 0; j < intervals.length; j++) {
                int index = i % powers[k + 1] / powers[k];
                y[j] = intervals[j].getXmin() + index;
                k++;
            }
            this.set(y, (T) vol.denseNDArray[i]);
        }
        return this;
    }

    public int size() {
        return this.powers[this.powers.length - 1];
    }

    public int[] getDim() {
        return this.dim;
    }

    public int[] shape() {
        return this.dim;
    }

    @Override
    public String toString() {
        return toStringRecursive(TypedTuple.EMPTY).toString();
    }

    private StringBuilder toStringRecursive(TypedTuple<Integer> coord) {
        StringBuilder stringBuilder = new StringBuilder();
        int size = coord.size();
        if (size != this.dim.length) {
            stringBuilder.append("[ ");
            for (int j = 0; j < this.dim[this.dim.length - 1 - size]; j++) {
                stringBuilder.append(toStringRecursive(new TypedTuple<>(j).union(coord)));
            }
            stringBuilder.append(" ]");
        } else {
            stringBuilder.append(StringUtils.interp("{}, ", this.get(coord)));
        }
        return stringBuilder;
    }

    @Override
    public DenseNDArray<T> copy() {
        return new DenseNDArray<>(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DenseNDArray<?> that = (DenseNDArray<?>) o;
        return Arrays.equals(denseNDArray, that.denseNDArray) &&
                Arrays.equals(powers, that.powers) &&
                Arrays.equals(dim, that.dim);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(denseNDArray);
        result = 31 * result + Arrays.hashCode(powers);
        result = 31 * result + Arrays.hashCode(dim);
        return result;
    }

    private int[] computeNewDim(Interval<Integer>[] intervals) {
        List<Integer> dimBuff = new ArrayList<>(intervals.length);
        for (int i = 0; i < intervals.length; i++) {
            int dx = intervals[i].getXmax() - intervals[i].getXmin();
            if (dx != 0) {
                dimBuff.add(dx + 1);
            }
        }
        int[] newDim = new int[dimBuff.size()];
        for (int i = 0; i < newDim.length; i++) {
            newDim[i] = dimBuff.get(i);
        }
        return newDim;
    }

    private Interval<Integer>[] getIntervalFromStr(String x) {
        String xProcess = x.replace(" ", "");
        String[] split = xProcess.split(",");
        checkIndexDimension(split.length);
        Interval<Integer>[] intervals = new Interval[split.length];
        for (int i = 0; i < split.length; i++) {
            String[] intervalBounds = split[i].split(":");
            switch (intervalBounds.length) {
                case 0:
                    intervals[i] = new Interval<>(0, this.dim[i] - 1);
                    break;
                case 1:
                    Integer integer = Integer.valueOf(intervalBounds[0]);
                    intervals[i] = new Interval<>(integer, split[i].contains(":") ? this.dim[i] - 1 : integer);
                    break;
                case 2:
                    int xmin = (int) MathUtil.clamp("".equals(intervalBounds[0]) ? 0 : Integer.valueOf(intervalBounds[0]), 0, dim[i] - 1);
                    int xmax = (int) MathUtil.clamp("".equals(intervalBounds[1]) ? this.dim[i] - 1 : Integer.valueOf(intervalBounds[1]), 0, dim[i] - 1);
                    Interval<Integer> myInterval = new Interval<>(xmin, xmax);
                    if (myInterval.isEmptyInterval()) {
                        throw new RuntimeException("empty interval xmax : " + xmax + " < xmin : " + xmin);
                    }
                    intervals[i] = myInterval;
                    break;
                default:
            }
        }
        return intervals;
    }

    private int getIndex(int[] x) {
        int index = 0;
        for (int i = 0; i < dim.length; i++) {
            index += x[i] * powers[i];
        }
        return index;
    }

    private void checkIndexDimension(int d) {
        if (d != dim.length) {
            throw new MatrixRunTimeException("index dimension incorrect : " + d + " correct dimension should be : " + dim.length);
        }
    }

    private int[] computePowers(int[] dim) {
        int[] powers = new int[dim.length + 1];
        int acc = 1;
        powers[0] = acc;
        for (int i = 0; i < dim.length; i++) {
            acc *= dim[i];
            powers[i + 1] = acc;
        }
        return powers;
    }

    public static class DenseNDArrayBuilder<T> {

        private List<DenseNDArray<T>> listOfLowerDimArray;

        DenseNDArrayBuilder() {
            this.listOfLowerDimArray = new ArrayList<>();
        }

        public DenseNDArrayBuilder<T> add(DenseNDArray<T> array) {
            if (this.listOfLowerDimArray.isEmpty()) {
                this.listOfLowerDimArray.add(array);
            } else {
                // check if compatible dimension
                int[] dim = this.listOfLowerDimArray.get(0).getDim();
                if (!Arrays.equals(dim, array.getDim())) {
                    throw new MatrixRunTimeException("no compatible concatenation");
                }
                listOfLowerDimArray.add(array);
            }
            return this;
        }

        public DenseNDArrayBuilder<T> add(T[] array) {
            return this.add(new DenseNDArray<>(array, new int[]{array.length}));
        }

        public DenseNDArrayBuilder<T> add(T[][] array) {
            DenseNDArray<T> denseNDArray = new DenseNDArray<>(new int[]{array[0].length, array.length});
            for (int i = 0; i < array[0].length; i++) {
                for (int j = 0; j < array.length; j++) {
                    denseNDArray.set(TypedTuple.of(i, j), array[j][i]);
                }
            }
            return this.add(denseNDArray);
        }

        public DenseNDArray<T> build() {
            int[] dim = this.listOfLowerDimArray.get(0).getDim();
            int size = this.listOfLowerDimArray.size();
            if (size == 1) {
                return listOfLowerDimArray.get(0);
            }
            int[] concat = ArrayUtils.concat(dim, size);
            DenseNDArray<T> dense = new DenseNDArray<>(concat);
            for (int i = 0; i < size; i++) {
                DenseNDArray<T> vol = listOfLowerDimArray.get(i);
                dense.set(StringUtils.interp("{}{}", StringUtils.mult(":,", vol.dim.length), i), vol);
            }
            return dense;
        }

    }
}
