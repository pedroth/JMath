package tuple;

import exceptions.MatrixRunTimeException;
import utils.StringUtils;

import java.util.Arrays;

public class TypedTuple<T> {
    public static final TypedTuple EMPTY = new TypedTuple<>();

    private boolean isEmpty;
    private T[] tuple;

    public TypedTuple(T ... objs) {
        if (objs == null || objs.length == 0) {
            this.isEmpty = true;
            this.tuple = null;
        } else {
            this.isEmpty = false;
            this.tuple = Arrays.copyOf(objs, objs.length);
        }
    }

    public T get(int i) {
        if (isEmpty) {
            throw new MatrixRunTimeException("Access empty tuple");
        }
        if (i < 0 || i > this.tuple.length - 1) {
            throw new MatrixRunTimeException("Index out of range");
        }
        return this.tuple[i];
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public int size() {
        return this.isEmpty ? 0 : this.tuple.length;
    }

    public TypedTuple<T> union(TypedTuple<T> tuple) {
        if (this.isEmpty && tuple.isEmpty) {
            return TypedTuple.EMPTY;
        }
        if(this.isEmpty) {
            return tuple;
        }
        if(tuple.isEmpty) {
            return this;
        }
        int size = this.size();
        int inputSize = tuple.size();
        T[] objsUnion = Arrays.copyOf(this.tuple, size + inputSize);
        System.arraycopy(tuple.tuple, 0, objsUnion, size, inputSize);
        return new TypedTuple<>(objsUnion);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(this.tuple.length << 1);
        stringBuilder.append("< ");
        for (int i = 0; i < this.tuple.length; i++) {
            stringBuilder.append(StringUtils.interp("{}" + (i == this.tuple.length - 1 ? "" : ", "), this.tuple[i]));
        }
        stringBuilder.append(" >");
        return stringBuilder.toString();
    }

    public T[] toArray() {
        return Arrays.copyOf(this.tuple, this.size());
    }

    public static <K> TypedTuple of(K ... objs) {
        return new TypedTuple<>(objs);
    }
}
