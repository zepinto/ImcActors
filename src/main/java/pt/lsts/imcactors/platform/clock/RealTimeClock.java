package pt.lsts.imcactors.platform.clock;

/**
 * Implements {@link IPlatformClock} by using the system's real time clock
 * @author zp
 */
public class RealTimeClock implements IPlatformClock {
	
	long StartTime = System.currentTimeMillis();

	public long currentTime() {
        return System.currentTimeMillis();
    }

    public long ellapsedTime() {
        return currentTime() - StartTime;
    }
}
