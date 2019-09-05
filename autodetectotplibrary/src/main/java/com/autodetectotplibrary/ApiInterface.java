package com.autodetectotplibrary;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("AutoOTP.json")
    Call<ArrayList<String>> getBankOtpList();

}
