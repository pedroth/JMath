package interval;

import java.util.Objects;

public class Interval<T extends Comparable<T>> {
    private T xmin, xmax;
    private boolean isEmptyInterval;

    public Interval(T xmin, T xmax) {
        if (xmax.compareTo(xmin) >= 0) {
            this.xmin = xmin;
            this.xmax = xmax;
            this.isEmptyInterval = false;
        } else {
            this.isEmptyInterval = true;
        }
    }

    public T getXmin() {
        return xmin;
    }

    public T getXmax() {
        return xmax;
    }

    public Interval<T> union(Interval<T> interval) {
        T newXmin = min(this.xmin, interval.getXmin());
        T newXmax = max(this.xmax, interval.getXmax());
        return new Interval<>(newXmin, newXmax);
    }

    private T max(T x, T y) {
        return x.compareTo(y) >= 0 ? x : y;
    }

    private T min(T x, T y) {
        return x.compareTo(y) <= 0 ? x : y;
    }

    public Interval<T> intersection(Interval<T> interval) {
        T newXmin = max(this.xmin, interval.getXmin());
        T newXmax = min(this.xmax, interval.getXmax());
        return new Interval<>(newXmin, newXmax);
    }

    public boolean isEmptyInterval() {
        return isEmptyInterval;
    }

    public void setEmptyInterval(boolean emptyInterval) {
        isEmptyInterval = emptyInterval;
    }

    public boolean isOneItem() {
        return xmax.compareTo(xmin) == 0;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (isEmptyInterval) {
            return "Empty interval";
        }
        stringBuilder.append("[ ").append(xmin.toString()).append(" , ").append(xmax.toString()).append(" ]");
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Interval<?> interval = (Interval<?>) o;
        return isEmptyInterval == interval.isEmptyInterval &&
                Objects.equals(xmin, interval.xmin) &&
                Objects.equals(xmax, interval.xmax);
    }

    @Override
    public int hashCode() {
        return Objects.hash(xmin, xmax, isEmptyInterval);
    }
}
