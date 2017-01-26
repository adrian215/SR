package com.sr.endpoint;

import com.sr.common.model.Task;
import com.sr.tasks.TaskExecuteQueueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainEndpoint {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private TaskExecuteQueueService taskExecuteQueueService;

    @Autowired
    public MainEndpoint(TaskExecuteQueueService taskExecuteQueueService) {
        this.taskExecuteQueueService = taskExecuteQueueService;
    }

    @RequestMapping
    public void processTask(@ModelAttribute Task task) {
        log.debug("Task {} received", task);
        taskExecuteQueueService.addTask(task);
    }
}
