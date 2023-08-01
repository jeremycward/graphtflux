package cellgraph;

import demo.Cell;
import demo.RelationshipEdge;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.AttributeType;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.dot.DOTExporter;
import org.jgrapht.nio.graphml.GraphMLExporter;

import java.io.OutputStream;
import java.util.*;

public class CellGraph {
    Graph<Cell, RelationshipEdge> graph =
            new DefaultDirectedGraph<>(RelationshipEdge.class);

    public void addCell( Cell <?> newCell, List<Cell>   feeders  ){
        graph.addVertex(newCell);
        feeders.stream().forEach(feeder-> graph.addEdge(feeder,newCell,new RelationshipEdge(UUID.randomUUID().toString())));
    }

    public void exportDotty(OutputStream bos){
        DOTExporter<Cell,RelationshipEdge> exporter = new DOTExporter<>();
        exporter.setVertexAttributeProvider((v) -> {
            Map<String, Attribute> map = new LinkedHashMap<>();
            map.put("label", DefaultAttribute.createAttribute(v.getId().split("__")[0]));
            return map;
        });
        exporter.exportGraph(graph, bos);
    }
    public void exportGraphMl(OutputStream bos){
        GraphMLExporter<Cell,RelationshipEdge> exporter = createExporter();
        exporter.exportGraph(graph,bos);
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
