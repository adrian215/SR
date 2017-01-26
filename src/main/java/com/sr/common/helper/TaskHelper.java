package com.sr.common.helper;

import com.sr.common.model.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskHelper {

    private static final String LOCAL_SOURCE = "CLI";

    public boolean isLocalTask(Task task) {
        return task.getSource().toUpperCase().startsWith(LOCAL_SOURCE);
    }

    public boolean isRemoteTask(Task task) {
        return !isLocalTask(task);
    }
}
