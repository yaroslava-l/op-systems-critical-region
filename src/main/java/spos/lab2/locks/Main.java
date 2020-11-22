package spos.lab2.locks;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        AbstractFixnumLock bakeryLock = new BakeryLock(5);

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < 5; ++i){
            threads.add(new Thread(new BakeryLockRunnable(bakeryLock)));
        }

        for (Thread i : threads){
            i.start();
        }

        for (Thread i : threads){
            i.join();
        }
        System.out.println("DekkerDemonstration:");
        new DekkerDemonstration().run();
    }
}
