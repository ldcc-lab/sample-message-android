package io.github.smarthyun.example_http;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button BtnPost = findViewById(R.id.BtnPost);

        BtnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeakReference<Context> myWeakCOntext = new WeakReference<>(getApplicationContext());
                KakaoApi kakaoApi = new KakaoApi(myWeakCOntext);
                final String body = "{\n" +
                        "    \"msg_id\": \"00000\",\n" +
                        "    \"send_time\": \"\",\n" +
                        "    \"dest_phone\": \"00000\",\n" +
                        "    \"send_phone\": \"00000\",\n" +
                        "    \"sender_key\": \"00000\",\n" +  // 센더 키
                        "    \"msg_body\": \"친구톡 메시지 발송 테스트\",\n" +
                        "    \"ad_flag\": \"N\"\n" +
                        "}";
                kakaoApi.execute(body);
            }
        });
    }
}

class KakaoApi extends AsyncTask<String, Void, Integer> {

    private WeakReference<Context> weakContext;

    KakaoApi(WeakReference weakReference) { this.weakContext = weakReference; }

    @Override
    protected Integer doInBackground(String... strings) {
        try{
            String urlKakao = "http://127.0.0.1"; // API URL 주소
            URL url = new URL(urlKakao);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestProperty("Authorization","Basic ABCD"); // 인증 키
            byte[] outputInBytes = strings[0].getBytes("UTF-8");
            OutputStream os = conn.getOutputStream();
            os.write( outputInBytes );
            os.close();

            int respCode = conn.getResponseCode(); // 응답코드

            InputStream is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder response = new StringBuilder();
            while((line = br.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            br.close();

            String resp = response.toString(); // 응답

            return respCode;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        Toast.makeText(weakContext.get() , "StatusCode=" + integer.toString(), Toast.LENGTH_LONG).show();
    }
}




