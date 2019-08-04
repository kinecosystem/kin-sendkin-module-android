package org.kin.sending.presenter;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;

import org.kin.sending.view.SendKinView;
import org.kin.sendkin.core.base.BasePresenterImpl;
import org.kin.sendkin.core.callbacks.BalanceCallback;
import org.kin.sendkin.core.callbacks.SendingKinCallback;
import org.kin.sendkin.core.model.KinAccountUtils;
import org.kin.sendkin.core.model.KinManager;
import org.kin.sendkin.core.model.KinManagerImpl;

import kin.sdk.KinAccount;

public class SendKinPresenterImpl extends BasePresenterImpl<SendKinView> implements SendKinPresenter {
    private static String TAG = SendKinPresenterImpl.class.getSimpleName();
    private KinManager kinManager;
    private String toAddress = "";
    private TextWatcher addressTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            toAddress = s.toString();
        }
    };

    public SendKinPresenterImpl(@NonNull KinAccount kinAccount) {
        kinManager = new KinManagerImpl(kinAccount);
    }

    @Override
    public void onResume() {
        requestBalance();
    }

    @Override
    public void onShowPublicAddressClicked() {
        getView().showPublicAddressPopup(kinManager.getPublicAddress());
    }



    @Override
    public void onSendClicked() {

        if(!toAddress.isEmpty() && !KinAccountUtils.isValidPublicAddress(toAddress)){
            getView().onRecipientAddressNotValid();
            return;
        }
        String receiverAddress = "GDTSUE3LS2S6OVFWVG4EX7I6LM2Y4PKDTZKM22CRRIY3GYWS5PV3TJFA";
        if(!toAddress.isEmpty()){
            receiverAddress = toAddress;
        }
        int amount = 2;
        updateStatus(TransactionStatus.STRATED);
        //TODO update memo
        String memo = "MIMI";

        kinManager.sendKin(receiverAddress, amount, memo, new SendingKinCallback() {
            @Override
            public void onSendKinCompleted(String transactionId, String receiverAddress, int amount) {
                updateStatus(TransactionStatus.COMPLETED);
                requestBalance();
            }

            @Override
            public void onSendKinFailed(String error, String receiverAddress, int amount) {
                updateStatus(TransactionStatus.FAILED);
            }

            @Override
            public void onSendKinTimeout(String error, String receiverAddress, int amount) {
                updateStatus(TransactionStatus.TIMEOUT);
            }
        });
    }

    @Override
    public TextWatcher getAddressTextWatcher() {
        return addressTextWatcher;
    }

    @Override
    public void onBackClicked() {

    }

    private void updateStatus(TransactionStatus status) {
        view.updateStatus(status.toString());
    }

    private void requestBalance() {
        kinManager.getCurrentBalance(new BalanceCallback() {
            @Override
            public void onBalanceReceived(int balance) {
                getView().updateBalance(balance);
            }

            @Override
            public void onBalanceError(String error) {

            }
        });
    }


}
