# Instamojo SDK Integration Documentation

## Installation
### Include SDK
The SDK currently supports Android Version >= ICS 4.0.3(14). Just add the following to your application’s `build.gradle` file, inside the dependencies section.
```
Maven repository here
```

### SDK Permissions
The following are the minimum set of permissions required by the SDK. Add the following set of permissions in the application’s Manifest file above the `<application>` tag.
```
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.READ_SMS" />
<uses-permission android:name="android.permission.RECEIVE_SMS" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
```

### Proguard rules
If you are using Proguard for code obfuscation, add following rules in the proguard configuration file `proguard-rules.pro`
```
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keepclassmembers class **.R$* {
    public static <fields>;
}
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
-keepattributes JavascriptInterface
-keep public class com.instamojo.android.network.JavaScriptInterface
-keep public class * implements com.instamojo.android.network.JavaScriptInterface
-keepclassmembers class com.instamojo.android.network.JavaScriptInterface{
    <methods>;
}

# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version.  We know about them, and they are safe.
-dontwarn android.support.**

# Keep source file names, line numbers for easier debugging
-keepattributes SourceFile,LineNumberTable

-keepattributes Signature
-dontwarn com.squareup.**
-dontwarn okio.**

# OkHttp-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**

# apache http
-dontwarn org.apache.http.**
-dontwarn android.net.http.AndroidHttpClient

# Juspay rules
-keep class in.juspay.** {*;}
-dontwarn in.juspay.**

# support class
-keep class android.support.v4.** { *; }
-keep class android.support.v7.** { *; }
```

## Initiating Payment
To initiate a Payment, the following mandatory fields are required by the SDK.

1. Name of the buyer &nbsp;
2. Email of the buyer &nbsp;
3. Purpose of the transaction &nbsp;
4. Phone number of the buyer &nbsp;
5. Transaction amount &nbsp;
6. Access Token &nbsp;

### Generating Access Token
A valid access token should be generated on your server using your `Client ID` and `Client Secret` and the token is then passed on to the application.
Access token will be valid for a max of 30 minutes after generation.

### Creating Transaction Object
With all the mandatory fields mentioned above, a `Transaction` object can created like this.
``` Java
Transaction transaction = new Transaction(name, email, phone, amount, purpose, accessToken);
```

Add the following code snippet to validate the `Transaction` object.
``` Java
//Show progressDialog while the SDK validates the Transaction.
final ProgressDialog dialog = ProgressDialog.show(getBaseContext(), "", "please wait...", true, false);
Request request = new Request(transaction, new OrderRequestCallBack() {
            @Override
            public void onFinish(Transaction transaction, Exception error) {
                dialog.dismiss();
                if (error != null) {
                    if (error instanceof Errors.ConnectionException) {
                           Log.e("App", "No internet connection");
                    } else if (error instanceof Errors.ServerException) {
                           try {
                                JSONObject errorObject = new JSONObject(error.getMessage());
                
                                if (errorObject.has("success")){
                                      Log.e("App", "Invalid access token");
                                      return;
                                }
                
                                if (errorObject.has("buyer_phone")){
                                      Log.e("App", "Buyer's Phone Number is invalid");
                                      return;
                                }
                
                                if (errorObject.has("buyer_email")){
                                      Log.e("App", "Buyer's Email is invalid");
                                      return;
                                }
                
                                if (errorObject.has("buyer_name")){
                                      Log.e("App", "Buyer's Name is required");
                                      return;
                                }
                           } catch (JSONException e) {}
                    } else {
                            Log.e("App", error.getMessage());
                    }
                    return;
                }
                
                startPreCreatedUI(transaction);
            }
 });
 
 request.execute();
```

## Payment
### Collecting Payment Information
SDK currently supports to forms of Payment methods.

1. Debit/Credit Card
2. Netbanking

These details can be collected in two ways.

1. Pre-Created UI that comes with the SDK.
2. Creating Custom Debit/Credit card and Netbanking UI.

#### Using Pre-Created UI
Add the following code snippet to your application's activity/fragment to use Pre-created UI.
``` Java
private void startPreCreatedUI(Transaction transaction){
        //Using Pre created UI
        Intent intent = new Intent(getBaseContext(), PaymentDetailsActivity.class);
        intent.putExtra(PaymentDetailsActivity.TRANSACTION, transaction);
        startActivityForResult(intent, 9);
}
```

#### Using Custom Created UI
We know that every application is unique. If you choose to create your own UI to collect Payment information, SDK has necessary APIs to achieve this.
Use `CustomPaymentMethodActivity` activity, which uses SDK APIs to collect Payment Information, to extend and modify as per your needs.

### Receiving Payment result
Add the following code snippet in the same activity.
If `resultCode == RESULT_OK`, then payment is successful. Else Payment Failed. 
``` Java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 9) {
            if (resultCode == RESULT_OK) {
                //handle successful payment here
                String status = data.getStringExtra(PaymentActivity.TRANSACTION_STATUS);
                String id = data.getStringExtra(PaymentActivity.ORDER_ID);
                Toast.makeText(this, status + " - " + id, Toast.LENGTH_LONG).show();
            } else {
                //handle failed payment here
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            }
        }
}
```

## Debugging
Debugging can very useful during SDK Integration.
Add following `meta-data` in your application's `Manifest` file under `<application>` tag.
``` XML
<meta-data
            android:name="instamojo_sdk_log_level"
            android:value="debug" />
```

Once the application is ready to be pushed to the Play Store, change `android:value="error"` to log only errors.
