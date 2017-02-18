package io.github.imsmobile.fahrplan;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.widget.AutoCompleteTextView;

import io.github.imsmobile.fahrplan.adapter.StationAdapter;
import io.github.imsmobile.fahrplan.listener.TextWatcherAdapter;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.settings_title));

        AutoCompleteTextView inputTakeMeHome = (AutoCompleteTextView) findViewById(R.id.input_take_me_home);
        inputTakeMeHome.setAdapter(new StationAdapter(this, R.layout.autocomplete_item));
        inputTakeMeHome.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable e) {
                saveSettings(e.toString());
            }
        });
        inputTakeMeHome.setText(getSettings());
    }

    private void saveSettings(String home) {
        SharedPreferences preferences = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(getString(R.string.setting_key_take_me_home), home);
        editor.apply();
    }

    private String getSettings() {
        SharedPreferences preferences = this.getPreferences(Context.MODE_PRIVATE);
        return preferences.getString(getString(R.string.setting_key_take_me_home), "");
    }
}

