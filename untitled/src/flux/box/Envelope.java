package flux.box;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Envelope {
    private final List<FluxEvent> events;
    private final String destination;

    public Envelope(String destination,FluxEvent... events ) {
        this.events = Arrays.stream(events)
                .collect(Collectors.toUnmodifiableList());
        this.destination = destination;
    }

    public List<FluxEvent> getEvents() {
        return events;
    }

    public String getDestination() {
        return destination;
    }
}
