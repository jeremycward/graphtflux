package cellgraph;

import cellgraph.mutations.CellMutation;
import demo.Action;
import demo.BaseCellImpl;
import demo.Mutation;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CurveCell extends BaseCellImpl {
    public CurveCell( String id) {
        super(String.class, id);
    }

    @Override
    public Action process(Action input) {
        List<Segment> incomingSegs =
                input.getMutations().stream()
                        .filter(CellMutation.class::isInstance)
                        .map(CellMutation.class::cast)
                        .map(CellMutation::getCurrentValue)
                        .filter(Segment.class::isInstance)
                        .map(Segment.class::cast).collect(Collectors.toUnmodifiableList());

        if (incomingSegs.isEmpty()){
            return Action.EMPTY_ACTION;
        }else{
            String newVal = incomingSegs.stream()
                    .map(Segment::toString)
                    .collect(Collectors.joining(","));
            List<Mutation> mutations= List.of(regenerate(newVal));
            return new Action(mutations);

        }

    }

}
