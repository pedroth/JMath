package array;

import org.junit.Assert;
import org.junit.Test;
import tuple.TypedTuple;
import utils.ArrayUtils;

import java.util.Arrays;

public class DenseNDArrayTest {

    @Test
    public void basicTest() {
        final DenseNDArray<Double> denseNDArray = new DenseNDArray<>(new Double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0}, new int[]{3, 3});
        Assert.assertEquals(denseNDArray.get(TypedTuple.of(0,0)), 1.0, 1E-10);
        Assert.assertEquals(denseNDArray.get(TypedTuple.of(1,2)), 6.0, 1E-10);
        Assert.assertEquals(denseNDArray.get(TypedTuple.of(0,2)), 3.0, 1E-10);
        Assert.assertEquals(denseNDArray.get(TypedTuple.of(2,1)), 8.0, 1E-10);

        final DenseNDArray<Double> denseNDArray1 = new DenseNDArray<>(denseNDArray, new int[]{9,1});
        Assert.assertEquals(denseNDArray1.get(TypedTuple.of(0,0)), 1.0, 1E-10);
        Assert.assertEquals(denseNDArray1.get(TypedTuple.of(4,0)), 5.0, 1E-10);
        Assert.assertEquals(denseNDArray1.get(TypedTuple.of(8,0)), 9.0, 1E-10);
    }


        @Test
    public void denseTest() {
        DenseNDArray<Integer> table = new DenseNDArray<>(new int[]{3, 3, 3});
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    table.set(new int[]{i, j, k}, i + 3 * j + 9 * k);
                }
            }
        }
        System.out.println(table);

        Assert.assertTrue(table.get("1,:,:").get(new int[]{0, 0}) == 1);
        Assert.assertTrue(table.get("1,:,:").get(new int[]{1, 1}) == 13);
        Assert.assertTrue(table.get("1,:,:").get(new int[]{2, 2}) == 25);

        DenseNDArray<Integer> secondTable = table.get("0 : 1, 1 : 2 , : ");
        System.out.println(secondTable);
        Assert.assertTrue(secondTable.get(new int[]{1, 1, 0}) == 6);
        Assert.assertTrue(secondTable.get(new int[]{1, 1, 1}) == 2);
        Assert.assertTrue(secondTable.get(new int[]{1, 1, 2}) == 14);

        DenseNDArray<Integer> thirdTable = new DenseNDArray<>(new int[]{3, 3});
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 3; i++) {
                thirdTable.set(new int[]{i, j}, 100);
            }
        }

        table.set("1,:,:", thirdTable);

        Assert.assertTrue(table.get(new int[]{0, 0, 0}) == 0);
        Assert.assertTrue(table.get(new int[]{1, 1, 1}) == 100 && table.get(new int[]{1, 2, 1}) == 100);
        Assert.assertTrue(table.get(new int[]{2, 2, 2}) == 26);

        DenseNDArray<Integer> denseNDArray = table.get("1:,0:,:1");
        Assert.assertTrue(Arrays.equals(denseNDArray.getDim(), new int[]{2, 3, 2}));
        Assert.assertTrue(11 == denseNDArray.get(TypedTuple.of(1, 1, 1)));

    }

    @Test
    public void denseCreationTest() {
        DenseNDArray array = new DenseNDArray(new Double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0}, new int[]{3, 3});
        DenseNDArray cube = DenseNDArray.builder()
                .add(array)
                .add(array)
                .add(array)
                .build();

        Assert.assertTrue(Arrays.equals(new int[]{3, 3, 3}, cube.getDim()));
        Assert.assertTrue(array.get(TypedTuple.of(2,2)).equals(cube.get(":,:,1").get(TypedTuple.of(2,2))));
    }
}
