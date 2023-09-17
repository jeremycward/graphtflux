package cellgraph;

import demo.Action;
import demo.Cell;
import demo.RelationshipEdge;

import org.apache.commons.lang3.tuple.Pair;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.AttributeType;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.dot.DOTExporter;
import org.jgrapht.nio.graphml.GraphMLExporter;
import org.jgrapht.traverse.BreadthFirstIterator;
import reactor.core.publisher.Flux;

import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

public class CellGraph {


    Graph<Cell, RelationshipEdge> graph =
            new DefaultDirectedGraph<>(RelationshipEdge.class);

    public void addCell(Cell<?> newCell, List<Cell> feeders) {
        graph.addVertex(newCell);
        feeders.stream().forEach(feeder -> graph.addEdge(feeder, newCell, new RelationshipEdge(UUID.randomUUID().toString())));
    }

    public Pair<Cell, Cell> findLinkPoints() {
        List<Cell> inputNodes = graph.vertexSet().stream().filter(this::isBaseCell).collect(Collectors.toUnmodifiableList());
        List<Cell> outputNode = graph.vertexSet().stream().filter(this::isLeafCell).collect(Collectors.toUnmodifiableList());
        if (inputNodes.size() == 1 && outputNode.size() == 1) {
            return Pair.of(inputNodes.get(0), outputNode.get(0));
        } else {
            throw new IllegalArgumentException(String.format("Incorrect number of input and output points : in [%s] out [%s]",inputNodes.size(),outputNode.size()));
        }

    }
    public  Flux<Action> connect(Flux<Action> input){
        Pair<Cell,Cell> linkPoints = findLinkPoints();
        Cell inputCell = linkPoints.getLeft();
        Iterator<Cell<String>> iterator = new BreadthFirstIterator(graph,inputCell);
        CellConnector cc = new CellConnector(graph);
        Flux<Action> flux = input;
        while (iterator.hasNext()){
            flux = cc.join(iterator.next(),flux);
        }
        return flux;
    }

    private boolean isBaseCell(Cell c) {
        return graph.incomingEdgesOf(c).size() == 0;
    }

    private boolean isLeafCell(Cell c) {
        return graph.outgoingEdgesOf(c).size() == 0;
    }

    public void exportDotty(OutputStream bos) {
        DOTExporter<Cell, RelationshipEdge> exporter = new DOTExporter<>();
        exporter.setVertexAttributeProvider((v) -> {
            Map<String, Attribute> map = new LinkedHashMap<>();
            map.put("label", DefaultAttribute.createAttribute(v.getId().split("__")[0]));
            return map;
        });
        exporter.exportGraph(graph, bos);
    }

    public void exportGraphMl(OutputStream bos) {
        GraphMLExporter<Cell, RelationshipEdge> exporter = createExporter();
        exporter.exportGraph(graph, bos);
    }

    private static GraphMLExporter<Cell, RelationshipEdge> createExporter() {
        /*
         * Create the exporter. The constructor parameter is a function which generates for each
         * vertex a unique identifier.
         */
        GraphMLExporter<Cell, RelationshipEdge> exporter =
                new GraphMLExporter<>(v -> v.getId());

        exporter.setVertexAttributeProvider(v -> {
            Map<String, Attribute> m = new HashMap<>();
            m.put("name", DefaultAttribute.createAttribute("node-" + v.getId()));
            return m;
        });

        /*
         * Set the edge id provider.
         *
         * The exporter needs to generate for each edge a unique identifier.
         */
        exporter.setEdgeIdProvider(it -> it.getLabel());

        /*
         * The exporter may need to generate for each edge a set of attributes.
         */
        exporter.setEdgeAttributeProvider(e -> {
            Map<String, Attribute> m = new HashMap<>();
            m.put("name", DefaultAttribute.createAttribute(e.toString()));
            return m;
        });

        exporter.registerAttribute("name", GraphMLExporter.AttributeCategory.ALL, AttributeType.STRING);

        return exporter;
    }

    public Graph<Cell, RelationshipEdge> getGraph() {
        return graph;
    }
}
