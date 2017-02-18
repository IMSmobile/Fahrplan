package io.github.imsmobile.fahrplan.task;

import android.os.AsyncTask;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ch.schoeb.opendatatransport.IOpenTransportRepository;
import ch.schoeb.opendatatransport.OpenDataTransportException;
import ch.schoeb.opendatatransport.OpenTransportRepositoryFactory;
import ch.schoeb.opendatatransport.model.Connection;
import io.github.imsmobile.fahrplan.Constants;
import io.github.imsmobile.fahrplan.SearchResultActivity;

public class ConnectionSearchTask extends AsyncTask<String,Void,List<Connection>> {


    private final SearchResultActivity activity;


    public ConnectionSearchTask(SearchResultActivity activity) {
        this.activity = activity;
    }

    @Override
    protected List<Connection> doInBackground(String... params) {
        String from = params[0];
        String to = params[1];
        boolean isArrival = Boolean.parseBoolean(params[2]);
        Date dateTime = getDate(params[3]);
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(dateTime);
        String time =new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(dateTime);
        IOpenTransportRepository repo = OpenTransportRepositoryFactory.CreateOnlineOpenTransportRepository();
        try {
            return repo.searchConnections(from, to, null, date, time, isArrival).getConnections();
        } catch (OpenDataTransportException e) {
            Log.e(Constants.LOG, e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    private Date getDate(String date) {
        try {
            return SimpleDateFormat.getDateTimeInstance().parse(date);
        } catch (ParseException e) {
            throw new IllegalStateException(e);
        }
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        activity.startProgressDialog();
    }

    @Override
    protected void onPostExecute(List<Connection> connections) {
        super.onPostExecute(connections);
        activity.stopProcessDialog();
        activity.setResult(connections);
    }
}