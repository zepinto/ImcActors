package pt.lsts.imcactors.environment;

import pt.lsts.imc4j.annotations.Parameter;
import pt.lsts.imc4j.msg.Message;

public class SatelliteMedium extends AbstractMedium {

    @Parameter(description = "Baudrate in Bytes/s")
    double baudrate = 50;

    @Parameter(description = "Baudrate in Bytes/s")
    double fixedDelay = 30;

    @Override
    public String name() {
        return "Satellite";
    }


    @Override
    Double transmissionDelay(PhysicalState src, PhysicalState dst, Message m) {
        if (src.getDepth() > 0 || dst.getDepth() > 0)
            return null;

        int msgSize = m.serialize().length;

        return fixedDelay + msgSize / baudrate;
    }
}
