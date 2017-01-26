package com.sr.tasks.cache;

import com.sr.common.model.Task;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.time.LocalTime.now;

@Component
public class TaskCache {

    private Map<String, TaskCacheRecord> handledTasks = new ConcurrentHashMap<>();

    public void put(Task task, TaskExecuteDestiny executeDestiny) {
        handledTasks.put(
                task.getSource(),
                TaskCacheRecord.builder().
                        task(task)
                        .time(now())
                        .executeDestiny(executeDestiny)
                        .build()
                );
    }
}