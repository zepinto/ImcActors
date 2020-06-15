package pt.lsts.imcactors.actors;

import pt.lsts.imc4j.msg.Message;
import pt.lsts.imcactors.annotations.Receive;
import pt.lsts.imcactors.exceptions.MalformedActorException;
import pt.lsts.imcactors.platform.ImcPlatform;
import pt.lsts.imcactors.util.ReflectionUtilities;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public abstract class ImcActor {
    private final ArrayList<Message> inbox = new ArrayList<>();

    private final ConcurrentHashMap<Class<? extends Message>, ArrayList<Method>> consumers = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Class<? extends Message>, ArrayList<Method>> producers = new ConcurrentHashMap<>();
    private ImcPlatform platform;

    public ImcActor() { }

    public final Collection<Class<? extends Message>> getSubscriptions() {
        return consumers.keySet();
    }

    public final void init(ImcPlatform platform) throws MalformedActorException {
        this.platform = platform;
        List<Method> msgConsumers = ReflectionUtilities.getAnnotatedMethods(Receive.class, getClass());

        // register consumers
        for (Method c : msgConsumers) {
            Class<?>[] params = c.getParameterTypes();

            if (params.length != 1)
                throw new MalformedActorException("ImcSubscriber methods need one argument: " + c.toGenericString());
            if (!Message.class.isAssignableFrom(params[0]))
                throw new MalformedActorException("ImcSubscriber argument must be a Message: " + c.toGenericString());
            if (c.getReturnType() != Void.TYPE) {
                if (Message.class.isAssignableFrom(c.getReturnType())) {
                    producers.putIfAbsent((Class<? extends Message>)c.getReturnType(), new ArrayList<>());
                    producers.get(c.getReturnType()).add(c);
                }
                else if (Collection.class.isAssignableFrom(c.getReturnType())) {
                    try {
                        Type type = ((ParameterizedType) c.getGenericReturnType()).getActualTypeArguments()[0];
                        if ((type instanceof Class<?> && Message.class.isAssignableFrom((Class<?>)type))) {
                            producers.putIfAbsent((Class<? extends Message>)type, new ArrayList<>());
                            producers.get((Class<?>)type).add(c);
                        }
                        else {
                            throw new MalformedActorException("ImcSubscriber must return void, a Message or a list of Messages: " + c.toGenericString());
                        }
                    }
                    catch (Exception e) {
                        throw new MalformedActorException("ImcSubscriber must return void, a Message or a list of Messages: " + c.toGenericString());
                    }
                }
                else {
                    throw new MalformedActorException("ImcSubscriber must return void, a Message or a list of Messages: " + c.toGenericString());
                }
            }
            consumers.putIfAbsent((Class<? extends Message>)params[0], new ArrayList<>());
            consumers.get(params[0]).add(c);
        }
    }

    private final ArrayList<Message> invoke(Method m, Object... args) {
        ArrayList<Message> outbox = new ArrayList<>();
        try {
            Object result = m.invoke(this, args);

            if (result != null && Message.class.isAssignableFrom(result.getClass()))
                outbox.add((Message)result);
            else if (result != null && Collection.class.isAssignableFrom(result.getClass()))
                outbox.addAll((Collection<Message>) result);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return outbox;
    }

    /**
     * Process a single message (by calling all associated subscribers in this actor)
     *
     * @param msg The message to process
     * @return A (possibly empty) resulting list of messages
     */
    public final ArrayList<Message> process(Message msg, boolean loopback) {
        ArrayList<Message> outbox = new ArrayList<>();
        Class<?> clazz = msg.getClass();
        while (!clazz.equals(Object.class)) {
            List<Method> methods = consumers.getOrDefault(msg, new ArrayList<>());
            // for determinism
            methods.sort(Comparator.comparing(Method::getName));

            methods.forEach(m -> {
                try {
                    Receive annotation = m.getAnnotation(Receive.class);
                    if (!"".equals(annotation.source())) {
                        // TODO filter source
                    }
                    if (!"".equals(annotation.entity())) {
                        // TODO filter entity
                    }
                    if (!loopback || annotation.loopback()) {
                        outbox.addAll(invoke(m, msg));
                    }
                } catch (Exception e) {
                    Logger.getLogger(getClass().getSimpleName()).throwing(getClass().getSimpleName(),
                            "process()", e);
                }
            });
        }
        return outbox;
    }

    public final long getTimeSinceEpoch() {
        return platform.timeSinceEpoch();
    }

    public final long getTimeEllapsed() {
        return platform.timeSinceStart();
    }

}
