package org.kin.sendkin.core.view;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.kin.sendkin.core.model.RecipientContact;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Utils {

    private static Gson gson = new Gson();

    public static String getPasteString(@NonNull ClipboardManager clipboard) {
        ClipData pData = clipboard.getPrimaryClip();
        if (pData != null) {
            ClipData.Item item = pData.getItemAt(0);
            if (item != null && item.getText() != null) {
                return item.getText().toString();
            }
        }
        return null;
    }

    public static void saveTo(@NonNull ClipboardManager clipboard, @NonNull String data) {
        ClipData clip = ClipData.newPlainText("My public address", data);
        clipboard.setPrimaryClip(clip);
    }

    public static @NonNull
    String parseToString(@NonNull RecipientContact contact) {
        return gson.toJson(contact);
    }

    public static @NonNull
    RecipientContact parseFromString(@NonNull String contact) {
        return gson.fromJson(contact, RecipientContact.class);
    }

    public static @NonNull
    String parseToString(@NonNull ArrayList<RecipientContact> contacts) {
        return gson.toJson(contacts);
    }

    public static @NonNull
    ArrayList<RecipientContact> parseContactsFromString(@NonNull String contacts) {
        Type type = new TypeToken<ArrayList<RecipientContact>>() {
        }.getType();
        return gson.fromJson(contacts, type);
    }

    public static String getAddressShortenFormat(@NonNull String publicAddress) {
        if (publicAddress.length() > 4) {
            return "XXX-" + publicAddress.substring(publicAddress.length() - 4);
        }
        return publicAddress;
    }
}
