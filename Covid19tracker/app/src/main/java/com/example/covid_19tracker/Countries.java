package com.example.covid_19tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.leo.simplearcloader.SimpleArcLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Countries extends AppCompatActivity {
EditText search;
ListView listView;
SimpleArcLoader simpleArcLoader;
public static List<Model> modellist=new ArrayList<>();
Model model;
Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countries);
        search=findViewById(R.id.search);
        listView=findViewById(R.id.listview);
        simpleArcLoader=findViewById(R.id.loader1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Affected Countries");
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Downloader task=new Downloader();
        task.execute("https://disease.sh/v3/covid-19/countries");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(),Details.class).putExtra("pos",position));
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public class Downloader extends AsyncTask<String,Void,String>
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
                Log.i("result",res);
                return res;
            }catch(Exception e)
            {e.printStackTrace();
                Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONArray arr=new JSONArray(s);
                for(int i=0;i<arr.length();i++)
                {
                    JSONObject jsonObject=arr.getJSONObject(i);
                    String countryname=jsonObject.getString("country");
                    String cases=jsonObject.getString("cases");
                    String todaycases=jsonObject.getString("todayCases");
                    String deaths=jsonObject.getString("deaths");
                    String todaydeaths=jsonObject.getString("todayDeaths");
                    String recovered=jsonObject.getString("recovered");
                    String active=jsonObject.getString("active");
                    String critical=jsonObject.getString("critical");
                    JSONObject object=jsonObject.getJSONObject("countryInfo");
                    String flag=object.getString("flag");
                    model=new Model(flag,countryname,cases,todaycases,deaths,todaydeaths,recovered,active,critical);
                    modellist.add(model);
                    }
                adapter=new Adapter(Countries.this,modellist);
                listView.setAdapter(adapter);
                simpleArcLoader.stop();
                simpleArcLoader.setVisibility(View.GONE);

            }catch(Exception e)
            {
                e.printStackTrace();

            }
        }
    }
}
