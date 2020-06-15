package pt.lsts.imcactors.environment;

public interface IActuator<A extends IActuation> extends IDevice {
    void setActuation(A value);
}
