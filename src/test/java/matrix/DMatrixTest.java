package matrix;

import fields.Real;
import org.junit.Assert;
import org.junit.Test;
import tuple.TypedTuple;

public class DMatrixTest {

    @Test
    public void matrixConstruction() {
        double delta = 0.001;
        DMatrix<Real> m1 = new DMatrix<>(new Real[][]{{Real.of(1), Real.of(0)},{Real.of(0), Real.of(1)}});
        Assert.assertEquals(m1.get(TypedTuple.of(1,1)).getDouble(), 1.0, delta);
        Assert.assertEquals(m1.get(TypedTuple.of(0,1)).getDouble(), 0.0, delta);
        Assert.assertEquals(m1.get("1,0").get().getDouble(), 0.0, delta);
        Assert.assertEquals(m1.get(new int[]{0,0}).getDouble(), 1.0, delta);
    }
}
