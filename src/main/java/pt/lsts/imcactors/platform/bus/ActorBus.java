package pt.lsts.imcactors.platform.bus;

import pt.lsts.imc4j.msg.Message;
import pt.lsts.imcactors.actors.ImcActor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ActorBus {

    private ConcurrentHashMap<ImcActor, ArrayList<Class<? extends Message>>> actors = new ConcurrentHashMap<>();
    private ConcurrentHashMap<ImcActor, ArrayList<Message>> inboxes = new ConcurrentHashMap<>();

    /**
     * Register an actor in this bus
     * @param actor
     */
    public void register(ImcActor actor) {
        ArrayList<Class<? extends Message>> subs = new ArrayList<>();
        subs.addAll(actor.getSubscriptions());
        actors.putIfAbsent(actor, subs);
    }

    /**
     * post message to the inboxes of actors that subscribed to it
     * @param msg Message to post
     */
    public synchronized void post(Message msg) {
        actors.forEach((actor,subscriptions) -> {
            if (subscriptions.contains(msg.getClass())) {
                ArrayList<Message> inbox = inboxes.getOrDefault(actor, new ArrayList<>());
                inboxes.putIfAbsent(actor, inbox);
                inbox.add(msg);
            }
        });
    }

    /**
     * Retrieve pending messages
     * @param actor The actor for which to retrieve messages
     * @param timestamp Retrieve messages only until this time
     * @return All messages in the actor's inbox until given time
     */
    public List<Message> poll(ImcActor actor, double timestamp) {
        ArrayList<Message> inbox = inboxes.getOrDefault(actor, new ArrayList<>());
        List<Message> curMsgs = inbox.stream()
            .filter(msg->msg.timestamp <= timestamp).collect(Collectors.toList());
        inbox.removeAll(curMsgs);
        return curMsgs;
    }
}
