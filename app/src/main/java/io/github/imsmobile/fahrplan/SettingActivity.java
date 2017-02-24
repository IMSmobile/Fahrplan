package io.github.imsmobile.fahrplan;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;

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
                saveSettings(getString(R.string.setting_key_take_me_home), e.toString());
            }
        });
        inputTakeMeHome.setText(getSettings(getString(R.string.setting_key_take_me_home), ""), false);
        initSettingSelection(R.id.cb_train, R.string.setting_transportation_train);
        initSettingSelection(R.id.cb_tram, R.string.setting_transportation_tram);
        initSettingSelection(R.id.cb_bus, R.string.setting_transportation_bus);
        initSettingSelection(R.id.cb_boat, R.string.setting_transportation_ship);
        initSettingSelection(R.id.cb_firstclass, R.string.setting_classes_first);
        initSettingSelection(R.id.cb_secondclass, R.string.setting_classes_second);
    }

    private void initSettingSelection(@IdRes int checkboxId , @StringRes int keyResId) {
        CheckBox transportationCheckbox = (CheckBox) findViewById(checkboxId);
        final String key = getString(keyResId);
        transportationCheckbox.setChecked(Boolean.valueOf(getSettings(key, String.valueOf(Boolean.TRUE))));
        transportationCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveSettings(key, String.valueOf(isChecked));
            }
        });
    }

    private void saveSettings(String key, String value) {
        SharedPreferences preferences = this.getSharedPreferences(getString(R.string.setting_name), MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private String getSettings(String key, String defaultValue) {
        SharedPreferences preferences = this.getSharedPreferences(getString(R.string.setting_name), MODE_PRIVATE);
        return preferences.getString(key, defaultValue);
    }
}

