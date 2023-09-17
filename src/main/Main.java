package main;

import java.util.Scanner;

import lib280.graph.Vertex280;
import lib280.graph.WeightedGraphAdjListRep280;

public class Main {
    public static void main(String[] args) {
        // Read inputs
        final Scanner scanner = new Scanner(System.in);

        // Read N
        final int n = Integer.parseInt(scanner.nextLine());
        final WeightedGraphAdjListRep280<Vertex280> oldSpanningTree = new WeightedGraphAdjListRep280<Vertex280>(n, false);
        oldSpanningTree.ensureVertices(n);

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

                double weight = scanner.nextInt();

                if (oldSpanningTree.isAdjacent(srcIdx, dstIdx))
                    throw new RuntimeException("Edge (" + srcIdx + ", " + dstIdx + ") appears multiple times in the data file.");

                // Add the new edge
                oldSpanningTree.addEdge(srcIdx, dstIdx);

                // Set the weight of the edge.
                oldSpanningTree.setEdgeWeight(srcIdx, dstIdx, weight);

            }
        }
        System.out.println(oldSpanningTree);

        final int originalCost = -1;
        final int updatedCost = -1;
        System.out.println(originalCost);
        System.out.println(updatedCost);
    }
}