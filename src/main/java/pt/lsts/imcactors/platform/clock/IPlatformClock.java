package pt.lsts.imcactors.platform.clock;

/**
 * This interface represents a clock, able to measure time passage
 * @author zp
 */
public interface IPlatformClock {
    
	/**
     * @return The current epoch time (milliseconds since 1st January 1970) 
     */
	long currentTime();
	
	/**
	 * 
	 * @return The current time in milliseconds since some time in the past
	 */
    long ellapsedTime();
}
