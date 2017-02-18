package io.github.imsmobile.fahrplan;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.github.imsmobile.fahrplan.adapter.StationAdapter;
import io.github.imsmobile.fahrplan.fragment.TimePickerFragment;

public class MainActivity extends AppCompatActivity {
    public static final String FROM_MESSAGE = "io.github.imsmoble.fahrplan.from";
    public static final String TO_MESSAGE = "io.github.imsmoble.fahrplan.to";
    public static final String DEPARTURE_MESSAGE = "io.github.imsmoble.fahrplan.departure";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        setDepartureTime(new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(new Date()));
        registerSearchButton();
        registerOppositeButton();
        registerTakeMeHomeButton();

        AutoCompleteTextView inputFrom = (AutoCompleteTextView) findViewById(R.id.input_from);
        inputFrom.setAdapter(new StationAdapter(this, R.layout.autocomplete_item));

        AutoCompleteTextView inputTo= (AutoCompleteTextView) findViewById(R.id.input_to);
        inputTo.setAdapter(new StationAdapter(this, R.layout.autocomplete_item));

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
        SharedPreferences preferences = this.getSharedPreferences(getString(R.string.setting_name), MODE_PRIVATE);
        String home = preferences.getString(getString(R.string.setting_key_take_me_home), "");
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

        TextView departureTimeText = (TextView) findViewById(R.id.label_departure);
        String departureTime = departureTimeText.getText().toString().substring(getDepartureTimePrefix().length());

        if(to.isEmpty() || from.isEmpty()) {
            Toast.makeText(this, this.getResources().getText(R.string.error_search_incomplete), Toast.LENGTH_LONG).show();
        } else {
            startSearchActivity(to, from, departureTime);
        }

    }
    private void startSearchActivity(String to, String from, String departureTime) {
        Intent intent = new Intent(this, SearchResultActivity.class);
        intent.putExtra(FROM_MESSAGE, from);
        intent.putExtra(TO_MESSAGE, to);
        intent.putExtra(DEPARTURE_MESSAGE, departureTime);
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

    private void startSettingActivity() {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    private void startAboutActivity() {
        Intent intentAbout = new Intent(this, AboutActivity.class);
        startActivity(intentAbout);
    }
    public void showTimePickerDialog(View view) {
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setOnTimeSetListener(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                setDepartureTime(String.format(Locale.ENGLISH, "%02d", hourOfDay) + ":" + String.format(Locale.ENGLISH, "%02d", minute));
            }
        });
        fragment.show(getSupportFragmentManager(), "timePicker");
    }

    private void setDepartureTime(String departureTime) {
        TextView editText = (TextView) findViewById(R.id.label_departure);
        editText.setText(getDepartureTimePrefix()+ departureTime);
    }

    @NonNull
    private String getDepartureTimePrefix() {
        return getResources().getString(R.string.label_departure) + " ";
    }

}
