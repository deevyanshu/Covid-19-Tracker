package com.example.covid_19tracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends ArrayAdapter<Model> {
    Context context;
    List<Model> modellist;
    List<Model> Filtered;
    public Adapter( Context context, List<Model> modellist) {
        super(context, R.layout.list_custom,modellist);
        this.context=context;
        this.modellist=modellist;
        Filtered=modellist;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_custom,null,true);
        TextView countryname=view.findViewById(R.id.tvname);
        ImageView imageView=view.findViewById(R.id.imgflag);
        countryname.setText(Filtered.get(position).getCountry());
        Glide.with(context).load(Filtered.get(position).getFlag()).into(imageView);
        return view;
    }

    public int getCount()
    {
        return Filtered.size();
    }

    @Nullable
    @Override
    public Model getItem(int position) {
        return Filtered.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        Filter filter=new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults=new FilterResults();
                if(constraint==null||constraint.length()==0)
                {
                    filterResults.count=modellist.size();
                    filterResults.values=modellist;
                }else
                {
                    List<Model> result=new ArrayList<>();
                    String search=constraint.toString().toLowerCase();
                    for(Model items:modellist)
                    {
                        if(items.getCountry().toLowerCase().contains(search))
                        {
                            result.add(items);
                        }
                        filterResults.count=result.size();
                        filterResults.values=result;
                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                Filtered=(List<Model>)results.values;
                Countries.modellist=(List<Model>)results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }
}
