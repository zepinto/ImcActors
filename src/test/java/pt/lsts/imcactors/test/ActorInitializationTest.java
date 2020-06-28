package pt.lsts.imcactors.test;

import org.junit.Assert;
import org.junit.Test;
import pt.lsts.imc4j.msg.Announce;
import pt.lsts.imc4j.msg.EstimatedState;
import pt.lsts.imcactors.actors.AbstractActor;
import pt.lsts.imcactors.annotations.Receive;
import pt.lsts.imcactors.annotations.Device;
import pt.lsts.imcactors.environment.ISensor;
import pt.lsts.imcactors.exceptions.MalformedActorException;
import pt.lsts.imcactors.platform.ImcPlatform;

import java.util.Arrays;
import java.util.List;

public class ActorInitializationTest {

    class CorrectActor extends AbstractActor {
        @Receive
        void on(EstimatedState msg) {

        }

        @Receive
        List<Announce> on2(EstimatedState msg) {
            return Arrays.asList(new Announce());
        }
    }

    @Test
    public void correctActor() throws MalformedActorException {
        CorrectActor actor = new CorrectActor();
        actor.init(new ImcPlatform());
    }

    class MalformedActor extends AbstractActor {
        @Receive
        public void on(EstimatedState msg, @Device("Temperature") ISensor tempSensor) {
            tempSensor.sample();
        }

        @Receive
        public List<Object> on2(EstimatedState msg) {
            return Arrays.asList(new Announce());
        }
    }

    @Test
    public void malformedActor() {
        try {
            new MalformedActor().init(new ImcPlatform());
        }
        catch (Exception e) {
            Assert.assertEquals(e.getClass(), MalformedActorException.class);
        }
    }
}
