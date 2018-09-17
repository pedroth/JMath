package fields;

import org.junit.Assert;
import org.junit.Test;

public class RealTest {

    @Test
    public void testRealOperations() {
        Assert.assertTrue(new Real(1)
                             .prod(new Real(2))
                             .add(Real.of(1))
                             .sub(Real.of(1))
                             .equals(new Real(2))
        );
        Assert.assertTrue(Real.of(1)
                          .conj()
                          .equals(Real.of(1))
        );
        Assert.assertTrue(Real.of(1)
                          .symmetric()
                          .equals(Real.of(-1))
        );
    }
}
