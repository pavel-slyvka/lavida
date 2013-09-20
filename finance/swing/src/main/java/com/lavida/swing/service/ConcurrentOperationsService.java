package com.lavida.swing.service;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * ConcurrentOperationsService
 * <p/>
 * Created: 15:32 20.09.13
 *
 * @author Pavel
 */
@Service
public class ConcurrentOperationsService {
    private Set<Thread> threads = new HashSet<>();

    public void startOperation(Runnable runnable) {
        Thread newThread = new Thread(runnable);
        threads.add(newThread);
//        newThread.setDaemon(true);    // todo possible
        newThread.start();
    }

    public boolean hasActiveThreads() { // todo use
        for (Iterator<Thread> it = threads.iterator(); it.hasNext();) {
            Thread thread = it.next();
            if (!thread.isAlive()) {
                it.remove();
            }
        }
        return !threads.isEmpty();
    }
}