package pt.lsts.imcactors.actors;

import pt.lsts.imcactors.platform.ImcPlatform;

public class PhysicalActor extends AbstractActor {

    private ImcPlatform platform = null;

    public static final PhysicalActor create(ImcPlatform platform) {
        return new PhysicalActor(platform);
    }

    private PhysicalActor(ImcPlatform platform) {
        this.platform = platform;
    }

    public PhysicalActor() {
        // this actor won't be able to do anything
    }
}
