package cellgraph;

import demo.Action;
import demo.BaseCellImpl;

public class OutputCell extends BaseCellImpl<Integer> {
    public static final String OUTPUT_CELL_ID = java.util.UUID.randomUUID().toString();

    @Override
    public Action process(Action input) {
        return input;
    }

    public OutputCell() {
        super(Integer.class, OUTPUT_CELL_ID);
    }

}
