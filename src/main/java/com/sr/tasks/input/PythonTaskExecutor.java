package com.sr.tasks.input;

import com.sr.common.model.Task;

public interface PythonTaskExecutor {
    void executeTask(Task task, int localTaskId);
}
