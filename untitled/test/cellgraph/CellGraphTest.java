package cellgraph;

import cellgraph.mutations.CellMutation;
import cellgraph.mutations.MktDataCapture;
import demo.Action;
import demo.BaseCellImpl;
import demo.Cell;
import demo.Mutation;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


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
        final int count=1000;
        MajorCcyBuilder builder = new MajorCcyBuilder();
        CellGraph gr =builder.getCellGraph();
        List<Mutation> captures =
        builder.getMktSetGenerators().stream()
                .map(gen->gen.next(count))
                .flatMap(Collection::stream).collect(Collectors.toUnmodifiableList());
        Assert.assertEquals((40* count),captures.size());

        Action act = new Action(captures);
        Flux<Action> fl = gr.connect(Flux.just(act));

        StepVerifier.create(fl)
                .assertNext(action-> {
                    Assert.assertEquals(5,action.getMutations().size());

                } )
                .verifyComplete();
    }

    @Test
    public void testBasicCurveGraph(){
        MajorCcyBuilder builder = new MajorCcyBuilder();
        CellGraph gr =builder.getCellGraph();

        MktDataIdentifier mktDataIdentifier = new MktDataIdentifier("6M",Currency.getInstance("EUR"));
        MktDataCapture capture =  new MktDataCapture(mktDataIdentifier,3.14d, LocalDateTime.now());
        Action act = new Action(List.of(capture));
        Flux<Action> fl = gr.connect(Flux.just(act));

        StepVerifier.create(fl)
                .assertNext(action-> {
                    Assert.assertEquals(1,action.getMutations().size());
                    CellMutation mutation =
                    action.getMutations().stream()
                            .filter(CellMutation.class::isInstance)
                            .map(CellMutation.class :: cast)
                            .findFirst().get();

                    Assert.assertEquals("EUR_PRICING_CURVE",mutation.getCellId());

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


