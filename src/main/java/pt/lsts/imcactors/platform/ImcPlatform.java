package pt.lsts.imcactors.platform;

import pt.lsts.imc4j.msg.Message;
import pt.lsts.imcactors.ImcActor;
import pt.lsts.imcactors.platform.clock.IPlatformClock;
import pt.lsts.imcactors.platform.clock.RealTimeClock;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;

public class ImcPlatform {

    private IPlatformClock clock = new RealTimeClock();
    private ConcurrentHashMap<Integer, ImcActor> actors = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, String> actorNames = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Class<? extends Message>, HashSet<ImcActor>> subscribers = new ConcurrentHashMap<>();
    private int localImcId;
    private int nextActorId = 0;

    private void registerActor(Class<? extends ImcActor> actorClass, String name) throws Exception {
        ImcActor actor = actorClass.newInstance();
        actor.init(this);
        actor.getSubscriptions().forEach(clazz -> {
            subscribers.putIfAbsent(clazz, new HashSet<>());
            subscribers.get(clazz).add(actor);
        });
        int actorId = ++nextActorId;
        actors.put(actorId, actor);
        actorNames.put(actorId, name);
    }

    public Collection<ImcActor> subscribingActors(Class<? extends Message> message) {
        return subscribers.get(message);
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

    public ImcPlatform() {
        this(0, "DummyPlatform");
    }

    public ImcActor instantiateActor(String actorClass, LinkedHashMap<String, String> state) {
        // TODO
        return null;
    }

    public void destroyActor(ImcActor actor) {
        // TODO
    }

}
