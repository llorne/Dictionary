//Сделать чтобы сохранялись звездочки.
//Сделать чтобы можно было удалять термины (Почему-то удаляются, но перед этим крашится) УБРАЛ td.update(), но приходится рефрешить самому АНЛААК

//СДЕЛАТЬ АКТИВИТИ С ИЗБРАННЫМИ
//СДЕЛАТЬ ОПРЕДЕЛЕНИЕ ТЕРМИНОВ АКТИВИТИ
//ПРИДУМАТЬ КУДА в приложении должен использоваться NavigationView
//возможность поделиться термином и его определением через другое приложение (Telegram, WhatsApp, почта и т.д.)



package com.mirea.kt.ribo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private EditText user_login;
    private EditText user_password;
    private Button main_btn,hidenFastStart;
    private TextView result_info;
    public String value,valueDescription;

    private String g = "RIBO-04-22";

    private Button buttonStart;
    int k = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) { // Сработает при создании Activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Устанавливаем ссылки на объекты из дизайна
        user_login = findViewById(R.id.user_login);
        user_password = findViewById(R.id.user_password);
        main_btn = findViewById(R.id.main_btn);
        result_info = findViewById(R.id.result_info);
        buttonStart = findViewById(R.id.buttonStart);
        hidenFastStart=findViewById(R.id.HidenFastStart);
        // Обработчик нажатия на кнопку
        main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                result_info.setText("msgClick");
                if (user_login.getText().toString().trim().equals("") || user_password.getText().toString().trim().isEmpty())
                    Toast.makeText(MainActivity.this, R.string.no_user_input, Toast.LENGTH_LONG).show();
                else {
                    String login = user_login.getText().toString();
                    String password = user_password.getText().toString();
                    String url = "https://android-for-students.ru/coursework/login.php";
                    HashMap<String, String> map = new HashMap<>();
                    map.put("lgn", login);
                    map.put("pwd", password);
                    map.put("g", g);
                    HTTPRunnable httpRunnable = new HTTPRunnable(url, map);
                    Thread th = new Thread(httpRunnable);
                    th.start();
                    try {
                        th.join();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } finally {
                        try {
                            JSONObject jsonObject = new JSONObject(httpRunnable.getResponseBody());
                            int result = jsonObject.getInt("result_code");
                            if (result == 1) {
                                TextView textView = findViewById(R.id.result_info);

                                String message = String.format("%s\n%s\n",
                                        jsonObject.getString("title"),
                                        jsonObject.getString("task"),
                                        jsonObject.getString("variant"));
                                try {
                                    value = jsonObject.getString("data");
                                }catch (Exception e){
                                    Log.e("Data_Exception","Нет даты");
                                }
//                                valueDescription = jsonObject.getString("description");
                                textView.setText(message);
                                k++;
                                Log.e("myTask",jsonObject.toString());


                            } else if (result == -1) {
                                Toast.makeText(getApplicationContext(), "Сервер не отвечает", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException | NullPointerException exception) {
                            exception.printStackTrace();
                        }
                    }

//                    new HttpPostRequest().execute(url, login, password, g);
//                    handleHttpResponse(response);
//                    onPostExecute()
//                    result_info.setText(response);
                }

            }
        });
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (k != 0) {

                    Toast.makeText(getApplicationContext(), "Вы успешно вошли!", Toast.LENGTH_LONG).show();
                    Intent switcher = new Intent(MainActivity.this, TermsDictionary.class);
                    switcher.putExtra("data",value);
//                    switcher.putExtra("description",valueDescription);
                    startActivity(switcher);
                } else {
                    Toast.makeText(MainActivity.this, R.string.user_not_check_quest, Toast.LENGTH_LONG).show();
                }
            }
        });
        hidenFastStart.setOnClickListener(v -> {
            user_login.setText("Student82830");
            user_password.setText("sjE5tyv");
        });
    }
}

//    public String handleHttpResponse(String result) {
//
//         Обработка ответа от сервера, например, отображение в TextView
//        return result;
//    }





/*   @SuppressLint("StaticFieldLeak")
    private class GetURLData extends AsyncTask<String, String, String> {

        // Будет выполнено до отправки данных по URL
        protected void onPreExecute() {
            super.onPreExecute();
            result_info.setText("Ожидайте...");
        }

        // Будет выполняться во время подключения по URL
        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                // Создаем URL подключение, а также HTTP подключение
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // Создаем объекты для считывания данных из файла
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                // Генерируемая строка
                StringBuilder buffer = new StringBuilder();
                String line = "";

                // Считываем файл и записываем все в строку
                while((line = reader.readLine()) != null)
                    buffer.append(line).append("\n");

                // Возвращаем строку
                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Закрываем соединения
                if(connection != null)
                    connection.disconnect();

                try {
                    if (reader != null)
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        // Выполняется после завершения получения данных
        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Конвертируем JSON формат и выводим данные в текстовом поле
            try {
                JSONObject jsonObject = new JSONObject(result);
                result_info.setText("Задание: " + jsonObject.getJSONObject("main").getDouble("temp"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
*/
