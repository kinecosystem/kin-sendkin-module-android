package kin.sendkin.core.store;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import java.util.ArrayList;

import kin.sendkin.core.model.RecipientContact;

public interface RecipientContactsStore {
    void save(ArrayList<RecipientContact> recipientContacts);
    @NonNull ArrayList<RecipientContact> getRecipientContacts();

    @VisibleForTesting
    void deleteAll();
}
