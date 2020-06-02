package pt.lsts.imcactors.platform.events;

import pt.lsts.imcactors.ImcActor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public abstract class AbstractEvent<T> {

    private double timestamp;
    private T payload;
    private ImcActor source = null;
    private static SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss.SSS");

    static {
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public AbstractEvent(double timestamp, T payload) {
        this.timestamp = timestamp;
        this.payload = payload;
    }

    public double getTimestamp() {
        return timestamp;
    }

    public T getPayload() {
        return payload;
    }

    public ImcActor getSource() {
        return source;
    }

    public void setSource(ImcActor source) {
        this.source = source;
    }

    public String toString() {
        return String.format("[ %s | %s | %s ] %s", sdf.format(new Date((long)(timestamp*1000))),
                getSource().getClass().getSimpleName(), getClass().getSimpleName(), ""+payload);
    }
}
