package pt.lsts.imcactors.platform.events;

import pt.lsts.imcactors.actors.ImcActor;
import pt.lsts.imcactors.platform.ImcPlatform;

import java.util.ArrayList;
import java.util.List;

/**
 * Actor becomes inactive
 */
public class DestroyActorEvent extends PlatformEvent {

    public DestroyActorEvent(ImcActor actor, long timestamp) {
        super(actor, timestamp);
    }

    @Override
    public List<PlatformEvent> processEvent(ImcPlatform platform) {
        platform.destroyActor(actor);
        return new ArrayList<>();
    }

    @Override
    public String describe() {
        return "DestroyActor{}";
    }
}
