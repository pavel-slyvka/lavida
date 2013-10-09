package com.lavida.swing.service;

import org.springframework.stereotype.Service;

import java.util.*;

/**
 * ConcurrentOperationsService
 * <p/>
 * Created: 15:32 20.09.13
 *
 * @author Pavel
 */
@Service
public class ConcurrentOperationsService {
    private Set<ConcurrentOperation> concurrentOperations = new HashSet<>();

    public void startOperation(String operationName, Runnable thread) {
        Thread newThread = new Thread(thread);
        concurrentOperations.add(new ConcurrentOperation(operationName, new Date(), newThread));
//        newThread.setDaemon(true);    // todo possible
        newThread.start();
    }

    public List<ConcurrentOperation> getActiveThreads() { // todo use for active threads table
        for (Iterator<ConcurrentOperation> it = concurrentOperations.iterator(); it.hasNext();) {
            ConcurrentOperation concurrentOperation = it.next();
            if (!concurrentOperation.getThread().isAlive()) {
                it.remove();
            }
        }
        return new ArrayList<>(concurrentOperations);
    }

    public boolean hasActiveThreads() { // todo use when application is about to close
        return !getActiveThreads().isEmpty();
    }
}