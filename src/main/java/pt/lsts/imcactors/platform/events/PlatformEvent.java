package pt.lsts.imcactors.platform.events;

import pt.lsts.imcactors.ImcActor;
import pt.lsts.imcactors.platform.ImcPlatform;

import java.util.List;

public abstract class PlatformEvent implements Comparable<PlatformEvent> {
    protected ImcActor actor;
    protected long timestamp;

    public PlatformEvent(ImcActor actor, long timestamp) {
        this.actor = actor;
        this.timestamp = timestamp;
    }

    public abstract List<PlatformEvent> processEvent(ImcPlatform platform);

    public abstract String describe();

    @Override
    public String toString() {
        return getClass().getSimpleName().replaceAll("Event", "")
                +"{" +
                    "timestamp=" + timestamp +
                    ", actor=" + actor +
                    ", event="+describe()
                +"}";
    }

    @Override
    public int compareTo(PlatformEvent o) {
        return toString().compareTo(o.toString());
    }
}
