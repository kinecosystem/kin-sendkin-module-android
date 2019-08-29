package kin.sendkin.core.store;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import java.util.ArrayList;

import kin.sendkin.core.model.RecipientContact;
import kin.sendkin.core.view.Utils;

class RecipientContactsLocalStore {

    private final String KEY_ALL_CONTACTS = "KEY_ALL_CONTACTS";
    private LocalStore store;


    public RecipientContactsLocalStore(@NonNull LocalStore store) {
        this.store = store;
    }

    public void save(ArrayList<RecipientContact> recipientContacts) {
        store.putString(KEY_ALL_CONTACTS, Utils.parseToString(recipientContacts));
    }

    @NonNull
    public ArrayList<RecipientContact> getRecipientContacts() {
        String contactsStr = store.getString(KEY_ALL_CONTACTS);
        if (contactsStr == null) {
            return new ArrayList<>();
        }
        return Utils.parseContactsFromString(store.getString(KEY_ALL_CONTACTS));
    }

    @VisibleForTesting
    public void deleteAll() {
        store.clearAll();
    }
}
