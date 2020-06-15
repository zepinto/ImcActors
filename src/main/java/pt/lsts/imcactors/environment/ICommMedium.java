package pt.lsts.imcactors.environment;

import pt.lsts.imc4j.msg.Message;

public interface ICommMedium extends IDevice {
    void transmit(Message m);
}
