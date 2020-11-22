package spos.lab2.locks;

public class DekkerDemonstration {
    private static final int ITERATIONS = 1_000_00;

    private volatile long counter;

    private Thread firstThread;
    private Thread secondThread;
    private DekkerLock dekkerLock = new DekkerLock();


    private void increment() throws InterruptedException {
        counter++;
    }

    private void manipulate() {
        for (int i = 0; i < ITERATIONS; i++) {
            try {
                increment();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public  void run() throws InterruptedException {
        firstThread = new Thread(this::manipulate);
        secondThread = new Thread(this::manipulate);

        firstThread.start();
        secondThread.start();

        firstThread.join();
        secondThread.join();

        report();
    }

    private void report() {
        System.out.println("With lock: " + (ITERATIONS * 2)  + "\n" +
                "Without lock: " + counter);
    }



}
