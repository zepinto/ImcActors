package pt.lsts.imcactors.platform.environment;

import pt.lsts.imc4j.msg.Message;

public interface ICommMedium extends IDevice {

    default Message transmit(Message m, PhysicalState src, PhysicalState dst) {
        return null;
    }
}
