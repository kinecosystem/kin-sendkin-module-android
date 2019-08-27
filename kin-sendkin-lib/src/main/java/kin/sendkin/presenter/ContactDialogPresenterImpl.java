package kin.sendkin.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import kin.sendkin.view.ContactDialogView;
import kin.sendkin.core.base.BasePresenterImpl;
import kin.sendkin.core.model.KinAccountUtils;
import kin.sendkin.core.model.RecipientContact;

import java.util.UUID;

public class ContactDialogPresenterImpl extends BasePresenterImpl<ContactDialogView> implements ContactDialogPresenter {

    private SendKinPresenter sendKinPresenter;
    @Nullable
    private UUID contactId;
    private RecipientContact recipientContact;

    public ContactDialogPresenterImpl(SendKinPresenter sendKinPresenter, @Nullable UUID contactId) {
        this.sendKinPresenter = sendKinPresenter;
        this.contactId = contactId;
    }

    @Override
    public void onResume() {
        if (contactId != null) {
            recipientContact = sendKinPresenter.getContact(contactId);
            if (recipientContact != null) {
                getView().setEditLayout(recipientContact.getName(), recipientContact.getAddress());
            }
        } else {
            getView().setNewLayout();
        }
    }

    @Override
    public void onCancelClicked() {
        sendKinPresenter.reset();
        getView().dismiss();
    }

    @Override
    public void onDeleteClicked() {
        if (recipientContact != null) {
            getView().setConfirmDeleteLayout(recipientContact.getName());
        }
    }


    @Override
    public void onConfirmDeleteClicked() {
        if (contactId != null) {
            sendKinPresenter.removeRecipientContact(contactId);
            getView().dismiss();
        }
    }


    @Override
    public void onSaveClicked(@NonNull String name, @NonNull String address) {
        final boolean isValidAddress = validateAddress(address);
        final boolean isValidName = validateName(name);
        if (isValidAddress && isValidName) {
            sendKinPresenter.setContactName(name);
            sendKinPresenter.setRecipientAddress(address);
            if (contactId != null) {
                sendKinPresenter.updateContact(contactId, name, address);
            } else {
                sendKinPresenter.saveNewContact(name, address);
            }
            getView().dismiss();
        }
    }

    @Override
    public void onBackClicked() {

    }

    private boolean validateName(@NonNull String name) {
        if (!name.isEmpty()) {
            return true;
        } else {
            getView().showNameValidity(false);
            return false;
        }
    }

    private boolean validateAddress(@NonNull String address) {
        if (!address.isEmpty()) {
            if (KinAccountUtils.isValidPublicAddress(address)) {
                getView().showAddressValidity(true, false);
                return true;
            } else {
                getView().showAddressValidity(false, true);
            }
        } else {
            getView().showAddressValidity(false, false);
        }
        return false;
    }
}
