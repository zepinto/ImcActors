package pt.lsts.imcactors.util;

import pt.lsts.imc4j.msg.Message;

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
}
