package com.sr.tasks.execute;

import com.sr.common.model.Task;
import com.sr.common.model.TaskResponse;
import com.sr.tasks.OutputTaskHandler;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

import static com.sr.common.model.TaskResponse.Builder.taskResponse;
import static java.nio.charset.Charset.defaultCharset;
import static org.apache.commons.io.FileUtils.write;

@Component
class LocalPythonTaskExecutor implements PythonTaskExecutor {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private OutputTaskHandler outputTaskHandler;

    @Autowired
    LocalPythonTaskExecutor(OutputTaskHandler outputTaskHandler) {
        this.outputTaskHandler = outputTaskHandler;
    }

    @Override
    public void executeTask(Task task, int localTaskId) {
        log.info("Execute task locally {}", localTaskId);
        log.debug("Task info: {}", task);
        try {
            String scriptResult = executeScript(task.getScript());

            outputTaskHandler.processTaskResponse(
                    taskResponse()
                            .withId(localTaskId)
                            .andResult(scriptResult));

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private String executeScript(String script) throws IOException, InterruptedException {
        String fileName = "temp";
        File temp = createFile(fileName);
        write(temp, script, defaultCharset());
        String scriptResult = executeFileScript(fileName);
        temp.delete();
        return scriptResult;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private File createFile(String fileName) throws IOException {
        File temp = new File(fileName);
        temp.createNewFile();
        temp.setExecutable(true);
        return temp;
    }

    private String executeFileScript(String fileName) throws IOException, InterruptedException {
        Runtime runtime = Runtime.getRuntime();
        Process exec = runtime.exec("./" + fileName);
        exec.waitFor();
        return IOUtils.toString(exec.getInputStream(), defaultCharset());
    }
}
