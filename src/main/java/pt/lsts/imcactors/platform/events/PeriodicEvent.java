package pt.lsts.imcactors.platform.events;

import pt.lsts.imc4j.msg.Message;
import pt.lsts.imcactors.actors.AbstractActor;
import pt.lsts.imcactors.annotations.Periodic;
import pt.lsts.imcactors.platform.ImcPlatform;
import pt.lsts.imcactors.util.DurationUtilities;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PeriodicEvent extends PlatformEvent {

    private Method method;

    public PeriodicEvent(AbstractActor actor, long timestamp, Method m) {
        super(actor, timestamp);
    }

    @Override
    public List<PlatformEvent> processEvent(ImcPlatform platform) {
        long period = DurationUtilities.parseDuration(method.getAnnotation(Periodic.class).value());
        ArrayList<PlatformEvent> events = new ArrayList<>();

        try {
            Object result = method.invoke(actor);
            if (result != null && Message.class.isAssignableFrom(result.getClass()))
                events.add(new OutgoingEvent(actor, (Message)result));
            else if (result != null && Collection.class.isAssignableFrom(result.getClass()))
                ((Collection<Message>) result).forEach(msg -> events.add(new OutgoingEvent(actor, msg)));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        events.add(new PeriodicEvent(actor, timestamp+period, method));
        return events;
    }

    @Override
    public String describe() {
        return "{\"method\"="+method.getName()+", \"every\"="+method.getAnnotation(Periodic.class).value()+"}";
    }
}
