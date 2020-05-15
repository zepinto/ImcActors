package pt.lsts.imcactors.platform;

import pt.lsts.imc4j.msg.Message;
import pt.lsts.imcactors.ImcActor;
import pt.lsts.imcactors.platform.clock.IPlatformClock;
import pt.lsts.imcactors.platform.clock.RealTimeClock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ImcPlatform {

    private IPlatformClock clock = new RealTimeClock();
    private ConcurrentHashMap<Integer, ImcActor> actors = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, String> actorNames = new ConcurrentHashMap<>();
    private int localImcId;
    private int nextActorId = 0;

    private void registerActor(Class<? extends ImcActor> actorClass, String name) throws Exception {
        ImcActor actor = actorClass.newInstance();
        actor.init(this);
        ArrayList<Class<? extends Message>> subs = new ArrayList<>();
        subs.addAll(actor.getSubscriptions());
        int actorId = ++nextActorId;
        actors.put(actorId, actor);
        actorNames.put(actorId, name);
    }

    public long timeSinceEpoch() {
        return clock.currentTime();
    }

    public long timeSinceStart() {
        return clock.ellapsedTime();
    }

    public ImcPlatform(int imcId, String name) {
        this.localImcId = imcId;
    }


}
