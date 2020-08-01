package com.ciit.freelanceplus.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ciit.freelanceplus.R;

import java.util.List;


/**
 * Created by Rp on 6/14/2016.
 */
public class RecycleviewAdapter_navigation extends BaseAdapter {

    Context mContext;
    List<String> models;
    LayoutInflater layoutInflater;



    public RecycleviewAdapter_navigation(Context mContext, List<String> models) {

        this.mContext = mContext;
        this.models = models;
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public Object getItem(int position) {
        return models.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View root;
        if(convertView == null)
        {
            root = layoutInflater.inflate(R.layout.item_navigation_list, null);
        }else{
            root = convertView;
        }
        TextView name;
        name =  root.findViewById(R.id.name);
        String list = models.get(position);
        name.setText(list);

        return root;
    }
}


