package com.autodetectotp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.autodetectotplibrary.GetOtpMessage;

public class MainActivity extends AppCompatActivity {

    private GetOtpMessage getOtpMessage;

    @SuppressLint({"ClickableViewAccessibility", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView webView = findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
        });
        String yourData = "<!doctype html>\n" +
                "<html>\n" +
                "\n" +
                "<head>\n" +
                "    <title>Checkout Demo</title>\n" +
                "    <meta name=\"viewport\" content=\"width=device-width\" />\n" +
                "\t\t<script src=\"https://www.paynimo.com/paynimocheckout/client/lib/jquery.min.js\" type=\"text/javascript\"></script>\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "\n" +
                "    <!-- <button id=\"btnSubmit\">Make a Payment</button> -->\n" +
                "\n" +
                "    <script type=\"text/javascript\" src=\"https://www.tecprocesssolution.com/proto/test/jscheckout/server/lib/checkout.js\"></script>\n" +
                "\n" +
                "    <script type=\"text/javascript\">\n" +
                "        $(document).ready(function() {\n" +
                "            function handleResponse(res) {\n" +
                "                if (typeof res != 'undefined' && typeof res.paymentMethod != 'undefined' && typeof res.paymentMethod.paymentTransaction != 'undefined' && typeof res.paymentMethod.paymentTransaction.statusCode != 'undefined' && res.paymentMethod.paymentTransaction.statusCode == '0300') {\n" +
                "                    // success block\n" +
                "                } else if (typeof res != 'undefined' && typeof res.paymentMethod != 'undefined' && typeof res.paymentMethod.paymentTransaction != 'undefined' && typeof res.paymentMethod.paymentTransaction.statusCode != 'undefined' && res.paymentMethod.paymentTransaction.statusCode == '0398') {\n" +
                "                    // initiated block\n" +
                "                } else {\n" +
                "                    // error block\n" +
                "                }\n" +
                "            };\n" +
                "\n" +
                "            //$(document).off('click', '#btnSubmit').on('click', '#btnSubmit', function(e) {\n" +
                "                //e.preventDefault();\n" +
                "\n" +
                "                var options = {\n" +
                "                    'tarCall': false,\n" +
                "                    'features': {\n" +
                "                        'showPGResponseMsg': true,\n" +
                "                        'enableExpressPay': true\n" +
                "                    },\n" +
                "                    'consumerData': {\n" +
                "                        'deviceId': 'web',\t//possible values 'WEBSH1', 'WEBSH2' and 'WEBMD5'\n" +
                "                        'token': '6ee363fc850b38e5c885d2953a1cbddc99982b2a03c8db04c528cab6bf8e999551a7f676e146cdf717b47866e4530dc48995b0861a0c7206c8e2404024ed05e2',\n" +
                "                        'returnUrl': 'https://www.tekprocess.co.in/MerchantIntegrationClient/MerchantResponsePage.jsp',    //merchant response page URL\n" +
                "                        'responseHandler': handleResponse,\n" +
                "                        'paymentMode': 'all',\n" +
                "                        'merchantLogoUrl': 'https://www.paynimo.com/CompanyDocs/company-logo-md.png',  //provided merchant logo will be displayed\n" +
                "                        'merchantId': 'L3348',\n" +
                "                        'currency': 'INR',\n" +
                "                        'consumerId': 'c964634',\n" +
                "                        'consumerMobileNo': '9876543210',\n" +
                "                        'consumerEmailId': 'test@test.com',\n" +
                "                        'txnId': '1481197581115',   //Unique merchant transaction ID\n" +
                "                        'items': [{\n" +
                "                            'itemId': 'test',\n" +
                "                            'amount': '2',\n" +
                "                            'comAmt': '0'\n" +
                "                        }],\n" +
                "                        'customStyle': {\n" +
                "                            'PRIMARY_COLOR_CODE': '#3977b7',   //merchant primary color code\n" +
                "                            'SECONDARY_COLOR_CODE': '#FFFFFF',   //provide merchant's suitable color code\n" +
                "                            'BUTTON_COLOR_CODE_1': '#1969bb',   //merchant's button background color code\n" +
                "                            'BUTTON_COLOR_CODE_2': '#FFFFFF'   //provide merchant's suitable color code for button text\n" +
                "                        }\n" +
                "                    }\n" +
                "                };\n" +
                "\t\t\t\tvar loop = setInterval(function(){\n" +
                "\t\t\t\t\tif(typeof $.pnCheckout != 'undefined'){\n" +
                "\t\t\t\t\t\t$.pnCheckout(options);\n" +
                "\t\t\t\t\t\tif(options.features.enableNewWindowFlow){\n" +
                "\t\t\t\t\t\t\t\tpnCheckoutShared.openNewWindow();\n" +
                "\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\tclearInterval(loop);\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t},500)\n" +
                "\n" +
                "            //});\n" +
                "        });\n" +
                "    </script>\n" +
                "</body>\n" +
                "\n" +
                "</html>";
        webView.loadDataWithBaseURL("https://www.tecprocesssolution.com",yourData
                , "text/html", "UTF-8", null);
        getOtpMessage = new GetOtpMessage(this, webView);
    }

    @Override
    public void onResume() {
        getOtpMessage.start();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        getOtpMessage.stop();
    }
}
