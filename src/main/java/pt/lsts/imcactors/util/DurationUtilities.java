package pt.lsts.imcactors.util;

import java.time.Duration;

public class DurationUtilities {
    public static long parseDuration(String duration) {
        try {
            return Duration.parse("PT"+duration).toMillis();
        }
        catch (Exception e) {
            return 1000;
        }
    }
}
