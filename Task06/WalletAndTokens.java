class Wallet {
    private int balance;
    Wallet(int balance) { this.balance = balance; }
    int getBalance() { return balance; }
    void add(int amount) { balance += amount; }
    void subtract(int amount) { balance -= amount; }
}

class Transfers {
    static void transfer(Wallet from, Wallet to, int amount) {
        Wallet first = from;
        Wallet second = to;
        if (System.identityHashCode(first) > System.identityHashCode(second)) {
            first = to; second = from;
        }
        synchronized (first) {
            synchronized (second) {
                if (from.getBalance() >= amount) {
                    from.subtract(amount); to.add(amount);
                }
            }
        }
    }
}

class TokenCounter {
    private final int[] buffer = new int[5];
    private int count = 0, in = 0, out = 0;
    public synchronized void put(int token) throws InterruptedException {
        while (count == buffer.length) wait();
        buffer[in] = token;
        in = (in + 1) % buffer.length;
        count++;
        notifyAll();
    }
    public synchronized int take() throws InterruptedException {
        while (count == 0) wait();
        int token = buffer[out];
        out = (out + 1) % buffer.length;
        count--;
        notifyAll();
        return token;
    }
}

public class WalletAndTokens {
    public static void main(String[] args) throws InterruptedException {
        Wallet a = new Wallet(1000), b = new Wallet(1000);
        Thread t1 = new Thread(() -> { for (int i=0;i<100;i++) Transfers.transfer(a,b,1); });
        Thread t2 = new Thread(() -> { for (int i=0;i<100;i++) Transfers.transfer(b,a,1); });
        t1.start(); t2.start(); t1.join(); t2.join();
        System.out.println("Wallet total: " + (a.getBalance() + b.getBalance()));

        TokenCounter counter = new TokenCounter();
        int[] collected = new int[2];
        Thread cook = new Thread(() -> { try { for (int i=1;i<=20;i++) counter.put(i); }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); } });
        Thread[] students = new Thread[2];
        for (int s=0;s<2;s++) { final int id=s; students[s]=new Thread(() -> {
            try { for (int k=0;k<10;k++) collected[id]+=counter.take(); }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }); }
        cook.start(); for (Thread t:students) t.start();
        cook.join(); for (Thread t:students) t.join();
        System.out.println("Sum of tokens collected: " + (collected[0]+collected[1]) + " (expected 210)");
    }
}
