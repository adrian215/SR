package com.sr.tasks.handler;

import com.sr.common.model.Task;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

import static java.nio.charset.Charset.defaultCharset;
import static org.apache.commons.io.FileUtils.write;

@Component
class LocalTaskHandler implements TaskHandler {

    @Override
    public void executeTask(Task task) {
        try {
            String scriptResult = executeScript(task.getScript());
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

    private String executeFileScript(String fileName) throws IOException, InterruptedException {
        Runtime runtime = Runtime.getRuntime();
        Process exec = runtime.exec("./" + fileName);
        exec.waitFor();
        return IOUtils.toString(exec.getInputStream(), defaultCharset());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private File createFile(String fileName) throws IOException {
        File temp = new File(fileName);
        temp.createNewFile();
        temp.setExecutable(true);
        return temp;
    }
}
