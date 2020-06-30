package pt.lsts.imcactors.platform.events;

import pt.lsts.imc4j.msg.Message;
import pt.lsts.imcactors.platform.ImcPlatform;
import pt.lsts.imcactors.util.ImcUtilities;

public class IncomingMessageEvent extends PlatformEvent {

    ImcPlatform source;
    Message msg;

    public IncomingMessageEvent(ImcPlatform source, ImcPlatform destination, Message message, long timestamp) {
        super(destination, timestamp);
        this.msg = message;
        this.source = source;
    }

    @Override
    public void processEvent() {
        Message clone = ImcUtilities.clone(msg);
        platform.postMessage(msg);
    }

    @Override
    public String describe() {
        return "Incoming{"+msg+"}";
    }
}
