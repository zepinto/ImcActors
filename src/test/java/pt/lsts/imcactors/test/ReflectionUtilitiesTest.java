package pt.lsts.imcactors.test;

import org.junit.Assert;
import org.junit.Test;
import pt.lsts.imc4j.msg.EstimatedState;
import pt.lsts.imcactors.ImcActor;
import pt.lsts.imcactors.annotations.Receive;
import pt.lsts.imcactors.util.ReflectionUtilities;

import java.lang.reflect.Method;
import java.util.List;

public class ReflectionUtilitiesTest {

    @Test
    public void testGetAnnotatedMethods() {
        List<Method> methods = ReflectionUtilities.getAnnotatedMethods(Receive.class, ChildClass.class);
        Assert.assertEquals(2, methods.size());
    }

    static class SuperClass extends ImcActor {
        @Receive
        void on(EstimatedState state) { }
    }

    static class ChildClass extends SuperClass {
        @Receive
        void on(EstimatedState state) { }
    }
}
