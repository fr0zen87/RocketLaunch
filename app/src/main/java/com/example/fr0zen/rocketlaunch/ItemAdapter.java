package com.example.fr0zen.rocketlaunch;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ItemAdapter extends ArrayAdapter<ListItem> {

    private LayoutInflater inflater;
    private int layout;
    private List<ListItem> items;

    public ItemAdapter(@NonNull Context context, int resource, @NonNull List<ListItem> objects) {
        super(context, resource, objects);
        this.layout = resource;
        this.items = objects;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = inflater.inflate(this.layout, parent, false);
        ListItem item = items.get(position);

        ImageView missionImage = view.findViewById(R.id.missionImage);
        missionImage.setImageBitmap(item.getMissionImage());

        TextView rocketName = view.findViewById(R.id.rocketName);

        TextView launchDate = view.findViewById(R.id.launchDate);
        Date date = new Date(item.getLaunchDate() * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss zzz");

        TextView details = view.findViewById(R.id.details);

        rocketName.setText(String.format("Rocket name: %s", item.getRocketName()));
        launchDate.setText(String.format("Launch date: %s", sdf.format(date)));
        details.setText(String.format("Details: %s", item.getDetails()));

        return view;
    }
}
