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
    private @Nullable
    UUID contactId;
    private String name = "";
    private String address = "";
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
        //TODO change to contact obj
        if (contactId != null) {
            sendKinPresenter.removeRecipientContact(contactId);
            getView().dismiss();
        }
    }


    @Override
    public void onSaveClicked() {
        final boolean isValidAddress = checkAddressValidity();
        final boolean isValidName = checkNameValidity();
        if (isValidAddress && isValidName) {
            sendKinPresenter.setContactName(name);
            sendKinPresenter.setRecipientAddress(address);
            if (contactId != null) {
                sendKinPresenter.updateContact(contactId);
            } else {
                sendKinPresenter.saveNewContact();
            }
            getView().dismiss();
        }
    }

    @Override
    public void onNameChanged(@NonNull String name) {
        this.name = name;
    }

    @Override
    public void onAddressChanged(@NonNull String address) {
        this.address = address;
    }

    @Override
    public void onBackClicked() {

    }

    private boolean checkNameValidity() {
        if (!name.isEmpty()) {
            return true;
        } else {
            getView().showNameValidity(false);
            return false;
        }
    }

    private boolean checkAddressValidity() {
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
