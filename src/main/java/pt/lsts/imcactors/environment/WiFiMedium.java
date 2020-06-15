package pt.lsts.imcactors.environment;

import pt.lsts.imc4j.annotations.Parameter;
import pt.lsts.imc4j.msg.Message;

import java.util.Random;

public class WiFiMedium extends AbstractMedium {

    @Parameter(description = "Maximum range, in meters")
    double maximumRange = 1000;

    @Parameter(description = "Average delay, in milliseconds")
    double averageDelay = 0;

    @Parameter(description = "Random seed")
    long randomSeed = 0;

    private Random random = null;

    @Override
    public String name() {
        return "WiFi";
    }

    @Override
    Double transmissionDelay(PhysicalState src, PhysicalState dst, Message m) {
        if (random == null)
            random = new Random(randomSeed);

        if (src.distance(dst) < maximumRange)
            return random.nextGaussian() * averageDelay + averageDelay;
        else
            return null;
    }
}
