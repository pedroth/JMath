package tuple;

import exceptions.MatrixRunTimeException;
import utils.Pair;
import utils.StringUtils;

import java.util.Arrays;

/**
 * Immutable tuple object
 */
public class Tuple {
    public static final Tuple EMPTY = Tuple.of();

    private boolean isEmpty;
    private Object[] tuple;
    private Class[] classes;

    public Tuple(Object... objs) {
        if (objs == null || objs.length == 0) {
            this.isEmpty = true;
        } else {
            this.isEmpty = false;
            this.tuple = Arrays.copyOf(objs, objs.length);
            this.classes = new Class[objs.length];
            for (int i = 0; i < objs.length; i++) {
                this.classes[i] = objs[i].getClass();
            }
        }
    }

    private Tuple(Class[] classes, Object[] objs) {
        if (objs == null || objs.length == 0) {
            this.isEmpty = true;
            this.tuple = null;
        } else {
            this.isEmpty = false;
            this.tuple = Arrays.copyOf(objs, objs.length);
            this.classes = Arrays.copyOf(classes, classes.length);
        }
    }

    public <T> T get(int i) {
        if (isEmpty) {
            throw new MatrixRunTimeException("Access empty tuple");
        }
        if (i < 0 || i > this.tuple.length - 1) {
            throw new MatrixRunTimeException("Index out of range");
        }
        return (T) this.classes[i].cast(this.tuple[i]);
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public int size() {
        return this.isEmpty ? 0 : this.tuple.length;
    }

    public Tuple union(Tuple tuple) {
        if (this.isEmpty && tuple.isEmpty) {
            return Tuple.EMPTY;
        }
        if(this.isEmpty) {
            return tuple;
        }
        if(tuple.isEmpty) {
            return this;
        }
        int length = this.tuple.length;
        int inputLength = tuple.tuple.length;
        Object[] objsUnion = Arrays.copyOf(this.tuple, length + inputLength);
        Class[] classesUnion = Arrays.copyOf(this.classes, length + inputLength);
        System.arraycopy(tuple.tuple, 0, objsUnion, length, inputLength);
        System.arraycopy(tuple.classes, 0, classesUnion, length, inputLength);
        return new Tuple(classesUnion, objsUnion);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(this.tuple.length << 1);
        stringBuilder.append("< ");
        for (int i = 0; i < this.tuple.length; i++) {
            stringBuilder.append(StringUtils.interp("{}" + (i == this.tuple.length - 1 ? "" : ", "), Pair.of(this.classes[i], this.tuple[i])));
        }
        stringBuilder.append(" >");
        return stringBuilder.toString();
    }

    public static Tuple of(Object... objs) {
        return new Tuple(objs);
    }
}
