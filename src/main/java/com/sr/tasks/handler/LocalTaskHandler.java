package com.sr.tasks.handler;

import com.sr.common.model.Task;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static java.nio.charset.Charset.defaultCharset;

@Component
class LocalTaskHandler implements TaskHandler {

    @Override
    public void executeTask(Task task) {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ls = runtime.exec("ls");
            String s = IOUtils.toString(ls.getInputStream(), defaultCharset());
            System.out.println(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
