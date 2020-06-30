package pt.lsts.imcactors.platform.events;

import pt.lsts.imcactors.actors.AbstractActor;
import pt.lsts.imcactors.platform.ImcPlatform;

public abstract class PlatformEvent implements Comparable<PlatformEvent> {
    protected AbstractActor actor;
    protected ImcPlatform platform;
    protected long timestamp;

    public PlatformEvent(ImcPlatform platform, long timestamp) {
        this.platform = platform;
        this.timestamp = timestamp;
    }

    public abstract void processEvent();

    public abstract String describe();

    @Override
    public String toString() {
        return getClass().getSimpleName().replaceAll("Event", "")
                +"{" +
                    "timestamp=" + timestamp +
                    ", platform=" + platform +
                    ", event="+describe()
                +"}";
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public int compareTo(PlatformEvent o) {
        return Long.compare(timestamp, o.timestamp);
    }
}
