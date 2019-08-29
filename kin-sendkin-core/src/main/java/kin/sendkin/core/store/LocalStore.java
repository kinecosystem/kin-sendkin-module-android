package kin.sendkin.core.store;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class LocalStore {

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    public LocalStore(@NonNull Context context, @NonNull String name) {
        sharedPref = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    public void putString(@NonNull String key, @Nullable String value) {

        editor.putString(key, value);
        editor.apply();
    }

    public void putInt(@NonNull String key, int value) {
        editor.putInt(key, value);
        editor.apply();
    }

    public void putBoolean(@NonNull String key, boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }

    public void putFloat(@NonNull String key, float value) {
        editor.putFloat(key, value);
        editor.apply();
    }

    public void putLong(@NonNull String key, long value) {
        editor.putLong(key, value);
        editor.apply();
    }

    public void removeKey(@NonNull String key) {
        editor.remove(key);
        editor.apply();

    }

    public void clearAll() {
        editor.clear();
        editor.apply();
    }

    public String getString(@NonNull String key, @Nullable String defaultValue) {
        return sharedPref.getString(key, defaultValue);
    }

    public String getString(@NonNull String key) {
        return getString(key, null);
    }

    public int getInt(@NonNull String key, int defaultValue) {
        return sharedPref.getInt(key, defaultValue);
    }

    public boolean getBoolean(@NonNull String key, boolean defaultValue) {
        return sharedPref.getBoolean(key, defaultValue);

    }

    public float getFloat(@NonNull String key, float defaultValue) {
        return sharedPref.getFloat(key, defaultValue);

    }

    public long getLong(@NonNull String key, long defaultValue) {
        return sharedPref.getLong(key, defaultValue);
    }

    public boolean containsKey(@NonNull String key) {
        return sharedPref.contains(key);
    }
}
