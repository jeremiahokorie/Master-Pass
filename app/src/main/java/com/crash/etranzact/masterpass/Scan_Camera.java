package com.crash.etranzact.masterpass;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
//import com.google.zxing.client.android.Intents;
import com.mastercard.masterpassscan.processor.MasterPassCheck;
import com.mastercard.masterpassscan.processor.MasterPassProcessor;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class Scan_Camera extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    public static final int PERMISSION_REQUEST_CAMERA = 1;
    private static final String TAG = "scanActivity";
    private ZXingScannerView mScannerView;
    Resumer resumer;
    Finisher finisher;
    SurfaceView cameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: method was called ");

        ArrayList<BarcodeFormat> formats = new ArrayList<>(1);
        Resumer resumer;
        Finisher finisher;
        formats = new ArrayList<>(1);
        formats.add(BarcodeFormat.QR_CODE);
        mScannerView = new ZXingScannerView(this);
        mScannerView.setFormats(formats);
        startCamera();
        setContentView(mScannerView);
    }

    public void startCamera()
    {
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    public void stopCamera()
    {
        mScannerView.stopCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mScannerView.stopCamera();
        Log.d(TAG, "onDestroyed: method was called ");
       // mScannerView.stopCamera();

    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }


    @Override
    public void onPause() {

        super.onPause();
        mScannerView.removeAllViews();
        mScannerView.stopCamera();

    }

    @Override
    public void onRestart() {
        Log.d(TAG, "onRestart: method was called ");
        super.onRestart();
        mScannerView.resumeCameraPreview(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mScannerView.removeAllViews();
        mScannerView.stopCamera();
        finish();
        System.exit(0);
    }

    @Override
    public void handleResult(Result result) {

        if (resumer == null) resumer = new Resumer(mScannerView, Scan_Camera.this);
        if (finisher == null) finisher = new Finisher(Scan_Camera.this);


        final String qr = result.getText();
        //Log.v(TAG, qr);

        try {
            MasterPassProcessor masterPassProcessor = new MasterPassProcessor();
            String masterPassResponse = masterPassProcessor.scan(qr);

            if (masterPassResponse == null) {
                showError();
                return;
            }

            MasterPassCheck masterPassCheck = new MasterPassCheck();


            if (masterPassCheck.checkStaticOldQR(qr)) {
                JSONObject jsonObject = new JSONObject(masterPassResponse);
                String initiationMethod = jsonObject.optString("POINT_OF_INITIATION");
                String merchantName = jsonObject.optString("MERCHANT_NAME");
                String crc = jsonObject.optString("CRC");
                String mccCode = jsonObject.optString("MCC_CODE");
                String merchantCity = jsonObject.optString("MERCHANT_CITY");
                String merchantCountry = jsonObject.optString("MERCHANT_COUNTRY");
                String merchantCurrency = jsonObject.optString("MERCHANT_CURRENCY");
                String pan = jsonObject.optString("PAN");
                String merchantNumber = jsonObject.optString("MERCHANT_MOBILE_NUMBER");
                String bankAccountNumber = jsonObject.optString("BANK_ACCOUNT_NUMBER");
                String merchantAddress = jsonObject.optString("MERCHANT_ADDRESS");
                String postalCode = jsonObject.optString("POSTAL_CODE");
                String otherDataElements = jsonObject.optString("OTHER_DATA_ELEMENTS");

                Log.v(TAG, initiationMethod);
                Log.v(TAG, merchantName);
                Log.v(TAG, merchantCity);
                Log.v(TAG, mccCode);
                Log.v(TAG, crc);
                Log.v(TAG, merchantCountry);
                Log.v(TAG, merchantCurrency);
                Log.v(TAG, pan);
                Log.v(TAG, merchantNumber);
                Log.v(TAG, bankAccountNumber);
                Log.v(TAG, merchantAddress);
                Log.v(TAG, postalCode);
                Log.v(TAG, otherDataElements);
                finish();

                Intent intent = new Intent(Scan_Camera.this,PaymentActivity.class);
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra("qr_title", merchantName);
                intent.setType("text/plain");
                intent.putExtra("barcode", qr);
                finish();
                startActivity(intent);

            } else if (masterPassCheck.checkDynamicOldQR(qr)) {

                JSONObject jsonObject = new JSONObject(masterPassResponse);
                String initiationMethod = jsonObject.optString("POINT_OF_INITIATION");
                String merchantName = jsonObject.optString("MERCHANT_NAME");
                String crc = jsonObject.optString("CRC");
                String mccCode = jsonObject.optString("MCC_CODE");
                String merchantCity = jsonObject.optString("MERCHANT_CITY");
                String merchantCountry = jsonObject.optString("MERCHANT_COUNTRY");
                String merchantCurrency = jsonObject.optString("MERCHANT_CURRENCY");
                String pan = jsonObject.optString("PAN");
                String merchantNumber = jsonObject.optString("MERCHANT_MOBILE_NUMBER");
                String bankAccountNumber = jsonObject.optString("BANK_ACCOUNT_NUMBER");
                String transAmount = jsonObject.optString("TRANS_AMOUNT");
                String tipAmount = jsonObject.optString("TIP_AMOUNT");
                String tipPrompt = jsonObject.optString("TIP_PROMPT");
                String billNumber = jsonObject.optString("BILL_NUMBER");
                String merchantAddress = jsonObject.optString("MERCHANT_ADDRESS");
                String postalCode = jsonObject.optString("POSTAL_CODE");
                String mobileDeviceId = jsonObject.optString("MOBILE_DEVICE_ID");
                String otherDataElements = jsonObject.optString("OTHER_DATA_ELEMENTS");

                Log.v(TAG, initiationMethod);
                Log.v(TAG, merchantName);
                Log.v(TAG, merchantCity);
                Log.v(TAG, mccCode);
                Log.v(TAG, crc);
                Log.v(TAG, merchantCountry);
                Log.v(TAG, merchantCurrency);
                Log.v(TAG, pan);
                Log.v(TAG, merchantNumber);
                Log.v(TAG, bankAccountNumber);
                Log.v(TAG, merchantAddress);
                Log.v(TAG, postalCode);
                Log.v(TAG, otherDataElements);
                Log.v(TAG, transAmount);
                Log.v(TAG, tipAmount);
                Log.v(TAG, tipPrompt);
                Log.v(TAG, billNumber);
                Log.v(TAG, mobileDeviceId);


//                    showPinAmountDialog(merchantName, qr);

            } else {

                JSONObject jsonObject = new JSONObject(masterPassResponse);
                String indicator = jsonObject.optString("INDICATOR");
                String initiationMethod = jsonObject.optString("INITIATION_METHOD");
                String masterPassQRMerchantId = jsonObject.optString("MASTERPASS_QR_MERCHANT_ID");
                String mccCode = jsonObject.optString("MERCHANT_CATEGORY_CODE");
                String transactionCurrencyCode = jsonObject.optString("TRANSACTION_CURRENCY_CODE");
                String transactionAmount = jsonObject.optString("TRANSACTION_AMOUNT");
                String tipIndicator = jsonObject.optString("TIP_INDICATOR");
                String valueOfConvenienceFeeFixed = jsonObject.optString("VALUE_OF_CONVENIENCE_FEE_FIXED");
                String valueOfConvenienceFeePercentage = jsonObject.optString("VALUE_OF_CONVENIENCE_FEE_PERCENTAGE");
                String countryCode = jsonObject.optString("COUNTRY_CODE");
                String merchantName = jsonObject.optString("MERCHANT_NAME");
                String merchantCity = jsonObject.optString("MERCHANT_CITY");
                String postalCode = jsonObject.optString("POSTAL_CODE");
                String crc = jsonObject.optString("CRC");

                Log.v(TAG, indicator);
                Log.v(TAG, initiationMethod);
                Log.v(TAG, merchantName);
                Log.v(TAG, merchantCity);
                Log.v(TAG, mccCode);
                Log.v(TAG, crc);
                Log.v(TAG, masterPassQRMerchantId);
                Log.v(TAG, transactionCurrencyCode);
                Log.v(TAG, transactionAmount);
                Log.v(TAG, tipIndicator);
                Log.v(TAG, valueOfConvenienceFeeFixed);
                Log.v(TAG, valueOfConvenienceFeePercentage);
                Log.v(TAG, postalCode);
                Log.v(TAG, countryCode);

                JSONObject additionalDataField = null;

                if (((additionalDataField != null)) && (!(additionalDataField.length() == 0))) {
                    additionalDataField = jsonObject.optJSONObject("ADDITIONAL_DATA_FIELD");
                    String loyaltyNumber = additionalDataField.optString("LOYALTY_NUMBER");
                    String billNumber = additionalDataField.optString("BILL_NUMBER");
                    String storeId = additionalDataField.optString("STORE_ID");
                    String mobileNumber = additionalDataField.optString("MOBILE_NUMBER");
                    String referenceId = additionalDataField.optString("REFERENCE_ID");
                    String consumerId = additionalDataField.optString("CONSUMER_ID");
                    String terminalId = additionalDataField.optString("TERMINAL_ID");
                    String purpose = additionalDataField.optString("PURPOSE");

                    Log.v(TAG, loyaltyNumber);
                    Log.v(TAG, billNumber);
                    Log.v(TAG, storeId);
                    Log.v(TAG, mobileNumber);
                    Log.v(TAG, referenceId);
                    Log.v(TAG, consumerId);
                    Log.v(TAG, terminalId);
                    Log.v(TAG, purpose);
                    //showPinAmountDialog(merchantName, qr);
                }

            }
            // Log.v(TAG, masterPassResponse);
        } catch (Exception e) {
            e.printStackTrace();
            showError();
        }
        mScannerView.removeAllViews();
       mScannerView.stopCamera();
    }

    private void showPinAmountDialog(String merchantName, String qr) {

        new AlertDialog.Builder(this)
                .setCancelable(true)
                .setTitle("Pay with MasterPass")
                .setMessage("Please scan a valid QR Code.")
                .setPositiveButton("Re-scan", resumer)
                .setNegativeButton("Cancel", finisher)
                .setOnCancelListener(resumer)
                .setOnDismissListener(resumer)
                .show();

    }

    private void showError() {

        new AlertDialog.Builder(this)
                .setCancelable(true)
                .setTitle("Pay with MasterPass")
                .setMessage("Please scan a valid QR Code.")
                .setPositiveButton("Re-scan", resumer)
                .setNegativeButton("Cancel", finisher)
                .setOnCancelListener(resumer)
                .setOnDismissListener(resumer)
                .show();
    }

    private static class Resumer implements
            DialogInterface.OnClickListener,
            DialogInterface.OnDismissListener,
            DialogInterface.OnCancelListener {

        WeakReference<ZXingScannerView> v;
        WeakReference<ZXingScannerView.ResultHandler> h;

        private Resumer(ZXingScannerView v, ZXingScannerView.ResultHandler h) {
            this.v = new WeakReference<>(v);
            this.h = new WeakReference<>(h);
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            if (h.get() != null && v.get() != null) v.get().resumeCameraPreview(h.get());
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (h.get() != null && v.get() != null) v.get().resumeCameraPreview(h.get());
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            if (h.get() != null && v.get() != null) v.get().resumeCameraPreview(h.get());
        }
    }

    private static class Finisher implements
            DialogInterface.OnClickListener,
            DialogInterface.OnDismissListener,
            DialogInterface.OnCancelListener {

        final WeakReference<Activity> a;

        private Finisher(Activity a) {
            this.a = new WeakReference<>(a);
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            if (a.get() != null) a.get().finish();
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (a.get() != null) a.get().finish();
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            if (a.get() != null) a.get().finish();
        }
    }
}

