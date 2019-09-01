package kin.sendkin.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import java.util.ArrayList;
import java.util.UUID;

import kin.sendkin.core.base.BasePresenter;
import kin.sendkin.core.model.RecipientContact;
import kin.sendkin.core.store.ContactsListener;
import kin.sendkin.core.store.RecipientContactsRepo;
import kin.sendkin.view.RecipientContactsAdapter;
import kin.sendkin.view.SendKinView;

public interface SendKinPresenter extends BasePresenter<SendKinView>, RecipientContactsAdapter.RecipientContactTouchListener {
    void onResume();

    int getAmount();

    int getCurrentBalance();

    void setAmount(int amount);

    @NonNull
    ArrayList<RecipientContact> getRecipientContacts();

    void setContactsListener(@NonNull ContactsListener contactsListener);

    void setRecipientAddressListener(@NonNull RecipientAddressListener addressListener);

    void removeRecipientContact(@NonNull UUID id);

    @Nullable
    RecipientContact getContact(@NonNull UUID id);

    @NonNull
    String getRecipientAddress();

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

    void reset();

    void loadContacts();

    RecipientContactsRepo getRecipientContactsRepo();

    RecipientContact getChosenContact();

    @VisibleForTesting
    void deleteAllContacts();

    @VisibleForTesting
    int getBalance();

    void updateContact(@NonNull UUID contactId, @NonNull String name, @NonNull String address);

    void saveNewContact(@NonNull String name, @NonNull String address);
}
