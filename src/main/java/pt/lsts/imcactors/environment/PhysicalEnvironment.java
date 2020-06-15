package pt.lsts.imcactors.environment;

import pt.lsts.imc4j.msg.Message;
import pt.lsts.imcactors.platform.ImcPlatform;
import pt.lsts.imcactors.platform.events.PlatformEvent;

import java.util.Collections;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

public class PhysicalEnvironment {

    // list of platforms
    private ConcurrentHashMap<Integer, ImcPlatform> platforms = new ConcurrentHashMap<>();

    // the physical state of all platforms
    private ConcurrentHashMap<Integer, PhysicalState> states = new ConcurrentHashMap<>();

    // the current actuations for all platforms
    private ConcurrentHashMap<Integer, IActuation> actuations = new ConcurrentHashMap<>();

    // list of events that will be triggered in the future
    private TreeSet<PlatformEvent> upcomingEvents = new TreeSet<>();

    public Map<Integer, PhysicalState> physicalStates() {
        return Collections.unmodifiableMap(states);
    }

    public PhysicalState getState(int platform) {
        return states.get(platform);
    }

    public void register(int id, ImcPlatform platform) throws RuntimeException {
        if (platforms.containsKey(id)) {
            throw new RuntimeException(new Exception("Another platform with same id has already registered"));
        }
        platforms.put(id, platform);
    }

    public void deliver(Message m, int platform) {

    }

    public void actuate(ImcPlatform platform, IActuation actuation) {

    }

}
