package pt.lsts.imcactors.environment;

import pt.lsts.imc4j.msg.Message;

public abstract class AbstractSensor<M extends Message> implements ISensor<M> {
    private M msg;

    AbstractSensor(Class<M> msgType) {
        try {
            msg = msgType.newInstance();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setValue(float val) throws NoSuchFieldException, IllegalAccessException {
        msg.getClass().getField("value").setAccessible(true);
        msg.getClass().getField("value").set(msg, val);
    }

    @Override
    public M sample() {
        return msg;
    }
}
