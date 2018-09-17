package fields;

import org.junit.Assert;
import org.junit.Test;

public class ComplexTest {

    @Test
    public void testComplexOperations() {
        Assert.assertTrue(
        		Complex.I
        		.prod(Complex.I)
        		.prod(Complex.I)
        		.add(Complex.of(1,0))
        		.sub(Complex.of(1,0))
        		.equals(Complex.I.conj())
        		);
    }
}
