package com.highgreen.catalogs.core.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.meiya.simon.R;
import com.highgreen.catalogs.core.bean.PersonInfo;

import java.util.List;

/**
 * Created by ruantihong on 1/27/16.
 */
public class PersonAdapter extends ArrayAdapter<PersonInfo> {

    private ViewHolder holder = null;

    public PersonAdapter(Context context, int resource, List<PersonInfo> data) {
        super(context, resource, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.person_item, null);
            holder.contactsListView = (ListView) convertView.findViewById(R.id.contact_listview);
            holder.person_name = (TextView) convertView.findViewById(R.id.person_name);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PersonInfo personInfo = getItem(position);
        holder.person_name.setText(personInfo.getName());
        ContactItemAdapter contactItemAdapter = new ContactItemAdapter(getContext(), 0, personInfo.getContacts());
        holder.contactsListView.setAdapter(contactItemAdapter);
        setListViewHeightBasedOnChildren(holder.contactsListView);
        return convertView;
    }

    private static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1))
                + 20;

        listView.setLayoutParams(params);
    }

    static class ViewHolder {
        TextView person_name;
        ListView contactsListView;
    }
}
