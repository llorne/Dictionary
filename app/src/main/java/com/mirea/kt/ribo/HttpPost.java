package com.mirea.kt.ribo;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpPost extends AppCompatActivity {
    private TextView result_info2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_http_post);
        result_info2 = findViewById(R.id.result_info2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        class HttpPostRequest extends AsyncTask<String, Void, String> {
            private MainActivity mActivity;



            @Override
            protected String doInBackground(String... params) {
                String urlString = params[0];
                String login = params[1];
                String password = params[2];
                String group = params[3];
                StringBuilder result = new StringBuilder();
                HttpURLConnection urlConnection = null;

                try {
                    URL url = new URL(urlString);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type", "application/json");

                    // Формируем данные для отправки на сервер
                    String postData = "lgn=" + login + "&pwd=" + password + "&g=" + group;

                    OutputStream outputStream = urlConnection.getOutputStream();
                    outputStream.write(postData.getBytes());
                    outputStream.flush();

                    // Получаем ответ от сервера
                    BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    reader.close();

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
                result_info2.setText(result.toString());
                return result.toString();

            }


        }

    }
}