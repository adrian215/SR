package com.sr.endpoint;

import com.sr.common.model.Task;
import com.sr.tasks.TaskExecuteQueueService;
import com.sr.tasks.OutputTaskHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static com.sr.common.model.TaskResponse.Builder.taskResponse;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class RestEndpoint {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private TaskExecuteQueueService taskExecuteQueueService;
    private OutputTaskHandler outputTaskHandler;

    @Autowired
    public RestEndpoint(TaskExecuteQueueService taskExecuteQueueService, OutputTaskHandler outputTaskHandler) {
        this.taskExecuteQueueService = taskExecuteQueueService;
        this.outputTaskHandler = outputTaskHandler;
    }

    @RequestMapping(method = POST, value = "/task_input")
    public void processTask(@ModelAttribute Task task, @RequestBody Map<String, String> content) {
        task.setScript(content.get("content"));
        log.info("Task {} received by REST from {}", task.getId(), task.getSrc());
        log.debug("Task info: {}", task);
        taskExecuteQueueService.addTask(task);
    }

    @RequestMapping(method = POST, value = "/task_result")
    public void getResponse(@RequestParam("id") int id, @RequestBody Map<String, String> resultMap,
                            HttpServletRequest request) {

        String result = resultMap.get("result");
        String senderInfo = String.format("%s:%s", request.getRemoteAddr(), request.getRemotePort());
        log.info("Got task response with id: {} and result: {} from: {}", id, result, senderInfo);

        outputTaskHandler.processTaskResponse(
                taskResponse()
                        .withId(id)
                        .andResult(result));
    }

    public int getServerStatus() {
        //todo check how many tasks is executed locally and send this information back
        throw new NotImplementedException();
    }
}
