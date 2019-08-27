package kin.sendkin.core.store;

import android.support.annotation.NonNull;

import kin.sendkin.core.model.RecipientContact;

import java.util.ArrayList;
import java.util.UUID;

public interface RecipientContactsStore {
    void save(ArrayList<RecipientContact> recipientContacts);
    @NonNull ArrayList<RecipientContact> getRecipientContacts();
    boolean delete(@NonNull UUID recipientContactId);
    RecipientContact getContact(@NonNull UUID id);
}
