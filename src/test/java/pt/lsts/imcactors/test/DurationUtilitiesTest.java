package pt.lsts.imcactors.test;

import org.junit.Assert;
import org.junit.Test;
import pt.lsts.imcactors.util.DurationUtilities;

public class DurationUtilitiesTest {

    @Test
    public void durationsTest() {
        Assert.assertEquals(10_000, DurationUtilities.parseDuration("10s"));
        Assert.assertEquals(60_000, DurationUtilities.parseDuration("1m"));
        Assert.assertEquals(3_661_000, DurationUtilities.parseDuration("1h1m1s"));
    }
}
