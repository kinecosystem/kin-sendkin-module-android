package org.kin.sendkin.core.store;

public interface ContactsListener {
    void onContactChanged(boolean isEmptyList);
    void onContactAdded(int position);
    void onContactsLoaded(boolean isEmptyList);
    void onContactsLoading();
}