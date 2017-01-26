package com.sr.common.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Task {
    private String source;
    private int taskIdAtSource;
    private int hopsLeft;
    private int timeout;
    private String script;
}
