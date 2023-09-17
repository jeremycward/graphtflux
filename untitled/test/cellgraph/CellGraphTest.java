package cellgraph;

import cellgraph.mutations.CellMutation;
import cellgraph.mutations.MktDataCapture;
import demo.Action;
import demo.BaseCellImpl;
import demo.Cell;
import demo.Mutation;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;


@RunWith(MockitoJUnitRunner.class)
public class CellGraphTest {

    private Cell<String> one = aCell("one");
    private Cell<String> two = aCell("two");
    private Cell<String> three = aCell("three");
    private Cell<String> four = aCell("four");
    private Cell<String> five = aCell("five");
    private Cell<String> six = aCell("six");
    private Cell<String> seven = aCell("seven");
    private Cell<String> eight = aCell("eight");

    private static final Cell<String> aCell(String name) {
        return new BaseCellImpl<>(String.class, name) {
            @Override
            public Action process(Action input) {

                return input;
            }
        };

    }


    @Test
    public void testPopulatedGraph(){
        final int count=10;
        MajorCcyGraph builder = MajorCcyGraph.aCcyGraph().build();
        CellGraph gr =builder.getCellGraph();
        List<Mutation> captures =
        builder.getMktSetGenerators().stream()
                .map(gen->gen.next(count))
                .flatMap(Collection::stream).collect(Collectors.toUnmodifiableList());
        assertEquals((40* count),captures.size());

        Action act = new Action(captures);
        Flux<Action> fl = gr.connect(Flux.just(act));

        StepVerifier.create(fl)
                .assertNext(action-> {
                    assertEquals(5,action.getMutations().size());

                } )
                .verifyComplete();
    }
    @Test
    public void testEmptyGraphWithOneUpdate(){

        MajorCcyGraph builder = MajorCcyGraph.aCcyGraph()
                .withCcys("USD").withLongTenors("1Y")
                .withShortTenors("1D").build();
        CellGraph gr =builder.getCellGraph();
        Mutation mutation =  new MktDataCapture(new MktDataIdentifier("1Y",Currency.getInstance("USD")),2,LocalDateTime.now());
        Flux<Action> actionList = Flux.just(new Action(List.of(mutation)));
        Flux<Action> actions = gr.connect(actionList);
        StepVerifier.create(actions)
                .expectNextMatches(action->{
                    if (action.getMutations().size()!=1) return false;
                    CellMutation cellMutation = (CellMutation) action.getMutations().get(0);
                    return cellMutation.getCellId().equals("USD_PRICING_CURVE");
                }).expectComplete()
                .verify();

    }
    @Test
    public void testEmptyGraphWithOneUpdateNoMutations(){

        MajorCcyGraph builder = MajorCcyGraph.aCcyGraph()
                .withCcys("USD").withLongTenors("1Y")
                .withShortTenors("1D").build();
        CellGraph gr =builder.getCellGraph();
        Mutation mutation =  new MktDataCapture(new MktDataIdentifier("1Y",Currency.getInstance("JPY")),2,LocalDateTime.now());
        Flux<Action> actionList = Flux.just(new Action(List.of(mutation)));
        Flux<Action> actions = gr.connect(actionList);
        StepVerifier.create(actions)
                .expectNextMatches(action->{
                    return action.getMutations().size()==0;
                })
                .verifyComplete();
    }


    @Test
    public void testBasicCurveGraph(){
        MajorCcyGraph builder = new MajorCcyGraph.Builder().build();
        CellGraph gr =builder.getCellGraph();

        MktDataIdentifier mktDataIdentifier = new MktDataIdentifier("6M",Currency.getInstance("EUR"));
        MktDataCapture capture =  new MktDataCapture(mktDataIdentifier,3.14d, LocalDateTime.now());
        Action act = new Action(List.of(capture));
        Flux<Action> fl = gr.connect(Flux.just(act));

        StepVerifier.create(fl)
                .assertNext(action-> {
                    assertEquals(1,action.getMutations().size());
                    CellMutation mutation =
                    action.getMutations().stream()
                            .filter(CellMutation.class::isInstance)
                            .map(CellMutation.class :: cast)
                            .findFirst().get();

                    assertEquals("EUR_PRICING_CURVE",mutation.getCellId());

                } )
                .verifyComplete();


    }
    @Test
    public void testConnections(){
        CellGraph gr = new CellGraph();
        gr.addCell(one, List.of());
        gr.addCell(two, List.of(one));
        gr.addCell(three, List.of(one));
        gr.addCell(four,List.of(two,three));

        Action single = new Action(new ArrayList<>());
        Flux inputFlux = Flux.just(single);
        Flux outputFlux = gr.connect(inputFlux);

        StepVerifier.create(outputFlux).expectNextCount(1).verifyComplete();
    }

    @Test
    public void testGraphml() {
        CellGraph gr = new CellGraph();
        gr.addCell(one, List.of());
        gr.addCell(two, List.of(one));
        gr.addCell(three, List.of(one));
        gr.addCell(four, List.of(one));
        gr.addCell(five, List.of(three));
        gr.addCell(six, List.of(two));
        gr.addCell(seven, List.of(two,five,four));
        gr.addCell(eight, List.of(six,seven));
        gr.exportDotty(System.out);

        Iterator<Cell<String>> iterator = new BreadthFirstIterator(gr.getGraph(), one);
        while (iterator.hasNext()) {
            Cell<String> next = iterator.next();
            System.out.println(next.getId() +" " + gr.getGraph().incomingEdgesOf(next).size() );
        }
    }


}


