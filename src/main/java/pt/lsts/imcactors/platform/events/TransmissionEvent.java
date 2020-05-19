package pt.lsts.imcactors.platform.events;

import pt.lsts.imc4j.msg.Message;
import pt.lsts.imcactors.ImcActor;
import pt.lsts.imcactors.platform.ImcPlatform;
import pt.lsts.imcactors.platform.environment.ICommMedium;

import java.util.List;

public class TransmissionEvent extends PlatformEvent {

    private Message message;
    private String medium;

    public TransmissionEvent(ImcActor actor, Message message, String medium, long timestamp) {
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
