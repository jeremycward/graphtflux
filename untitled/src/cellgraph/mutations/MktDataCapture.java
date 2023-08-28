package cellgraph.mutations;

import cellgraph.MktDataIdentifier;
import demo.Mutation;

import java.time.LocalDateTime;

public class MktDataCapture implements Mutation {
    private final MktDataIdentifier mktDataIdentifier;
    private final double value;
    private final LocalDateTime captured;

    public MktDataCapture(MktDataIdentifier mktDataIdentifier, double value, LocalDateTime captured) {
        this.mktDataIdentifier = mktDataIdentifier;
        this.value = value;
        this.captured = captured;
    }

    public MktDataIdentifier getMktDataIdentifier() {
        return mktDataIdentifier;
    }

    public double getValue() {
        return value;
    }

    public LocalDateTime getCaptured() {
        return captured;
    }

    @Override
    public String toString() {
        return "MktDataCapture{" +
                "mktDataIdentifier=" + mktDataIdentifier +
                ", value=" + value +
                ", captured=" + captured +
                '}';
    }
}
