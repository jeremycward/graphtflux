package demo;

import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.AttributeType;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.graphml.GraphMLExporter;

import java.util.HashMap;
import java.util.Map;
public class GraphExporterFunc {


    public static org.jgrapht.nio.GraphExporter<Cell, RelationshipEdge> createExporter() {

        /*
         * Create the exporter. The constructor parameter is a function which generates for each
         * vertex a unique identifier.
         */
        GraphMLExporter<Cell, RelationshipEdge> exporter =
                new GraphMLExporter<>(v -> v.getId());

        /*
         * Set to export the internal edge weights
         */


        /*
         * The exporter may need to generate for each vertex a set of attributes.
         */
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

        /*
         * Register additional color attribute for vertices
         */


        /*
         * Register additional name attribute for vertices and edges
         */
        exporter.registerAttribute("name", GraphMLExporter.AttributeCategory.ALL, AttributeType.STRING);

        return exporter;
    }

}