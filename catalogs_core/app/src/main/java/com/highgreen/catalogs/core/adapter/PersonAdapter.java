package com.highgreen.catalogs.core.adapter;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ImageView;

import com.highgreen.catalogs.core.MainApplication;
import com.highgreen.catalogs.core.R;
import com.highgreen.catalogs.core.bean.Person;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

/**
 * Created by ruantihong on 1/27/16.
 */
public class PersonAdapter extends ArrayAdapter<Person> {
    private DisplayImageOptions options;
    private ViewHolder holder = null;

    public PersonAdapter(Context context, int resource, List<Person> data) {
        super(context, resource, data);
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .displayer(new RoundedBitmapDisplayer(10))
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .build();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.person_item, null);
            holder.contactsListView = (ListView) convertView.findViewById(R.id.contact_listview);
            holder.person_name = (TextView) convertView.findViewById(R.id.person_name);
            holder.person_photo = (ImageView) convertView.findViewById(R.id.person_photo);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Person person = getItem(position);
        holder.person_name.setText(person.getName());
        ContactItemAdapter contactItemAdapter = new ContactItemAdapter(getContext(), 0, person.getContacts());
        holder.contactsListView.setAdapter(contactItemAdapter);
        setListViewHeightBasedOnChildren(holder.contactsListView);

        if(person.getPhoto() != null && !person.getPhoto().equals("")) {
            String photoPath = MainApplication.CONTACT_UPYUN_URL + person.getPhoto();
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(photoPath, holder.person_photo, options);
        } else {
            Log.i("", "nulllllll");

            ViewGroup.LayoutParams layoutParams = holder.person_photo.getLayoutParams();
            layoutParams.height = 0;
            holder.person_photo.setLayoutParams(layoutParams);
        }

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
        ImageView person_photo;
        ListView contactsListView;
    }
}
