package com.example.xinyue.helloworld.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xinyue.helloworld.AllShareApplication;
import com.example.xinyue.helloworld.R;

import org.w3c.dom.Text;

import java.util.List;

import imageCache.AsyncImageLoader;

/**
 * Created by xinyue on 5/18/15.
 */
public class PlanAdapter extends ArrayAdapter<PlanItem> {
    private LayoutInflater inflater;
    private int res;

    public PlanAdapter(Context context, int resource,
                            List<PlanItem> objects) {
        super(context, resource, objects);
        res = resource;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(res, null);
            holder.plan_avatar = (ImageView) convertView
                    .findViewById(R.id.plan_avatar);
            holder.plan_name = (TextView) convertView
                    .findViewById(R.id.plan_name);
            holder.plan_title = (TextView) convertView
                    .findViewById(R.id.plan_title);
            holder.plan_current_size = (TextView) convertView
                    .findViewById(R.id.plan_current_size);
            holder.plan_start_date = (TextView) convertView
                    .findViewById(R.id.plan_start_date);
            holder.plan_total_size = (TextView) convertView
                    .findViewById(R.id.plan_total_size);
            holder.plan_description = (TextView) convertView
                    .findViewById(R.id.plan_description);
            holder.plan_destination = (TextView) convertView
                    .findViewById(R.id.plan_destination);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PlanItem les = getItem(position);
        holder.plan_name.setText(les.getName());
        holder.plan_title.setText(les.getTitle());
        holder.plan_total_size.setText(Integer.toString(les.getGroupSize()));
        holder.plan_current_size.setText(Integer.toString(les.getGroupSize()));
        holder.plan_destination.setText(Integer.toString(les.getGroupSize()));
        holder.plan_start_date.setText(les.getDateFrom());
        holder.plan_description.setText(les.getDescription());
        Drawable draw = getDrawable(les.getAvatar());

        if (draw != null)
            holder.plan_avatar.setImageDrawable(draw);
        else{
            holder.plan_avatar.setImageResource(R.drawable.noimage);
        }
//        Uri avatarURI = Uri.parse(les.getAvatar());
//        holder.plan_avatar.setImageURI(null);
//        holder.plan_avatar.setImageURI(avatarURI);
        return convertView;
    }

    private class ViewHolder {
        TextView plan_name;
        TextView plan_title;
        ImageView plan_avatar;
        TextView plan_destination;
        TextView plan_current_size;
        TextView plan_total_size;
        TextView plan_description;
        TextView plan_start_date;
    }

    Drawable getDrawable(String imageUrl) {
        AsyncImageLoader asyncImageLoader = new AsyncImageLoader(this.getContext());
        Drawable drawable = asyncImageLoader.loadDrawable(
                imageUrl, new AsyncImageLoader.ImageCallback() {
                    @Override
                    public void imageLoaded(Drawable imageDrawable,
                                            String imageUrl) {
                        notifyDataSetChanged();
                    }
                });
        return drawable;
    }
}
