package main;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collector;
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
        final int n = Integer.parseInt(scanner.nextLine());

        // Read the graph 'graph' which has N vertices, and have T + K edges
        // and apply Prim's algorithm to find the new spanning tree T'
        // (We don't need to add the rest of originally available high-speed lines (lets call them U)
        // since for each vertex, its minimum edge must be included in T
        // we know the edge that is in T would always smaller or equal to that one that is in U)
        // so we only need to consider the edge in T vs the new edge in K (if any)
        final WeightedGraphAdjListRep280<Vertex280> graph = new WeightedGraphAdjListRep280<Vertex280>(n, false);
        graph.ensureVertices(n);

        List<WeightedEdge280> allEdgesToConsider = new ArrayList<>();

        // Read the old spanning tree T
        // by reading the next N-1 line
        // We know T has N-1 edges because T is spanning tree with n vertices
        for (int i = 0; i < n - 1; i++) {
            if (scanner.hasNext()) {
                WeightedEdge280<Vertex280> addedEdge = addEdgeForGraphFromStandarIn(graph, scanner);

                // summing the weight of the original network (T)
                originalCost += addedEdge.getWeight();

                // Add the edge to the old spanning tree's edge list
                allEdgesToConsider.add(addedEdge);
            }
        }

        // Add the next K edges to the graph
        final int k = scanner.nextInt();

        for (int i = 0; i < k; i++) {
            WeightedEdge280<Vertex280> addedEdge = addEdgeForGraphFromStandarIn(graph, scanner);
            // Add the edge to the K's edge list
            allEdgesToConsider.add(addedEdge);
        }

        allEdgesToConsider = allEdgesToConsider.stream()
                .sorted(Comparator.comparingDouble(WeightedEdge280::getWeight))
                .collect(Collectors.toList());

        final double updatedCost = calculateCost(graph);

        System.out.println(originalCost);
        System.out.print((int) updatedCost);
    }

    private static int getNextInt(final Scanner scanner) {
        if (!scanner.hasNextInt())
            throw new RuntimeException("Wrong input format or unexpected end of file!");
        int number = scanner.nextInt();
        return number;
    }

    private static double calculateCost(WeightedGraphAdjListRep280<Vertex280> newGraph) {
        final Vertex280 firstVertex = newGraph.vertex(1);
        Set<Vertex280> isSeen = new HashSet<>();

        isSeen.add(firstVertex);
        List<WeightedEdge280<Vertex280>> edgeOptions = newGraph.getAdjacentVertices(firstVertex);

        edgeOptions = edgeOptions.stream()
                .filter(e -> !isSeen.contains(e.other(firstVertex)))
                .sorted(Comparator.comparingDouble(WeightedEdge280::getWeight))
                .collect(Collectors.toList());

        double sum = 0.0;
        while (!edgeOptions.isEmpty() && isSeen.size() != newGraph.numVertices()) {
            // the least-heavy edge
            WeightedEdge280<Vertex280> choosenEdge = edgeOptions.get(0);

            Vertex280 currentVertex = isSeen.contains(choosenEdge.firstItem()) ? choosenEdge.firstItem()
                    : choosenEdge.secondItem();

            final Vertex280 otherVertex = choosenEdge.other(currentVertex);
            isSeen.add(otherVertex);

            sum += choosenEdge.getWeight();

            edgeOptions.remove(0);

            // Add new options
            List<WeightedEdge280<Vertex280>> newOptions = newGraph.getAdjacentVertices(otherVertex).stream()
                    .filter(e -> !isSeen.contains(e.other(otherVertex)))
                    .collect(Collectors.toList());

            edgeOptions.addAll(newOptions);

            edgeOptions = edgeOptions.stream()
                    .filter(e -> !isSeen.contains(e.firstItem()) || !isSeen.contains(e.secondItem()))
                    .sorted(Comparator.comparingDouble(WeightedEdge280::getWeight))
                    .collect(Collectors.toList());
        }
        return sum;
    }

    private static WeightedEdge280<Vertex280> addEdgeForGraphFromStandarIn(
            final WeightedGraphAdjListRep280<Vertex280> graph, final Scanner scanner) {
        // Get source vertex.
        int srcIdx = getNextInt(scanner);

        // Get destination vertex.
        int dstIdx = getNextInt(scanner);

        // Get the weight of the edge.
        int weight = getNextInt(scanner);

        if (graph.isAdjacent(srcIdx, dstIdx))
            throw new RuntimeException(
                    "Edge (" + srcIdx + ", " + dstIdx + ") appears multiple times in the data file.");

        // Add the new edge.
        graph.addEdge(srcIdx, dstIdx);

        // Set the weight of the new edge.
        graph.setEdgeWeight(srcIdx, dstIdx, weight);

        return graph.getEdge(srcIdx, dstIdx);
    }

}