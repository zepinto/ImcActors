package pt.lsts.imcactors.platform;

import pt.lsts.imc4j.msg.Message;
import pt.lsts.imcactors.ImcActor;
import pt.lsts.imcactors.platform.clock.IPlatformClock;
import pt.lsts.imcactors.platform.clock.RealTimeClock;

import java.util.ArrayList;
import java.util.List;

public class ImcPlatform {

    private IPlatformClock clock = new RealTimeClock();
    private ArrayList<ImcActor> actors = new ArrayList<>();

    private void registerActor(Class<? extends ImcActor> actorClass) throws Exception {
        ImcActor actor = actorClass.newInstance();
        actor.init(this);
        ArrayList<Class<? extends Message>> subs = new ArrayList<>();
        subs.addAll(actor.getSubscriptions());
        actors.add(actor);
    }

    public long timeSinceEpoch() {
        return clock.currentTime();
    }

    public long timeSinceStart() {
        return clock.ellapsedTime();
    }

    public void post(ImcActor source, List<Message> messages) {

    }
}
