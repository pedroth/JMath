package array;

import exceptions.MatrixRunTimeException;
import interval.Interval;
import tuple.Tuple;
import tuple.TypedTuple;
import utils.*;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The type Dense nD array. Immutable size.
 * @param <T>  the type parameter
 */
public class DenseNDArray<T> implements Printable {
    /**
     * The Dense nD array.
     */
    /*
     * row major array
     */
    private Object[] denseNDArray;

    /**
     * The Powers.
     */
    private int[] powers;

    /**
     * The Dim.
     */
    private int[] dim;

    /**
     * Instantiates a new Dense nD array.
     *
     * @param dim the dim
     */
    public DenseNDArray(int[] dim) {
        this.dim = dim;
        computePowers(dim);
        this.denseNDArray = new Object[this.powers[this.powers.length - 1]];
    }

    public DenseNDArray(T element) {
        this(new int[]{1});
        this.denseNDArray[0] = element;
    }

    public DenseNDArray(T ... elements) {
        this(new int[]{elements.length});
        for (int i = 0; i < elements.length; i++) {
            this.denseNDArray[i] = elements[i];
        }
    }

    private DenseNDArray(DenseNDArray denseNDArray) {
        this.dim = Arrays.copyOf(denseNDArray.dim, denseNDArray.dim.length);
        this.powers = Arrays.copyOf(denseNDArray.powers, denseNDArray.powers.length);
        this.denseNDArray = Arrays.copyOf(denseNDArray.denseNDArray, denseNDArray.denseNDArray.length);
    }

    private void computePowers(int[] dim) {
        this.powers = new int[dim.length + 1];
        int acc = 1;
        this.powers[0] = acc;
        for (int i = 0; i < dim.length; i++) {
            acc *= dim[i];
            this.powers[i + 1] = acc;
        }
    }

    /**
     * Get t.
     *
     * @param x the x
     * @return the t
     */
    public T get(int[] x) {
        checkIndexDimension(x.length);
        return (T) this.denseNDArray[getIndex(x)];
    }

    public T get(TypedTuple<Integer> x) {
        int[] y = Arrays.stream(x.toArray()).mapToInt(Integer::intValue).toArray();
        checkIndexDimension(y.length);
        return (T) this.denseNDArray[getIndex(y)];
    }

    /**
     * Set value.
     *
     * @param x the x
     * @param value the value
     */
    public void set(int[] x, T value) {
        checkIndexDimension(x.length);
        denseNDArray[getIndex(x)] = value;
    }

    public void set(TypedTuple<Integer> x, T value) {
        int[] y = Arrays.stream(x.toArray()).mapToInt(Integer::intValue).toArray();
        checkIndexDimension(y.length);
        denseNDArray[getIndex(y)] = value;
    }

    /**
     * For each.
     *
     * @param function the function
     */
    public void forEach(Function<T, T> function) {
        int size = size();
        for (int i = 0; i < size; i++) {
            this.denseNDArray[i] = function.apply((T) this.denseNDArray[i]);
        }
    }

    /**
     * Get dense nD array.
     *
     * @param x the x
     * @return the dense nD array
     */
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
                int index = i % newDenseNDArray.powers[k + 1] / newDenseNDArray.powers[k];
                k = dx == 0 ? k : k + 1;
                y[j] = dx == 0 ? interval.getXmin() : interval.getXmin() + index;
            }
            newDenseNDArray.denseNDArray[i] = this.get(y);
        }
        return newDenseNDArray;
    }

    /**
     * Set void.
     *
     * @param x the x
     * @param vol the vol
     */
    public void set(String x, DenseNDArray<T> vol) {
        Interval<Integer>[] intervals = getIntervalFromStr(x);
        int size = vol.size();
        int[] y = new int[dim.length];
        for (int i = 0; i < size; i++) {
            int k = 0;
            for (int j = 0; j < intervals.length; j++) {
                Interval<Integer> interval = intervals[j];
                int dx = interval.getXmax() - interval.getXmin();
                int index = i % vol.powers[k + 1] / vol.powers[k];
                k = dx == 0 ? k : k + 1;
                y[j] = dx == 0 ? interval.getXmin() : interval.getXmin() + index;
            }
            this.set(y, (T) vol.denseNDArray[i]);
        }
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
                    intervals[i] = new Interval<>(integer, integer);
                    break;
                case 2:
                    int xmin = (int) MathUtil.clamp(Integer.valueOf(intervalBounds[0]), 0, dim[i] - 1);
                    int xmax = (int) MathUtil.clamp(Integer.valueOf(intervalBounds[1]), 0, dim[i] - 1);
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

    /**
     * Size int.
     *
     * @return the int
     */
    public int size() {
        return this.powers[this.powers.length - 1];
    }

    /**
     * Get dim.
     *
     * @return the int [ ]
     */
    public int[] getDim() {
        return this.dim;
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

    @Override
    public String toString() {
        return toStringRecursive(TypedTuple.EMPTY, this.dim[this.dim.length - 1]).toString();
    }

    private StringBuilder toStringRecursive(TypedTuple<Integer> coord, int d) {
        StringBuilder stringBuilder = new StringBuilder();
        if (d > this.dim.length) {
            return stringBuilder;
        }
        if(coord.size() != this.dim.length) {
            stringBuilder.append("[ ");
            for (int j = 0; j < this.dim[d - 1]; j++) {
                stringBuilder.append(toStringRecursive(new TypedTuple<>(j).union(coord), d - 1));
            }
            stringBuilder.append(" ]");
        } else  {
            stringBuilder.append(StringUtils.interp("{}, ", this.get(coord)));
        }
        return stringBuilder;
    }

    public static DenseNDArrayBuilder builder() {
        return new DenseNDArray.DenseNDArrayBuilder();
    }


    /**
     * Dense Array builder allows change in size.
     * Size is changing according valid concatenation rules
     * @param <K>
     */
    public static class DenseNDArrayBuilder<K> {
        private List<DenseNDArray<K>> listOfLowerDimArray;
        DenseNDArrayBuilder() {
            this.listOfLowerDimArray = new ArrayList<>();
        }

        public void add(DenseNDArray<K> array) {
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
        }

        public DenseNDArray<K> build() {
            int[] dim = this.listOfLowerDimArray.get(0).getDim();
            int size = this.listOfLowerDimArray.size();
            List<Integer> arrayList = Arrays.stream(dim).boxed().collect(Collectors.toList());
            arrayList.add(size);
            return new DenseNDArray<>(new int[]{1,1});
        }
    }
}
