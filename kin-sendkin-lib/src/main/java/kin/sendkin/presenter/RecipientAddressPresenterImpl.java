package kin.sendkin.presenter;

import android.content.ClipboardManager;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import kin.sendkin.view.RecipientAddressView;
import kin.sendkin.core.base.BasePresenterImpl;
import kin.sendkin.core.model.KinAccountUtils;
import kin.sendkin.core.store.ContactsListener;
import kin.sendkin.core.view.Utils;


public class RecipientAddressPresenterImpl extends BasePresenterImpl<RecipientAddressView> implements RecipientAddressPresenter, ContactsListener, RecipientAddressListener {
    private String recipientAddress = "";
    private final SendKinPresenter sendKinPresenter;
    private ClipboardManager clipboard;

    public RecipientAddressPresenterImpl(@NonNull SendKinPresenter sendKinPresenter, @NonNull ClipboardManager clipboard) {
        this.clipboard = clipboard;
        this.sendKinPresenter = sendKinPresenter;
        sendKinPresenter.setContactsListener(this);
        sendKinPresenter.setRecipientAddressListener(this);
    }

    @Override
    public void onShowPublicAddressClicked() {
        sendKinPresenter.onShowPublicAddressDialogClicked();
    }

    @Override
    public void onAddNewContactClicked() {
        sendKinPresenter.onAddNewContactClicked();
    }

    @Override
    public void onWhatIsPublicAddressClicked() {
        sendKinPresenter.onShowWhatIsPublicAddressDialogClicked();
    }

    @Override
    public void onNextClicked() {
        if (!recipientAddress.isEmpty()) {
            if (KinAccountUtils.isValidPublicAddress(recipientAddress)) {
                sendKinPresenter.onNext();
            } else {
                getView().showAddressValidity(false, true);
            }
        } else {
            getView().showAddressValidity(false, false);
        }
    }

    @Override
    public void onPasteClicked() {
        String pasteData = Utils.getPasteString(clipboard);
        if (pasteData != null) {
            getView().updateReceiverAddress(pasteData);
        }
    }

    @Override
    public void setRecipientAddress(@NonNull String address) {
        getView().showAddressValidity(true, false);
        recipientAddress = address;
        sendKinPresenter.setRecipientAddress(recipientAddress);
    }

    @Override
    public void onResume() {
        sendKinPresenter.loadContacts();
    }

    @Override
    public void onBackClicked() {
        sendKinPresenter.onPrevious();
    }

    @Override
    public void onRecipientAddressChanged(@NonNull String address) {
        getView().updateReceiverAddress(address);
    }

    @Override
    public void onContactChanged(boolean isEmptyList) {
        getView().notifyContactChanged();
        getView().updateListVisibility(isEmptyList);
    }

    @Override
    public void onContactAdded(int position) {
        getView().notifyContactChanged();
        getView().updateListVisibility(false);
        getView().scrollToPosition(position, true);
    }

    @Override
    public void onContactsLoaded(boolean isEmptyList) {
        getView().updateListVisibility(isEmptyList);
    }

    @Override
    public void onContactsLoading() {
        getView().showContactsLoader();
    }

    @VisibleForTesting
    @Override
    public void deleteAll() {
        sendKinPresenter.deleteAllContacts();
    }
}
