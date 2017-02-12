package io.github.imsmobile.fahrplan.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
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
import ch.schoeb.opendatatransport.model.Journey;
import ch.schoeb.opendatatransport.model.Section;
import io.github.imsmobile.fahrplan.R;

public class ConnectionAdapter extends BaseAdapter {

    private Context context;
    private List<Connection> connections;
    private static final DateFormat DF = DateFormat.getTimeInstance(DateFormat.SHORT);
    private static final String TRAM_ICON = "\uD83D\uDE8B";
    private static final String BUS_ICON = "\uD83D\uDE8C";
    private static final String ARROW = "\u279C";

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

        Connection connection = (Connection) getItem(position);

        fillConnectionItem(convertView, connection);

        return convertView;
    }

    private void fillConnectionItem(View view, Connection connection) {
        setViewText(view, R.id.item_from, connection.getFrom().getStation().getName());
        setViewText(view, R.id.item_to, connection.getTo().getStation().getName());
        setViewText(view, R.id.departure_time, formatTimestamp(connection.getFrom().getDepartureTimestamp()));
        setViewText(view, R.id.duration, prettifyDuration(connection.getDuration()));
        setViewText(view, R.id.arrival_time, formatTimestamp(Long.parseLong(connection.getTo().getArrivalTimestamp())));
        setViewText(view, R.id.journey_stops, buildVehicles(connection));
    }

    @NonNull
    private String buildVehicles(Connection connection) {
        StringBuilder vehicles = new StringBuilder();
        for (Section section: connection.getSections()) {
            Journey journey = section.getJourney();
            if(journey != null) {
                String category =  journey.getCategory();
                switch(category) {
                    case "S":
                        vehicles.append(category);
                        vehicles.append(journey.getNumber());
                        break;
                    case "T":
                        vehicles.append(TRAM_ICON);
                        vehicles.append(journey.getNumber());
                        break;
                    case "BUS":
                        vehicles.append(BUS_ICON);
                        vehicles.append(journey.getNumber());
                        break;
                    default:
                        vehicles.append(category);
                }
                vehicles.append(ARROW);
            }
        }
        vehicles.deleteCharAt(vehicles.length()-1);
        return vehicles.toString();
    }

    private void setViewText(View convertView, int id, String str) {
        TextView fromTextView = (TextView) convertView.findViewById(id);
        fromTextView.setText(str);
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

    private String formatTimestamp(Number timestamp) {
        return DF.format(new Date(timestamp.longValue()*1000));
    }

}
