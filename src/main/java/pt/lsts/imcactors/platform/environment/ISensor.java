package pt.lsts.imcactors.platform.environment;

public interface ISensor<T> extends IDevice {

    default T sample(PhysicalState state) {
        return null;
    }

}
