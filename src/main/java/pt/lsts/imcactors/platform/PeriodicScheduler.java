package pt.lsts.imcactors.platform;

import pt.lsts.imc4j.msg.Message;
import pt.lsts.imcactors.actors.AbstractActor;
import pt.lsts.imcactors.annotations.Periodic;
import pt.lsts.imcactors.util.DurationUtilities;
import pt.lsts.imcactors.util.ReflectionUtilities;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

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

    public List<Message> trigger(PeriodicCallback callback) {
        boolean addBack = false;
        if (callbacks.contains(callback)) {
            callbacks.remove(callback);
            addBack = true;
        }

        Object result = null;
        try {
            result = callback.call();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (addBack)
            callbacks.add(callback);

        if (result == null)
            return new ArrayList<>();
        if (result instanceof Message)
            return Collections.singletonList((Message)result);
        else if (Collection.class.isAssignableFrom(result.getClass())) {
            ArrayList<Message> res = new ArrayList<>();
            res.addAll((Collection<Message>)result);
            return res;
        }
        else {
            System.err.println("Unexpected return type: "+result.getClass());
            return new ArrayList<>();
        }


    }

    public List<Message> callFirst() {
        return trigger(callbacks.pollFirst());
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

        public long getNextCallbackTimeMillis() {
            return nextCallbackTimeMillis;
        }

        public Object call() throws Exception {
            Object result = m.invoke(instance);
            nextCallbackTimeMillis += periodMillis;
            return result;
        }
    }
}
