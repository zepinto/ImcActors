package pt.lsts.imcactors.platform.clock;

public class SimulationClock implements IPlatformClock {

    private long curTime = StartTime;

    void setTime(long millis) {
        this.curTime = millis;
    }

    void advance(long millis) {
        this.curTime += millis;
    }

    @Override
    public long currentTime() {
        return curTime;
    }
}
