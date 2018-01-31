package com.crash.etranzact.masterpass;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.dd.CircularProgressButton;
import com.karan.churi.PermissionManager.PermissionManager;
//import com.dd.CircularProgressButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    PermissionManager permissionManager;
    ProgressDialog progressDialog;
    String etEmail, etPassword;
    RequestQueue queue = null;
    Button Login;
    CircularProgressButton btn;
    /*
    Initialize variables
    */
    private EditText Email, Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        queue = Volley.newRequestQueue(this);
        Email = (EditText) findViewById(R.id.etEmail);
        Password = (EditText) findViewById(R.id.etPassword);
        final Button login = (Button) findViewById(R.id.btnLogin);
       // CircularProgressButton btn = (CircularProgressButton) findViewById(R.id.btnLogin);
        login.setText("Sign in");

        // Allow device to access camera.
        permissionManager = new PermissionManager() {
        };
        permissionManager.checkAndRequestPermissions(this);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*
                show progress dialog at the click of button login.
                 */
                //((Button) findViewById(R.id.btnLogin)).setText("Please wait");


                etEmail = Email.getText().toString();
                etPassword = Password.getText().toString();

                login.setError(null);
                Email.setError(null);

                boolean cancel = false;
                View focusView = null;

                if (!TextUtils.isEmpty(etPassword) && !isPasswordValid(etPassword)) {
                    Password.setError(getString(R.string.error_invalid_password));
                    focusView = Password;
                    cancel = true;

                }

                // Check for a valid email address.
                if (TextUtils.isEmpty(etEmail)) {
                    Email.setError(getString(R.string.error_field_required));
                    focusView = Email;
                    cancel = true;
                    //((Button) findViewById(R.id.btnLogin)).setText("Please wait....");
                } else if (!isEmailValid(etEmail)) {
                    Email.setError(getString(R.string.error_invalid_email));
                    focusView = Email;
                    cancel = true;
                }

                if (!etEmail.isEmpty() && !etPassword.isEmpty()) {
                    JSONObject param = new JSONObject();

                    try {
                        param.put("email", etEmail);
                        param.put("pass", etPassword);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String url = "";
                    String para = "";

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

                    StrictMode.setThreadPolicy(policy);

                    try {
                        //((Button) findViewById(R.id.btnLogin)).setText("Please wait...");

//                        url = "https://www.etranzact.net/moneysend/auth/login";
                        url = "https://demo.etranzact.com/moneysend/auth/login";
                        para = "email=" + URLEncoder.encode(etEmail, "utf-8") + "&password=" + URLEncoder.encode(etPassword, "utf-8");
                    } catch (Exception xc) {
                    }
                    //String url = "https://www.remitpals.com/rp/m?operation=login&json=" + param.toString();
                    Log.v("url", url);
                    String res = new HttpConnectionUtil().getURLConnection(url, para);


                    try {
                        Log.i("res", res);
                        JSONObject obj = new JSONObject(res);

//                        if (progressDialog != null && progressDialog.isShowing()) {
//                            progressDialog.setMessage("check your internet");
//                            progressDialog.dismiss();
//                            ((Button) findViewById(R.id.btnLogin)).setText("Please wait....");
//                        }

                        Log.i("token", obj.has("token") + "");
                        if (obj.has("token")) {
                          if (progressDialog !=null && progressDialog.isShowing()){
                                progressDialog.setMessage("Please wait");
                                progressDialog.dismiss();
                            }
                            String first_name = obj.getJSONObject("user").getString("firstName");
                            String last_name = obj.getJSONObject("user").getString("lastName");

                            SQLHelper helper = new SQLHelper(getApplicationContext());
                            SQLiteDatabase sqliteDB = helper.getWritableDatabase();
                            ContentValues cv = new ContentValues();
                            cv.put("firstname", first_name);
                            cv.put("lastname", last_name);

                            long newRowId = sqliteDB.insert("scanner", null, cv);
                            String token = obj.getString("token");
                            ContentValues cvToken = new ContentValues();
                            cvToken.put("token", token);
                            sqliteDB.insert("token_table", null, cvToken);
                            Log.i("newRowId", newRowId + "");

                            ((Button) findViewById(R.id.btnLogin)).setText("Please wait...");

                            if (newRowId > 0) {
                                ((Button) findViewById(R.id.btnLogin)).setText("Please wait...");

                                Intent i = new Intent(MainActivity.this, DashBoardActivity.class);
                                Toast.makeText(getApplicationContext(), "Welcome to MasterPass " + first_name + " " + last_name, Toast.LENGTH_SHORT).show();
                                //finish();
                                startActivity(i);

                            }


                            //error else if (!obj.getString("error")
//                        } else if (obj.has("error")) {
                        } else {
                            ((Button) findViewById(R.id.btnLogin)).setText("sign in");


                            // circularProgressButton.setProgress(0);
//                            Toast.makeText(MainActivity.this, "Check internet connectio", Toast.LENGTH_LONG).show();
//                            progressDialog = new ProgressDialog(MainActivity.this);
//                            progressDialog.setMessage("Loading...");
//                            progressDialog.setCancelable(true);
//                            progressDialog.cancel();
//                            progressDialog.show();

                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("Login");
                            builder.setMessage("Incorrect email or password. ");
                            builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });

                            builder.setNegativeButton("Try Again.", null);
                            AlertDialog alertDialoq = builder.create();
                            alertDialoq.show();
                            // ((Button) findViewById(R.id.btnLogin)).setText("Please wait....");
                            //Toast.makeText(getApplicationContext(), obj.getString("error"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception dc) {
                        dc.printStackTrace();

                    }
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.v("response", error.toString());
                            //((Button) findViewById(R.id.btnLogin)).setText("Sign in");
                            Toast.makeText(getApplicationContext(), "Check your internet connection.", Toast.LENGTH_SHORT).show();
                        }
                    };

                } else {

                    Toast.makeText(getApplicationContext(), "Incomplete Login information.", Toast.LENGTH_SHORT).show();
                    ((Button) findViewById(R.id.btnLogin)).setText("Sign in");

//                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                    builder.setTitle("Incomplete Login information");
//                    builder.setMessage("Please ensure you enter valid login details ");
//                    builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            finish();
//                        }
//                    });
//                    builder.setNegativeButton("Try Again.", null);
//                    AlertDialog alertDialoq = builder.create();
//                    alertDialoq.show();
//                    // Toast.makeText(getApplicationContext(), "Incomplete Login information.", Toast.LENGTH_SHORT).show();
//                }
                }
            }

            private boolean isPasswordValid(String etPassword) {
                //TODO: Replace this with your own logic
                return etPassword.length() > 4;
            }


        });
    }

    private boolean isEmailValid(String etPassword) {
        //TODO: Replace this with your own logic
        return etPassword.contains("@");
    }


}

