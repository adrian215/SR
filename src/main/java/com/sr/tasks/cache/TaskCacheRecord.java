package com.sr.tasks.cache;

import com.sr.common.model.Task;
import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;

@Data
@Builder
public class TaskCacheRecord {

    private Task task;
    private LocalTime time;
    private TaskExecuteDestiny executeDestiny;
}
