package pt.lsts.imcactors.environment;

import pt.lsts.imc4j.msg.Message;
import pt.lsts.imcactors.util.ImcUtilities;

import java.util.Map;

public abstract class AbstractMedium extends AbstractDevice implements ICommMedium {

    /**
     * Calculate the latency after which the message is received in the destination platform
     * @param src Source of the message
     * @param dst Destination of the message
     * @param m Message to transmit
     * @return Delay, in seconds, after which the message is received.
     * If the message shall not be transmitted, the value <code>null</code> shall be returned.
     */
    abstract Double transmissionDelay(PhysicalState src, PhysicalState dst, Message m);

    private void transmit(Message m, int platform) {
        Double latency = transmissionDelay(state, environment.getState(platform), m);

        if (latency == null)
            return;

        Message delayed = ImcUtilities.clone(m);
        delayed.timestamp += latency;
        environment.deliver(delayed, platform);
    }

    @Override
    public void transmit(Message m) {
        if (m.dst == 0xFFFF) {
            for (Map.Entry<Integer, PhysicalState> platf : environment.physicalStates().entrySet()) {
                PhysicalState dst = platf.getValue();

                if (dst == state)
                    continue;
                transmit(m, platf.getKey());
            }
        }
        else {
            transmit(m, m.dst);
        }
    }
}
