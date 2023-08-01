package flux.box;


import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.function.Consumer;


public class InputGate {
    private final Map<String, Consumer<FluxEvent>> inputChannels;

    public InputGate(Map<String, Consumer<FluxEvent>> inputChannels) {
        Objects.requireNonNull(inputChannels);
        this.inputChannels = inputChannels;
    }


    private static IllegalArgumentException illegalChannelException(String channelId) {
        return new IllegalArgumentException("Invalid channel Id: " + channelId);
    }

    private void despatch(Envelope... envelopes){

    }


    public void post(Envelope... envelopes) {
        Arrays.stream(envelopes).forEach(env -> {
                    Optional<Consumer<FluxEvent>> found = Optional.ofNullable(inputChannels.get(env.getDestination()));
                    Consumer<FluxEvent> channel = found.orElseThrow(() -> illegalChannelException(env.getDestination()));
                    env.getEvents().stream().forEach(channel::accept);
                }
        );

    }
}