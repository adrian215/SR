package com.sr.endpoint.client;

import com.sr.common.model.Task;

public interface RemoteServer {

    void runTaskOnServer(Task task);
}
