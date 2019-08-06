package org.kin.sending.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import org.kin.sending.view.KinBalanceActionBar;
import org.kin.sending.view.Navigator;
import org.kin.sending.view.SendKinView;
import org.kin.sendkin.core.base.BasePresenterImpl;
import org.kin.sendkin.core.callbacks.BalanceCallback;
import org.kin.sendkin.core.callbacks.SendingKinCallback;
import org.kin.sendkin.core.model.KinManager;

public class SendKinPresenterImpl extends BasePresenterImpl<SendKinView> implements SendKinPresenter, KinBalanceActionBar.OnClickCallback {
    private static String TAG = SendKinPresenterImpl.class.getSimpleName();
    private KinManager kinManager;
    private Navigator navigator;

    private String recipientAddress = "";
    private int amount;
    private int balance = 0;
    private String memo = "MEMO"; //TODO update memo

    public SendKinPresenterImpl(@NonNull KinManager kinManager, Navigator navigator) {
        this.kinManager = kinManager;
        this.navigator = navigator;
    }

    @Override
    public void setRecipientAddress(String recipientAddress) {
        this.recipientAddress = recipientAddress;
    }

    @Override
    public void setAmount(int amount) {
        this.amount = amount;
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
        navigator.updateStep(Navigator.STEP_RECIPIENT_ADDRESS);
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
    }

    @Override
    public void onShowWhatIsPublicAddressDialogClicked() {
        view.showWhatIsPublicAddressDialog();
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
        kinManager.sendKin(recipientAddress, amount, memo, new SendingKinCallback() {
            @Override
            public void onSendKinCompleted(String transactionId, String receiverAddress, int amount) {
                navigator.updateStep(Navigator.STEP_TRANSFER_COMPLETE);
                requestBalance();
            }

            @Override
            public void onSendKinFailed(String error, String receiverAddress, int amount) {
                navigator.updateStep(Navigator.STEP_TRANSFER_FAILED);
            }

            @Override
            public void onSendKinTimeout(String error, String receiverAddress, int amount) {
                navigator.updateStep(Navigator.STEP_TRANSFER_TIMEOUT);
            }
        });
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

    @VisibleForTesting
    @Override
    public int getBalance() {
        return balance;
    }
}
