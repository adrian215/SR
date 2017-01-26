package com.sr.remote.ping;

import com.sr.remote.client.RemoteServer;
import com.sr.tasks.cache.TaskCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class CyclicPinger {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private TaskCache taskCache;
    private RemoteServer remoteServer;
    private AtomicInteger remoteServerQueueSize = new AtomicInteger(9999);

    @Autowired
    public CyclicPinger(TaskCache taskCache, RemoteServer remoteServer) {
        this.taskCache = taskCache;
        this.remoteServer = remoteServer;
    }

    @Scheduled(fixedRate = 1000)
    private void run() {
        taskCache.disposeOldTasks();
        try {
            Response<Integer> response = remoteServer.checkQueueStatus().execute();
            Integer queueSize = response.body();
            remoteServerQueueSize.set(queueSize);
        } catch (IOException e) {
            log.error("Cannot ping remote server", e);
            remoteServerQueueSize.set(9999);
        }
    }

    public int getRemoteServerQueue() {
        return remoteServerQueueSize.get();
    }
}
