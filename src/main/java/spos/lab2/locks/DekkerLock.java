package spos.lab2.locks;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class DekkerLock extends AbstractFixnumLock{

    private volatile int turn;
    private ReentrantLock lock = new ReentrantLock();
    private AtomicBoolean flag[];


    public DekkerLock() {
        super(2);
        turn = 0;
        flag = new AtomicBoolean[numberOfThreads];
        for(int i = 0; i < numberOfThreads; ++i) {
            flag[i] = new AtomicBoolean(false);
        }
    }


    @Override
    public void lock() {

        try {
            lock.lock();
            register();
            int id = getId();
            int another = 1-id;
            flag[id].set(true);
            lock.unlock();
            while (flag[another].get()) {
                if(turn == another) {
                    flag[id].set(false);
                    while (turn == another) {}
                    flag[id].set(true);
                }
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void unlock() {
        turn = 1-getId();
        flag[getId()].set(false);
    }

    public boolean tryLock(long l, TimeUnit u) {
        return false;
    }
    public boolean tryLock() {
        int id = getId();
        int another = 1 - id;  //if curr ID is 1, then other will be 0, 0 - 1
        flag[getId()].set(true);
        turn = id;
        return !(turn == id && flag[another].get());
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }


    @Override
    public Condition newCondition() {
        return null;
    }

    @Override
    public void use() {

    }
}
