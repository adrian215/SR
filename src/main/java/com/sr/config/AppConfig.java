package com.sr.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConfig {

    @Value("${app.remote.host}")
    public String remoteHost;
    @Value("${app.remote.port}")
    public String remotePost;
    @Value("${app.settings.max-hops}")
    public int maxHops;
    @Value("${app.settings.queue-size}")
    public int queueSize;
}
