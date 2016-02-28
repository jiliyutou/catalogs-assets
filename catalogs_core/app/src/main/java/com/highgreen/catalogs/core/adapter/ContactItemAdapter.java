package com.highgreen.catalogs.core.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.highgreen.catalogs.core.R;
import com.highgreen.catalogs.core.bean.ParamKVPair;

import java.util.List;

/**
 * Created by ruantihong on 1/27/16.
 */
public class ContactItemAdapter extends ArrayAdapter<ParamKVPair> {

    private Context mContext;

    public ContactItemAdapter(Context context, int resource, List<ParamKVPair> data) {
        super(context, resource, data);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.contact_item, null);

            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.value = (TextView) convertView.findViewById(R.id.value);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ParamKVPair item = getItem(position);
        holder.name.setText(item.getParamKey() + " : ");
        holder.value.setText(item.getParamValue());

        if (item.getParamKey().equalsIgnoreCase("email")) {
            holder.value.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("message/rfc822");
                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{item.getParamValue()});
                    try {
                        mContext.startActivity(Intent.createChooser(i, "Send email..."));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(mContext, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        if (item.getParamKey().equalsIgnoreCase("phone") || item.getParamKey().equalsIgnoreCase("mobile")) {
            holder.value.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + item.getParamValue()));
                    mContext.startActivity(intent);
                }
            });
        }

        return convertView;
    }

    static class ViewHolder {
        TextView name;
        TextView value;
    }
}
