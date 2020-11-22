package spos.lab2.locks;
public class BakeryLockRunnable implements Runnable {
    private AbstractFixnumLock bakeryLock;

    BakeryLockRunnable(AbstractFixnumLock bakeryLock){
        this.bakeryLock = bakeryLock;
    }

    @Override
    public void run() {
        bakeryLock.register();
        bakeryLock.lock();

        //critical section
        bakeryLock.use();

        bakeryLock.unlock();
        bakeryLock.unregister();
    }
}
