package pt.lsts.imcactors.platform.clock;

public interface IPlatformClock {

    long StartTime = System.currentTimeMillis();

    default long currentTime() {
        return System.currentTimeMillis();
    }

    default long ellapsedTime() {
        return currentTime() - StartTime;
    }

}
