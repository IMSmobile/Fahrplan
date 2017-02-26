package io.github.imsmobile.fahrplan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.github.imsmobile.fahrplan.adapter.FavoriteAdapter;
import io.github.imsmobile.fahrplan.adapter.StationAdapter;
import io.github.imsmobile.fahrplan.fragment.TimeDialogFragment;
import io.github.imsmobile.fahrplan.fragment.listener.TimeDialogListener;
import io.github.imsmobile.fahrplan.model.DepartureArrivalModel;
import io.github.imsmobile.fahrplan.model.FavoriteModel;
import io.github.imsmobile.fahrplan.model.FavoriteModelItem;
import io.github.imsmobile.fahrplan.setting.Setting;

public class MainActivity extends AppCompatActivity {
    public static final String FROM_MESSAGE = "io.github.imsmoble.fahrplan.from";
    public static final String TO_MESSAGE = "io.github.imsmoble.fahrplan.to";
    public static final String DATETIME_MESSAGE = "io.github.imsmoble.fahrplan.departure";
    public static final String IS_ARRIVAL_TIME_MESSAGE = "io.github.imsmoble.fahrplan.departureOrArrival";
    public static final String IS_TRAIN_MESSAGE = "io.github.imsmoble.fahrplan.train.active";
    public static final String IS_TRAM_MESSAGE = "io.github.imsmoble.fahrplan.tram.active";
    public static final String IS_BUS_MESSAGE = "io.github.imsmoble.fahrplan.bus.active";
    public static final String IS_SHIP_MESSAGE = "io.github.imsmoble.fahrplan.ship.active";


    private DepartureArrivalModel model = new DepartureArrivalModel();
    private FavoriteModel favorite;
    private Setting setting;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setting = new Setting(this);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        setDepartureTime(new Date());
        registerSearchButton();
        registerOppositeButton();
        registerTakeMeHomeButton();

        AutoCompleteTextView inputFrom = (AutoCompleteTextView) findViewById(R.id.input_from);
        inputFrom.setAdapter(new StationAdapter(this, R.layout.autocomplete_item));

        AutoCompleteTextView inputTo= (AutoCompleteTextView) findViewById(R.id.input_to);
        inputTo.setAdapter(new StationAdapter(this, R.layout.autocomplete_item));

        favorite = new FavoriteModel(this);
        ListView listView = (ListView)findViewById(R.id.list_favorites);
        FavoriteAdapter favoriteadapter = new FavoriteAdapter(this, favorite);
        listView.setAdapter(favoriteadapter);
    }

    private void registerSearchButton() {
        Button buttonOne = (Button) findViewById(R.id.btn_search);
        buttonOne.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                trySearch();
            }
        });
    }

    private void registerOppositeButton() {
        Button buttonOne = (Button) findViewById(R.id.oppositeButton);
        buttonOne.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startChangeDirection();
            }
        });
    }

    private void registerTakeMeHomeButton() {
        Button buttonOne = (Button) findViewById(R.id.btn_take_me_home);
        buttonOne.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fillToWithTakeMeHome();
            }
        });
    }

    private void fillToWithTakeMeHome() {
        String home = setting.getSettings(R.string.setting_key_take_me_home, "");
        if(home.isEmpty()) {
            Toast.makeText(this, this.getResources().getText(R.string.error_missing_take_me_home), Toast.LENGTH_LONG).show();
        } else {
            AutoCompleteTextView toText = (AutoCompleteTextView) findViewById(R.id.input_to);
            toText.setText(home, false);
        }
    }

    private void startChangeDirection() {
        AutoCompleteTextView fromText = (AutoCompleteTextView) findViewById(R.id.input_from);
        String bufferFrom = fromText.getText().toString();

        AutoCompleteTextView toText = (AutoCompleteTextView) findViewById(R.id.input_to);
        String bufferTo = toText.getText().toString();

        fromText.setText(bufferTo, false);
        toText.setText(bufferFrom, false);
    }

    private void trySearch() {
        EditText fromText = (EditText) findViewById(R.id.input_from);
        String from = fromText.getText().toString();

        EditText toText = (EditText) findViewById(R.id.input_to);
        String to = toText.getText().toString();

        if(to.isEmpty() || from.isEmpty()) {
            Toast.makeText(this, this.getResources().getText(R.string.error_search_incomplete), Toast.LENGTH_LONG).show();
        } else {
            startSearchActivity(to, from);
        }

    }
    private void startSearchActivity(String to, String from) {
        Intent intent = new Intent(this, SearchResultActivity.class);
        intent.putExtra(FROM_MESSAGE, from);
        intent.putExtra(TO_MESSAGE, to);
        intent.putExtra(DATETIME_MESSAGE, SimpleDateFormat.getDateTimeInstance().format(model.getSelectedDateTime().toDate()));
        intent.putExtra(IS_ARRIVAL_TIME_MESSAGE, String.valueOf(model.isArrival()));
        intent.putExtra(IS_TRAIN_MESSAGE, setting.getSettings(R.string.setting_transportation_train, String.valueOf(Boolean.TRUE)));
        intent.putExtra(IS_TRAM_MESSAGE, setting.getSettings(R.string.setting_transportation_tram, String.valueOf(Boolean.TRUE)));
        intent.putExtra(IS_BUS_MESSAGE, setting.getSettings(R.string.setting_transportation_bus, String.valueOf(Boolean.TRUE)));
        intent.putExtra(IS_SHIP_MESSAGE, setting.getSettings(R.string.setting_transportation_ship, String.valueOf(Boolean.TRUE)));
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startSettingActivity();
                return true;
            case R.id.action_about:
                startAboutActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void removeFavorite(int position) {
        favorite.remove(position);
    }

    public void activateFavorite(int position) {
        FavoriteModelItem fav = favorite.get(position);
        AutoCompleteTextView fromText = (AutoCompleteTextView) findViewById(R.id.input_from);
        AutoCompleteTextView toText = (AutoCompleteTextView) findViewById(R.id.input_to);
        fromText.setText(fav.getFrom(), false);
        toText.setText(fav.getTo(), false);
    }

    private void startSettingActivity() {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    private void startAboutActivity() {
        Intent intentAbout = new Intent(this, AboutActivity.class);
        startActivity(intentAbout);
    }
    public void showTimePickerDialog(View view) {
        TimeDialogFragment fragment = new TimeDialogFragment();
        fragment.setDepartureArrivalModel(model);
        fragment.setTimeDialogListener(new TimeDialogListener() {
            @Override
            public void timeSelected(DepartureArrivalModel model) {
                MainActivity.this.model = new DepartureArrivalModel(model);
                setDepartureTime(model.getSelectedDateTime().toDate());
            }
        });
        fragment.show(getFragmentManager(), "Dialog Fragment");
    }

    private void setDepartureTime(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        TextView editText = (TextView) findViewById(R.id.label_departure);
        editText.setText(getDepartureTimePrefix()+ df.format(date));
    }

    @NonNull
    private String getDepartureTimePrefix() {
        return getDepartureOrArrivalText()  + " ";
    }

    private String getDepartureOrArrivalText() {
        if (model.isDeparture()) {
            return getResources().getString(R.string.label_departure);
        } else {
            return getResources().getString(R.string.label_arrival);
        }
    }



}
