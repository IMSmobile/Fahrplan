package io.github.imsmobile.fahrplan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;

import ch.schoeb.opendatatransport.model.Connection;

public class ConnectionActivity extends AppCompatActivity {

    private Connection connection;
    private ShareActionProvider shareAction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.label_connection));

        Gson gson = new Gson();
        Intent intent = getIntent();

        connection = gson.fromJson(intent.getStringExtra(SearchResultActivity.CONNECTION), Connection.class);

        TextView text = (TextView) findViewById(R.id.connection);
        text.setText(connection.toString());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.share, menu);

        MenuItem item = menu.findItem(R.id.action_share);
        shareAction = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        Intent send = new Intent();
        send.setAction(Intent.ACTION_SEND);
        send.putExtra(Intent.EXTRA_TEXT, connection.toString());
        send.setType("text/plain");
        if(shareAction != null) {
            shareAction.setShareIntent(send);
        }

        return super.onCreateOptionsMenu(menu);
    }
}
