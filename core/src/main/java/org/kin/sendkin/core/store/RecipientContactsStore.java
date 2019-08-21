package org.kin.sendkin.core.store;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import org.kin.sendkin.core.model.RecipientContact;

import java.util.ArrayList;

public interface RecipientContactsStore {
    void save(ArrayList<RecipientContact> recipientContacts);
    @NonNull ArrayList<RecipientContact> getRecipientContacts();

    @VisibleForTesting
    void deleteAll();
}
