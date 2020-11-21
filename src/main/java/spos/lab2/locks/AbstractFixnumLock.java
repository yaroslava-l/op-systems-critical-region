package spos.lab2.locks;
import java.util.BitSet;
import java.util.logging.Logger;

public abstract class AbstractFixnumLock implements FixnumLock{
    private static final Logger log = Logger.getLogger(AbstractFixnumLock.class.getName());
    public int numberOfThreads = 10;
    public int numberOfRegistered = 0;
    private ThreadLocal<Integer> ID = new ThreadLocal<>();
    private BitSet registeredThreads = new BitSet(numberOfThreads);
    private static final Object monitor = new Object();

    public AbstractFixnumLock(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
    }

    /**
     * Returns the id of the thread that is locking lock at the moment.
     * @return      the id of the thread
     */

    @Override
    public int getId() {
        if(ID.get() != null && ID.get() != -1)
            return ID.get();
        return -1;
    }

    /**
     * Registers a thread that can lock this lock.
     * @return     -1 if thread can't be registered or the id of registered thread.
     */

    @Override
    public int register() {
        synchronized (monitor) {
            if (numberOfRegistered < numberOfThreads) {
                if (ID.get() != null && ID.get() != -1) {
                    log.info("Thread with ID - " + ID.get() + " - already registered");
                    return getId();
                }

                Thread thread = Thread.currentThread();
                int freeId = registeredThreads.nextClearBit(0);
                registeredThreads.set(freeId);
                ID.set(freeId);
                numberOfRegistered += 1;
                log.info(String.format("Thread %d has been registered as %d", thread.getId(), freeId));
                return freeId;
            }
            log.info("Can't register the thread because of the thread limit.");
            return -1;
        }
    }

    /**
     * Opposite function to register. Unregister a thread if it is possible.
     * @return      the index of unregistered thread
     */

    @Override
    public int unregister() {
        synchronized (monitor){
            int threadID = getId();
            if(registeredThreads.get(threadID)){
                registeredThreads.clear(threadID);
                numberOfRegistered -= 1;
                ID.set(-1);
                log.info(String.format("ID %d is free now.", threadID));
                return threadID;
            }
            return -1;
        }
    }

    /**
     * Resets lock's state to initial.
     */

    private void reset() {
        synchronized (monitor){
            ID = new ThreadLocal<>();
            numberOfRegistered = 0;
            registeredThreads.clear();
        }
    }
}
