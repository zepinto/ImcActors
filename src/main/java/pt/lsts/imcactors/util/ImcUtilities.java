package pt.lsts.imcactors.util;

import pt.lsts.imc4j.msg.Message;
import pt.lsts.imc4j.msg.MessageFactory;

import java.nio.ByteBuffer;

public class ImcUtilities {

    public static <M extends Message> M createReply(Class<M> clazz, Message original) {
        try {
            M m = (M) clazz.newInstance();
            m.dst_ent = original.src_ent;
            m.dst = original.src;
            return m;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <M extends Message> M clone(M original) {
        try {
            M ret = (M) Message.deserialize(original.serialize());
            return ret;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
