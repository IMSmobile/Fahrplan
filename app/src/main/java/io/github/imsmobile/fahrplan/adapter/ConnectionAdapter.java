package io.github.imsmobile.fahrplan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import ch.schoeb.opendatatransport.model.Connection;
import io.github.imsmobile.fahrplan.R;

public class ConnectionAdapter extends BaseAdapter {

    private Context context;
    private List<Connection> connections;

    public ConnectionAdapter(Context context, List<Connection> connections) {
        this.context = context;
        this.connections = connections;
    }

    @Override
    public int getCount() {
        return connections.size();
    }

    @Override
    public Object getItem(int position) {
        return connections.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.search_item, parent, false);
        }

        DateFormat df = DateFormat.getTimeInstance(DateFormat.SHORT);
        Connection connection = (Connection) getItem(position);
        Number departure_ts = connection.getFrom().getDepartureTimestamp();
        Number arrival_ts = Long.parseLong(connection.getTo().getArrivalTimestamp());
        Date departure = new Date(departure_ts.longValue()*1000);
        Date arrival = new Date(arrival_ts.longValue()*1000);

        String duration = prettifyDuration(connection.getDuration());

        TextView fromTextView = (TextView) convertView.findViewById(R.id.item_from);
        fromTextView.setText(connection.getFrom().getStation().getName());

        TextView toTextView = (TextView) convertView.findViewById(R.id.item_to);
        toTextView.setText(connection.getTo().getStation().getName());

        TextView departureTextView = (TextView) convertView.findViewById(R.id.departure_time);
        departureTextView.setText(df.format(departure));

        TextView durationTextView = (TextView) convertView.findViewById(R.id.duration);
        durationTextView.setText(duration);

        TextView arrivalTextView = (TextView) convertView.findViewById(R.id.arrival_time);
        arrivalTextView.setText(df.format(arrival));

        return convertView;
    }

    private String prettifyDuration(String duration) {
        PeriodFormatter formatter = new PeriodFormatterBuilder()
                .appendDays().appendSuffix("d")
                .appendHours().appendSuffix(":")
                .appendMinutes().appendSuffix(":")
                .appendSeconds().toFormatter();

        Period p = formatter.parsePeriod(duration);

        String dayshort = context.getResources().getString(R.string.day_short);

        if(p.getDays() > 0) {
            return String.format("%d"+ dayshort + " %dh %dm", p.getDays(), p.getHours(), p.getMinutes());
        } else if (p.getHours() > 0) {
            return String.format("%dh %dm", p.getHours(), p.getMinutes());
        } else {
            return String.format("%dm", p.getMinutes());
        }
    }


}
