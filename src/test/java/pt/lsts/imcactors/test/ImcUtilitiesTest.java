package pt.lsts.imcactors.test;

import org.junit.Assert;
import org.junit.Test;
import pt.lsts.imc4j.msg.EstimatedState;
import pt.lsts.imc4j.msg.PlanControlState;
import pt.lsts.imcactors.util.ImcUtilities;

public class ImcUtilitiesTest {
    @Test
    public void testImcUtilities() {
        EstimatedState original = new EstimatedState();
        original.src = 30;
        original.src_ent = 20;

        PlanControlState pcs = ImcUtilities.createReply(PlanControlState.class, original);
        Assert.assertEquals(30, pcs.dst);
        Assert.assertEquals(20, pcs.dst_ent);
    }
}
