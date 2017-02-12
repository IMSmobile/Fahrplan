package io.github.imsmobile.fahrplan.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import ch.schoeb.opendatatransport.IOpenTransportRepository;
import ch.schoeb.opendatatransport.OpenDataTransportException;
import ch.schoeb.opendatatransport.OpenTransportRepositoryFactory;
import ch.schoeb.opendatatransport.model.Station;
import io.github.imsmobile.fahrplan.Constants;

public class StationAdapter extends ArrayAdapter<String> implements Filterable {

    private List<String> stations;

    public StationAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public int getCount() {
        return stations.size();
    }

    @Override
    public String getItem(int index) {
        return stations.get(index);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResult = new FilterResults();
                if (constraint != null) {
                    stations = autocomplete(constraint.toString());

                    filterResult.values = stations;
                    filterResult.count = stations.size();
                }
                return filterResult;
            }

            private List<String> autocomplete(String query) {
                IOpenTransportRepository repo = OpenTransportRepositoryFactory.CreateOnlineOpenTransportRepository();
                try {
                    List<Station> stations = repo.findStations(query).getStations();
                    List<String> stationNames = new CopyOnWriteArrayList<>();
                    for (Station station : stations) {
                        stationNames.add(station.getName());
                    }
                    return stationNames;
                } catch (OpenDataTransportException e) {
                    Log.e(Constants.LOG, e.getMessage(), e);
                    return Collections.emptyList();
                }
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                }
                else {
                    notifyDataSetInvalidated();
                }
            }};
    }


}