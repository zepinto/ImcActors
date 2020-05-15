package pt.lsts.imcactors.platform.environment;

import pt.lsts.imc4j.annotations.Parameter;
import pt.lsts.imc4j.msg.Message;

public class AcousticMedium implements ICommMedium {

    @Parameter(description = "Sound speed, in m/s")
    double soundSpeed = 1500;

    @Parameter(description = "Baud rate in Bytes/s")
    double baudrate = 100;

    @Parameter(description = "Maximum range, in meters")
    double maximumRange = 2500;

    @Override
    public String name() {
        return "AcousticModem";
    }

    @Override
    public Message transmit(Message m, PhysicalState src, PhysicalState dst) {
        // if one of the vehicles is outside the water...
        if (src.getDepth() < 0 || dst.getDepth() < 0)
            return null;

        // vehicles are too far away
        if (src.distance(dst) > maximumRange)
            return null;

        // add latency to the message
        int msgSize = m.serializeFields().length;
        double latency = (src.distance(dst) / soundSpeed) + msgSize / baudrate;
        m.timestamp += latency;

        return m;
    }
}
