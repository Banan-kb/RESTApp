package com.example.restapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    TextView temperature, description, humid, feels, sunriseView, sunsetView;
    JSONObject jsonObj;
    ImageView wBckgrnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button= (Button) findViewById(R.id.button);
        final Spinner cities= (Spinner) findViewById(R.id.spinner);



        final String weatherWebserviceURL =
                "https://api.openweathermap.org/data/2.5/weather?q=riyadh&appid=6602f949c47335204ba59921399752fe&units=metric";

        temperature= (TextView) findViewById(R.id.temperature);
        description= (TextView) findViewById(R.id.description);
        humid= (TextView) findViewById(R.id.humidty);
        feels= (TextView) findViewById(R.id.feels);
        sunriseView= (TextView) findViewById(R.id.sunriseView);
        sunsetView= (TextView) findViewById(R.id.sunsetView);

        wBckgrnd= (ImageView) findViewById(R.id.weatherbackground);


        weather(weatherWebserviceURL);

         button.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String city= cities.getSelectedItem().toString();

                 String url=
                         "https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=6602f949c47335204ba59921399752fe&units=metric";
                 weather(url);

             }
         });



    }

    public void weather(String url) {
        JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("banan", "rsponse received");

                        Log.d("banan", response.toString());
                        try{

                            String town= response.getString("name");
                            JSONObject jMain= response.getJSONObject("main");

                            JSONObject jSys= response.getJSONObject("sys");

                            long sunrise= jSys.getLong("sunrise");
                            long sunset= jSys.getLong("sunset");

                            SimpleDateFormat spf= new SimpleDateFormat("HH:mm:ss");

                            Date srDate= new Date(sunrise*1000);
                            Date ssDate= new Date(sunset*1000);


                            sunriseView.setText("Sunrise: "+spf.format(srDate));
                            sunsetView.setText("Sunset: "+spf.format(ssDate));


                            double temp= jMain.getDouble("temp");
                            double feels_like= jMain.getDouble("feels_like");

                            int humidity= jMain.getInt("humidity");



                            temperature.setText(String.valueOf(temp));
                            description.setText(town);
                            humid.setText("Humidity: "+humidity);
                            feels.setText("Feels Like: "+feels_like);

                            JSONArray jsonWeatherArray= response.getJSONArray("weather");

                            chooseBackground(jsonWeatherArray);

                        }catch(JSONException e){
                            e.printStackTrace();
                            Log.e("Receive Error", e.toString());

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("banan", "error retrieving url");

            }
        }
        );
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObj);
    }

    public void chooseBackground(JSONArray jsonWeatherArray){
        try {
        for(int i=0; i<jsonWeatherArray.length(); i++){
            JSONObject oneObject= jsonWeatherArray.getJSONObject(i);


                String weather= oneObject.getString("main");

                if(weather.equals("Clear")){
                Glide.with(this)
                        .load("https://cdn.pixabay.com/photo/2016/01/02/00/42/cloud-1117279_1280.jpg")
                        .into(wBckgrnd);
            }
            if(weather.equals("Clouds")){
                Glide.with(this)
                        .load("https://cdn.pixabay.com/photo/2016/03/27/07/32/light-1282314_1280.jpg")
                        .into(wBckgrnd);
            }if(weather.equals("Rainy")){
                Glide.with(this)
                        .load("https://cdn.pixabay.com/photo/2015/07/27/19/48/rain-863339_1280.jpg")
                        .into(wBckgrnd);
            }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }
}