package pt.lsts.imcactors.platform.environment;

public interface IDevice {
    default String name() {
        return getClass().getSimpleName();
    }
    default String type() {
        if (this instanceof ISensor)
            return "Sensor";
        if (this instanceof ICommMedium)
            return "Medium";
        if (this instanceof IActuator)
            return "Actuator";
        return "Other";
    }
}
