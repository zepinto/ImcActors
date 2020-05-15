package pt.lsts.imcactors.platform.events;

import pt.lsts.imc4j.msg.Message;
import pt.lsts.imcactors.ImcActor;
import pt.lsts.imcactors.platform.ImcPlatform;
import pt.lsts.imcactors.util.ImcUtilities;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OutgoingEvent extends PlatformEvent {

    private Message msg;

    public OutgoingEvent(ImcActor actor, Message message) {
        super(actor, (long)(message.timestamp * 1000.0));
        this.msg = message;
    }

    @Override
    public List<PlatformEvent> processEvent(ImcPlatform platform) {
        ArrayList<PlatformEvent> events = new ArrayList<>();
        platform.subscribingActors(msg.getClass()).forEach(target -> {
            if (actor == target)
            events.add(new IncomingEvent(target, msg));
        });
        return events;
    }

    @Override
    public String describe() {
        return msg.toString();
    }
}
