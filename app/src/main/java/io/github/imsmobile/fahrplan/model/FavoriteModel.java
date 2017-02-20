package io.github.imsmobile.fahrplan.model;

import android.content.SharedPreferences;
import android.support.v4.util.ArraySet;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import io.github.imsmobile.fahrplan.R;

import static android.content.Context.MODE_PRIVATE;

public class FavoriteModel {
    private final Gson gson;
    private final Type favoritesType;
    private final AppCompatActivity context;
    private final SharedPreferences preferences;
    private ArraySet<FavoriteModelItem> favorites;

    public FavoriteModel(AppCompatActivity context) {
        this.gson = new Gson();
        this.favoritesType = new TypeToken<ArraySet<FavoriteModelItem>>() {}.getType();
        this.context = context;
        preferences = context.getSharedPreferences(context.getString(R.string.setting_name), MODE_PRIVATE);

        loadFromSettings();
    }

    public boolean add(String from, String to) {
        FavoriteModelItem f = new FavoriteModelItem(from, to);
        favorites.add(f);
        return saveToSettings();
    }

    public boolean remove(String from, String to) {
        FavoriteModelItem f = new FavoriteModelItem(from, to);
        favorites.remove(f);
        return saveToSettings();
    }

    public boolean contains(String from, String to) {
        FavoriteModelItem f = new FavoriteModelItem(from, to);
        return favorites.contains(f);
    }

    private void loadFromSettings() {
        String json = preferences.getString(context.getString(R.string.setting_favorites), "");
        if(json.isEmpty()) {
            favorites = new ArraySet<>();
        } else {
            favorites = gson.fromJson(json, favoritesType);
        }
    }

    private boolean saveToSettings() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(context.getString(R.string.setting_favorites), gson.toJson(favorites));
        if(editor.commit()) {
            context.invalidateOptionsMenu();
            return true;
        } else {
            return false;
        }
    }
}
