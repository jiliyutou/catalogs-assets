package com.highgreen.catalogs.core.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.highgreen.catalogs.core.R;
import com.highgreen.catalogs.core.bean.ParamKVPair;

import java.util.List;

/**
 * Created by ruantihong on 1/27/16.
 */
public class ContactItemAdapter extends ArrayAdapter<ParamKVPair> {

    public ContactItemAdapter(Context context, int resource, List<ParamKVPair> data) {
        super(context, resource, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.contact_item,null);

            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.value =(TextView) convertView.findViewById(R.id.value);
            convertView.setTag(holder);

        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        ParamKVPair item = getItem(position);
        holder.name.setText(item.getParamKey()+" : ");
        holder.value.setText(item.getParamValue());

        return convertView;
    }

    static class ViewHolder{
        TextView name;
        TextView value;
    }
}
