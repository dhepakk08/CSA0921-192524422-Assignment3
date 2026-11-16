import java.io.*;

public class SalesReport {
    static void manualVersion() {
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            reader = new BufferedReader(new FileReader("sales.txt"));
            writer = new BufferedWriter(new FileWriter("manual-report.txt"));
            String line;
            int total = 0;
            while ((line = reader.readLine()) != null) {
                int amount = Integer.parseInt(line.trim());
                total += amount;
            }
            writer.write("Total sales: " + total);
            writer.newLine();
            System.out.println("Manual report created");
        } catch (IOException | NumberFormatException e) {
            System.out.println("Manual version error: " + e.getMessage());
        } finally {
            try { if (reader != null) reader.close(); } catch (IOException e) { System.out.println("Reader close failed"); }
            try { if (writer != null) writer.close(); } catch (IOException e) { System.out.println("Writer close failed"); }
        }
    }

    static void safeVersion() {
        int total = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader("sales.txt"));
             BufferedWriter writer = new BufferedWriter(new FileWriter("safe-report.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) total += Integer.parseInt(line.trim());
            writer.write("Total sales: " + total);
            writer.newLine();
            System.out.println("Try-with-resources report created");
        } catch (IOException | NumberFormatException e) {
            System.out.println("Safe version error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        manualVersion();
        safeVersion();
    }
}
