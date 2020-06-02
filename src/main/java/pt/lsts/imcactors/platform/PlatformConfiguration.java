package pt.lsts.imcactors.platform;

import pt.lsts.imc4j.util.PojoConfig;
import pt.lsts.imcactors.platform.environment.*;
import pt.lsts.imcactors.util.IniConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * This class holds a platform configuration: its sensors, actuators, media and actors
 */
public class PlatformConfiguration {

    private ArrayList<ISensor> sensors = new ArrayList<>();
    private ArrayList<IActuator> actuators = new ArrayList<>();
    private ArrayList<ICommMedium> media = new ArrayList<>();

    private String platformName;
    private int imcId;

    public static PlatformConfiguration load(IniConfiguration ini) {
        PlatformConfiguration configuration = new PlatformConfiguration();

        configuration.platformName = ini.get("Platform", "name");
        configuration.imcId = Integer.decode(ini.get("Platform", "id"));

        ini.sections().forEach(s -> {
            if (!("Platform".equals(s))) {
                try {
                    String className = ini.get(s, "class");
                    Object o = Class.forName(className).newInstance();
                    for (Map.Entry<String, String> p : ini.getParams(s).entrySet()) {
                        if (!("class".equals(p.getKey())))
                            PojoConfig.setProperty(o, p.getKey(), p.getValue());
                    }

                    if (o instanceof ISensor)
                        configuration.sensors.add((ISensor) o);
                    if (o instanceof IActuator)
                        configuration.actuators.add((IActuator) o);
                    if (o instanceof ICommMedium)
                        configuration.media.add((ICommMedium) o);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return configuration;
    }

    public String toString() {
        IniConfiguration ini = new IniConfiguration();
        ini.set("Platform", "name", platformName);
        ini.set("Platform", "id", ""+imcId);

        List<IDevice> allDevices = new ArrayList<>();
        allDevices.addAll(sensors);
        allDevices.addAll(actuators);
        allDevices.addAll(media);

        for (IDevice device : allDevices) {
            try {
                ini.set(device.type()+"."+device.name(), "class", device.getClass().getName());
                Properties props = PojoConfig.getProperties(device);
                props.stringPropertyNames().forEach(prop -> {
                    ini.set(device.type()+"."+device.name(), prop, props.getProperty(prop));
                });
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        return ini.toString();
    }

    public ArrayList<ISensor> getSensors() {
        return sensors;
    }

    public ArrayList<IActuator> getActuators() {
        return actuators;
    }

    public ArrayList<ICommMedium> getMedia() {
        return media;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public void setImcId(int imcId) {
        this.imcId = imcId;
    }

    public String getPlatformName() {
        return platformName;
    }

    public int getImcId() {
        return imcId;
    }
}
