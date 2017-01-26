package com.sr.endpoint;

import com.sr.common.helper.IdGenerator;
import com.sr.common.helper.TaskHelper;
import com.sr.common.model.Task;
import com.sr.config.AppConfig;
import com.sr.tasks.TaskExecuteQueueService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.CharSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;
import static java.util.Optional.empty;

@Component
public class CLIEndpoint implements CommandLineRunner {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final Pattern pattern = Pattern.compile("([0-9]*);(.*)");
    private final int taskIdAtSource = -1;

    private AppConfig appConfig;
    private TaskExecuteQueueService taskExecuteQueueService;

    @Autowired
    public CLIEndpoint(AppConfig appConfig, TaskExecuteQueueService taskExecuteQueueService) {
        this.appConfig = appConfig;
        this.taskExecuteQueueService = taskExecuteQueueService;
    }

    @Async
    @Override
    public void run(String... args) throws Exception {
        String input;
        log.info("\n CLI started\n Enter command: <timeout>;<path to script file>");
        Scanner scanner = new Scanner(System.in);

        while (!"exit".equals(input = scanner.next())) {
            Matcher matcher = pattern.matcher(input);

            if (matcher.find()) {
                int timeout = parseInt(matcher.group(1));
                String path = matcher.group(2);

                readTaskFromFile(path, timeout)
                        .ifPresent(taskExecuteQueueService::addTask);
            }
        }

        log.info("CLI closed");
    }

    private Optional<Task> readTaskFromFile(String filePath, int timeout) {
        try {
            String script = FileUtils.readFileToString(new File(filePath), Charset.defaultCharset());

            Task task = Task.builder()
                    .script(script)
                    .src(TaskHelper.LOCAL_SOURCE)
                    .hops_left(appConfig.maxHops)
                    .id(taskIdAtSource)
                    .build();
            log.info("Task {} received by CLI", task);

            return Optional.of(task);

        } catch (IOException e) {
            log.error("Cannot read script from file {}", filePath);
            return empty();
        }
    }
}
