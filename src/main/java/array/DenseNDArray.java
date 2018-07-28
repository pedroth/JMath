package array;

import exceptions.MatrixRunTimeException;
import interval.Interval;
import tuple.TypedTuple;
import utils.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class DenseNDArray<T> implements Printable, Copyable<DenseNDArray<T>> {
    /*
     * row major array
     */
    private Object[] denseNDArray;

    private int[] powers;

    private int[] dim;

    public DenseNDArray(int[] dim) {
        this.dim = Optional.ofNullable(dim).orElseThrow(() -> new MatrixRunTimeException("null input exception"));
        this.computePowers(dim);
        this.denseNDArray = new Object[this.powers[this.powers.length - 1]];
    }

    public DenseNDArray(T element) {
        this(new int[]{1});
        this.denseNDArray[0] = element;
    }

    public DenseNDArray(T[] elements, int[] dim) {
        this.dim = Optional.ofNullable(dim).orElseThrow(() -> new MatrixRunTimeException("null input exception"));
        this.computePowers(dim);
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
        this.computePowers(this.dim);
        if (this.powers[this.powers.length - 1] != denseNDArray.size())
            throw new MatrixRunTimeException(StringUtils.interp("Shape not compatible, actual shape {}", TypedTuple.of(this.dim)));
        this.denseNDArray = Arrays.copyOf(denseNDArray.denseNDArray, denseNDArray.denseNDArray.length);
    }

    public static DenseNDArrayBuilder builder() {
        return new DenseNDArray.DenseNDArrayBuilder();
    }

    private void computePowers(int[] dim) {
        int[] aux = {1, 0};
        this.powers = new int[dim.length + 1];
        int acc = 1;
        this.powers[0] = acc;
        for (int i = 0; i < dim.length; i++) {
            // to be row major like numpy
            int j = i < 2 ? aux[i] : i;
            acc *= dim[j];
            this.powers[i + 1] = acc;
        }
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

    public void forEach(Function<T, T> function) {
        int size = size();
        for (int i = 0; i < size; i++) {
            this.denseNDArray[i] = function.apply((T) this.denseNDArray[i]);
        }
    }

    public DenseNDArray<T> get(String x) {
        Interval<Integer>[] intervals = getIntervalFromStr(x);
        int[] newDim = computeNewDim(intervals);
        DenseNDArray<T> newDenseNDArray = new DenseNDArray<>(newDim);
        int size = newDenseNDArray.size();
        int[] y = new int[dim.length];
        for (int i = 0; i < size; i++) {
            int k = 0;
            for (int j = 0; j < intervals.length; j++) {
                Interval<Integer> interval = intervals[j];
                int dx = interval.getXmax() - interval.getXmin();
                int index = i % this.powers[k + 1] / this.powers[k];
                k = dx == 0 ? k : k + 1;
                y[j] = dx == 0 ? interval.getXmin() : interval.getXmin() + index;
            }
            newDenseNDArray.denseNDArray[i] = this.get(y);
        }
        return newDenseNDArray;
    }

    public DenseNDArray<T> set(String x, DenseNDArray<T> vol) {
        Interval<Integer>[] intervals = getIntervalFromStr(x);
        int size = vol.size();
        int[] y = new int[dim.length];
        for (int i = 0; i < size; i++) {
            int k = 0;
            for (int j = 0; j < intervals.length; j++) {
                Interval<Integer> interval = intervals[j];
                int dx = interval.getXmax() - interval.getXmin();
                int index = i % this.powers[k + 1] / this.powers[k];
                k = dx == 0 ? k : k + 1;
                y[j] = dx == 0 ? interval.getXmin() : interval.getXmin() + index;
            }
            this.set(y, (T) vol.denseNDArray[i]);
        }
        return this;
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

    public int size() {
        return this.powers[this.powers.length - 1];
    }

    public int[] getDim() {
        return this.dim;
    }

    public int[] shape() {
        return this.dim;
    }

    private int getIndex(int[] x) {
        int[] aux = {1, 0};
        int index = 0;
        for (int i = 0; i < dim.length; i++) {
            // to be row major like numpy
            int j = i < 2 ? aux[i] : i;
            index += x[j] * powers[i];
        }
        return index;
    }

    private void checkIndexDimension(int d) {
        if (d != dim.length) {
            throw new MatrixRunTimeException("index dimension incorrect : " + d + " correct dimension should be : " + dim.length);
        }
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

    public static class DenseNDArrayBuilder<K> {
        private List<DenseNDArray<K>> listOfLowerDimArray;

        DenseNDArrayBuilder() {
            this.listOfLowerDimArray = new ArrayList<>();
        }

        public DenseNDArrayBuilder<K> add(DenseNDArray<K> array) {
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

        public DenseNDArray<K> build() {
            int[] dim = this.listOfLowerDimArray.get(0).getDim();
            int size = this.listOfLowerDimArray.size();
            int[] concat = ArrayUtils.concat(dim, size);
            DenseNDArray<K> dense = new DenseNDArray<>(concat);
            for (int i = 0; i < size; i++) {
                DenseNDArray<K> vol = listOfLowerDimArray.get(i);
                dense.set(StringUtils.interp("{}{}", StringUtils.mult(":,", vol.dim.length), i), vol);
            }
            return dense;
        }
    }
}
