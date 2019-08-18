package org.kin.sending.view;

import android.support.annotation.NonNull;

import org.kin.sending.presenter.SendKinPresenter;
import org.kin.sendkin.core.base.BaseView;

public interface SendKinView extends BaseView {

    @NonNull
    SendKinPresenter getSendKinPresenter();

    void updateBalance(int balance);

    void onClose();

    void enableBack(boolean enable);


    void showPublicAddressDialog(@NonNull final String publicAddress);

    void showWhatIsPublicAddressDialog();

    void showRecipientAddressPage();

    void showAmountPage();

    void showStartTransferPage();

    void showConfirmPage();

    void showTransferCompletePage();

    void showTransferFailedPage();

    void showTransferTimeoutPage();
}
