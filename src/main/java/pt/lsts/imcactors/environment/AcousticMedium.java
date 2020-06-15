package pt.lsts.imcactors.environment;

import pt.lsts.imc4j.annotations.Parameter;
import pt.lsts.imc4j.msg.Message;
import pt.lsts.imcactors.util.ImcUtilities;

public class AcousticMedium extends AbstractMedium {

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
    Double transmissionDelay(PhysicalState src, PhysicalState dst, Message m) {
        if (src.getDepth() < 0 || dst.getDepth() < 0)
            return null;
        if (src.distance(dst) > maximumRange)
            return null;
        int msgSize = m.serializeFields().length;
        return (src.distance(dst) / soundSpeed) + msgSize / baudrate;
    }
}
