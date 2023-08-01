package flux.box;

public class OutputChannel<T> {
    private final String id;
    private final Class<T> outputType;

    public OutputChannel(String id, Class<T> outputType) {
        this.id = id;
        this.outputType = outputType;
    }
}
