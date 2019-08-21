package org.kin.sending.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import org.kin.sending.events.EventsManager;
import org.kin.sending.events.SendKinPages;
import org.kin.sending.view.KinBalanceActionBar;
import org.kin.sending.view.Navigator;
import org.kin.sending.view.SendKinView;
import org.kin.sendkin.core.base.BasePresenterImpl;
import org.kin.sendkin.core.callbacks.BalanceCallback;
import org.kin.sendkin.core.callbacks.SendingKinCallback;
import org.kin.sendkin.core.model.KinManager;

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
        navigator.onNext();
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
    public void saveContact() {
        Log.d("###", "### save " + contactName + " address " + recipientAddress);
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

    @VisibleForTesting
    @Override
    public int getBalance() {
        return balance;
    }

    private void onViewPageEvent(@NonNull SendKinPages page){
        EventsManager.getInstance().onViewPage(page);
    }
}
