package com.sr.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaskResponse {
    private int id;
    private String result;

    public static class Builder {
        private int id;
        private String result;

        public static TaskResponse.Builder taskResponse() {
            return new Builder();
        }

        public Builder withId(int id) {
            this.id = id;
            return this;
        }

        public TaskResponse andResult(String result) {
            this.result = result;
            return get();
        }

        private TaskResponse get() {
            return new TaskResponse(id, result);
        }
    }
}
