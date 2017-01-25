package com.sr.rest;

import com.sr.common.model.Task;
import com.sr.tasks.TaskExecuteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainEndpoint {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private TaskExecuteService taskExecuteService;

    @Autowired
    public MainEndpoint(TaskExecuteService taskExecuteService) {
        this.taskExecuteService = taskExecuteService;
    }

    @RequestMapping
    public void processTask(@ModelAttribute Task task) {
        log.debug("Rest received task: {}", task);
        taskExecuteService.executeTask(task);
    }
}
