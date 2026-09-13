import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        String filename = "Data.txt";
        int[][] matrix = null;
        try {
            matrix = readInputFileIntoMatrix(filename);
            writeToOutputFile(matrix);

        } catch (IOException e) {
            System.out.println("Error reading file:");
            e.printStackTrace();
        }

        int[] shortestPath = Dijkstra(matrix, 12, 34);
        
        // print out the source and desitnation then the shortest path then the sum of
        // weights
        System.out.println("Source: " + 12);
        System.out.println("Destination: " + 34);
        System.out.print("Shortest path: ");
        for (int i = 0; i < shortestPath.length; i++) {
            System.out.print(shortestPath[i]);
            if (i < shortestPath.length - 1) {
                System.out.print(" -> ");
            }
        }
        System.out.println();
        int sum = 0;
        for (int i = 0; i < shortestPath.length - 1; i++) {
            sum += matrix[shortestPath[i]][shortestPath[i + 1]];
        }
        System.out.println("Sum of weights: " + sum);
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

    public static void writeToOutputFile(int[][] matrix) throws IOException {

        PrintWriter writer = new PrintWriter("output.txt");

        for (int i = 0; i < matrix.length; i++) {

            for (int j = 0; j < matrix[i].length; j++) {

                writer.print(matrix[i][j]);

                if (j < matrix[i].length - 1) {
                    writer.print("\t");
                }
            }

            writer.println();
        }

        writer.close();
    }
}
