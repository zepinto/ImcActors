package pt.lsts.imcactors.platform.events;

import pt.lsts.imcactors.platform.ImcPlatform;
import pt.lsts.imcactors.platform.PeriodicScheduler;

public class PeriodicEvent extends PlatformEvent {
    private PeriodicScheduler.PeriodicCallback callback;
    public PeriodicEvent(ImcPlatform platform, PeriodicScheduler.PeriodicCallback callback) {
        super(platform, callback.getNextCallbackTimeMillis());
        this.callback = callback;

    }

    @Override
    public void processEvent() {
        platform.processCallback(callback);
    }

    @Override
    public String describe() {
        return "PeriodicCallback{"+callback+"}";
    }
}
