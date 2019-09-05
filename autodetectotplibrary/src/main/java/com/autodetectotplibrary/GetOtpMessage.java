package com.autodetectotplibrary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetOtpMessage {
    private Context context;
    private WebView webView;
    private ArrayList<String> bankOtpList = new ArrayList<>();
    private boolean flag;
    private Handler handler = new Handler();

    public GetOtpMessage(Context context, WebView webView) {
        this.context = context;
        this.webView = webView;
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction().equalsIgnoreCase("otp")) {
                if (Utility.isDeviceOnline(context))
                    getBankOtpList(intent.getStringExtra("message"));
                else
                    Toast.makeText(context, "Please check internet connection", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public void start() {
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, new IntentFilter("otp"));
    }

    public void stop() {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);
    }

    private void getBankOtpList(final String otp) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ArrayList<String>> call = apiService.getBankOtpList();
        call.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<String>> call, @NonNull Response<ArrayList<String>> response) {
                if (response.body() != null && !response.body().isEmpty()) {
                    bankOtpList.addAll(response.body());
                    if (!bankOtpList.isEmpty()) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                autoFillOtp(webView, otp);
                            }
                        }, 2000);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<String>> call, @NonNull Throwable t) {
                Log.e("API Error", t.getMessage());
            }
        });
    }

    private void autoFillOtp(final WebView webView, String otp) {
        for (String otpCode : bankOtpList) {
            String splitedValue = otpCode.split("~")[1];
            if (!flag) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    webView.evaluateJavascript("javascript:(function otp() {\n" +
                                    "var flag = false;\n" +
                                    "if(document.querySelector('" + splitedValue + "')){\n" +
                                    "document.querySelector('" + splitedValue + "').value = '" + otp + "';\n" +
                                    "flag = true;\n" +
                                    "}\n" +
                                    "return flag;\n" +
                                    "})()",
                            new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(String s) {
                                    flag = Boolean.parseBoolean(s);
                                }
                            });
                }
            } else {
                break;
            }
        }
    }
}
