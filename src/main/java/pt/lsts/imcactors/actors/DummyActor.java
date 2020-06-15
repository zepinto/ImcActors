package pt.lsts.imcactors.actors;

import pt.lsts.imc4j.annotations.Parameter;
import pt.lsts.imc4j.msg.TextMessage;
import pt.lsts.imcactors.annotations.Periodic;

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
}
