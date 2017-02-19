package io.github.imsmobile.fahrplan;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArraySet;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import ch.schoeb.opendatatransport.model.Connection;
import io.github.imsmobile.fahrplan.adapter.ConnectionAdapter;
import io.github.imsmobile.fahrplan.task.ConnectionSearchTask;
import io.github.imsmobile.fahrplan.ui.ProgressDialogUI;

public class SearchResultActivity extends AppCompatActivity {

    private ProgressDialog dialog;
    private String from;
    private String to;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.search_result_title));

        Intent intent = getIntent();
        from = intent.getStringExtra(MainActivity.FROM_MESSAGE);
        to = intent.getStringExtra(MainActivity.TO_MESSAGE);
        String arrivalTime = intent.getStringExtra(MainActivity.IS_ARRIVAL_TIME_MESSAGE);
        String dateTime = intent.getStringExtra(MainActivity.DATETIME_MESSAGE);
        startSearch(from, to, arrivalTime, dateTime);
    }

    private void startSearch(String from, String to, String arrivalTime, String dateTime) {
        TextView fromView = (TextView) this.findViewById(R.id.result_from);
        fromView.setText(from);
        TextView toView = (TextView) this.findViewById(R.id.result_to);
        toView.setText(to);
        new ConnectionSearchTask(this).execute(from, to, arrivalTime, dateTime);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if(isInFavorites()) {
            inflater.inflate(R.menu.favorite_on, menu);
        } else {
            inflater.inflate(R.menu.favorite_off, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite_add:
                addToFavorites();
                return true;
            case R.id.action_favorite_remove:
                removeFromFavorites();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("CommitPrefEdits")
    private void addToFavorites() {
        Gson gson = new Gson();
        Type favoritesType = new TypeToken<ArraySet<Pair<String,String>>>() {}.getType();

        Pair favorite = new Pair<>(from, to);
        ArraySet<Pair<String,String>> favorites = new ArraySet<>();

        SharedPreferences preferences = this.getSharedPreferences(getString(R.string.setting_name), MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String json = preferences.getString(getString(R.string.setting_favorites), "");
        if(!json.isEmpty()) {
            favorites = gson.fromJson(json, favoritesType);
        }
        favorites.add(favorite);
        editor.putString(getString(R.string.setting_favorites), gson.toJson(favorites));
        editor.commit();
        invalidateOptionsMenu();
    }

    @SuppressLint("CommitPrefEdits")
    private void removeFromFavorites() {
        Gson gson = new Gson();
        Type favoritesType = new TypeToken<ArraySet<Pair<String,String>>>() {}.getType();

        Pair favorite = new Pair<>(from, to);

        SharedPreferences preferences = this.getSharedPreferences(getString(R.string.setting_name), MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String json = preferences.getString(getString(R.string.setting_favorites), "");
        if(!json.isEmpty()) {
            ArraySet<Pair<String,String>> favorites = gson.fromJson(json, favoritesType);
            for(Pair<String,String> f : favorites) {
                if (f.toString().equalsIgnoreCase(favorite.toString())) {
                    favorites.remove(f);
                    break;
                }
            }
            editor.putString(getString(R.string.setting_favorites), gson.toJson(favorites));
            editor.commit();
            invalidateOptionsMenu();
        }
    }

    private boolean isInFavorites() {
        Gson gson = new Gson();
        Type favoritesType = new TypeToken<ArraySet<Pair<String,String>>>() {}.getType();

        Pair favorite = new Pair<>(from, to);

        SharedPreferences preferences = this.getSharedPreferences(getString(R.string.setting_name), MODE_PRIVATE);
        String json = preferences.getString(getString(R.string.setting_favorites), "");
        if(json.isEmpty()) {
            return false;
        } else {
            ArraySet<Pair<String,String>> favorites = gson.fromJson(json, favoritesType);
            for(Pair<String,String> f : favorites) {
                if (f.toString().equalsIgnoreCase(favorite.toString())) {
                    return true;
                }
            }
        }
        return false;
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
