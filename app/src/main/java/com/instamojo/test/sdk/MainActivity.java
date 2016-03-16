package com.instamojo.test.sdk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.instamojo.mojosdk.activities.FormActivity;
import com.instamojo.mojosdk.activities.PaymentActivity;
import com.instamojo.mojosdk.callbacks.MojoRequestCallBack;
import com.instamojo.mojosdk.models.Transaction;
import com.instamojo.mojosdk.network.Request;

public class MainActivity extends AppCompatActivity {

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.pay);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Transaction transaction = new Transaction("vedhavyas",
                        "vedhavyas@instamojo.com", "9663556657", "100.00", "Test purpose");

                Request request = new Request(transaction, "123456", new MojoRequestCallBack() {
                    @Override
                    public void onError(Transaction transaction, Exception exception) {
                        Log.d("app", exception.getMessage());
                        //Handle issue here
                    }

                    @Override
                    public void onSuccess(Transaction transaction) {
                        Intent intent = new Intent(MainActivity.this, FormActivity.class);
                        intent.putExtra(FormActivity.TRANSACTION, transaction);
                        startActivityForResult(intent, 9);
                    }
                });

                request.execute();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 9) {
            if (resultCode == RESULT_OK) {
                String status = data.getStringExtra(PaymentActivity.TRANSACTION_STATUS);
                String id = data.getStringExtra(PaymentActivity.ORDER_ID);
                Toast.makeText(this, status + " - " + id, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }
}
