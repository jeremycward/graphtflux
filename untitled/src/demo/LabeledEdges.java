/*
 * (C) Copyright 2012-2023, by Barak Naveh and Contributors.
 *
 * JGraphT : a free Java graph-theory library
 *
 * See the CONTRIBUTORS.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0, or the
 * GNU Lesser General Public License v2.1 or later
 * which is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1-standalone.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR LGPL-2.1-or-later
 */

package demo;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.nio.GraphExporter;

import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * An example of how to apply edge labels using a custom edge class.
 *
 * @author Barak Naveh
 */
public class LabeledEdges
{
    private static final String FRIEND = "friend";
    private static final String ENEMY = "enemy";

    /**
     * The starting point for the demo.
     *
     * @param args ignored.
     */
    public static void main(String[] args)
    {
        // @example:create:begin
        Graph<Cell, RelationshipEdge> graph =
                new DefaultDirectedGraph<>(RelationshipEdge.class);

        Map<String, Cell> cells = Map.of();

//                List.of(
//                new Cell("John"),
//                new Cell("James"),
//                new Cell("Jeremy")
//        ).stream().collect(Collectors.toMap(Cell::getId, it->it));

//
//
//        graph.addVertex(cells.get("John"));
//        graph.addVertex(cells.get("James"));
//        graph.addVertex(cells.get("Jeremy"));
//        graph.addEdge(cells.get("John"),cells.get("James"),new RelationshipEdge("friend"));
//        graph.addEdge(cells.get("Jeremy"),cells.get("James"),new RelationshipEdge("enemy"));
//
//
//
//        // @example:print:begin
//        for (RelationshipEdge edge : graph.edgeSet()) {
//            Cell v1 = graph.getEdgeSource(edge);
//            Cell v2 = graph.getEdgeTarget(edge);
//            if (edge.getLabel().equals("enemy")) {
//                System.out.printf(v1 + " is an enemy of " + v2 + "\n");
//            } else if (edge.getLabel().equals("friend")) {
//                System.out.printf(v1 + " is a friend of " + v2 + "\n");
//            }
//        }
//        GraphExporter<Cell,RelationshipEdge> exporter = GraphExporterFunc.createExporter();
//        // export as string
//        Writer writer = new StringWriter();
//        exporter.exportGraph(graph, writer);
//        String graph1AsGraphML = writer.toString();
//
//        // display
//        System.out.println(graph1AsGraphML);
//
//

    }

    // @example:isEnemyOf:begin
    private static boolean isEnemyOf(
            Graph<String, RelationshipEdge> graph, String person1, String person2)
    {
        return graph.getEdge(person1, person2).getLabel().equals(ENEMY);
    }
    // @example:isEnemyOf:end
}

