package com.sr.tasks.handler;

import com.google.common.collect.ImmutableMap;
import com.sr.common.model.Task;
import com.sr.config.AppConfig;
import com.sr.endpoint.client.RemoteServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;

import static com.google.common.collect.ImmutableMap.of;

@Component
class RemoteTaskExecutor implements TaskExecutor {

    private AppConfig appConfig;
    private RemoteServer remoteServer;

    @Autowired
    RemoteTaskExecutor(AppConfig appConfig, RemoteServer remoteServer) {
        this.appConfig = appConfig;
        this.remoteServer = remoteServer;
    }

    @Override
    public void executeTask(Task task, int localTaskId) {
        task.setHopsLeft(task.getHopsLeft() - 1);
        String currentServerSrc = String.format("%s:%s", appConfig.localHost, appConfig.localPort);
        remoteServer.runScriptOnServer(currentServerSrc, localTaskId,
                task.getHopsLeft(), task.getTimeout(),
                of("content", task.getScript()));
    }
}
