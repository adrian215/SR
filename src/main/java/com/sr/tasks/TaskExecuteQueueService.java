package com.sr.tasks;

import com.sr.common.model.Task;
import com.sr.tasks.handler.InputTaskHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class TaskExecuteQueueService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private Queue<Task> tasks = new ConcurrentLinkedQueue<>();

    private InputTaskHandler inputTaskHandler;

    @Autowired
    public TaskExecuteQueueService(InputTaskHandler inputTaskHandler) {
        this.inputTaskHandler = inputTaskHandler;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    @Scheduled(fixedRate = 1000)
    private void runTaskFromQueue() {
        log.debug("Polling tasks queue");
        Task task;
        while ((task = tasks.poll()) != null) {
            inputTaskHandler.executeTask(task);
        }
    }
}
