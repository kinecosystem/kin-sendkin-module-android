package org.kin.sending.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import org.kin.sending.events.EventsManager;
import org.kin.sending.events.SendKinPages;
import org.kin.sending.view.KinBalanceActionBar;
import org.kin.sending.view.Navigator;
import org.kin.sending.view.SendKinView;
import org.kin.sendkin.core.base.BasePresenterImpl;
import org.kin.sendkin.core.callbacks.BalanceCallback;
import org.kin.sendkin.core.callbacks.SendingKinCallback;
import org.kin.sendkin.core.model.KinManager;
import org.kin.sendkin.core.model.RecipientContact;
import org.kin.sendkin.core.store.ContactsListener;
import org.kin.sendkin.core.store.RecipientContactsRepo;

import java.util.ArrayList;
import java.util.UUID;

public class SendKinPresenterImpl extends BasePresenterImpl<SendKinView> implements SendKinPresenter, KinBalanceActionBar.OnClickCallback {
    private static String TAG = SendKinPresenterImpl.class.getSimpleName();
    private static final int MIN_LENGTH_CONTACT_NAME = 1;
    private static final int MAX_LENGTH_CONTACT_NAME = 16;

    private final KinManager kinManager;
    private final Navigator navigator;

    private String recipientAddress = "";
    private String contactName = "";
    private int amount;
    private int balance = 0;
    private RecipientContactsRepo contactsRepo;
    private RecipientContact contactChosen;
    private RecipientAddressListener addressListener;

    public SendKinPresenterImpl(@NonNull KinManager kinManager, RecipientContactsRepo contactsRepo, Navigator navigator) {
        this.kinManager = kinManager;
        this.navigator = navigator;
        this.contactsRepo = contactsRepo;
    }

    @Override
    public void setRecipientAddress(@NonNull String recipientAddress) {
        this.recipientAddress = recipientAddress;
        if (contactChosen != null && !contactChosen.getAddress().equals(recipientAddress)) {
            contactChosen = null;
        }
    }

    @Override
    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public @NonNull
    ArrayList<RecipientContact> getRecipientContacts() {
        return contactsRepo.getAll();
    }

    @Override
    public void setContactsListener(@NonNull ContactsListener contactsListener) {
        contactsRepo.setContactListener(contactsListener);
    }

    @Override
    public void setRecipientAddressListener(@NonNull RecipientAddressListener addressListener) {
        this.addressListener = addressListener;
    }

    @Override
    public void removeRecipientContact(@NonNull UUID id) {
        contactsRepo.remove(id);
    }

    @Nullable
    @Override
    public RecipientContact getContact(@NonNull UUID id) {
        return contactsRepo.getRecipientContact(id);
    }

    @Override
    public String getRecipientAddress() {
        return recipientAddress;
    }

    @Override
    public int getCurrentBalance() {
        return balance;
    }

    @Override
    public void onAttach(SendKinView view) {
        super.onAttach(view);
        navigator.onNext();
        contactsRepo.load();
    }

    @Override
    public void onResume() {
        requestBalance();
    }

    @Override
    public void onNext() {
        navigator.onNext();
        if (navigator.shouldStartTransfer()) {
            startTransaction();
        }
    }

    @Override
    public void onPrevious() {
        navigator.onPrevious();
    }

    @Override
    public void onShowPublicAddressDialogClicked() {
        view.showPublicAddressDialog(kinManager.getPublicAddress());
        onViewPageEvent(SendKinPages.MY_PUBLIC_ADDRESS_POPUP);
    }

    @Override
    public void onShowWhatIsPublicAddressDialogClicked() {
        view.showWhatIsPublicAddressDialog();
        onViewPageEvent(SendKinPages.WHATIS_PUBLIC_ADDRESS_POPUP);
    }

    @Override
    public void onAddNewContactClicked() {
        view.showAddNewContactDialog();
    }

    @Override
    public boolean hasEnoughKin(int spendAmount) {
        return spendAmount >= 0 && spendAmount <= balance;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public void onCloseClicked() {
        getView().onClose();
    }

    @Override
    public void onBackClicked() {
        onPrevious();
    }

    @Override
    public void startTransaction() {
        kinManager.sendKin(recipientAddress, amount, new SendingKinCallback() {
            @Override
            public void onSendKinCompleted(String transactionId, String receiverAddress, int amount) {
                navigator.updateStep(Navigator.STEP_TRANSFER_COMPLETE);
                requestBalance();
                EventsManager.getInstance().onTransferSuccess(transactionId);
            }

            @Override
            public void onSendKinFailed(String error, String receiverAddress, int amount) {
                navigator.updateStep(Navigator.STEP_TRANSFER_FAILED);
                EventsManager.getInstance().onTransferFailed();
            }

            @Override
            public void onSendKinTimeout(String error, String receiverAddress, int amount) {
                navigator.updateStep(Navigator.STEP_TRANSFER_TIMEOUT);
                EventsManager.getInstance().onTransactionTimeout();
            }
        });
    }

    @Override
    public boolean setContactName(@NonNull String contactName) {
        if (isValidContactName(contactName)) {
            this.contactName = contactName;
            return true;
        }
        return false;
    }

    @Override
    public void saveNewContact() {
        contactsRepo.add(new RecipientContact(contactName, recipientAddress));
        reset();
    }

    @Override
    public void onEditContact(@NonNull UUID id) {
        getView().showContactDialog(id);
    }

    @Override
    public void onContactClicked(@NonNull UUID id) {
        contactChosen = getContact(id);
        if (addressListener != null) {
            addressListener.onRecipientAddressChanged(contactChosen.getAddress());
        }
    }

    @Override
    public void reset() {
        this.contactName = "";
        this.recipientAddress = "";
        this.amount = 0;
        this.contactChosen = null;
    }

    @Override
    public void loadContacts() {
        contactsRepo.load();
    }

    @Override
    public RecipientContactsRepo getRecipientContactsRepo() {
        return contactsRepo;
    }

    @Override
    public RecipientContact getChosenContact() {
        return contactChosen;
    }

    private boolean isValidContactName(@NonNull String contactName) {
        return !contactName.isEmpty() && contactName.length() >= MIN_LENGTH_CONTACT_NAME && contactName.length() <= MAX_LENGTH_CONTACT_NAME;
    }

    private void requestBalance() {
        kinManager.getCurrentBalance(new BalanceCallback() {
            @Override
            public void onBalanceReceived(int balance) {
                SendKinPresenterImpl.this.balance = balance;
                getView().updateBalance(balance);
            }

            @Override
            public void onBalanceError(String error) {

            }
        });
    }

    private void onViewPageEvent(@NonNull SendKinPages page) {
        EventsManager.getInstance().onViewPage(page);
    }

    @VisibleForTesting
    @Override
    public int getBalance() {
        return balance;
    }

    @Override
    public void updateContact(UUID contactId, String name, String address) {
        contactsRepo.update(contactId, name, address);
        reset();
    }

    @Override
    public void saveNewContact(String name, String address) {
        this.contactName = name;
        this.recipientAddress = address;
        saveNewContact();
    }

    @VisibleForTesting
    @Override
    public void deleteAllContacts() {
        contactsRepo.deleteAllContacts();
    }
}
