package pt.lsts.imcactors.environment;

import pt.lsts.imc4j.annotations.Parameter;
import pt.lsts.imc4j.msg.Temperature;

public class ConstantSensor extends AbstractDevice implements ISensor<Temperature> {

    @Parameter
    private double value;

    @Parameter
    private String name;

    public ConstantSensor() {
        name = getClass().getSimpleName();
        value = 0;
    }

    public ConstantSensor(String name, double value) {
        this.name = name;
        this.value = value;
    }

    public Temperature sample(PhysicalState state) {
        Temperature t = new Temperature();
        t.value = (float) value;
        return t;
    }
}
