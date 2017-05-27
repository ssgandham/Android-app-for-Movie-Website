package com.example.balaji.android_assignment_4;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edit_email, edit_password;
    Button button_submit;
    SharedPreferences shared_preference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit_email = (EditText) findViewById(R.id.email);
        edit_password = (EditText) findViewById(R.id.password);
        button_submit = (Button) findViewById(R.id.submit_button);

        button_submit.setOnClickListener(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        get_shared_preference();

    }

    @Override
    public void onPause() {
        super.onPause();
        get_shared_preference();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        get_shared_preference();
    }

    @Override
    public void onStop() {
        super.onStop();
        get_shared_preference();
    }

    public void get_shared_preference() {
        shared_preference = getSharedPreferences("login_preference", Context.MODE_PRIVATE);
        edit_email.setText(shared_preference.getString("username", ""));
        edit_password.setText(shared_preference.getString("password", ""));
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.submit_button:
                shared_preference = getSharedPreferences("login_preference", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = shared_preference.edit();
                editor.putString("username", edit_email.getText().toString());
                editor.putString("password", edit_password.getText().toString());
                editor.apply();
                editor.commit();
                new Thread(new Runnable() {
                    public void run() {

                        try {
                            URL url = new URL("http://54.183.57.169:8080/Project_Test_Android/connection");
                            URLConnection connection = url.openConnection();

                            String inputString = edit_email.getText().toString() + " " + edit_password.getText().toString();

                            Log.d("inputString", inputString);

                            connection.setDoOutput(true);
                            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                            out.write(inputString);
                            out.close();

                            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                            String returnString = "";
                            String str = "";

                            while ((returnString = in.readLine()) != null) {
                                str = str + returnString;
                            }
                            final String m = str;
                            in.close();
                            HttpURLConnection conn = (HttpURLConnection) connection;
                            conn.disconnect();
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    if (m.contains("true")) {
                                        Toast.makeText(MainActivity.this, "Logging in...", Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(MainActivity.this,Main2Activity.class);
                                        startActivity(i);
                                    }else{
                                        Toast.makeText(MainActivity.this, "Incorrect username or password", Toast.LENGTH_LONG).show();
                                    }

                                }
                            });

                        } catch (Exception e) {
                            Log.d("Exception", e.toString());
                        }

                    }
                }).start();

                break;
        }
    }

}
