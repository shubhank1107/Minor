package com.bloodlord.shubhank.mdemo;

import com.bloodlord.shubhank.mdemo.models.Server_Request_W;
import com.bloodlord.shubhank.mdemo.models.Server_Respond;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Shubhank on 11-10-2016.
 */

public interface RequestInterface {
    @POST("database-login/")
    Call<Server_Respond> operation(@Body Server_Request_W request);
}
