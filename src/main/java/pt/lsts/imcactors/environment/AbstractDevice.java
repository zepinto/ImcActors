package pt.lsts.imcactors.environment;

public abstract class AbstractDevice implements IDevice {

    protected PhysicalState state;
    protected PhysicalEnvironment environment;

    @Override
    public String name() {
        return getClass().getSimpleName();
    }

    @Override
    public void setPhysicalState(PhysicalState state) {
        this.state = state;
    }

    @Override
    public void setEnvironment(PhysicalEnvironment environment) {
        this.environment = environment;
    }

}
