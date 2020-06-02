package pt.lsts.imcactors.platform.environment;

import pt.lsts.imc4j.util.WGS84Utilities;

public class PhysicalState {

    // pose
    private final double latitude, longitude, depth, heading, speed;

    public PhysicalState(double latitude, double longitude, double depth, double heading, double speed) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.depth = depth;
        this.heading = heading;
        this.speed = speed;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getDepth() {
        return depth;
    }

    public double getHeading() {
        return heading;
    }

    public double getSpeed() {
        return speed;
    }

    public double distance(PhysicalState other) {
        return WGS84Utilities.distance(latitude, longitude, other.getLatitude(), other.getLongitude());
    }
}
