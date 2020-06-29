package pt.lsts.imcactors.platform;

import pt.lsts.imcactors.actors.AbstractActor;
import pt.lsts.imcactors.annotations.Periodic;
import pt.lsts.imcactors.util.DurationUtilities;
import pt.lsts.imcactors.util.ReflectionUtilities;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.TreeSet;

public class PeriodicScheduler {

    private TreeSet<PeriodicCallback> callbacks = new TreeSet<>();

    public void register(AbstractActor actor, long startTimeMillis) {
        List<Method> periodicCallbacks = ReflectionUtilities.getAnnotatedMethods(Periodic.class, actor.getClass());
        for (Method m : periodicCallbacks) {
            PeriodicCallback callback = new PeriodicCallback();
            callback.instance = actor;
            callback.periodMillis = DurationUtilities.parseDuration(m.getAnnotation(Periodic.class).value());
            callback.m = m;
            callback.nextCallbackTimeMillis = startTimeMillis + callback.periodMillis;
            callbacks.add(callback);
        }
    }

    public void removeActor(AbstractActor actor) {
        callbacks.removeIf(callback -> callback.instance == actor);
    }

    public long nextCallbackTime() {
        if (callbacks.isEmpty())
            return 0;

        return callbacks.first().nextCallbackTimeMillis;
    }

    public void callFirst() {
        PeriodicCallback first = callbacks.pollFirst();
        try {
            first.call();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        callbacks.add(first);
    }

    public static class PeriodicCallback implements Comparable<PeriodicCallback> {
        Object instance;
        Method m;
        long nextCallbackTimeMillis;
        long periodMillis;

        @Override
        public int compareTo(PeriodicCallback o) {
            return ((Long)nextCallbackTimeMillis).compareTo(o.nextCallbackTimeMillis);
        }

        public void call() throws Exception {
            m.invoke(instance);
            nextCallbackTimeMillis += periodMillis;
        }
    }
}
