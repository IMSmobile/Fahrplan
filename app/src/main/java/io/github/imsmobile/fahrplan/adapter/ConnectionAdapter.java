package io.github.imsmobile.fahrplan.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.common.base.Joiner;

import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ch.schoeb.opendatatransport.model.Connection;
import ch.schoeb.opendatatransport.model.Journey;
import ch.schoeb.opendatatransport.model.Section;
import io.github.imsmobile.fahrplan.R;


public class ConnectionAdapter extends BaseAdapter {

    private final Context context;
    private final List<Connection> connections;
    private static final DateFormat DF = DateFormat.getTimeInstance(DateFormat.SHORT);
    private static final String TRAM_ICON = "\uD83D\uDE8B";
    private static final String BUS_ICON = "\uD83D\uDE8C";
    private static final String ARROW = "\u279C";
    private static final char OCCUPANCY = '\u2581';

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
        setViewText(view, R.id.occupancy, buildOccupancy(connection));
        setViewText(view, R.id.arrival_time, formatTimestamp(Long.parseLong(connection.getTo().getArrivalTimestamp())));
        setViewText(view, R.id.journey_stops, buildVehicles(connection));
    }

    private String buildOccupancy(Connection connection) {
        StringBuilder occupancy = new StringBuilder();
        if((connection.getCapacity1st() != null) && Boolean.valueOf(getSettings(context.getString(R.string.setting_classes_first), String.valueOf(true)))) {
            occupancy.append(" 1.");
            occupancy.append((char) (OCCUPANCY + connection.getCapacity1st().intValue()*2));
        }
        if((connection.getCapacity2nd() != null) && Boolean.valueOf(getSettings(context.getString(R.string.setting_classes_second), String.valueOf(true)))) {
            occupancy.append(" 2.");
            occupancy.append((char) (OCCUPANCY + connection.getCapacity2nd().intValue()*2));
        }
        return occupancy.toString();
    }

    @NonNull
    private String buildVehicles(Connection connection) {
        List<String> journeys = getJourneys(connection);
        return Joiner.on(ARROW).join(journeys);
    }

    private List<String> getJourneys(Connection connection) {
        List<String> journeys = new ArrayList<>();
        for (Section section: connection.getSections()) {
            Journey journey = section.getJourney();
            if(journey != null) {
                String category =  journey.getCategory();
                switch(category) {
                    case "S":
                        journeys.add(category + journey.getNumber());
                        break;
                    case "T":
                        journeys.add(TRAM_ICON + journey.getNumber());
                        break;
                    case "BUS":
                    case "NFB":
                        journeys.add(BUS_ICON + journey.getNumber());
                        break;
                    default:
                        journeys.add(category);
                }
            }
        }
        return journeys;
    }

    private String prettifyDuration(String duration) {
        PeriodFormatter formatter = new PeriodFormatterBuilder()
                .appendDays().appendSuffix("d")
                .appendHours().appendSuffix(":")
                .appendMinutes().appendSuffix(":")
                .appendSeconds().toFormatter();

        Period p = formatter.parsePeriod(duration);

        String day = context.getResources().getString(R.string.day_short);

        if(p.getDays() > 0) {
            return String.format("%d"+ day + " %dh %dm", p.getDays(), p.getHours(), p.getMinutes());
        } else if (p.getHours() > 0) {
            return String.format(Locale.getDefault(), "%dh %dm", p.getHours(), p.getMinutes());
        } else {
            return String.format(Locale.getDefault(), "%dm", p.getMinutes());
        }
    }

    private String formatTimestamp(Number timestamp) {
        return DF.format(new Date(timestamp.longValue()*1000));
    }

    private void setViewText(View convertView, int id, String str) {
        TextView fromTextView = (TextView) convertView.findViewById(id);
        fromTextView.setText(str);
    }

    private String getSettings(String key, String defaultValue) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.setting_name), Context.MODE_PRIVATE);
        return preferences.getString(key, defaultValue);
    }
}
