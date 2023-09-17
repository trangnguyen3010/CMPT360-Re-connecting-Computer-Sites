package main;

import java.util.List;
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
        final WeightedGraphAdjListRep280<Vertex280> oldSpanningTree = new WeightedGraphAdjListRep280<Vertex280>(n, false);
        oldSpanningTree.ensureVertices(n);

        ArrayList<WeightedEdge280> oldTreeEdgeList = new ArrayList<>();

        // Read the old spanning tree T
        // by reading the next N-1 line
        // We know T has N-1 edges because T is spanning tree with n vertices
        for (int i = 0; i < n - 1; i++) {
            if (scanner.hasNext()) {
                // Get source vertex
                if (!scanner.hasNextInt())
                    throw new RuntimeException("Illegal file format or unexpected end of file.");
                int srcIdx = scanner.nextInt();

                // Get destination vertex
                if (!scanner.hasNextInt())
                    throw new RuntimeException("Illegal file format or unexpected end of file.");
                int dstIdx = scanner.nextInt();

                // Get the weight of the edge
                if (!scanner.hasNextInt())
                    throw new RuntimeException("Illegal file format or unexpected end of file.");
                int weight = scanner.nextInt();

                // summing the weight of the original network (T)
                originalCost += weight;

                if (oldSpanningTree.isAdjacent(srcIdx, dstIdx))
                    throw new RuntimeException("Edge (" + srcIdx + ", " + dstIdx + ") appears multiple times in the data file.");

                // Add the new edge
                oldSpanningTree.addEdge(srcIdx, dstIdx);

                // Set the weight of the edge.
                oldSpanningTree.setEdgeWeight(srcIdx, dstIdx, weight);

                // Add the edge to the old spanning tree's edge list
                oldTreeEdgeList.add(oldSpanningTree.getEdge(srcIdx, dstIdx));
            }
        }

        // Read K
        final int k = scanner.nextInt();
        ArrayList<WeightedEdge280> kEdgeList = new ArrayList<>();

        for (int i = 0; i < k; i++){
                // Get source vertex
                if (!scanner.hasNextInt())
                    throw new RuntimeException("Illegal file format or unexpected end of file.");
                int srcIdx = scanner.nextInt();

                // Get destination vertex
                if (!scanner.hasNextInt())
                    throw new RuntimeException("Illegal file format or unexpected end of file.");
                int dstIdx = scanner.nextInt();

                // Get the weight of the edge
                if (!scanner.hasNextInt())
                    throw new RuntimeException("Illegal file format or unexpected end of file.");
                int weight = scanner.nextInt();

                // Add the edge to the K's edge list
                kEdgeList.add(new WeightedEdge280<Vertex280>(oldSpanningTree.vertex(srcIdx), oldSpanningTree.vertex(dstIdx)));
        }

        List<WeightedEdge280> sortedTEdgeList = oldTreeEdgeList.stream().sorted(Comparator.comparingDouble(WeightedEdge280::getWeight)).collect(Collectors.toList());
        List<WeightedEdge280> sortedKEdgeList = kEdgeList.stream().sorted(Comparator.comparingDouble(WeightedEdge280::getWeight)).collect(Collectors.toList());

        final int updatedCost = -1;
        System.out.println(originalCost);
        System.out.print(updatedCost);
    }
}