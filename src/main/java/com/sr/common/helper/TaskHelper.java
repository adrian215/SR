package com.sr.common.helper;

import com.sr.common.model.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskHelper {

    public static final String LOCAL_SOURCE = "CLI";
    public final static int LOCALLY_STARTED_TASK_ID = -1;

    public boolean isLocalTask(Task task) {
        return task.getSrc().toUpperCase().startsWith(LOCAL_SOURCE);
    }

    public boolean isRemoteTask(Task task) {
        return !isLocalTask(task);
    }
}
