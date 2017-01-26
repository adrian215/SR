package com.sr.tasks;

import com.sr.common.helper.TaskHelper;
import com.sr.common.model.Task;
import com.sr.common.model.TaskResponse;
import com.sr.remote.client.RemoteServer;
import com.sr.tasks.cache.TaskCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static java.util.Collections.singletonMap;

@Component
public class OutputTaskHandler {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private TaskCache taskCache;
    private RemoteServer remoteServer;

    @Autowired
    public OutputTaskHandler(TaskCache taskCache, RemoteServer remoteServer) {
        this.taskCache = taskCache;
        this.remoteServer = remoteServer;
    }

    public void processTaskResponse(TaskResponse response) {
        Task task = taskCache.get(response.getId());

        if (task.getId() == TaskHelper.LOCALLY_STARTED_TASK_ID) {
            log.info("Script {} result: {}", response.getId(), response.getResult());
        } else {
            log.info("Sending task {} response to remote server with id {}", response.getId(), task.getId());
            try {
                remoteServer.sendResponseToServer(task.getId(), singletonMap("result", response.getResult())).execute();
            } catch (IOException e) {
                log.error("Cannot send task response to server", e);
            }
        }
    }
}
