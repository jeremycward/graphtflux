package cellgraph;

import demo.Cell;
import demo.Mutation;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ConnectedCell<T> {

   private final Flux<Mutation> output;

    public ConnectedCell(Cell<T> cell, List<Flux<Mutation>> downstreamSources) {


        Flux<List<Mutation>>  combined = Flux.zip(downstreamSources,ConnectedCell::zipitUp);
        output = combined.map(it->cell.process(it));
    }

    public Flux<Mutation> getOutput() {
        return output;
    }
    private static  List<Mutation> zipitUp(Object[] input){
        return Arrays.stream(input).map(Mutation.class::cast).collect(Collectors.toUnmodifiableList());
    }
}
