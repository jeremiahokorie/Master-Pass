package com.crash.etranzact.masterpass;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class DashBoardActivity extends AppCompatActivity {
    TextView textScan, textGallery, textMerchant;
    String Email, Password;
    Button Scan;
    TextView Message;
    ImageView ivImage;
    Integer REQUEST_CAMERA = 1, SELECT_File = 0;
    TextView Logout;
    // TextView payment;
    TextView User;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        textScan = (TextView) findViewById(R.id.scanCamera);
        textGallery = (TextView) findViewById(R.id.gallery);
        ivImage = (ImageView) findViewById(R.id.tvImage);
        Logout = (TextView) findViewById(R.id.tvLogout);
        //payment = (TextView) findViewById(R.id.tvSpin);
        User = (TextView) findViewById(R.id.tvUser);

        //create an SQL database to store the data to be displayed.
        SQLHelper helper = new SQLHelper(getApplicationContext());
        SQLiteDatabase sqliteDB = helper.getReadableDatabase();
        String[] columns = {"firstname", "lastname"};
        //Cursor cursor = sqliteDB.query("scanner", columns,null,null,null,null,null);
        Cursor cursor = sqliteDB.rawQuery("SELECT firstname, lastname FROM scanner", null);
        List itemIds = new ArrayList<>();


        // iterate through the names to be printed  using while statement
        while (cursor.moveToNext()) {
            String firstname = cursor.getString(cursor.getColumnIndexOrThrow("firstname"));
            String lastname = cursor.getString(cursor.getColumnIndexOrThrow("lastname"));
            itemIds.add(firstname + " " + lastname);

        }

        //print this information                                    // print the last user.
        Log.i("xxxxxxxxxxxxxxxxxxx ", itemIds.get(itemIds.size() - 1).toString());
        String name = itemIds.get(itemIds.size() - 1).toString();

        //print the first and the lastname to the next activity
        User.setText(name);
        // Logout Button
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                System.exit(0);

            }
        });

        // click to launch Camera and Launch
        textScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoardActivity.this, Scan_Camera.class);
                //finish();
                startActivity(intent);

            }
        });

        // click to upload from gallery

        textGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoardActivity.this, Upload_Gallery.class);
                //finish();
                startActivity(intent);

            }
        });


    }

    //    @Override
//    public void onBackPressed() {
//Intent i = new Intent(this,MainActivity.class);
//
////System.exit(0);
//        startActivity(i);
//        finish();
//    }
    private Boolean exit = false;

    @Override
    public void onBackPressed() {
        if (exit) {
            finish();
            System.exit(0);
            // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler() {
                public void postDelayed(Runnable runnable, int i) {
                }

                @Override
                public void publish(LogRecord record) {

                }

                @Override
                public void flush() {

                }

                @Override
                public void close() throws SecurityException {

                }
            }.postDelayed(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    Intent a = new Intent(Intent.ACTION_MAIN);
                    a.addCategory(Intent.CATEGORY_HOME);
                    a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(a);
                }
            }, 1000);
        }
    }
}
