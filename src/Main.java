import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws Exception {
        String filename = (args.length > 0) ? args[0] : "Data.txt";

        // optional source/destination as args[1], args[2]
        int source = 12;
        int destination = 34;
        if (args.length >= 3) {
            try {
                source = Integer.parseInt(args[1]);
                destination = Integer.parseInt(args[2]);
            } catch (NumberFormatException ex) {
                System.out.println("Invalid source/destination args; using defaults 12 and 34.");
            }
        }

        int[][] matrix;
        try {
            matrix = readInputFileIntoMatrix(filename);
        } catch (IOException e) {
            System.out.println("Error reading file: " + filename);
            e.printStackTrace();
            return;
        }

        if (matrix == null || matrix.length == 0) {
            System.out.println("Input matrix is empty.");
            return;
        }

        if (source < 0 || source >= matrix.length || destination < 0 || destination >= matrix.length) {
            System.out.println("Source or destination out of range. matrix size=" + matrix.length);
            return;
        }

        int[] shortestPath = Dijkstra(matrix, source, destination);

        String outFile = "output.txt";
        // clear existing output file (start fresh)
        try (PrintWriter clear = new PrintWriter(outFile)) {
            // truncate
        }

        // append the three-line result for the pair (source, destination)
        writeResultToFile(outFile, source, destination, shortestPath, matrix);

        System.out.println("Wrote result to " + Paths.get(outFile).toAbsolutePath());
    }

    // INPUT: the starting vertex s and ending vertex e, where A is the input
    // matrix.
    // OUTPUT: an array representing the shortest path from s to e.
    public static int[] Dijkstra(int[][] A, int s, int e) {

        // create three arrays: dist, prev, and visited
        int n = A.length;
        int[] dist = new int[n];
        int[] prev = new int[n];
        boolean[] visited = new boolean[n];

        // initialize dist array with infinity and prev array with -1
        for (int i = 0; i < n; i++) {
            dist[i] = Integer.MAX_VALUE;
            prev[i] = -1;
            visited[i] = false;
        }

        // set the distance of the starting vertex to 0
        dist[s] = 0;

        // main loop of Dijkstra's algorithm
        for (int i = 0; i < n; i++) {
            // find the vertex with the minimum distance that has not been visited
            int u = -1;
            int minDist = Integer.MAX_VALUE;
            for (int j = 0; j < n; j++) {
                if (!visited[j] && dist[j] < minDist) {
                    u = j;
                    minDist = dist[j];
                }
            }

            if (u == -1) {
                break; // all remaining vertices are inaccessible
            }

            visited[u] = true;

            // update distances for neighbors of u
            for (int v = 0; v < n; v++) {
                if (A[u][v] != 0 && !visited[v]) {
                    int alt = dist[u] + A[u][v];
                    if (alt < dist[v]) {
                        dist[v] = alt;
                        prev[v] = u;
                    }
                }
            }
        }

        // reconstruct the shortest path from s to e
        List<Integer> path = new ArrayList<>();
        for (int at = e; at != -1; at = prev[at]) {
            path.add(0, at);
        }
        if (path.get(0) != s) {
            return new int[0]; // no path found
        }

        return path.stream().mapToInt(Integer::intValue).toArray();
    }

    public static int[][] readInputFileIntoMatrix(String filename) throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(filename));

        List<int[]> rows = new ArrayList<>();

        String line;

        while ((line = reader.readLine()) != null) {

            // Split vertex label from adjacency matrix row
            String[] parts = line.split("\\t");

            String[] weights = parts[1].split(",");

            int[] row = new int[weights.length];

            for (int i = 0; i < weights.length; i++) {
                row[i] = Integer.parseInt(weights[i].trim());
            }

            rows.add(row);
        }

        reader.close();

        int[][] matrix = new int[rows.size()][];

        for (int i = 0; i < rows.size(); i++) {
            matrix[i] = rows.get(i);
        }

        return matrix;
    }

    public static void writeResultToFile(String outFilename, int s, int e, int[] path, int[][] matrix)
            throws IOException {
        // append the three-line result for pair (s,e) to outFilename
        try (FileWriter fw = new FileWriter(outFilename, true);
                PrintWriter writer = new PrintWriter(fw)) {

            // First row: "s, e"
            writer.println(s + ", " + e);

            // Second row: path or "No path"
            if (path == null || path.length == 0) {
                writer.println("No path");
                writer.println("INF");
                return;
            } else {
                for (int i = 0; i < path.length; i++) {
                    writer.print(path[i]);
                    if (i < path.length - 1)
                        writer.print(", ");
                }
                writer.println();
            }

            // Third row: total weight
            int sum = 0;
            if (path.length >= 2) {
                for (int i = 0; i < path.length - 1; i++) {
                    int u = path[i];
                    int v = path[i + 1];
                    sum += matrix[u][v];
                }
            }
            writer.println(sum);
        }
    }
}
