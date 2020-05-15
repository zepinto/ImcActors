package pt.lsts.imcactors.platform.environment;

import pt.lsts.imc4j.annotations.Parameter;
import pt.lsts.imc4j.msg.Message;

import java.util.Random;

public class WiFiMedium implements ICommMedium {

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
    public Message transmit(Message m, PhysicalState src, PhysicalState dst) {

        if (random == null)
            random = new Random(randomSeed);

        // delay the message according to average delay
        if (averageDelay > 0)
            m.timestamp += random.nextGaussian() * averageDelay + averageDelay;;

        if (src.distance(dst) < maximumRange)
            return m;
        else
            return null;
    }
}
