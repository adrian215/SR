package com.sr.tasks;

import com.sr.common.model.Task;
import com.sr.tasks.handler.TaskExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class TaskExecuteService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private Queue<Task> tasks = new ConcurrentLinkedQueue<>();

    private TaskExecutor taskExecutor;

    @Autowired
    public TaskExecuteService(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public void executeTask(Task task) {
        tasks.add(task);
    }

    @Scheduled(fixedRate = 1000)
    private void runTaskFromQueue() {
        Task task;
        while ((task = tasks.poll()) != null) {
            taskExecutor.executeTask(task);
        }
    }
}
