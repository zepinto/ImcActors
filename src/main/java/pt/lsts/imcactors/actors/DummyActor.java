package pt.lsts.imcactors.actors;

import pt.lsts.imc4j.annotations.Consume;
import pt.lsts.imc4j.annotations.Parameter;
import pt.lsts.imc4j.msg.EstimatedState;
import pt.lsts.imc4j.msg.TextMessage;
import pt.lsts.imcactors.annotations.Device;
import pt.lsts.imcactors.annotations.Periodic;
import pt.lsts.imcactors.environment.UnicycleActuator;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DummyActor extends AbstractActor {

    @Parameter
    String dateFormat = "YYYY-MM-dd HH:mm:ss";

    @Periodic("5s")
    TextMessage sendDateAsText() {
        TextMessage msg = new TextMessage();
        msg.text = "Current time: "+new SimpleDateFormat(dateFormat).format(new Date());
        return msg;
    }

    @Consume
    void on(EstimatedState state, @Device UnicycleActuator actuator) {
        UnicycleActuator.UnicycleActuation actuation = new UnicycleActuator.UnicycleActuation();
        actuation.speed = 10;
        actuation.yaw = -15;
        actuator.setActuation(actuation);
    }
}
