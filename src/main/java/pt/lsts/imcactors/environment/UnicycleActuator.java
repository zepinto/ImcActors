package pt.lsts.imcactors.environment;

import pt.lsts.imc4j.annotations.Parameter;
import pt.lsts.imc4j.util.WGS84Utilities;

public class UnicycleActuator extends AbstractDevice implements IActuator<UnicycleActuator.UnicycleActuation> {

    @Parameter
    double max_speed;

    @Parameter
    double turnRate;

    double desiredHeading = 0;
    double desiredSpeed = 0;

    public UnicycleActuator(double speed, double turnRate) {
        this.max_speed = speed;
        this.turnRate = turnRate;
    }

    public UnicycleActuator() {
        this.max_speed = 0;
        this.turnRate = 0;
    }

    @Override
    public void setActuation(UnicycleActuation value) {

    }

    @Override
    public void setPhysicalState(PhysicalState state) {

    }

    @Override
    public void setEnvironment(PhysicalEnvironment environment) {

    }

    public static class UnicycleActuation implements IActuation {

        public double yaw;
        public double speed;

        @Override
        public PhysicalState update(PhysicalState state, double deltaTime) {

            double diff = yaw - state.getHeading();

            double offsets[] = new double[] {
                    Math.cos(Math.toRadians(yaw)) * speed * deltaTime,
                    Math.sin(Math.toRadians(yaw)) * speed * deltaTime
            };

            double[] lld = WGS84Utilities.WGS84displace(state.getLatitude(), state.getLongitude(), 0, offsets[0], offsets[1], 0);

            return new PhysicalState(lld[0], lld[1], state.getDepth(), yaw, speed);
        }
    }

}
