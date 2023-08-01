package flux.box;

import java.util.List;

@FunctionalInterface
public interface InternalProcessor {
    List<FluxNotification> process(FluxEvent fluxEvent);
}
