import java.io.*;
import java.util.*;
import java.util.concurrent.*;

class ResultTask implements Callable<String> {
    private final File file;
    ResultTask(File file) { this.file = file; }
    public String call() throws Exception {
        if (!file.exists()) throw new FileNotFoundException(file.getName() + " is missing");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            int total = 0, count = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                int mark = Integer.parseInt(line.trim());
                if (mark < 0 || mark > 100) throw new IllegalArgumentException("Invalid mark in " + file.getName());
                total += mark; count++;
            }
            if (count == 0) throw new IllegalArgumentException("No marks in " + file.getName());
            return file.getName() + ": average=" + (total / (double) count);
        }
    }
}

public class ResultProcessor {
    public static void main(String[] args) throws InterruptedException {
        File folder = new File("input");
        if (!folder.exists()) { System.out.println("Input folder not found"); return; }
        String[] names = {"student01.txt", "student02.txt", "student03.txt", "student04.txt", "student05.txt"};
        ExecutorService pool = Executors.newFixedThreadPool(4);
        List<Future<String>> futures = new ArrayList<>();
        for (String name : names) futures.add(pool.submit(new ResultTask(new File(folder, name))));
        int success = 0, failure = 0;
        for (Future<String> future : futures) {
            try { System.out.println("SUCCESS " + future.get()); success++; }
            catch (ExecutionException e) { System.out.println("FAILED " + e.getCause().getMessage()); failure++; }
        }
        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println("Summary: success=" + success + ", failure=" + failure);
    }
}
