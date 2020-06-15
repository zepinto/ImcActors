package pt.lsts.imcactors.environment;

public interface IActuation {

    PhysicalState update(PhysicalState state, double deltaTime);

}
