package com.sr.tasks.handler;

import com.sr.common.model.Task;
import org.springframework.stereotype.Component;

@Component
class LocalTaskHandler implements TaskHandler {

    @Override
    public void executeTask(Task task) {
        System.out.println("wykonanie taska");
    }
}
