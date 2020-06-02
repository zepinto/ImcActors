package pt.lsts.imcactors.platform.environment;

public interface IActuator extends IDevice {

    default PhysicalState apply(PhysicalState state, double deltaTime) {
        // no actuation
        return state;
    }
}
