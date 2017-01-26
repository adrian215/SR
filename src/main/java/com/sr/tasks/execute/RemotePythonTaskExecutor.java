package com.sr.tasks.execute;

import com.sr.common.model.Task;
import com.sr.config.AppConfig;
import com.sr.remote.client.RemoteServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static java.util.Collections.singletonMap;

@Component
class RemotePythonTaskExecutor implements PythonTaskExecutor {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private AppConfig appConfig;
    private RemoteServer remoteServer;

    @Autowired
    RemotePythonTaskExecutor(AppConfig appConfig, RemoteServer remoteServer) {
        this.appConfig = appConfig;
        this.remoteServer = remoteServer;
    }

    @Override
    public void executeTask(Task task, int localTaskId) {
        log.info("Remote execution task {}", task.getId());
        log.debug("Task info: {}", task);

        task.setHops_left(task.getHops_left() - 1);
        String currentServerSrc = String.format("%s:%s", appConfig.localHost, appConfig.localPort);
        try {
            remoteServer.runScriptOnServer(currentServerSrc, localTaskId,
                    task.getHops_left(), task.getTimeout(),
                    singletonMap("content", task.getScript()))
                    .execute();
            log.info("Task {} sent successfully to remote server with local id {}", task.getId(), localTaskId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
