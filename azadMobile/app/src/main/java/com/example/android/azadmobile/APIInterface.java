package com.example.android.azadmobile;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by parsh on 14.05.2018.
 */

public interface APIInterface {

    @POST("/api/auth")
    Call<Integer> getUserBalance(@Body HashMap loginPasswordMap);

    @GET("/api/challenges")
    Call<List<String>> getChallenges();

    @POST("/api/teams")
    Call<List<String>> getTeams(@Body String challenge);

    @POST("/api/unknown")
    Call<Void> placeBet(@Body String bet);
}
