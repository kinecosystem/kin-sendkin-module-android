package org.kin.sending.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import org.kin.sending.view.RecipientContactsAdapter;
import org.kin.sending.view.SendKinView;
import org.kin.sendkin.core.base.BasePresenter;
import org.kin.sendkin.core.model.RecipientContact;
import org.kin.sendkin.core.store.ContactsListener;
import org.kin.sendkin.core.store.RecipientContactsRepo;

import java.util.ArrayList;
import java.util.UUID;

public interface SendKinPresenter extends BasePresenter<SendKinView>, RecipientContactsAdapter.RecipientContactTouchListener {
    void onResume();

    int getAmount();

    int getCurrentBalance();

    void setAmount(int amount);

    @NonNull ArrayList<RecipientContact> getRecipientContacts();

    void setContactsListener(@NonNull ContactsListener contactsListener);

    void setRecipientAddressListener(@NonNull RecipientAddressListener addressListener);

    void removeRecipientContact(@NonNull UUID id);

    @Nullable RecipientContact getContact(@NonNull UUID id);

    @NonNull String getRecipientAddress();

    void setRecipientAddress(@NonNull String recipientAddress);

    void startTransaction();

    void onNext();

    void onPrevious();

    void onShowPublicAddressDialogClicked();

    void onShowWhatIsPublicAddressDialogClicked();

    void onAddNewContactClicked();

    boolean hasEnoughKin(int spendAmount);

    boolean setContactName(@NonNull String contactName);

    void saveNewContact();

    void updateContact(@NonNull UUID id);


    void reset();

    void loadContacts();

    RecipientContactsRepo getRecipientContactsRepo();

    RecipientContact getChosenContact();

    @VisibleForTesting
    void deleteAllContacts();

    @VisibleForTesting
    int getBalance();


}
