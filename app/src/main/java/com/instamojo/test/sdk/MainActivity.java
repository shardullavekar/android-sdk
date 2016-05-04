package com.instamojo.test.sdk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.instamojo.android.activities.PaymentActivity;
import com.instamojo.android.activities.PaymentDetailsActivity;
import com.instamojo.android.callbacks.OrderRequestCallBack;
import com.instamojo.android.models.Errors;
import com.instamojo.android.models.Transaction;
import com.instamojo.android.network.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private String accessToken = "wLd5A00ZwskdBhIlGNuFSx5LyhrjpC";

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.pay);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ((EditText) findViewById(R.id.name)).getText().toString();
                String email = ((EditText) findViewById(R.id.email)).getText().toString();
                String phone = ((EditText) findViewById(R.id.phone)).getText().toString();
                String amount = ((EditText) findViewById(R.id.amount)).getText().toString();
                String purpose = ((EditText) findViewById(R.id.purpose)).getText().toString();
                Transaction transaction = new Transaction(name, email, phone, amount, purpose, accessToken);
                final ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "", "please wait...", true, false);
                Request request = new Request(transaction, new OrderRequestCallBack() {
                    @Override
                    public void onFinish(Transaction transaction, Exception error) {
                        dialog.dismiss();
                        if (error != null) {
                            if (error instanceof Errors.ConnectionException) {
                                //handle no internet connection
                                Log.d("App", "No internet connection");
                            } else if (error instanceof Errors.ServerException) {
                                //handle form level errors
                                Log.d("App", "Sever error - " + error.getMessage());
                            } else {
                                //handle other errors
                                Log.d("App", error.getMessage());
                            }
                            return;
                        }

//                        //Using Pre created UI
                        Intent intent = new Intent(getBaseContext(), PaymentDetailsActivity.class);
                        intent.putExtra(PaymentDetailsActivity.TRANSACTION, transaction);
                        startActivityForResult(intent, 9);

                        //Custom UI Implementation
//                        Intent intent = new Intent(getBaseContext(), CustomPaymentMethodActivity.class);
//                        intent.putExtra(CustomPaymentMethodActivity.TRANSACTION, transaction);
//                        startActivityForResult(intent, 9);
                    }
                });

                request.execute();

            }
        });

        Button updateToken = (Button) findViewById(R.id.update_token);
        updateToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateToken();
            }
        });
    }

    private void updateToken() {
        final ProgressDialog dialog = ProgressDialog.show(this, "", "Please wait...", true, false);
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("grant_type", "client_credentials")
                .add("client_id", "cNrgex0RQ3P176F0jCjFfEyCy2UnXjunM1AZCIT8")
                .add("client_secret", "SEqtkfR4GriSPtZkwgBWKEEYCpA8nxa7Q8bDRHqJSWEX1nPyTdNL8hglzYYNvI6kCVGlLr7abPWZ0L9S77VwpBDUTGdaSM9EdZdatQQjmmeykTlyyMqiNuSQs6N6WBsW")
                .build();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("https://www.instamojo.com/oauth2/token/")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                dialog.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                response.body().close();
                try {
                    JSONObject responseObject = new JSONObject(responseBody);
                    accessToken = responseObject.getString("access_token");
                    Log.d("app", accessToken);
                    Log.d("App", "Updated token");
                } catch (JSONException e) {
                    Log.d("App", "Failed to update token");
                }

                dialog.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 9) {
            if (resultCode == RESULT_OK) {
                //handle successful transaction here
                String status = data.getStringExtra(PaymentActivity.TRANSACTION_STATUS);
                String id = data.getStringExtra(PaymentActivity.ORDER_ID);
                Toast.makeText(this, status + " - " + id, Toast.LENGTH_LONG).show();
            } else {
                //handle failed transaction here
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }
}
