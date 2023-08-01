package cellgraph;

import demo.BaseCellImpl;
import demo.Cell;
import demo.Mutation;
import flux.box.*;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.DepthFirstIterator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;

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
            public List<Mutation> process(List<Mutation> input) {
                return List.of();
            }
        };

    }
    @Test
    public void testConnections(){
        CellGraph gr = new CellGraph();
        gr.addCell(one, List.of());
        gr.addCell(two, List.of(one));
        gr.addCell(three, List.of(one));



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


