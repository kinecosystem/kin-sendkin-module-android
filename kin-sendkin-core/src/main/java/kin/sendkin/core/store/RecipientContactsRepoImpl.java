package kin.sendkin.core.store;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import kin.sendkin.core.model.RecipientContact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

public class RecipientContactsRepoImpl implements RecipientContactsRepo {

    private ArrayList<RecipientContact> contacts;
    private final String KEY_ALL_CONTACTS = "ALL_RECIPIENT_CONTACTS";
    private final RecipientContactsLocalStore store;
    private final String STORE_NAME = "RecipientContactsRepo";
    private ContactsListener contactsListener = null;
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    public RecipientContactsRepoImpl(@NonNull Context context) {
        store = new RecipientContactsLocalStore(context, STORE_NAME);
    }

    @Override
    public void load() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                updateLoading();
                RecipientContactsRepoImpl.this.contacts = store.getRecipientContacts();
                updateLoadingComplete(RecipientContactsRepoImpl.this.contacts.isEmpty());
            }
        });
        thread.start();
    }

    @Override
    public ArrayList<RecipientContact> getAll() {
        return contacts;
    }

    @Override
    public void add(@NonNull RecipientContact recipientContact) {
        final boolean added = contacts.add(recipientContact);
        if (added) {
            Collections.sort(contacts);
            onNewContactAdded(contacts.indexOf(recipientContact));
        }
    }

    @Override
    public void remove(@NonNull RecipientContact recipientContact) {
        if (contacts.contains(recipientContact)) {
            final boolean isRemoved = contacts.remove(recipientContact);
            if (isRemoved) {
                onDataChanged();
            }
        }
    }

    @Override
    public void update(@NonNull UUID id, @NonNull String name, @NonNull String address) {
        RecipientContact recipientContact = getRecipientContact(id);
        if (recipientContact != null) {
            recipientContact.setName(name);
            recipientContact.setAddress(address);
            onDataChanged();
        }
    }

    @Override
    public void updateName(@NonNull UUID id, @NonNull String name) {
        RecipientContact recipientContact = getRecipientContact(id);
        if (recipientContact != null) {
            recipientContact.setName(name);
            onDataChanged();
        }
    }

    @Override
    public void updateAddress(@NonNull UUID id, @NonNull String address) {
        RecipientContact recipientContact = getRecipientContact(id);
        if (recipientContact != null) {
            recipientContact.setAddress(address);
            onDataChanged();
        }
    }

    @Override
    public void setContactListener(ContactsListener contactsListener) {
        this.contactsListener = contactsListener;
    }

    @Override
    public void remove(@NonNull UUID id) {
        RecipientContact recipientContact = getRecipientContact(id);
        if (recipientContact != null) {
            remove(recipientContact);
        }
    }

    @Override
    public @Nullable
    RecipientContact getRecipientContact(@NonNull UUID id) {
        for (RecipientContact recipientContact : contacts) {
            if (recipientContact.getId().equals(id)) {
                return recipientContact;
            }
        }
        return null;
    }

    private void updateLoading() {
        if (contactsListener != null) {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    contactsListener.onContactsLoading();
                }
            });
        }
    }

    private void onNewContactAdded(final int position) {
        store.save(contacts);
        if (contactsListener != null) {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    contactsListener.onContactAdded(position);
                }
            });
        }
    }

    private void updateLoadingComplete(final boolean isEmpty) {
        if (contactsListener != null) {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    contactsListener.onContactsLoaded(isEmpty);
                }
            });
        }

    }

    private void onDataChanged() {
        Collections.sort(contacts);
        store.save(contacts);
        if (contactsListener != null) {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    contactsListener.onContactChanged(contacts.isEmpty());
                }
            });
        }
    }

    @VisibleForTesting
    public void deleteAllContacts() {
        store.clearAll();
        contacts.clear();
        onDataChanged();
    }

}
