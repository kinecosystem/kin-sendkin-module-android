package org.kin.sending.presenter;

import android.content.ClipboardManager;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.text.Editable;
import android.text.TextWatcher;

import org.kin.sending.view.RecipientAddressView;
import org.kin.sendkin.core.base.BasePresenterImpl;
import org.kin.sendkin.core.model.KinAccountUtils;
import org.kin.sendkin.core.model.RecipientContact;
import org.kin.sendkin.core.view.Utils;


public class RecipientAddressPresenterImpl extends BasePresenterImpl<RecipientAddressView> implements RecipientAddressPresenter {
    private String recipientAddress = "";
    private final SendKinPresenter sendKinPresenter;
    private ClipboardManager clipboard;

    public RecipientAddressPresenterImpl(@NonNull SendKinPresenter sendKinPresenter, @NonNull ClipboardManager clipboard) {
        this.clipboard = clipboard;
        this.sendKinPresenter = sendKinPresenter;
    }

    @Override
    public void onShowPublicAddressClicked() {
        sendKinPresenter.onShowPublicAddressDialogClicked();
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
    public void onBackClicked() {
        sendKinPresenter.onPrevious();
    }

}
