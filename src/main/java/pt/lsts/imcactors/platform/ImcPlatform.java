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

public class ImcPlatform {

    private ConcurrentHashMap<Class<? extends Message>, HashSet<AbstractActor>> subscribers = new ConcurrentHashMap<>();
    private PlatformConfiguration configuration = null;

    public ImcPlatform(PlatformConfiguration configuration) {
        this.configuration = configuration;
        calcSubscribers();

        for (Map.Entry<String, AbstractActor> actor : configuration.getActors().entrySet()) {
            try{
                actor.getValue().init(this);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public ImcPlatform(File configIni) throws IOException {
        this(PlatformConfiguration.load(new IniConfiguration(configIni)));
    }

    private void calcSubscribers() {
        for (Map.Entry<String, AbstractActor> actor : configuration.getActors().entrySet()) {
            actor.getValue().getSubscriptions().forEach(m -> {
                subscribers.getOrDefault(m, new HashSet<>()).add(actor.getValue());
            });
        }
    }

    public Collection<AbstractActor> subscribingActors(Class<? extends Message> message) {
        return subscribers.get(message);
    }

    public long timeSinceEpoch() {
        return configuration.getClock().currentTime();
    }

    public long timeSinceStart() {
        return configuration.getClock().ellapsedTime();
    }

    public ImcPlatform(int imcId, String name) {
        configuration = new PlatformConfiguration();
        configuration.setImcId(imcId);
        configuration.setPlatformName(name);
    }

    public ImcPlatform() {
        this(0, "DummyPlatform");
    }

    public AbstractActor instantiateActor(String actorClass, LinkedHashMap<String, String> state) throws Exception {
        AbstractActor actor = (AbstractActor) Class.forName(actorClass).newInstance();

        for (Map.Entry<String, String> props : state.entrySet()) {
            PojoConfig.setProperty(actor, props.getKey(), props.getValue());
        }

        configuration.addActor(actor, actor.getClass().getSimpleName());
        return actor;
    }

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



    public void destroyActor(String name) {
        configuration.removeActor(name);
    }
}
