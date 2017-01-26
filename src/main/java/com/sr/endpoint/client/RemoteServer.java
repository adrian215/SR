package com.sr.endpoint.client;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.Map;

public interface RemoteServer {

    @POST("task_input")
    Call<Void> runScriptOnServer(
            @Query("src") String src,
            @Query("id") int id,
            @Query("hops_left") int hopsLeft,
            @Query("timeout") int timeout,
            @Body Map<String, String> script);

}
