package pt.lsts.imcactors.platform.events;

import pt.lsts.imcactors.platform.ImcPlatform;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Instantiate a new actor from its specification and state
 */
public class CreateActorEvent extends PlatformEvent {

    String actorClass;
    LinkedHashMap<String, String> state;
    public CreateActorEvent(String actorClass, LinkedHashMap<String, String> initialState, long timestamp) {
        super(null, timestamp);
        this.actorClass = actorClass;
        this.state = initialState;
    }

    @Override
    public List<PlatformEvent> processEvent(ImcPlatform platform) {
        try {
            platform.instantiateActor(actorClass, state);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public String describe() {
        return "CreateActor{class="+actorClass+", state="+state+"}";
    }
}
