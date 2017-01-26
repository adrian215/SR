package com.sr.tasks.execute;

import com.sr.common.model.Task;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

import static java.nio.charset.Charset.defaultCharset;
import static org.apache.commons.io.FileUtils.write;

@Component
class LocalPythonTaskExecutor implements PythonTaskExecutor {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void executeTask(Task task, int localTaskId) {
        log.info("Execute task locally {}", task.getId());
        log.debug("Task info: {}", task);
        try {
            String scriptResult = executeScript(task.getScript());
            //todo obsluzyc odpowiedz
            System.out.println(scriptResult);
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
