package com.sr.tasks.handler;

import com.sr.common.helper.IdGenerator;
import com.sr.common.model.Task;
import com.sr.tasks.cache.TaskCache;
import com.sr.tasks.cache.TaskExecuteDestiny;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import static com.sr.tasks.cache.TaskExecuteDestiny.REMOTE;

@Component
public class InputTaskHandler {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private IdGenerator idGenerator;
    private TaskCache taskCache;

    @Autowired
    @Qualifier("localTaskExecutor")
    private TaskExecutor localTaskExecutor;

    @Autowired
    @Qualifier("remoteTaskExecutor")
    private TaskExecutor remoteTaskExecutor;

    @Autowired
    public InputTaskHandler(IdGenerator idGenerator, TaskCache taskCache) {
        this.idGenerator = idGenerator;
        this.taskCache = taskCache;
    }

    @Async
    public void executeTask(Task task) {
        int localId = idGenerator.getId();
        taskCache.put(localId, task, REMOTE);
        remoteTaskExecutor.executeTask(task, localId);

//        if (taskHelper.isRemoteTask(task) && possibleToSendToNextServer(task)) {
//            log.info("Executing task remotely {}", task);
//            taskCache.put(task, REMOTE);
//            remoteTaskExecutor.executeTask(task);
//        } else {
//            log.info("Executing task locally {}", task);
//            taskCache.put(task, LOCAL);
//            localTaskExecutor.executeTask(task);
//        }
    }

    private boolean possibleToSendToNextServer(Task task) {
        return task.getHopsLeft() > 0;
    }

}
