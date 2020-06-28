package pt.lsts.imcactors.platform.events;

import pt.lsts.imc4j.msg.Message;
import pt.lsts.imcactors.actors.AbstractActor;
import pt.lsts.imcactors.platform.ImcPlatform;

import java.util.ArrayList;
import java.util.List;

public class OutgoingEvent extends PlatformEvent {

    private Message msg;

    public OutgoingEvent(AbstractActor actor, Message message) {
        super(actor, (long)(message.timestamp * 1000.0));
        this.msg = message;
    }

    @Override
    public List<PlatformEvent> processEvent(ImcPlatform platform) {
        ArrayList<PlatformEvent> events = new ArrayList<>();
        platform.subscribingActors(msg.getClass()).forEach(target -> {
            boolean loopback = (actor == target);
            events.add(new IncomingEvent(target, msg, loopback));
        });
        return events;
    }

    @Override
    public String describe() {
        return msg.toString();
    }
}
