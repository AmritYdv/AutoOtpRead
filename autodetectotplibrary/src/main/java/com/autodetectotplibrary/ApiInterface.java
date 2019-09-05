package com.autodetectotplibrary;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("getElements.txt")
    Call<ArrayList<String>> getBankOtpList();

}
