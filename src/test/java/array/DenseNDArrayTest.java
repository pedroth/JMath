package array;

import org.junit.Assert;
import org.junit.Test;

public class DenseNDArrayTest {

    @Test
    public void denseTest() {
        DenseNDArray<Integer> table = new DenseNDArray<>(new int[] { 3, 3, 3 });
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    table.set(new int[] { i, j, k }, i + 3 * j + 9 * k);
                }
            }
        }
        System.out.println(table);

        Assert.assertTrue(table.get("1,:,:").get(new int[] { 0, 0 }) == 1);
        Assert.assertTrue(table.get("1,:,:").get(new int[] { 1, 1 }) == 13);
        Assert.assertTrue(table.get("1,:,:").get(new int[] { 2, 2 }) == 25);

        DenseNDArray<Integer> secondTable = table.get("0 : 1, 1 : 2 , : ");
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

        Assert.assertTrue(table.get(new int[] { 0, 0, 0 }) == 0);
        Assert.assertTrue(table.get(new int[] { 1, 1, 1 }) == 100 && table.get(new int[] { 1, 2, 1 }) == 100);
        Assert.assertTrue(table.get(new int[] { 2, 2, 2 }) == 26);
    }

    @Test
    public void denseCreationTest() {
        DenseNDArray<Integer> denseNDArray = new DenseNDArray<>(new int[]{1});
    }
}
