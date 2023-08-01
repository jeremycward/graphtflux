package flux.box;

import java.util.function.Consumer;

@FunctionalInterface
public interface OutputGate {
    <T> void collect(OutputChannel outputPort, Consumer<T> consumer, Class<T> outputType);
}
