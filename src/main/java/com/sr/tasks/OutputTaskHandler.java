package com.sr.tasks;

import com.sr.common.model.TaskResponse;
import com.sr.tasks.cache.TaskCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OutputTaskHandler {

    private TaskCache taskCache;

    @Autowired
    public OutputTaskHandler(TaskCache taskCache) {
        this.taskCache = taskCache;
    }

    public void processTaskResponse(TaskResponse response) {
        //todo implement task response
        System.out.println("processing response");
    }
}
