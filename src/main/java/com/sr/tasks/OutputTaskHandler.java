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
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;

import static java.util.Collections.singletonMap;

@Component
public class OutputTaskHandler {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private TaskCache taskCache;

    @Autowired
    public OutputTaskHandler(TaskCache taskCache) {
        this.taskCache = taskCache;
    }

    public void processTaskResponse(TaskResponse response) {
        taskCache.get(response.getId())
                .ifPresent(task -> {
                    if (task.getId() == TaskHelper.LOCALLY_STARTED_TASK_ID) {
                        log.info("Script {} result: {}", response.getId(), response.getResult());
                    } else {
                        log.info("Sending task {} response to remote server with id {}", response.getId(), task.getId());
                        sendResponseToServer(response, task);
                    }
                });
    }

    private void sendResponseToServer(TaskResponse response, Task task) {
        try {
            RemoteServer senderInterface = new Retrofit.Builder()
                    .addConverterFactory(JacksonConverterFactory.create())
                    .baseUrl("http://" + task.getSrc())
                    .build().create(RemoteServer.class);
            senderInterface.sendResponseToServer(task.getId(), singletonMap("result", response.getResult())).execute();
        } catch (IOException e) {
            log.error("Cannot send task response to server", e);
        }
    }
}
