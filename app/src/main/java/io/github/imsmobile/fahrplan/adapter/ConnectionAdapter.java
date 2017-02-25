package io.github.imsmobile.fahrplan.adapter;

import android.content.Context;
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
import io.github.imsmobile.fahrplan.output.JourneyPrinter;
import io.github.imsmobile.fahrplan.output.Occupancy;
import io.github.imsmobile.fahrplan.R;
import io.github.imsmobile.fahrplan.SearchResultActivity;


public class ConnectionAdapter extends BaseAdapter {

    private final Context context;
    private final List<Connection> connections;
    private static final DateFormat DF = DateFormat.getTimeInstance(DateFormat.SHORT);
    private final Occupancy occupancy;

    public ConnectionAdapter(Context context, List<Connection> connections) {
        this.context = context;
        this.connections = connections;
        this.occupancy = new Occupancy(context);
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

        final Connection connection = (Connection) getItem(position);

        fillConnectionItem(convertView, connection);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchResultActivity s = (SearchResultActivity) context;
                s.startConnectionActivity(connection);
            }
        });

        return convertView;
    }

    private void fillConnectionItem(View view, Connection connection) {
        setViewText(view, R.id.item_from, connection.getFrom().getStation().getName());
        setViewText(view, R.id.item_to, connection.getTo().getStation().getName());
        setViewText(view, R.id.departure_time, formatTimestamp(connection.getFrom().getDepartureTimestamp()));
        setViewText(view, R.id.duration, prettifyDuration(connection.getDuration()));
        setViewText(view, R.id.occupancy, occupancy.buildOccupancy(connection));
        setViewText(view, R.id.arrival_time, formatTimestamp(Long.parseLong(connection.getTo().getArrivalTimestamp())));
        setViewText(view, R.id.journey_stops, buildVehicles(connection));
    }


    @NonNull
    private String buildVehicles(Connection connection) {
        List<String> journeys = getJourneys(connection);
        String iconRightArrow = context.getString(R.string.icon_rightward_arrow);
        return Joiner.on(iconRightArrow).join(journeys);
    }

    private List<String> getJourneys(Connection connection) {
        List<String> journeys = new ArrayList<>();
        JourneyPrinter journeyPrinter = new JourneyPrinter();
        for (Section section: connection.getSections()) {
            Journey journey = section.getJourney();
            if (journey != null) {
                String journeryText = journeyPrinter.getJourneryText(context, journey);
                journeys.add(journeryText);
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

}
