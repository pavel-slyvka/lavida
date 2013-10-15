package com.lavida.swing.service;

import com.lavida.swing.event.ConcurrentOperationCompleteEvent;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * ConcurrentOperationsService
 * <p/>
 * Created: 15:32 20.09.13
 *
 * @author Pavel
 */
@Service
public class ConcurrentOperationsService implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    private Set<ConcurrentOperation> concurrentOperations = new HashSet<>();

    public void startOperation(String operationName, Runnable thread) {
        Thread newThread = new Thread(thread);
        concurrentOperations.add(new ConcurrentOperation(operationName, new Date(), newThread));
//        newThread.setDaemon(true);    // todo possible
        newThread.start();
        publishEvent();
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

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void publishEvent(){
        applicationContext.publishEvent(new ConcurrentOperationCompleteEvent(this));
    }

//    @PostConstruct
//    protected void initTimer(){
//        Timer timer = new Timer(true);
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                publishEvent();
//            }
//        }, 10000, 5000);
//    }


}