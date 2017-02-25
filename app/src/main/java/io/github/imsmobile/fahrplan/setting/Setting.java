package io.github.imsmobile.fahrplan.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.StringRes;

import io.github.imsmobile.fahrplan.R;

/**
 * Created by sandro on 25.02.2017.
 */

public class Setting {

    private final Context context;

    public Setting(Context context) {
        this.context = context;
    }

    public boolean getBooleanSettings(@StringRes int key, boolean defaultValue) {
        return Boolean.valueOf(getSettings(key, String.valueOf(defaultValue)));
    }

    public String getSettings(@StringRes int key, String defaultValue) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.setting_name), Context.MODE_PRIVATE);
        return preferences.getString(context.getString(key), defaultValue);
    }
}
