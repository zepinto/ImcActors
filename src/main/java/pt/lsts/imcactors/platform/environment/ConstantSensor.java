package pt.lsts.imcactors.platform.environment;

import pt.lsts.imc4j.annotations.Parameter;

public class ConstantSensor implements ISensor<Double> {

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

    @Override
    public Double sample(PhysicalState state) {
        return value;
    }

    @Override
    public String name() {
        return name;
    }
}
