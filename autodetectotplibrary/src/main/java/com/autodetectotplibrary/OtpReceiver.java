package com.autodetectotplibrary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OtpReceiver extends BroadcastReceiver {

    private String receivedOTP = "";

    @Override
    public void onReceive(Context context, Intent intent) {

        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (Object o : pdusObj) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) o);

                    String senderNum = currentMessage.getDisplayOriginatingAddress();
                    /*String message = currentMessage.getDisplayMessageBody().split("OTP is")[1];
                    String message1 = message.split("for")[0];

                    message1 = message1.substring(0, message1.length() - 1);
                    Log.i("SmsReceiver", "senderNum: " + senderNum + "; message: " + message1);*/

                    String message = currentMessage.getDisplayMessageBody();
                    if (message.contains("OTP")
                            || message.contains("One Time Password")) {

                        if (message.contains("OTP ID is")) {
                            receivedOTP = getOTPFromString(message);
                        } else {
                            receivedOTP = getOTPFromString(message);
                        }
                    }

                    Intent myIntent = new Intent("otp");
                    myIntent.putExtra("message", receivedOTP);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(myIntent);
                }
            }
        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);
        }
    }

    private String getOTPFromString(String message) {
        String otp = "";

        Pattern digitPattern = Pattern.compile("(?<!\\d)\\d{6,8}(?!\\d)");
        Pattern alphaNumericPattern = Pattern.compile(
                "(?<!\\d)[a-zA-Z0-9\\d{1,}]{6}(?!\\d)");
//					"(\\s)^[a-zA-Z0-9]{6}$(\\s)");
//					"(?<!\\d)(\\d|\\D|\\S{6})(?!\\d)");
        Matcher matcher1 = digitPattern.matcher(message);
        Matcher matcher2 = alphaNumericPattern.matcher(message);

        if (matcher1.find()) {
            otp = matcher1.group();
        }
//			else if (matcher2.find()) {
//				canOnlySetOTP = true;
//				otp = matcher2.group();
//			}

        return otp;
    }

    private String getOTPFromOTPIDString(String message) {
        String otp = "";

        Pattern pattern = Pattern.compile("(?<!\\d)\\d{7}(?!\\d)");
        Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {
            otp = matcher.group();
        }

        return otp;
    }

}
