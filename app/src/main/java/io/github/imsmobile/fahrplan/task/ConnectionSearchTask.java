package io.github.imsmobile.fahrplan.task;

import android.os.AsyncTask;
import android.util.Log;

import java.util.Collections;
import java.util.List;

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
        IOpenTransportRepository repo = OpenTransportRepositoryFactory.CreateOnlineOpenTransportRepository();
        try {
            return repo.searchConnections(from, to).getConnections();
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