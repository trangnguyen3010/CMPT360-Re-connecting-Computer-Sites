package main;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
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
        final WeightedGraphAdjListRep280<Vertex280> oldSpanningTree = new WeightedGraphAdjListRep280<Vertex280>(n,
                false);
        oldSpanningTree.ensureVertices(n);

        List<WeightedEdge280> allEdgesToConsider = new ArrayList<>();

        // Read the old spanning tree T
        // by reading the next N-1 line
        // We know T has N-1 edges because T is spanning tree with n vertices
        for (int i = 0; i < n - 1; i++) {
            if (scanner.hasNext()) {
                WeightedEdge280<Vertex280> addedEdge = addEdgeForGraphFromStandarIn(oldSpanningTree, scanner);

                // summing the weight of the original network (T)
                originalCost += addedEdge.getWeight();

                // Add the edge to the old spanning tree's edge list
                allEdgesToConsider.add(addedEdge);
            }
        }

        // Read K
        final int k = scanner.nextInt();
        final WeightedGraphAdjListRep280<Vertex280> newGraph = (WeightedGraphAdjListRep280<Vertex280>) oldSpanningTree
                .clone();
        newGraph.ensureVertices(n);

        for (int i = 0; i < k; i++) {
        
            WeightedEdge280<Vertex280> addedEdge = addEdgeForGraphFromStandarIn(newGraph, scanner);

            // Add the edge to the K's edge list
            allEdgesToConsider.add(addedEdge);
        }

        allEdgesToConsider = allEdgesToConsider.stream()
                .sorted(Comparator.comparingDouble(WeightedEdge280::getWeight))
                .collect(Collectors.toList());

        final Optional<Integer> updatedCost = calculateNewCost(newGraph);

        System.out.println(originalCost);
        System.out.print(updatedCost.isPresent() ? updatedCost.get().intValue() : originalCost);
    }

    private static WeightedEdge280<Vertex280> addEdgeForGraphFromStandarIn (final WeightedGraphAdjListRep280<Vertex280> graph, final Scanner scanner){
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

    private static int getNextInt(final Scanner scanner) {
        if (!scanner.hasNextInt())
            throw new RuntimeException("Wrong input format or unexpected end of file!");
        int number = scanner.nextInt();
        return number;
    }

    private static Optional<Integer> calculateNewCost(WeightedGraphAdjListRep280<Vertex280> newGraph) {
        if (newGraph.numEdges() == 0) {
            return Optional.empty();
        } else {
            ArrayList<Vertex280> isSeen = new ArrayList<>();
            double sum = 0.0;
            for (int i = 1; i <= newGraph.numVertices(); i++) {
                if (!isSeen.contains(newGraph.vertex(i))) {
                    isSeen.add(newGraph.vertex(1));
                    sum += calculateCost(newGraph, newGraph.vertex(1), isSeen);
                }
            }
            return Optional.of((int) sum);
        }
    }

    private static double calculateCost(WeightedGraphAdjListRep280<Vertex280> newGraph, Vertex280 currentVertex,
            ArrayList<Vertex280> isSeen) {
        if (isSeen.size() == newGraph.numVertices()) {
            return 0.0;
        }

        // Get all edges that has current vertex as one end
        List<WeightedEdge280<Vertex280>> allAdjEdge = new ArrayList<>();
        allAdjEdge.addAll(newGraph.getAdjacentVertices(currentVertex));

        // Find the least-heavy edge that incident at a vertex which are not seen yet
        Optional<WeightedEdge280<Vertex280>> nextEdge = allAdjEdge.stream().sorted(Comparator.comparingDouble(WeightedEdge280::getWeight))
                .filter(e -> !isSeen.contains(e.other(currentVertex)))
                .findFirst();

        // this current vertex is a leaf node in the spaning tree
        if (!nextEdge.isPresent()) {
            return 0.0;
        } // not a leaf node, continue spanning
        else {
            isSeen.add(nextEdge.get().other(currentVertex));
            allAdjEdge.remove(nextEdge);
            double sum = nextEdge.get().getWeight()
                    + calculateCost(newGraph, nextEdge.get().other(currentVertex), isSeen);
            return sum;
        }
    }

}