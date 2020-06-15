package pt.lsts.imcactors.test;

import pt.lsts.imc4j.msg.Temperature;
import pt.lsts.imcactors.annotations.Device;
import pt.lsts.imcactors.annotations.Periodic;
import pt.lsts.imcactors.annotations.Receive;
import pt.lsts.imcactors.environment.ISensor;

public class ActorsTest {

    @Receive
    void react(Temperature temperature) {

    }

    @Periodic
    void requestTemperature(@Device("Temperature")ISensor sensor) {

    }

}
