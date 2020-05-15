package pt.lsts.imcactors.platform.environment;

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

    public Double sample(String sensor, PhysicalState state) {
        if (sensors.containsKey(sensor))
            return sensors.get(sensor).sample(state);
        Logger.getLogger(getClass().getName()).warning("Sensor not available: "+sensor);
        return null;
    }

    public Message transmit(Message m, String medium, PhysicalState src, PhysicalState dst) {
        if (mediums.containsKey(medium))
            return mediums.get(medium).transmit(m, src, dst);
        Logger.getLogger(getClass().getName()).warning("Medium not available: "+medium);
        return null;
    }
}
