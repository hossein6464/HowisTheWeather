package diana.soleil.howistheweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    public class DownloadJson extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection httpsURLConnection = (HttpURLConnection) url.openConnection();
                httpsURLConnection.connect();
                InputStream in = httpsURLConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int read = reader.read();
                String result = "";
                while (read !=-1) {
                    char c = (char) read;
                    result += c;
                    read = reader.read();
                }
                return  result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Failed";
            } catch (IOException e) {
                e.printStackTrace();
                return "Failed";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            String weatherMain = null;
            String weatherSecond = null;
            try {
                String jsonObjectWeather="";
                JSONObject jsonObject = new JSONObject(s);
                jsonObjectWeather = jsonObject.getString("weather");
                Log.i("BProblem",jsonObjectWeather);
                JSONArray jsonArray = new JSONArray(jsonObjectWeather);

                for (int i=0;i<jsonArray.length();i++) {
                    JSONObject weatheObject = jsonArray.getJSONObject(i);
                    weatherMain = "The weather is "+weatheObject.getString("main")+".";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                JSONObject jsonObjectWeather2;
                JSONObject jsonObject2 = new JSONObject(s);
                jsonObjectWeather2 = (JSONObject) jsonObject2.get("main");


                    weatherSecond = weatherMain + "The temperature is at " + jsonObjectWeather2.getString("temp") + " but it feels like "+ jsonObjectWeather2.getString("feels_like") + " and the humidity is at " + jsonObjectWeather2.getString("humidity")+"." ;

            } catch (JSONException e) {
                e.printStackTrace();
            }


            TextView textView = (TextView) findViewById(R.id.textView5);
            textView.setText(weatherSecond);
            super.onPostExecute(s);
        }
    }
    public void weatherButton(View view){
        String json1="";
        EditText editText = (EditText) findViewById(R.id.editText); 
        String textFromEdit = editText.getText().toString();
        DownloadJson downloadJson = new DownloadJson();
        try {
            json1 = downloadJson.execute("http://api.openweathermap.org/data/2.5/weather?q="+textFromEdit+"&appid=903d76aaa4f9b2037e57507227135349"). get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i("Json",String.valueOf(json1));
    }
  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}