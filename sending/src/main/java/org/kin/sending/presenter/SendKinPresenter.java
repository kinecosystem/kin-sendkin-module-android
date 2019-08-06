package org.kin.sending.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import org.kin.sending.view.SendKinView;
import org.kin.sendkin.core.base.BasePresenter;

public interface SendKinPresenter extends BasePresenter<SendKinView> {
    void onResume();

    int getAmount();

    int getCurrentBalance();

    void setAmount(int amount);

    String getRecipientAddress();

    void setRecipientAddress(@NonNull String recipientAddress);

    void startTransaction();

    void onCloseClicked();

    void onNext();

    void onPrevious();

    void onShowPublicAddressDialogClicked();

    void onShowWhatIsPublicAddressDialogClicked();

    @VisibleForTesting
    int getBalance();
}
