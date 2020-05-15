package pt.lsts.imcactors.platform.environment;

import pt.lsts.imc4j.msg.Message;

public interface ICommMedium {
    String name();

    default Message transmit(Message m, PhysicalState src, PhysicalState dst) {
        return null;
    }
}
