package pt.lsts.imcactors.platform.events;

import pt.lsts.imc4j.msg.Message;
import pt.lsts.imcactors.actors.ImcActor;
import pt.lsts.imcactors.platform.ImcPlatform;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class IncomingEvent extends PlatformEvent {

    private Message msg;
    private boolean loopback;

    public IncomingEvent(ImcActor actor, Message message, boolean loopback) {
        super(actor, (long)(message.timestamp * 1000.0));
        this.msg = message;
        this.loopback = loopback;
    }

    @Override
    public List<PlatformEvent> processEvent(ImcPlatform platform) {
        ArrayList<Message> messages = actor.process(msg, loopback);
        return messages.stream()
                .map(m -> new OutgoingEvent(actor, m)).collect(Collectors.toList());
    }

    @Override
    public String describe() {
        return msg.toString();
    }
}
