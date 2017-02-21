package io.github.imsmobile.fahrplan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ch.schoeb.opendatatransport.model.Connection;
import ch.schoeb.opendatatransport.model.ConnectionQuery;
import io.github.imsmobile.fahrplan.adapter.ConnectionAdapter;
import io.github.imsmobile.fahrplan.task.ConnectionSearchTask;
import io.github.imsmobile.fahrplan.ui.ProgressDialogUI;

public class SearchResultActivity extends AppCompatActivity {

    private ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.search_result_title));

        Intent intent = getIntent();
        ConnectionQuery query = new ConnectionQuery();
        query.setFrom(intent.getStringExtra(MainActivity.FROM_MESSAGE));
        query.setTo(intent.getStringExtra(MainActivity.TO_MESSAGE));
        query.setArrivalTime(Boolean.parseBoolean(intent.getStringExtra(MainActivity.IS_ARRIVAL_TIME_MESSAGE)));
        Date dateTime = getDate(intent.getStringExtra(MainActivity.DATETIME_MESSAGE));
        query.setDate(new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(dateTime));
        query.setTime(new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(dateTime));
        query.setTrain(Boolean.parseBoolean(intent.getStringExtra(MainActivity.IS_TRAIN_MESSAGE)));
        query.setTram(Boolean.parseBoolean(intent.getStringExtra(MainActivity.IS_TRAM_MESSAGE)));
        query.setBus(Boolean.parseBoolean(intent.getStringExtra(MainActivity.IS_BUS_MESSAGE)));
        query.setShip(Boolean.parseBoolean(intent.getStringExtra(MainActivity.IS_SHIP_MESSAGE)));
        startSearch(query);
    }

    private Date getDate(String date) {
        try {
            return SimpleDateFormat.getDateTimeInstance().parse(date);
        } catch (ParseException e) {
            throw new IllegalStateException(e);
        }
    }

    private void startSearch(ConnectionQuery query) {
        TextView fromView = (TextView) this.findViewById(R.id.result_from);
        fromView.setText(query.getFrom());
        TextView toView = (TextView) this.findViewById(R.id.result_to);
        toView.setText(query.getTo());
        new ConnectionSearchTask(this).execute(query);
    }


    public void setResult(List<Connection> connections) {
        ListView listView = (ListView)findViewById(R.id.result_list);

        ConnectionAdapter adapter = new ConnectionAdapter(this, connections);
        listView.setAdapter(adapter);
    }

    public void startProgressDialog() {
        dialog = new ProgressDialogUI(this);
        dialog.show();
    }

    public void stopProcessDialog() {
        dialog.dismiss();
    }
}
