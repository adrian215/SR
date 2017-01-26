package com.sr.endpoint.client;

import com.sr.config.AppConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;

@Configuration
public class RemoteServerProvider {

    @Bean
    public RemoteServer getRemoteServer(AppConfig appConfig) {
        String url = String.format("http://%s:%s", appConfig.localHost, appConfig.localPort);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .build();
        return retrofit.create(RemoteServer.class);
    }
}
