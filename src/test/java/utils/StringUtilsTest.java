package utils;

import org.junit.Assert;
import org.junit.Test;

public class StringUtilsTest {
    @Test
    public void interpolateTest() {
        String interp = StringUtils.interp("{} : {}", 1, 2);
        Assert.assertTrue("1 : 2".equals(interp));
    }

}
