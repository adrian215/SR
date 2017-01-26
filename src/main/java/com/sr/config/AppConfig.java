package com.sr.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConfig {

    @Value("${app.local.host}")
    public String localHost;
    @Value("${app.local.port}")
    public String localPort;
    @Value("${app.remote.host}")
    public String remoteHost;
    @Value("${app.remote.port}")
    public String remotePort;
    @Value("${app.settings.max-hops}")
    public int maxHops;
    @Value("${app.settings.queue-size}")
    public int queueSize;
}
