package cellgraph;

import cellgraph.mutations.MktDataCapture;

import java.util.List;
import java.util.stream.Collectors;

public class Segment {
    final List<MktDataCapture> captureList;

    public Segment(List<MktDataCapture> captureList) {
        this.captureList = captureList;
    }
    public String describe(){
        return captureList.stream().map(Object::toString).collect(Collectors.joining(","));

    }
}
