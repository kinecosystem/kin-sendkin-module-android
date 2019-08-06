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
import org.kin.sendkin.core.view.Utills;


public class RecipientAddressPresenterImpl extends BasePresenterImpl<RecipientAddressView> implements RecipientAddressPresenter {
    private String recipientAddress = "";
    private RecipientContact contact;
    private SendKinPresenter sendKinPresenter;
    private ClipboardManager clipboard;

    private TextWatcher addressTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            getView().showAddressValidity(true, false);
            recipientAddress = s.toString();
            sendKinPresenter.setRecipientAddress(recipientAddress);
        }
    };

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
        String pasteData = Utills.getPastString(clipboard);
        if (pasteData != null) {
            getView().updateReceiverAddress(pasteData);
        }

    }

    @Override
    public TextWatcher getAddressTextWatcher() {
        return addressTextWatcher;
    }

    @Override
    public void onBackClicked() {
        sendKinPresenter.onPrevious();
    }

    @VisibleForTesting
    @Override
    public void setReceiverAddressText(String text) {
        recipientAddress = text;
    }

}
