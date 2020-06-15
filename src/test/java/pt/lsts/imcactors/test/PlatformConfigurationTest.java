package pt.lsts.imcactors.test;

import org.junit.Assert;
import org.junit.Test;
import pt.lsts.imcactors.platform.PlatformConfiguration;
import pt.lsts.imcactors.environment.ConstantSensor;
import pt.lsts.imcactors.environment.SatelliteMedium;
import pt.lsts.imcactors.environment.UnicycleActuator;
import pt.lsts.imcactors.environment.WiFiMedium;
import pt.lsts.imcactors.util.IniConfiguration;

import java.io.File;
import java.nio.file.Files;

public class PlatformConfigurationTest {

    @Test
    public void loadConfig() throws Exception {
        PlatformConfiguration configuration = new PlatformConfiguration();
        configuration.getSensors().add(new ConstantSensor("Temperature", 23));
        configuration.getSensors().add(new ConstantSensor("Salinity", 34));
        configuration.getMedia().add(new WiFiMedium());
        configuration.getMedia().add(new SatelliteMedium());
        configuration.getActuators().add(new UnicycleActuator(1, 15));

        configuration.setImcId(7008);
        configuration.setPlatformName("ExamplePlatform");

        File tmpFile = File.createTempFile("PlatformConfiguration", "ini");
        Files.write(tmpFile.toPath(), configuration.toString().getBytes());
        IniConfiguration cfg2 = new IniConfiguration(tmpFile);
        PlatformConfiguration newOne = PlatformConfiguration.load(cfg2);

        System.out.println(newOne.toString());
        Assert.assertEquals(newOne.toString(), configuration.toString());
    }

}
