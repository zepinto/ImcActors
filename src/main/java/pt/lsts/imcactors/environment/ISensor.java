package pt.lsts.imcactors.environment;

import pt.lsts.imc4j.msg.Message;

public interface ISensor<M extends Message> extends IDevice {
    default M sample() {
        return null;
    }
}
