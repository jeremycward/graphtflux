package cellgraph;

import demo.Action;
import demo.Cell;
import demo.RelationshipEdge;
import org.jgrapht.Graph;
import reactor.core.publisher.Flux;

import java.util.*;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class CellConnector {
    private final Graph gr;
    private static final Logger logger = Logger.getLogger(CellConnector.class.getName());

    Function<Object[], ?  extends Action> combinerFunc = (Object[] in)->
        Action.gather(Arrays.stream(in).map(Action.class::cast).collect(Collectors.toUnmodifiableList()));

    Map<String,Flux<Action>> connectedCellMap = new HashMap<>();
    public Flux<Action> join(Cell<?> cell,Flux<Action> input){
        List<Flux<Action>> feeders = feedersOf(cell);
        Flux<Action> gathered = feeders.isEmpty() ? input : Flux.zip(feeders,combinerFunc);
        Flux<Action> retVal = gathered.map(it->this.processInput(it,cell));
        connectedCellMap.put(cell.getId(),retVal);
        return retVal;
    }
    private static Action processInput(Action input,Cell<?> cell){
        Action retVal = cell.process(input);
        logger.info(String.format("Processing cell [%s] input mutations (in: %s out: %s) [%s] output mutations [%s]",
                cell.getId(),
                input.getMutations().size(),
                retVal.getMutations().size(),
                input.getMutations(),retVal.getMutations()));
        return retVal;
    }
    private List<Flux<Action>> feedersOf(Cell it){
        Set<RelationshipEdge> incoming = gr.incomingEdgesOf(it);
        return
        incoming.stream().map(edge->gr.getEdgeSource(edge))
                .map(Cell.class::cast)
                .map(cell->this.connectedCellMap.get(cell.getId())).collect(Collectors.toUnmodifiableList());
    }

    public CellConnector(Graph gr) {
        this.gr = gr;
    }
}
