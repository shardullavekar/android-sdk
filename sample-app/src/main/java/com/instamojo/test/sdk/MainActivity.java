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

import com.instamojo.android.activities.PaymentDetailsActivity;
import com.instamojo.android.callbacks.OrderRequestCallBack;
import com.instamojo.android.helpers.Constants;
import com.instamojo.android.models.Errors;
import com.instamojo.android.models.Order;
import com.instamojo.android.network.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private String accessToken = "wLd5A00ZwskdBhIlGNuFSx5LyhrjpC";
    private Random random = new Random(System.currentTimeMillis());

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.pay);
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setMessage("please wait...");
        dialog.setCancelable(false);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ((EditText) findViewById(R.id.name)).getText().toString();
                String email = ((EditText) findViewById(R.id.email)).getText().toString();
                String phone = ((EditText) findViewById(R.id.phone)).getText().toString();
                String amount = ((EditText) findViewById(R.id.amount)).getText().toString();
                String purpose = ((EditText) findViewById(R.id.description)).getText().toString();
                Order order = new Order(accessToken, String.valueOf(random.nextInt()), name, email, phone, amount, purpose);
                dialog.show();
                Request request = new Request(order, new OrderRequestCallBack() {
                    @Override
                    public void onFinish(Order order, Exception error) {
                        dialog.dismiss();
                        if (error != null) {
                            if (error instanceof Errors.ConnectionError) {
                                Log.e("App", "No internet connection");
                            } else if (error instanceof Errors.ServerError) {
                                Log.e("App", "Server Error. Try again");
                            } else if (error instanceof Errors.AuthenticationError) {
                                Log.e("App", "Access token is invalid or expired");
                            } else if (error instanceof Errors.ValidationError) {
                                // Cast object to validation to pinpoint the issue
                                Errors.ValidationError validationError = (Errors.ValidationError) error;

                                if (!validationError.isValidTransactionID()) {
                                    Log.e("App", "Transaction ID is not Unique");
                                    return;
                                }

                                if (!validationError.isValidRedirectURL()) {
                                    Log.e("App", "Redirect url is invalid");
                                    return;
                                }

                                if (!validationError.isValidPhone()) {
                                    Log.e("App", "Buyer's Phone Number is invalid/empty");
                                    return;
                                }

                                if (!validationError.isValidEmail()) {
                                    Log.e("App", "Buyer's Email is invalid/empty");
                                    return;
                                }

                                if (!validationError.isValidAmount()) {
                                    Log.e("App", "Amount is either less than Rs.9 or has more than two decimal places");
                                    return;
                                }

                                if (!validationError.isValidName()) {
                                    Log.e("App", "Buyer's Name is required");
                                    return;
                                }
                            } else {
                                Log.e("App", error.getMessage());
                            }
                            return;
                        }

                        startCustomUI(order);
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

    private void startPreCreatedUI(Order order) {
        //Using Pre created UI
        Intent intent = new Intent(getBaseContext(), PaymentDetailsActivity.class);
        intent.putExtra(Constants.ORDER, order);
        startActivityForResult(intent, Constants.REQUEST_CODE);
    }

    private void startCustomUI(Order order) {
        //Custom UI Implementation
        Intent intent = new Intent(getBaseContext(), CustomUIActivity.class);
        intent.putExtra(Constants.ORDER, order);
        startActivityForResult(intent, Constants.REQUEST_CODE);
    }


    /**
     * Token should not be generated on the app like this. The token must be generated on your server
     * and should be fetched to your app.
     */
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
        if (requestCode == Constants.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                //handle successful payment here
                String status = data.getStringExtra(Constants.TRANSACTION_STATUS);
                String id = data.getStringExtra(Constants.ORDER_ID);
                Toast.makeText(this, status + " - " + id, Toast.LENGTH_LONG).show();
            } else {
                //handle failed payment here
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }
}
