package cellgraph;

import cellgraph.mutations.CellMutation;
import cellgraph.mutations.MktDataCapture;
import demo.Action;
import demo.BaseCellImpl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SegmentCell extends BaseCellImpl<Segment> {
    public SegmentCell( String id) {
        super(Segment.class, id);
    }

    @Override
    public Action process(Action input) {

        List<MktDataCapture> mktDatacaptures = input.getMutations().stream()
                .filter(CellMutation.class::isInstance)
                .map(CellMutation.class::cast)
                .map(CellMutation::getCurrentValue)
                .filter(MktDataCapture.class::isInstance)
                .map(MktDataCapture.class::cast).collect(Collectors.toUnmodifiableList());

        return  mktDatacaptures.isEmpty() ? Action.EMPTY_ACTION :
                new Action(List.of(regenerate(new Segment(mktDatacaptures))));






    }
}
