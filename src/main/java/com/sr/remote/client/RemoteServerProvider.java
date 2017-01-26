package com.sr.remote.client;

import com.sr.config.AppConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
public class RemoteServerProvider {

    @Bean
    public RemoteServer getRemoteServer(AppConfig appConfig) {
        String url = String.format("http://%s:%s", appConfig.remoteHost, appConfig.remotePort);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        return retrofit.create(RemoteServer.class);
    }
}
