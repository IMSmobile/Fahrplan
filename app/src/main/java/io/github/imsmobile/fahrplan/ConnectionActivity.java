package io.github.imsmobile.fahrplan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.gson.Gson;

import ch.schoeb.opendatatransport.model.Connection;

public class ConnectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.label_connection));

        Gson gson = new Gson();
        Intent intent = getIntent();

        Connection connection = gson.fromJson(intent.getStringExtra(SearchResultActivity.CONNECTION), Connection.class);

        TextView text = (TextView) findViewById(R.id.connection);
        text.setText(connection.toString());
    }
}
