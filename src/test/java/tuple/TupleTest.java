package tuple;

import fields.Complex;
import org.junit.Assert;
import org.junit.Test;
import utils.StopWatch;
import utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TupleTest {

    @Test
    public void testTuple() {
        Tuple tuple = new Tuple(1, new Tuple(1, "Pedro"), new Complex(1, 2));
        Tuple tuple1 = tuple.get(1);
        Assert.assertTrue("Pedro".equals(tuple1.get(1)));
        Assert.assertTrue(new Complex(1, 2).equals(tuple.get(2)));
        Assert.assertTrue(Tuple.of(null).isEmpty());
        Assert.assertTrue(Tuple.of(new Tuple[]{}).isEmpty());
    }

    @Test
    public void testUnion() {
        Tuple t1 = Tuple.of(1, 2, 3);
        Tuple union = t1.union(Tuple.of(4.0, 5.0));
        System.out.println(union);
        Assert.assertTrue(union.get(0).equals(1));
        Assert.assertTrue(union.get(4).equals(5.0));
    }

    @Test
    public void testSpeed() {
        int samples = 1000;
        int sampleUnions = 1000;
        double eleapsedTimeTuples = 0;
        double eleapsedTimeList = 0;
        for (int i = 0; i < samples; i++) {
            StopWatch stopWatch = new StopWatch();
            Tuple accTuple = Tuple.of(1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
            for (int j = 0; j < sampleUnions; j++) {
                accTuple.union(Tuple.of(1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
            }
            eleapsedTimeTuples += stopWatch.getEleapsedTime();
            stopWatch.resetTime();

            ArrayList<Integer> accList = new ArrayList<>(Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
            for (int j = 0; j < sampleUnions; j++) {
                accList.addAll(Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
            }
            eleapsedTimeList += stopWatch.getEleapsedTime();
        }
        Pair<Double, Double> pair = Pair.of(eleapsedTimeTuples / samples, 2 * (eleapsedTimeList / samples));
        System.out.println(StringUtils.interp("{} : {}", pair, pair.getFirst() / pair.getSecond()));
    }

    @Test
    public void testPointer() {
        List<String> list = Arrays.asList("1+2i", "1-2i");
        Tuple tuple = new Tuple(1, new Tuple(1, "Pedro"), list);
        System.out.println(tuple);
        list.set(1, "0");
        List<String> listTuple = tuple.get(2);
        Assert.assertTrue("0".equals(listTuple.get(1)));
    }
}
