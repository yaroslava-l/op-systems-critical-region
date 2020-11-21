package spos.lab2.locks;

import java.util.concurrent.locks.Lock;

public interface FixnumLock extends Lock {
    int getId();
    int register();
    int unregister();
    int reset();

    void lock();
    void unlock();
}
