package interval;

import org.junit.Assert;
import org.junit.Test;

public class IntervalTest {

    @Test
    public void testIntersection() {
        Interval<Double> interval1 = new Interval<>(0.0,1.0);
        Interval<Double> interval2 = new Interval<>(0.5,1.5);
        Interval<Double> interval3 = new Interval<>(1.0,2.0);
        Interval<Double> interval4 = new Interval<>(2.0,3.0);
        Interval<Double> intersection1 = interval1.intersection(interval2);
        Interval<Double> intersection2 = interval1.intersection(interval3);
        Interval<Double> intersection3 = interval1.intersection(interval4);
        Assert.assertTrue(intersection1.equals(new Interval<>(0.5, 1.0)));
        Assert.assertTrue(intersection2.equals(new Interval<>(1.0, 1.0)));
        Assert.assertFalse(intersection2.isEmptyInterval());
        Assert.assertTrue(intersection2.isOneItem());
        Assert.assertTrue(intersection3.isEmptyInterval());
    }

    @Test
    public void testUnion() {
        Interval<Double> interval1 = new Interval<>(0.0,1.0);
        Interval<Double> interval2 = new Interval<>(0.5,1.5);
        Interval<Double> interval3 = new Interval<>(1.0,2.0);
        Interval<Double> interval4 = new Interval<>(2.0,3.0);
        Interval<Double> union1 = interval1.union(interval2);
        Interval<Double> union2 = interval1.union(interval3);
        Interval<Double> union3 = interval1.union(interval4);
        Assert.assertTrue(union1.equals(new Interval<>(0.0, 1.5)));
        Assert.assertTrue(union2.equals(new Interval<>(0.0, 2.0)));
        Assert.assertTrue(union3.equals(new Interval<>(0.0, 3.0)));
    }
}
