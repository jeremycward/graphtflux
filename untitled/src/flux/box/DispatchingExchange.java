package flux.box;

import java.util.List;
import java.util.concurrent.Future;

@FunctionalInterface
public interface DispatchingExchange {
    Future<List<FluxNotification>> postSync(Envelope envelope);

}
