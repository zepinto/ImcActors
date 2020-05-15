package pt.lsts.imcactors.platform.environment;

import pt.lsts.imc4j.annotations.Parameter;
import pt.lsts.imc4j.msg.Message;

public class SatelliteMedium implements ICommMedium {

    @Parameter(description = "Baudrate in Bytes/s")
    double baudrate = 50;

    @Override
    public String name() {
        return "Satellite";
    }

    @Override
    public Message transmit(Message m, PhysicalState src, PhysicalState dst) {
        // platforms need to be at or above the surface
        if (src.getDepth() > 0 || dst.getDepth() > 0)
            return null;

        // add latency
        m.timestamp += m.serializeFields().length / baudrate;
        return m;
    }
}
