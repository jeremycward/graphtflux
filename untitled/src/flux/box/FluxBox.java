package flux.box;

import java.util.Objects;

public class FluxBox {
    private final InputGate inputGate;
    private final OutputGate outputGate;
    private final InternalProcessor internalProcessor;

    public FluxBox(InputGate inputGate, OutputGate outputGate, InternalProcessor internalProcessor) {
        Objects.requireNonNull(inputGate);
        Objects.requireNonNull(outputGate);
        Objects.requireNonNull(internalProcessor);
        this.inputGate = inputGate;
        this.outputGate = outputGate;
        this.internalProcessor = internalProcessor;
    }

    public InputGate getInputGate() {
        return inputGate;
    }


    public OutputGate getOutputGate() {
        return outputGate;
    }

    public InternalProcessor getInternalProcessor() {
        return internalProcessor;
    }
}
