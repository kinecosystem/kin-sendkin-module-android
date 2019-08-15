package org.kin.sendkin.core.view;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.support.annotation.NonNull;

public class Utils {

    public static String getPasteString(@NonNull ClipboardManager clipboard){
        ClipData pData = clipboard.getPrimaryClip();
        if(pData != null) {
            ClipData.Item item = pData.getItemAt(0);
            if (item != null && item.getText() != null) {
                return item.getText().toString();
            }
        }
        return null;
    }

    public static void saveTo(@NonNull ClipboardManager clipboard, @NonNull String data){
        ClipData clip = ClipData.newPlainText("My public address", data);
        clipboard.setPrimaryClip(clip);
    }
}
