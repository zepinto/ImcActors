package pt.lsts.imcactors.platform.events;

import pt.lsts.imcactors.actors.AbstractActor;
import pt.lsts.imcactors.platform.ImcPlatform;

import java.util.ArrayList;
import java.util.List;

/**
 * Actor becomes inactive
 */
public class DestroyActorEvent extends PlatformEvent {

    public DestroyActorEvent(AbstractActor actor, long timestamp) {
        super(actor, timestamp);
    }

    @Override
    public List<PlatformEvent> processEvent(ImcPlatform platform) {
        platform.destroyActor(actor.getClass().getSimpleName());
        return new ArrayList<>();
    }

    @Override
    public String describe() {
        return "DestroyActor{}";
    }
}
