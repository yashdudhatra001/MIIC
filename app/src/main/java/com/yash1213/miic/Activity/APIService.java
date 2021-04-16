package com.yash1213.miic.Activity;

import com.yash1213.miic.Notification.MyResponse;
import com.yash1213.miic.Notification.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA-M4lnic:APA91bG3lwtwaVaxaGq2ttvs8nk9s7k4H-_Uc6NJFylVa5EeyXb6FhiJm-7iJ64hoV1HzOp7eBBx1J5LxHadzai0kRkMi5h5BelSbVIcZVj5q8DAVhQXZDAjx1TNoFFegR9usAOV-Ver"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);


}
