package spos.lab2.locks;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

public class BakeryLock extends AbstractFixnumLock{
    private  int[] tickets;
    private boolean[] choosing;
    private int maxThreads = 20;

    private int resource = -1;

    public BakeryLock(int numberOfThreads) {
        super(numberOfThreads);

        tickets = new int[maxThreads];
        choosing = new boolean[maxThreads];

        for (int i = 0; i < maxThreads; ++i){
            choosing[i] = false;
            tickets[i] = -1;
        }
    }

    @Override
    public void lock() {
        //get number
        choosing[getId()] = true;

        int maxTicket = Arrays.stream(tickets).max().getAsInt();

        tickets[getId()] = maxTicket + 1;

        choosing[getId()] = false;

        for (int th = 0; th < maxThreads; ++th){
            while (choosing[th]){
                Thread.yield();
            }

            while(tickets[th] != -1 && (tickets[th] < tickets[getId()] ||
                    (tickets[th] == tickets[getId()] && th < getId()))){
                Thread.yield();
            }
        }
    }

    public void use() {
        if (resource != -1){
            System.out.println("RESOURCE WAS BUSY WHILE ACCESSED BY CURRENT THREAD");
        }

        resource = getId();
        System.out.println(" Thread #" + getId() + " using resources with number " + tickets[getId()]);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        resource = -1;
    }


    @Override
    public void unlock() {
        tickets[getId()] = -1;
    }

    @Override
    public Condition newCondition() {
        return null;
    }
    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }
}
