package org.kin.sendkin.core.store;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import org.kin.sendkin.core.model.RecipientContact;

import java.util.ArrayList;
import java.util.UUID;

public interface RecipientContactsRepo {

    void load();

    ArrayList<RecipientContact> getAll();

    void add(@NonNull RecipientContact recipientContact);

    void remove(@NonNull RecipientContact recipientContact);

    void update(@NonNull UUID id, @NonNull String name, @NonNull String address);

    void updateName(@NonNull UUID id, @NonNull String name);

    void updateAddress(@NonNull UUID id, @NonNull String address);

    void setContactListener(ContactsListener contactsListener);

    void remove(@NonNull UUID id);

    @Nullable
    RecipientContact getRecipientContact(@NonNull UUID id);

    @VisibleForTesting
    void deleteAllContacts();

    }
