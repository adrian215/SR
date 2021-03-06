package com.sr.tasks;

import com.sr.common.helper.IdGenerator;
import com.sr.common.helper.TaskHelper;
import com.sr.common.model.Task;
import com.sr.remote.ping.CyclicPinger;
import com.sr.tasks.cache.TaskCache;
import com.sr.tasks.execute.PythonTaskExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import static com.sr.tasks.cache.TaskExecuteDestiny.LOCAL;
import static com.sr.tasks.cache.TaskExecuteDestiny.REMOTE;

@Component
public class InputTaskHandler {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private IdGenerator idGenerator;
    private TaskCache taskCache;
    private TaskHelper taskHelper;
    private CyclicPinger cyclicPinger;

    @Autowired
    @Qualifier("localPythonTaskExecutor")
    private PythonTaskExecutor localPythonTaskExecutor;

    @Autowired
    @Qualifier("remotePythonTaskExecutor")
    private PythonTaskExecutor remotePythonTaskExecutor;

    @Autowired
    public InputTaskHandler(IdGenerator idGenerator, TaskCache taskCache, TaskHelper taskHelper, CyclicPinger cyclicPinger) {
        this.idGenerator = idGenerator;
        this.taskCache = taskCache;
        this.taskHelper = taskHelper;
        this.cyclicPinger = cyclicPinger;
    }

    @Async
    public void executeTask(Task task) {
        log.info("Starting execute task with id {}", task.getId());

        int localId = idGenerator.getId();
        log.info("Task {} has local id {}", task.getId(), localId);
        taskCache.put(localId, task, REMOTE);

        if (possibleToSendToNextServer(task) && cyclicPinger.getRemoteServerQueue() < taskCache.getNumberOfProcessingTasks()) {
            taskCache.put(localId, task, REMOTE);
            remotePythonTaskExecutor.executeTask(task, localId);
        } else {
            taskCache.put(localId, task, LOCAL);
            localPythonTaskExecutor.executeTask(task, localId);
        }
    }

    private boolean possibleToSendToNextServer(Task task) {
        return task.getHops_left() > 0;
    }

}
