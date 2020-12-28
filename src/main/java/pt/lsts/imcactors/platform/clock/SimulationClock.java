package pt.lsts.imcactors.platform.clock;

/**
 * Implements {@link IPlatformClock} by allowing time to advance arbitrarily since a given start time
 * @author zp
 */
public class SimulationClock implements IPlatformClock {

	private final long StartTime;
	private long curTime;
	
	/**
	 * Create a simulation clock with given start time
	 * @param startTime The start time of this clock in milliseconds since 1st Jan 1970
	 * @see SimulationClock#SimulationClock()
	 */
	public SimulationClock(long startTime) {
		StartTime = startTime;
	}
	
	/**
	 * Create simulation clock that starts at the current time
	 */
	public SimulationClock() {
		this(System.currentTimeMillis());
	}	
	
	/**
	 * Change the current time
	 * @param millis Time in milliseconds since 1st Jan 1970
	 */
    void setTime(long millis) {
        this.curTime = millis;
    }

    /**
     * Advance the current time
     * @param millis Time to advance, in milliseconds
     * @return The new current time
     */
    long advance(long millis) {
        this.curTime += millis;
        return this.curTime;
    }

    @Override
    public long currentTime() {
        return curTime;
    }

	@Override
	public long ellapsedTime() {
		return currentTime()-StartTime;
	}
}
