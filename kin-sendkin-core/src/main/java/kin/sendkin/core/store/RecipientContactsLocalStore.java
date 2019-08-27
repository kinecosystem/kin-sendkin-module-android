package kin.sendkin.core.store;

import android.content.Context;
import android.support.annotation.NonNull;

import kin.sendkin.core.model.RecipientContact;
import kin.sendkin.core.view.Utils;

import java.util.ArrayList;
import java.util.UUID;

public class RecipientContactsLocalStore extends LocalStore implements RecipientContactsStore {

    private final String KEY_ALL_CONTACTS = "KEY_ALL_CONTACTS";


    public RecipientContactsLocalStore(@NonNull Context context, @NonNull String name) {
        super(context, name);
    }

    @Override
    public void save(ArrayList<RecipientContact> recipientContacts) {
        putString(KEY_ALL_CONTACTS, Utils.parseToString(recipientContacts));

    }

    @NonNull
    @Override
    public ArrayList<RecipientContact> getRecipientContacts() {
        String contactsStr = getString(KEY_ALL_CONTACTS);
        if (contactsStr == null) {
            return new ArrayList<>();
        }
        return Utils.parseContactsFromString(getString(KEY_ALL_CONTACTS));
    }

    @Override
    public boolean delete(@NonNull UUID recipientContactId) {
        String key = recipientContactId.toString();
        if (containsKey(key)) {
            removeKey(key);
            return true;
        }
        return false;
    }

    @Override
    public RecipientContact getContact(@NonNull UUID id) {
        String recipientContactStr = getString(id.toString());
        if (recipientContactStr != null) {
            return Utils.parseFromString(recipientContactStr);
        }
        return null;
    }
}
