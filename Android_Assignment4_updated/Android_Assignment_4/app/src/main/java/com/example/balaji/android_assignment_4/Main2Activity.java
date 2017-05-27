package com.example.balaji.android_assignment_4;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class Main2Activity extends AppCompatActivity implements View.OnClickListener {
    EditText edit_search;
    Button button_search, button_prev, button_next;
    TextView text_search;
    int result_count, current_page = 0, results_perpage = 5;
    List<String> list_movies = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        edit_search = (EditText) findViewById(R.id.search);
        button_search = (Button) findViewById(R.id.search_button);
        text_search = (TextView) findViewById(R.id.display_text);
        button_prev = (Button) findViewById(R.id.prev);
        button_next = (Button) findViewById(R.id.next);
        button_search.setOnClickListener(this);
        button_prev.setOnClickListener(this);
        button_next.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.search_button:

                new Thread(new Runnable() {
                    public void run() {

                        try {
                            URL url = new URL("http://54.183.57.169:8080/Project_Test_Android/test_servlet");
                            URLConnection connection = url.openConnection();

                            String inputString = edit_search.getText().toString();

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
                            final String[] opt_split_string = str.split(",");
                            result_count = opt_split_string.length;
                            in.close();

                            runOnUiThread(new Runnable() {
                                public void run() {
//                                    if (m.contains("true")) {
                                    String display_text = "";
                                    for (int i = 0; i < result_count; i++) {
                                        list_movies.add(opt_split_string[i]);
                                    }
                                    for (int i = 0; i < results_perpage; i++) {
                                        display_text = display_text + opt_split_string[i] + "\n";
                                        Log.d("Final String", display_text);
                                    }

                                    if (!display_text.equals("")) {
                                        text_search = (TextView) findViewById(R.id.display_text);
                                        text_search.setText(display_text);
                                    } else {
                                        text_search = (TextView) findViewById(R.id.display_text);
                                        text_search.setText("No Movies found for the entered query");
                                    }
//                                        Intent i = new Intent(Main2Activity.this,Main2Activity.class);
//                                        startActivity(i);
//                                    }else{
//                                        Toast.makeText(Main2Activity.this, "Incorrect username or password", Toast.LENGTH_LONG).show();
//                                    }

                                }
                            });

                        } catch (Exception e) {
                            Log.d("Exception", e.toString());
                        }

                    }
                }).start();
                break;
            case R.id.prev:
                String display_text = "";
                Toast.makeText(Main2Activity.this, "previous button is pressed", Toast.LENGTH_LONG).show();
                break;
            case R.id.next:
                String display_text1 = "";
                Toast.makeText(Main2Activity.this, "next button is pressed", Toast.LENGTH_LONG).show();
                try {
                    if((current_page+results_perpage)<result_count)
                    current_page += results_perpage;
                    for (int i = current_page; i <= (current_page + results_perpage); i++) {
                        if (list_movies.get(i) != null) {
                            display_text1 = display_text1 + list_movies.get(i) + "\n";
                        }
                        text_search = (TextView) findViewById(R.id.display_text);
                        text_search.setText("");
                        text_search.setText(display_text1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

}



