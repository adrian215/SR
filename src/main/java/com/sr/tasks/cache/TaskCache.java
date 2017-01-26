package com.sr.tasks.cache;

import com.sr.common.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.time.Instant.now;

@Component
public class TaskCache {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private Map<Integer, TaskCacheRecord> handledTasks = new ConcurrentHashMap<>();

    public void put(int taskId, Task task, TaskExecuteDestiny executeDestiny) {
        handledTasks.put(
                taskId,
                TaskCacheRecord.builder().
                        task(task)
                        .time(now())
                        .executeDestiny(executeDestiny)
                        .build()
        );
    }

    public Optional<Task> get(int id) {
        TaskCacheRecord record = handledTasks.get(id);
        handledTasks.remove(id);
        if (record != null)
            return Optional.of(record.getTask());
        else
            return Optional.empty();

    }

    public void disposeOldTasks() {
        handledTasks.forEach((i, t) -> {
            if (t.getTime().getEpochSecond() + t.getTask().getTimeout() < now().getEpochSecond()) {
                handledTasks.remove(i);
                log.info("Task {} removed beacuse of timetout", i);
            }
        });
    }

    private long inSeconds(long seconds) {
        return seconds * 1000;
    }

    public int getNumberOfProcessingTasks() {
        return handledTasks.size();
    }
}
