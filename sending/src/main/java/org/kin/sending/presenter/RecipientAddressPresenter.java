package org.kin.sending.presenter;

import android.support.annotation.VisibleForTesting;
import android.text.TextWatcher;

import org.kin.sending.view.RecipientAddressView;
import org.kin.sendkin.core.base.BasePresenter;

public interface RecipientAddressPresenter extends BasePresenter<RecipientAddressView> {
    void onShowPublicAddressClicked();
    void onWhatIsPublicAddressClicked();
    void onNextClicked();
    void onPasteClicked();
    TextWatcher getAddressTextWatcher();

    @VisibleForTesting
    void setReceiverAddressText(String text);
}
