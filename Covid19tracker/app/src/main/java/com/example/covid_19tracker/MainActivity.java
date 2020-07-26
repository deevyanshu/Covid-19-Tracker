package com.example.covid_19tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.leo.simplearcloader.SimpleArcLoader;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
TextView tvcases,tvrecovered,tvcritical,tvactive,tvtotaldeaths,tvtodaydeaths,tvaffectedcountries,tvtodaycases;
SimpleArcLoader simpleArcLoader;
ScrollView scrollView;
PieChart pieChart;
Button countries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvcases=findViewById(R.id.tvcases);
        tvactive=findViewById(R.id.tvactive);
        tvcritical=findViewById(R.id.tvcritical);
        tvrecovered=findViewById(R.id.tvrecovered);
        tvtotaldeaths=findViewById(R.id.tvtotaldeaths);
        tvtodaydeaths=findViewById(R.id.tvtodaydeaths);
        tvaffectedcountries=findViewById(R.id.tvaffectedcountries);
        tvtodaycases=findViewById(R.id.tvtodaycases);
        countries=findViewById(R.id.btn_track);

        simpleArcLoader=findViewById(R.id.loader);
        pieChart=findViewById(R.id.piechart);
        scrollView=findViewById(R.id.scroll_stats);

        Downloader task=new Downloader();
        task.execute("https://disease.sh/v3/covid-19/all");

        countries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Countries.class);
                startActivity(intent);
            }
        });
    }

    class Downloader extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            simpleArcLoader.start();
        }

        @Override
        protected String doInBackground(String... urls) {
            String res="";
            URL url;
            HttpURLConnection urlConnection=null;
            try{
                url=new URL(urls[0]);
                urlConnection=(HttpURLConnection) url.openConnection();
                InputStream in=urlConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data=reader.read();
                while(data!=-1)
                {
                    char cur=(char)data;
                    res+=cur;
                    data=reader.read();
                }
                return res;
            }catch(Exception e)
            {e.printStackTrace();
            return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                tvcases.setText(jsonObject.getString("cases"));
                tvactive.setText(jsonObject.getString("active"));
                tvcritical.setText(jsonObject.getString("critical"));
                tvtodaycases.setText(jsonObject.getString("todayCases"));
                tvtodaydeaths.setText(jsonObject.getString("todayDeaths"));
                tvrecovered.setText(jsonObject.getString("recovered"));
                tvaffectedcountries.setText(jsonObject.getString("affectedCountries"));
                tvtotaldeaths.setText(jsonObject.getString("deaths"));
                pieChart.addPieSlice(new PieModel("cases",Integer.parseInt(tvcases.getText().toString()),Color.parseColor("#F9A602")));
                pieChart.addPieSlice(new PieModel("active",Integer.parseInt(tvactive.getText().toString()),Color.parseColor("#1520A6")));
                pieChart.addPieSlice(new PieModel("recovered",Integer.parseInt(tvrecovered.getText().toString()),Color.parseColor("#028A0F")));
                pieChart.addPieSlice(new PieModel("deaths",Integer.parseInt(tvtotaldeaths.getText().toString()),Color.parseColor("#D0312D")));
                pieChart.startAnimation();
                simpleArcLoader.stop();
                simpleArcLoader.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);

            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }


}
