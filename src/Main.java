import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        String filename = "Data.txt";
        try {
            int[][] matrix = readInputFileIntoMatrix(filename);
            writeToOutputFile(matrix);

        } catch (IOException e) {
            System.out.println("Error reading file:");
            e.printStackTrace();
        }

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
