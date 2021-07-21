package com.anh.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.app.VoiceInteractor;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    EditText edtSeach;
    Button btSeach, btPage2;
    TextView tvCity, tvCountry, tvTemp, tvStatus, tvHumidity, tvCloud, tvWind, tvDay;
    ImageView imgIcon;
    String city = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Anhxa();
        GetCurrentWeatherData("Hanoi");
        btSeach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cityInput = edtSeach.getText().toString();
                if (cityInput.equals("")){
                    city = "Hanoi";
                    GetCurrentWeatherData(city);
                }else {
                    city = cityInput;
                    GetCurrentWeatherData(city);
                }
            }
        });
        btSeach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cityInput = edtSeach.getText().toString();
                Intent intent =new Intent(MainActivity.this, MainActivity2.class);
                intent.putExtra("city",cityInput);
                startActivity(intent);
            }
        });
    }
    public void  GetCurrentWeatherData(String data){
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url ="https://api.openweathermap.org/data/2.5/weather?q="+data+"&units=metric&appid=2b95e9f03989e735bf49ecbc735c126e";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject =new JSONObject(response);
                            String  day = jsonObject.getString("dt");
                            String  tp = jsonObject.getString("name");
                            tvCity.setText(tp);

                            long l = Long.valueOf(day);
                            Date date = new Date(l*1000L);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE YYYY-MM-DD HH-mm-ss");
                            String Day = simpleDateFormat.format(date);
                            tvDay.setText(Day);
                            JSONArray jsonArrayWeather =jsonObject.getJSONArray("weather");
                            JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                            String  status = jsonObjectWeather.getString("main");
                            String icon = jsonObjectWeather.getString("icon");

                            Picasso.with(MainActivity.this).load("https://openweathermap.org/img/wn/"+ icon +".png").into(imgIcon);
                            tvStatus.setText(status);

                            JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                            String temp = jsonObjectMain.getString("temp");
                            String humidity = jsonObjectMain.getString("humidity");

                            double a = Double.valueOf(temp);
                            String stTemp = String.valueOf(a);
                            tvTemp.setText(stTemp + "°C");
                            tvHumidity.setText(humidity + "%");

                            JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");
                            String wind = jsonObjectWind.getString("speed");
                            tvWind.setText(wind +"m/s");

                            JSONObject jsonObjectCloud = jsonObject.getJSONObject("clouds");
                            String clouds = jsonObjectCloud.getString("all");
                            tvCloud.setText(clouds +"%");

                            JSONObject jsonObjectSYS = jsonObject.getJSONObject("sys");
                            String country = jsonObjectSYS.getString("country");
                            tvCountry.setText(country);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(stringRequest);
    }
    private  void  Anhxa(){
        edtSeach = (EditText) findViewById(R.id.edtSeach);
        btSeach = (Button) findViewById(R.id.btSeach);
        tvCity = (TextView) findViewById(R.id.tvCity);
        tvCountry = (TextView) findViewById(R.id.tvCountry);
        tvTemp = (TextView) findViewById(R.id.tvTemp);
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        tvHumidity = (TextView) findViewById(R.id.tvhumidity);
        tvCloud = (TextView) findViewById(R.id.tvcloud);
        tvWind = (TextView) findViewById(R.id.tvWind);
        tvDay = (TextView) findViewById(R.id.tvDay);
        imgIcon= (ImageView) findViewById(R.id.imgIcon);
    }
}