package main;

import java.util.List;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import lib280.graph.Vertex280;
import lib280.graph.WeightedEdge280;
import lib280.graph.WeightedGraphAdjListRep280;

public class Main {
    public static void main(String[] args) {
        int originalCost = 0;

        // Read inputs
        final Scanner scanner = new Scanner(System.in);

        // Read N
        final int n = getInt(scanner.nextLine());

        // Read the graph 'graph' which has N vertices
        final WeightedGraphAdjListRep280<Vertex280> graph = new WeightedGraphAdjListRep280<Vertex280>(n, false);
        graph.ensureVertices(n);

        // We know T has N-1 edges because T is spanning tree with n vertices
        for (int i = 0; i < n - 1; i++) {
            WeightedEdge280<Vertex280> addedEdge = addEdgeForGraphFromStandarIn(graph, scanner);

            // summing the weight of the original network (T)
            originalCost += addedEdge.getWeight();
        }

        // Add the next K edges to the graph
        final int k = getInt(scanner.nextLine());

        for (int i = 0; i < k; i++) {
            addEdgeForGraphFromStandarIn(graph, scanner);
        }

        // Add the next M edges to the graph
        final int m = getInt(scanner.nextLine());
        
        for (int i = 0; i < m; i++) {
            addEdgeForGraphFromStandarIn(graph, scanner);
        }

        final double updatedCost = calculateCostUsingPrimsAlgo(graph);

        System.out.println(originalCost);
        System.out.print((int) updatedCost);
    }

    private static double calculateCostUsingPrimsAlgo(WeightedGraphAdjListRep280<Vertex280> newGraph) {
        final Vertex280 firstVertex = newGraph.vertex(1);
        Set<Vertex280> isSeen = new HashSet<>();

        isSeen.add(firstVertex);
        List<WeightedEdge280<Vertex280>> edgeOptions = newGraph.getAllEdgesFromV(firstVertex);

        edgeOptions = edgeOptions.stream()
                .filter(e -> !isSeen.contains(e.other(firstVertex)))
                .collect(Collectors.toList());

        double sum = 0.0;

        while (isSeen.size() != newGraph.numVertices()) {
            // the least-weight edge in the edge options
            WeightedEdge280<Vertex280> choosenEdge = edgeOptions.stream().min(Comparator.comparingDouble(WeightedEdge280::getWeight)).get();
            sum += choosenEdge.getWeight();

            // Remove the edge from options as we already account for it in the sum
            edgeOptions.remove(choosenEdge);

            Vertex280 newVertex = isSeen.contains(choosenEdge.firstItem()) ? choosenEdge.secondItem()
                    : choosenEdge.firstItem();

            isSeen.add(newVertex);

            // Add new edge options which are edges that incident at the newly-added vertex
            List<WeightedEdge280<Vertex280>> newOptions = newGraph.getAllEdgesFromV(newVertex);
            edgeOptions.addAll(newOptions);

            edgeOptions = edgeOptions.stream()
                    .filter(e -> !isSeen.contains(e.firstItem()) || !isSeen.contains(e.secondItem()))
                    .collect(Collectors.toList());
        }
        return sum;
    }

    private static WeightedEdge280<Vertex280> addEdgeForGraphFromStandarIn(
            final WeightedGraphAdjListRep280<Vertex280> graph, final Scanner scanner)
            throws IllegalArgumentException {
        if (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split(" ");

            if (line.length != 3) {
                throw new IllegalArgumentException("Expected 3 numbers but there's " + line.length + " numbers!");
            }

            // Get source vertex.
            int srcIdx = getInt(line[0]);

            // Get destination vertex.
            int dstIdx = getInt(line[1]);

            // Get the weight of the edge.
            int weight = getInt(line[2]);

            if (!graph.isAdjacent(srcIdx, dstIdx)) {
                // Add the new edge.
                graph.addEdge(srcIdx, dstIdx);

                // Set the weight of the new edge.
                graph.setEdgeWeight(srcIdx, dstIdx, weight);

                return graph.getEdge(srcIdx, dstIdx);
            }
        }
        return null;
    }

    private static int getInt(final String string) throws IllegalArgumentException {
        try {
            return Integer.parseInt(string);
        } catch (Exception e) {
            throw new IllegalArgumentException("Detected non-integer input " + string + "!");
        }
    }

}