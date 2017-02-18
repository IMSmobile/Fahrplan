package io.github.imsmobile.fahrplan.task;

import android.os.AsyncTask;
import android.util.Log;

import java.util.Collections;
import java.util.List;

import ch.schoeb.opendatatransport.IOpenTransportRepository;
import ch.schoeb.opendatatransport.OpenDataTransportException;
import ch.schoeb.opendatatransport.OpenTransportRepositoryFactory;
import ch.schoeb.opendatatransport.model.Connection;
import ch.schoeb.opendatatransport.model.ConnectionQuery;
import io.github.imsmobile.fahrplan.Constants;
import io.github.imsmobile.fahrplan.SearchResultActivity;

public class ConnectionSearchTask extends AsyncTask<ConnectionQuery,Void,List<Connection>> {


    private final SearchResultActivity activity;


    public ConnectionSearchTask(SearchResultActivity activity) {
        this.activity = activity;
    }

    @Override
    protected List<Connection> doInBackground(ConnectionQuery... params) {
        ConnectionQuery query =  params[0];
        String from = query.getFrom();
        String to = query.getTo();
        boolean isArrival = query.isArrivalTime();
        boolean isTrain = query.isTrain();
        boolean isTram = query.isTram();
        boolean isBus = query.isBus();
        boolean isShip = query.isShip();

        String date = query.getDate();
        String time =query.getTime();
        IOpenTransportRepository repo = OpenTransportRepositoryFactory.CreateOnlineOpenTransportRepository();
        try {
            return repo.searchConnections(from, to, null, date, time, isArrival).getConnections();
        } catch (OpenDataTransportException e) {
            Log.e(Constants.LOG, e.getMessage(), e);
            return Collections.emptyList();
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