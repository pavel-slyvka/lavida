package com.lavida.swing.service;

import com.lavida.service.ViewColumn;

import java.util.Date;

/**
 * ConcurrentOperation
 * <p/>
 * Created: 13:15 09.10.13
 *
 * @author Pavel
 */
public class ConcurrentOperation {

    @ViewColumn(titleKey = "dialog.concurrent.operations.name.titleKey", columnWidth = 150)
    private String name;

    @ViewColumn(titleKey = "dialog.concurrent.operations.startTime.titleKey", datePattern = "dd.MM.yyyy HH:mm:ss",
            columnWidth = 150)
    private Date startTime;

    private Thread thread;

    public ConcurrentOperation(String name, Date startTime, Thread thread) {
        this.name = name;
        this.startTime = startTime;
        this.thread = thread;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    @Override
    public String toString() {
        return "ConcurrentOperation{" +
                "name='" + name + '\'' +
                ", startTime=" + startTime +
                ", thread=" + thread +
                '}';
    }
}
