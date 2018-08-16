package array;

import org.junit.Assert;
import org.junit.Test;
import tuple.TypedTuple;
import utils.ArrayUtils;
import utils.StringUtils;

import java.util.Arrays;
import java.util.function.Function;

public class DenseNDArrayTest {

    @Test
    public void testBasic() {
        final DenseNDArray<Double> denseNDArray = new DenseNDArray<>(new Double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0}, new int[]{3, 3});
        Assert.assertEquals(denseNDArray.get(TypedTuple.of(0, 0)), 1.0, 1E-10);
        Assert.assertEquals(denseNDArray.get(TypedTuple.of(1, 2)), 8.0, 1E-10);
        Assert.assertEquals(denseNDArray.get(TypedTuple.of(0, 2)), 7.0, 1E-10);
        Assert.assertEquals(denseNDArray.get(TypedTuple.of(2, 1)), 6.0, 1E-10);

        final DenseNDArray<Double> denseNDArray1 = new DenseNDArray<>(denseNDArray, new int[]{9, 1});
        Assert.assertEquals(denseNDArray1.get(TypedTuple.of(0, 0)), 1.0, 1E-10);
        Assert.assertEquals(denseNDArray1.get(TypedTuple.of(4, 0)), 5.0, 1E-10);
        Assert.assertEquals(denseNDArray1.get(TypedTuple.of(8, 0)), 9.0, 1E-10);

        final DenseNDArray<Double> denseNDArray2 = new DenseNDArray<>(denseNDArray, new int[]{9});
        Assert.assertEquals(denseNDArray2.get(TypedTuple.of(0)), 1.0, 1E-10);
        Assert.assertEquals(denseNDArray2.get(TypedTuple.of(4)), 5.0, 1E-10);
        Assert.assertEquals(denseNDArray2.get(TypedTuple.of(8)), 9.0, 1E-10);

        Assert.assertEquals(denseNDArray.get("1,2").get(), 8.0, 1E-10);
        Assert.assertEquals(denseNDArray.get("1,1").get(), 5.0, 1E-10);
    }


    @Test
    public void testDense() {
        DenseNDArray<Integer> table = new DenseNDArray<>(new int[]{3, 3, 3});
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    table.set(new int[]{i, j, k}, i + 3 * j + 9 * k);
                }
            }
        }
        System.out.println(StringUtils.interp("table : {}", table));

        Assert.assertTrue(table.get("1,:,:").get(new int[]{0, 0}) == 1);
        Assert.assertTrue(table.get("1,:,:").get(new int[]{1, 1}) == 13);
        Assert.assertTrue(table.get("1,:,:").get(new int[]{2, 2}) == 25);
        Assert.assertTrue(table.get("1,:,:").get(new int[]{2, 1}) == 16);

        DenseNDArray<Integer> secondTable = table.get("0 : 1, 1 : 2 , : ");
        System.out.println(StringUtils.interp("secondTable : {}", secondTable));

        Assert.assertTrue(secondTable.get(new int[]{1, 1, 0}) == 7);
        Assert.assertTrue(secondTable.get(new int[]{1, 1, 1}) == 16);
        Assert.assertTrue(secondTable.get(new int[]{1, 1, 2}) == 25);

        DenseNDArray<Integer> thirdTable = new DenseNDArray<>(new int[]{3, 3});
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 3; i++) {
                thirdTable.set(new int[]{i, j}, 100);
            }
        }

        table.set("1,:,:", thirdTable);

        Assert.assertTrue(table.get(new int[]{1, 0, 0}) == 100);
        Assert.assertTrue(table.get(new int[]{1, 1, 1}) == 100);
        Assert.assertTrue(table.get(new int[]{1, 1, 2}) == 100);
        Assert.assertTrue(table.get(new int[]{1, 2, 2}) == 100);
        Assert.assertTrue(table.get(new int[]{0, 2, 2}) == 24);

        DenseNDArray<Integer> denseNDArray = table.get("1:,0:,:1");
        Assert.assertTrue(Arrays.equals(denseNDArray.getDim(), new int[]{2, 3, 2}));
        Assert.assertTrue(denseNDArray.get(TypedTuple.of(0, 0, 1)) == 100);
        Assert.assertTrue(denseNDArray.get(TypedTuple.of(1, 1, 0)) == 5);
        Assert.assertTrue(denseNDArray.get(TypedTuple.of(1, 2, 1)) == 17);
    }

    @Test
    public void testDenseCreation() {
        DenseNDArray<Double> array = new DenseNDArray<>(new Double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0}, new int[]{3, 3});
        DenseNDArray<Double> cube = DenseNDArray.builder().add(array).add(array).add(array).build();
        Assert.assertTrue(Arrays.equals(new int[]{3, 3, 3}, cube.getDim()));
        Assert.assertTrue(array.equals(cube.get(":,:,0")));
        Assert.assertTrue(array.equals(cube.get(":,:,1")));
        Assert.assertTrue(array.equals(cube.get(":,:,2")));
        DenseNDArray<Integer> secondArray = DenseNDArray.builder().add(new Integer[]{1, 2, 3}).add(new Integer[]{4, 5, 6}).build();
        Assert.assertTrue(secondArray.get(new int[]{0, 1}) == 4);
        Assert.assertTrue(secondArray.get(new int[]{1, 1}) == 5);
        Assert.assertTrue(secondArray.get(new int[]{2, 0}) == 3);
        DenseNDArray<Integer> thirdArray = DenseNDArray.builder().add(new Integer[][]{{1, 2, 3}, {4, 5, 6}}).add(new Integer[][]{{7, 8, 9}, {10, 11, 12}}).build();
        Assert.assertTrue(thirdArray.get(new int[]{0, 1, 0}) == 4);
        Assert.assertTrue(thirdArray.get(new int[]{1, 1, 0}) == 5);
        Assert.assertTrue(thirdArray.get(new int[]{2, 0, 0}) == 3);
        Assert.assertTrue(thirdArray.get(new int[]{2, 0, 1}) == 9);

        DenseNDArray build = DenseNDArray.builder().add(new Integer[][]{{1, 2}, {3, 4}, {5, 6}}).build();
        DenseNDArray buildEqual = new DenseNDArray(new Integer[]{1, 2, 3, 4, 5, 6}, new int[]{2, 3});
        Assert.assertTrue(Arrays.equals(build.shape(), new int[]{2, 3}));
        Assert.assertTrue(build.equals(buildEqual));
    }


    @Test
    public void testMap() {
        DenseNDArray<Integer> array = new DenseNDArray<>(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9}, new int[]{3, 3});
        DenseNDArray<Integer> arraySq = new DenseNDArray<>(new Integer[]{1, 4, 9, 16, 25, 36, 49, 64, 81}, new int[]{3, 3});
        Assert.assertTrue(array.map(x -> x * x).equals(arraySq));
    }

    @Test
    public void testReduce() {
        int n = 10;
        double sum = n * (n - 1) / 2.0;
        DenseNDArray<Integer> array = new DenseNDArray<>(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9}, new int[]{3, 3});
        Assert.assertEquals(array.reduce(0.0, (x, y) -> x + y), sum, 1E-10);
        array.forEach(System.out::println);
        SingletonAcumulator acc = new SingletonAcumulator();
        array.forEach(acc::add);
        Assert.assertEquals(acc.acc, sum, 1E-10);
    }

    static final class SingletonAcumulator {
        int acc = 0;

        SingletonAcumulator() {
        }

        void add(int value) {
            this.acc += value;
        }
    }
}
