package pt.lsts.imcactors.platform;

import pt.lsts.imc4j.msg.Message;
import pt.lsts.imc4j.util.PojoConfig;
import pt.lsts.imcactors.actors.AbstractActor;
import pt.lsts.imcactors.annotations.Device;
import pt.lsts.imcactors.environment.IActuator;
import pt.lsts.imcactors.environment.ICommMedium;
import pt.lsts.imcactors.environment.IDevice;
import pt.lsts.imcactors.environment.ISensor;
import pt.lsts.imcactors.util.IniConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * This class represents a platform (vehicle, console, gateway, ...)
 * A platform holds an hardware configuration and a series of actors.
 */
public class ImcPlatform {

    private ConcurrentHashMap<Class<? extends Message>, HashSet<AbstractActor>> subscribers = new ConcurrentHashMap<>();
    private PlatformConfiguration configuration = null;
    private PeriodicScheduler schedule = new PeriodicScheduler();
    private TreeSet<Message> inbox = new TreeSet<>((m1, m2) -> new Double(m1.timestamp).compareTo(m2.timestamp));

    public Long nextEvent() {
        Message firstMessage = inbox.first();
        long nextCallback = schedule.nextCallbackTime();

        if (nextCallback == 0) {
            if (firstMessage == null)
                return null;
            else
                return (long) firstMessage.timestamp * 1000;
        }
        else {
            if (firstMessage == null)
                return nextCallback;
            else return Math.min(nextCallback,  (long) firstMessage.timestamp * 1000);
        }
    }

    public void advanceToTime(long time) {
        while (true) {
            long nextCall = schedule.nextCallbackTime();
            long nextMessage = inbox.isEmpty()? 0 : (long) inbox.first().timestamp * 1000;

            if (nextCall == 0) {
                if (nextMessage == 0 || nextMessage > time)
                    break;
                postMessage(inbox.pollFirst());
                continue;
            }
            else if (nextMessage == 0) {
                if (nextCall == 0 || nextCall > time)
                    break;
                schedule.callFirst();
                continue;
            }
            else {
                if (nextCall < nextMessage)
                    schedule.callFirst();
                else
                    postMessage(inbox.pollFirst());
                continue;
            }
        }
    }

    private void postMessage(Message m) {
        ArrayList<AbstractActor> subs = new ArrayList<>();
        subs.addAll(subscribers.getOrDefault(m.getClass(), new HashSet<>()));
        subs.sort(Comparator.comparing(a -> a.getClass().getSimpleName()));

        subs.forEach(actor -> actor.process(m, false));
    }

    /**
     * Creates a platform with given configuration
     * @param configuration
     */
    public ImcPlatform(PlatformConfiguration configuration) {
        this.configuration = configuration;

        for (Map.Entry<String, AbstractActor> actor : configuration.getActors().entrySet())
            initActor(actor.getValue());
    }

    /**
     * Creates a platform by reading a configuration from disk
     * @param configIni The file holding the platform's configuration
     * @throws IOException In case the file cannot be parsed correctly
     */
    public ImcPlatform(File configIni) throws IOException {
        this(PlatformConfiguration.load(new IniConfiguration(configIni)));
    }

    /**
     * Create an empty platform
     * @param imcId The IMC identifier of the platform
     * @param name The name of thew platform
     */
    public ImcPlatform(int imcId, String name) {
        configuration = new PlatformConfiguration();
        configuration.setImcId(imcId);
        configuration.setPlatformName(name);
    }

    /**
     * Create an empty (dummy) platform.
     * Id will be 0 and name will be "DummyPlatform".
     */
    public ImcPlatform() {
        this(0, "DummyPlatform");
    }

    /**
     * Creates and initializes an actor given its class and state
     * @param actorClass The class of the actor to instantiate, should extend AbstractActor class
     * @param state The initial state of the actor
     * @return The instantiated actor
     * @throws Exception In case the actor cannot be instantiated in this platform
     */
    public AbstractActor instantiateActor(String actorClass, LinkedHashMap<String, String> state) throws Exception {
        AbstractActor actor = (AbstractActor) Class.forName(actorClass).newInstance();

        for (Map.Entry<String, String> props : state.entrySet()) {
            PojoConfig.setProperty(actor, props.getKey(), props.getValue());
        }
        initActor(actor);

        configuration.addActor(actor, actor.getClass().getSimpleName());

        return actor;
    }

    private void initActor(AbstractActor actor) {
        try{
            actor.init(this);
            actor.getSubscriptions().forEach(m -> {
                subscribers.getOrDefault(m, new HashSet<>()).add(actor);
            });
            schedule.register(actor, timeSinceEpoch());

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a list of actors that react to a type of message
     * @param message The message type
     * @return Corresponding list of subscribers
     */
    public Collection<AbstractActor> subscribingActors(Class<? extends Message> message) {
        return subscribers.get(message);
    }

    /**
     * Retrieve absolute time (given platform's configured clock)
     * @return absolute time, in milliseconds, since midnight 1st Jan 1970
     */
    public long timeSinceEpoch() {
        return configuration.getClock().currentTime();
    }

    /**
     * Time, in milliseconds, since some time in the past (start of the platform)
     * @return time since some time in the past (start of the platform)
     */
    public long timeSinceStart() {
        return configuration.getClock().ellapsedTime();
    }

    /**
     * Retrieve a device from the platform's hardware configuration
     * @param device The device specification
     * @param type The class of the device to retrieve
     * @return The corresponding device or <code>null</code> if the device is not present.
     */
    public IDevice getDevice(Device device, Class<?> type) {

        Optional<ISensor> sensor = configuration.getSensors().stream().filter(s -> s.getClass().equals(type)).findFirst();
        if (sensor.isPresent())
            return sensor.get();

        Optional<IActuator> actuator = configuration.getActuators().stream().filter(a -> a.getClass().equals(type)).findFirst();
        if (actuator.isPresent())
            return actuator.get();

        Optional<ICommMedium> medium = configuration.getMedia().stream().filter(m -> m.getClass().equals(type)).findFirst();
        if (medium.isPresent())
            return medium.get();

        return null;
    }

    /**
     * Remove an actor from this platform
     * @param name
     */
    public void destroyActor(String name) {
        configuration.removeActor(name);
    }



}
