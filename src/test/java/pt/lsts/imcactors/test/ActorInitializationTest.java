package pt.lsts.imcactors.test;

import org.junit.Assert;
import org.junit.Test;
import pt.lsts.imc4j.msg.Announce;
import pt.lsts.imc4j.msg.EstimatedState;
import pt.lsts.imcactors.ImcActor;
import pt.lsts.imcactors.annotations.ImcSubscriber;
import pt.lsts.imcactors.exceptions.MalformedActorException;
import pt.lsts.imcactors.platform.ImcPlatform;

import java.util.Arrays;
import java.util.List;

public class ActorInitializationTest {

    class CorrectActor extends ImcActor {
        @ImcSubscriber
        void on(EstimatedState msg) {

        }

        @ImcSubscriber
        List<Announce> on2(EstimatedState msg) {
            return Arrays.asList(new Announce());
        }
    }

    @Test
    public void correctActor() throws MalformedActorException {
        CorrectActor actor = new CorrectActor();
        actor.init(new ImcPlatform());
    }

    class MalformedActor extends ImcActor {
        @ImcSubscriber
        public void on(EstimatedState msg) {

        }

        @ImcSubscriber
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
