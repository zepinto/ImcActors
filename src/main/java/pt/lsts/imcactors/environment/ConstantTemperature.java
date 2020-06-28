package pt.lsts.imcactors.environment;

import pt.lsts.imc4j.annotations.Parameter;
import pt.lsts.imc4j.msg.Temperature;

public class ConstantTemperature extends AbstractDevice implements ISensor<Temperature> {

    @Parameter
    private double value;

    public ConstantTemperature() {
        value = 0;
    }

    public ConstantTemperature(double val) {
        this.value = val;
    }

    @Override
    public Temperature sample() {
        Temperature temp = new Temperature();
        temp.value = (float)value;
        return temp;
    }
}
