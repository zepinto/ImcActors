package pt.lsts.imcactors.platform.environment;

import pt.lsts.imc4j.annotations.Parameter;
import pt.lsts.imc4j.util.WGS84Utilities;

public class UnicycleActuator implements IActuator {

    @Parameter
    double speed;

    @Parameter
    double turnRate;

    double desiredHeading = 0;

    public UnicycleActuator(double speed, double turnRate) {
        this.speed = speed;
        this.turnRate = turnRate;
    }

    public UnicycleActuator() {
        this.speed = 0;
        this.turnRate = 0;
    }

    @Override
    public PhysicalState apply(PhysicalState state, double deltaTime) {
        double yaw = Math.toRadians(state.getHeading());

        double offsets[] = new double[] {
                Math.cos(yaw) * speed * deltaTime,
                Math.sin(yaw) * speed * deltaTime
        };

        double newPos[] = WGS84Utilities.WGS84displace(state.getLatitude(), state.getLongitude(), state.getDepth(),
                offsets[0], offsets[1], 0);

        return new PhysicalState(newPos[0], newPos[1], state.getDepth(), state.getHeading(), speed);
    }



}
