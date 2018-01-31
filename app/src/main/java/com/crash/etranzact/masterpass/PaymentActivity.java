package com.crash.etranzact.masterpass;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//import com.dd.CircularProgressButton;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PaymentActivity extends AppCompatActivity {
    TextView merchant;
    Button Pay;
    EditText Amount;
    String bankCode = "";
    Spinner spinner;
    Button Back;
    int etAmount;
    ProgressBar mprogressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        merchant = (TextView) findViewById(R.id.tvMerchantName);
        Amount = (EditText) findViewById(R.id.etAmount);
        Back = (Button) findViewById(R.id.btnBack);
        Pay = (Button) findViewById(R.id.btnPay);
        //get the spinner from the xml.
        final Spinner dropdown = (Spinner) findViewById(R.id.spinner1);

        String marchant = getIntent().getStringExtra("qr_title");
        final String barcode = getIntent().getStringExtra("barcode");
        //Log.i("MERCHANT", marchant);
        merchant.setText(marchant);

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PaymentActivity.this, DashBoardActivity.class);
                finish();
                startActivity(i);

            }
        });

        Pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Amount.getText().toString().isEmpty()) {
                    Toast.makeText(PaymentActivity.this, "Enter a valied amount", Toast.LENGTH_LONG).show();


                } else {

//                    String url = "https://www.etranzact.net/moneysend/qr/test";
                    String url = "https://demo.etranzact.com/moneysend/qr/test";

                    String selBank = dropdown.getSelectedItem().toString().split(":")[1];
                    Log.i("selBank", selBank);
                    String amount = Amount.getText().toString();
                    JSONObject obj = new JSONObject();

                    SQLHelper helper = new SQLHelper(getApplicationContext());
                    SQLiteDatabase sqliteDB = helper.getReadableDatabase();
                    Cursor cursor = sqliteDB.rawQuery("SELECT token FROM token_table", null);
                    List itemIds = new ArrayList<>();

                    while (cursor.moveToNext()) {
                        String token = cursor.getString(cursor.getColumnIndexOrThrow("token"));
                        itemIds.add(token);
                    }

                    try {

                        String selectedBank = dropdown.getSelectedItem().toString().split(":")[1];
                        obj.put("amount", amount);
                        obj.put("sourceBank", selectedBank.trim());
                        obj.put("qrString", barcode);

                        String token = itemIds.get(itemIds.size() - 1).toString();
                        String res = new token_getter().postURLConnection("https://demo.etranzact.com/moneysend/qr/test", token, obj.toString());
//                        "https://www.etranzact.net/moneysend/qr/test
                        System.out.println(res);
                        JSONObject objres = new JSONObject(res);
                        String responseCode = objres.optString("responseCode");
                        String responseMessage = objres.optString("responseMessage");
                        String balance = objres.optString("balance");
                        if (responseCode.equals("0")) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this);
                            builder.setTitle("Confirm Payment");
                            builder.setMessage("Payment Successful");
                            builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                            builder.setNegativeButton("Thank you.", null);
                            AlertDialog alertDialoq = builder.create();
                            alertDialoq.show();

                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this);
                            builder.setTitle("Confirm Payment");
                            builder.setMessage("Payment not Successful");
                            builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();

                                }
                            });
                            builder.setNegativeButton("Thank you.", null);
                            AlertDialog alertDialoq = builder.create();
                            alertDialoq.show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                }
            }
        });

        //create a list of items for the spinner.
//        https://www.etranzact.net/moneysend/qr/sourceBanks";
        String url = "https://demo.etranzact.com/moneysend/qr/sourceBanks";
        //  String auth_token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjozLCJzdWJqZWN0IjoiZmliYm9jb21tIiwiZW1haWwiOiJqZXJlbWlhaC5pbW9AZXRyYW56YWN0LmNvbSIsImlhdCI6MTUwODE2MTg0MSwiZXhwIjoxNTA4MTY1NDQxfQ.203SceceC7h24eF8Y0L69_0fuYzCWnyMowL9JUDNNmM";
        SQLHelper helper = new SQLHelper(getApplicationContext());
        SQLiteDatabase sqliteDB = helper.getReadableDatabase();
        Cursor cursor = sqliteDB.rawQuery("SELECT token FROM token_table", null);
        List itemIds = new ArrayList<>();

        while (cursor.moveToNext()) {
            String token = cursor.getString(cursor.getColumnIndexOrThrow("token"));
            itemIds.add(token);
        }

        Log.i("TOKEN VALUE ", itemIds.get(itemIds.size() - 1).toString());
        String tokenValue = itemIds.get(itemIds.size() - 1).toString();


        try {
            String res = new token_getter().getURLConnection(url, tokenValue);

            JsonArray resArray = new JsonParser().parse(res).getAsJsonArray();//create an adapter to describe how the items are displayed, adapters are used in several places in android.
            ArrayList itemsd = new ArrayList<String>();
            for (int t = 0; t < resArray.size(); t++) {
                try {
                    System.out.println(resArray.get(t));
                    System.out.println(resArray.get(t).toString());
                    JSONObject rec = new JSONObject(resArray.get(t).toString());
                    itemsd.add(rec.get("bankName") + " : " + rec.get("bankCode"));

                } catch (Exception s) {
                    s.printStackTrace();
                }

            }

            System.out.println(itemsd.get(0) + " CCCCCCCCCCCCCCCCC " + itemsd.toString());
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, itemsd);
            //set the spinners adapter to the previously created one.
            dropdown.setAdapter(adapter);
        } catch (Exception e) {
            try {


                Toast.makeText(getApplicationContext(), "Token Expired...", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(PaymentActivity.this, MainActivity.class);
                //finish();
                startActivity(i);
            } catch (Exception e1) {
            }


        }
    }
}
