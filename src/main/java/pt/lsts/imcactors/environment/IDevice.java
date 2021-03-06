package pt.lsts.imcactors.environment;

public interface IDevice {
    default String name() {
        return getClass().getSimpleName();
    }

    void setPhysicalState(PhysicalState state);
    void setEnvironment(PhysicalEnvironment environment);

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
