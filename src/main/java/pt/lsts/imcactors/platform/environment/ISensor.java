package pt.lsts.imcactors.platform.environment;

public interface ISensor {

    String name();

    default Double sample(PhysicalState state) {
        return null;
    }

}
