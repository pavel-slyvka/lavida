package com.lavida.swing.form.component;

import com.lavida.TaskProgressEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * ProgressComponent
 * Created: 14:51 19.09.13
 *
 * @author Pavel
 */
@Component
public class ProgressComponent implements ApplicationListener<TaskProgressEvent> {
    private JLabel label;
    private volatile JProgressBar progressBar;

    private boolean initialized = false;
    private boolean finished = false;
    private volatile Timer timer = new Timer(0, null);
    private Queue<ProgressWorkHolder> progressWorkQueue = new ConcurrentLinkedQueue<>();
    private Set<Integer> gotEventHashes = new HashSet<>();
    private volatile long startTime;
    private List<Long> correctedTaskTimes = Collections.emptyList();

    @PostConstruct
    private void init() {
        if (this.label == null) {
            this.label = new JLabel("");
            this.label.setVisible(false);
        }
        if (this.progressBar == null) {
            this.progressBar = new JProgressBar();
            this.progressBar.setVisible(false);
        }
    }

    public ProgressComponent reinitialize(String label) {
        timer.stop();
        finished = true;
        this.label.setText(label);
        progressWorkQueue.clear();
        initialized = true;
        return this;
    }

    public ProgressComponent addWork(long waitingTimeInMilis, boolean needTimer) {
        progressWorkQueue.add(new ProgressWorkHolder(waitingTimeInMilis, needTimer));
        return this;
    }

    public void start() {
        if (!finished) {
            throw new RuntimeException("Can't start ProgressComponent, already started!");
        }
        if (!initialized) {
            throw new RuntimeException("Can't start ProgressComponent, not initialized!");
        }
        if (progressWorkQueue.isEmpty()) {
            throw new RuntimeException("Can't start ProgressComponent, neither work added!");
        }
        progressBar.setIndeterminate(false);
        progressBar.setMinimum(0);
        int max = 0;
        int lastThreshold = 0;
        for (ProgressWorkHolder progressWorkHolder : progressWorkQueue) {
            progressWorkHolder.progressThreshold = (int) (lastThreshold + progressWorkHolder.waitingTimeInMillis / 100);
            lastThreshold = progressWorkHolder.progressThreshold;
            max += progressWorkHolder.waitingTimeInMillis / 100;
        }
        progressBar.setMaximum(max);
        progressBar.setValue(0);
        label.setVisible(true);
        progressBar.setVisible(true);
        initialized = false;
        finished = false;
        correctedTaskTimes = new ArrayList<>(progressWorkQueue.size());
        startNewTask();
    }

    private synchronized void startNewTask() {
        startTime = System.currentTimeMillis();
        if (progressWorkQueue.peek().needTimer) {
            timer = new Timer(200, new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    if (progressBar.getValue() < progressWorkQueue.peek().progressThreshold) {
                        progressBar.setValue(progressBar.getValue() + 2);
                    } else {
                        timer.stop();
                        progressBar.setIndeterminate(true);
                    }
                }
            });
            timer.start();
        }
    }

    @Override
    public void onApplicationEvent(TaskProgressEvent event) {
        if (!gotEventHashes.contains(event.getEventHash()) && !finished && !progressWorkQueue.isEmpty()) {
            gotEventHashes.add(event.getEventHash());
            if (TaskProgressEvent.TaskProgressType.COMPLETE == event.getType()) {
                timer.stop();
                progressBar.setIndeterminate(false);
                progressBar.setValue(progressWorkQueue.peek().progressThreshold);
                progressWorkQueue.remove();
                correctedTaskTimes.add(System.currentTimeMillis() - startTime);
                if (!progressWorkQueue.isEmpty()) {
                    startNewTask();

                } else {    // last task finished
                    finished = true;
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    label.setVisible(false);
                    progressBar.setVisible(false);
                }
            }
        }
    }

    private class ProgressWorkHolder {
        long waitingTimeInMillis;
        boolean needTimer;
        int progressThreshold;

        private ProgressWorkHolder(long waitingTimeInMilis, boolean needTimer) {
            this.waitingTimeInMillis = waitingTimeInMilis;
            this.needTimer = needTimer;
        }
    }

    public JLabel getLabel() {
        return label;
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public Long[] getCorrectedTaskTimes() {
        return correctedTaskTimes.toArray(new Long[correctedTaskTimes.size()]);
    }
}
