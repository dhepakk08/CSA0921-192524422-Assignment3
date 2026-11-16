class ScannerStation extends Thread {
    private int scripts;
    ScannerStation(String name, int scripts) { super(name); this.scripts = scripts; }
    public void run() {
        try {
            for (int i = 1; i <= scripts; i++) Thread.sleep(40);
            System.out.println(getName() + " scanned " + scripts + " scripts");
        } catch (InterruptedException e) {
            System.out.println(getName() + " was interrupted");
            Thread.currentThread().interrupt();
        }
    }
}

class RunnableStation implements Runnable {
    private String name;
    RunnableStation(String name) { this.name = name; }
    public void run() { System.out.println(name + " completed using Runnable"); }
}

public class ThreadStations {
    public static void main(String[] args) throws InterruptedException {
        ScannerStation[] stations = new ScannerStation[4];
        for (int i = 0; i < 4; i++) stations[i] = new ScannerStation("Station-" + (i + 1), (i + 1) * 2);
        for (ScannerStation station : stations) station.start();
        for (ScannerStation station : stations) station.join();
        System.out.println("All stations finished");
        Thread runnableThread = new Thread(new RunnableStation("Station-R"));
        runnableThread.start();
        runnableThread.join();
        System.out.println("Runnable station finished");
    }
}
