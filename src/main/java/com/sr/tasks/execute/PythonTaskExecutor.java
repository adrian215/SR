package com.sr.tasks.execute;

import com.sr.common.model.Task;

public interface PythonTaskExecutor {
    void executeTask(Task task, int localTaskId);
}
