package io.github.imsmobile.fahrplan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.util.List;

import ch.schoeb.opendatatransport.model.Connection;
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
        String from = intent.getStringExtra(MainActivity.FROM_MESSAGE);
        String to = intent.getStringExtra(MainActivity.TO_MESSAGE);
        startSearch(from, to);
    }

    private void startSearch(String from, String to) {
        TextView fromView = (TextView) this.findViewById(R.id.result_from);
        fromView.setText(from);
        TextView toView = (TextView) this.findViewById(R.id.result_to);
        toView.setText(to);

        new ConnectionSearchTask(this).execute(from, to);
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
