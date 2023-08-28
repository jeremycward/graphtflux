package cellgraph.mutations;

import demo.Mutation;

import java.util.Optional;

public class CellMutation<T> implements Mutation {
    private final String cellId;
    private final Optional<T> prevValue;
    private final T currentValue;

    public CellMutation(String cellId, Optional<T> prevValue, T currentValue) {
        this.cellId = cellId;
        this.prevValue = prevValue;
        this.currentValue = currentValue;
    }

    public String getCellId() {
        return cellId;
    }

    public Optional<T> getPrevValue() {
        return prevValue;
    }

    public T getCurrentValue() {
        return currentValue;
    }
}
