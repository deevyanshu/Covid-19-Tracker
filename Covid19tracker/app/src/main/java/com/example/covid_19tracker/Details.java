package com.example.covid_19tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class Details extends AppCompatActivity {
int position;
TextView country,cases,todaycases,deaths,todaydeaths,recovered,active,critical;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        country=findViewById(R.id.tvcountry);
        cases=findViewById(R.id.tvcases);
        todaycases=findViewById(R.id.tvtodaycases);
        deaths=findViewById(R.id.tvdeaths);
        todaydeaths=findViewById(R.id.tvtodaydeaths);
        recovered=findViewById(R.id.tvrecovered);
        active=findViewById(R.id.tvactive);
        critical=findViewById(R.id.tvcritical);
        position=getIntent().getIntExtra("pos",0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Details of"+" "+Countries.modellist.get(position).getCountry());
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        country.setText(Countries.modellist.get(position).getCountry());
        cases.setText(Countries.modellist.get(position).getCases());
        todaycases.setText(Countries.modellist.get(position).getTodaycases());
        deaths.setText(Countries.modellist.get(position).getDeaths());
        todaydeaths.setText(Countries.modellist.get(position).getTodaydeaths());
        recovered.setText(Countries.modellist.get(position).getRecovered());
        active.setText(Countries.modellist.get(position).getActivecases());
        critical.setText(Countries.modellist.get(position).getCriticalcases());
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
