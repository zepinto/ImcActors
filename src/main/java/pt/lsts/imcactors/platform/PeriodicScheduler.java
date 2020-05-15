package pt.lsts.imcactors.platform;

import pt.lsts.imcactors.ImcActor;
import pt.lsts.imcactors.annotations.ImcPeriodic;
import pt.lsts.imcactors.platform.bus.ActorBus;
import pt.lsts.imcactors.util.DurationUtilities;
import pt.lsts.imcactors.util.ReflectionUtilities;

import java.lang.reflect.Method;
import java.util.List;
import java.util.TreeSet;

public class PeriodicScheduler {

    TreeSet<PeriodicCallback> callbacks = new TreeSet<>();

    public void register(ImcActor actor, long startTimeMillis) {
        List<Method> periodicCallbacks = ReflectionUtilities.getAnnotatedMethods(ImcPeriodic.class, actor.getClass());
        for (Method m : periodicCallbacks) {
            PeriodicCallback callback = new PeriodicCallback();
            callback.instance = actor;
            callback.periodMillis = DurationUtilities.parseDuration(m.getAnnotation(ImcPeriodic.class).value());
            callback.m = m;
            callback.nextCallbackTimeMillis = startTimeMillis + callback.periodMillis;
            callbacks.add(callback);
        }
    }

    public void removeActor(ImcActor actor) {
        callbacks.removeIf(callback -> callback.instance == actor);
    }

    public long nextCallbackTime() {
        if (callbacks.isEmpty())
            return 0;

        return callbacks.first().nextCallbackTimeMillis;
    }

    private class PeriodicCallback implements Comparable<PeriodicCallback> {
        Object instance;
        Method m;
        long nextCallbackTimeMillis;
        long periodMillis;

        @Override
        public int compareTo(PeriodicCallback o) {
            return ((Long)nextCallbackTimeMillis).compareTo(o.nextCallbackTimeMillis);
        }
    }


}
