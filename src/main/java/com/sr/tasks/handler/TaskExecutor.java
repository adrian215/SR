package com.sr.tasks.handler;

import com.sr.common.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class TaskExecutor {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @Qualifier("localTaskHandler")
    private TaskHandler localTaskHandler;

    @Async
    public void executeTask(Task task) {
        localTaskHandler.executeTask(task);
    }

}
