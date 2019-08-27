package kin.sendkin.core.view;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import kin.sendkin.core.model.RecipientContact;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Utils {

    private static Gson gson = new Gson();
    private static final int SHORT_ADDRESS_DIGITS_LEN = 6;

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
        if (publicAddress.length() > SHORT_ADDRESS_DIGITS_LEN) {
            return "XXX-" + publicAddress.substring(publicAddress.length() - SHORT_ADDRESS_DIGITS_LEN);
        }
        return publicAddress;
    }
}
