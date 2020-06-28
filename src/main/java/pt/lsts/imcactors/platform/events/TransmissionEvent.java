package pt.lsts.imcactors.platform.events;

import pt.lsts.imc4j.msg.Message;
import pt.lsts.imcactors.actors.AbstractActor;
import pt.lsts.imcactors.platform.ImcPlatform;

import java.util.List;

public class TransmissionEvent extends PlatformEvent {

    private Message message;
    private String medium;

    public TransmissionEvent(AbstractActor actor, Message message, String medium, long timestamp) {
        super(actor, timestamp);
        this.medium = medium;
        this.message = message;
    }
    @Override
    public List<PlatformEvent> processEvent(ImcPlatform platform) {
        return null;
    }

    @Override
    public String describe() {
        return null;
    }
}
