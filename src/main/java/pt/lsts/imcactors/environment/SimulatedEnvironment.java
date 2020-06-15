package pt.lsts.imcactors.environment;

import pt.lsts.imc4j.msg.Message;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

public class SimulatedEnvironment {

    LinkedHashMap<String, ICommMedium> mediums = new LinkedHashMap<>();
    LinkedHashMap<String, ISensor> sensors = new LinkedHashMap<>();

    public Collection<String> availableSensors() {
        return sensors.keySet();
    }

    public Collection<String> availableMediums() {
        return mediums.keySet();
    }

    public <T> T sample(String sensor, PhysicalState state) {
        if (sensors.containsKey(sensor))
            return (T)sensors.get(sensor).sample();
        Logger.getLogger(getClass().getName()).warning("Sensor not available: "+sensor);
        return null;
    }

}
