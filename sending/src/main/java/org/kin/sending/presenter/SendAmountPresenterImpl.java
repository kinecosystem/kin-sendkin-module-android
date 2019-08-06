package org.kin.sending.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.text.Editable;
import android.text.TextWatcher;

import org.kin.sending.view.SendAmountView;
import org.kin.sending.view.SendKinView;
import org.kin.sendkin.core.base.BasePresenterImpl;


public class SendAmountPresenterImpl extends BasePresenterImpl<SendAmountView> implements SendAmountPresenter {
    private String amountStr = "";
    private SendKinPresenter sendKinPresenter;

    private TextWatcher amountTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            getView().showAmountValidity(true, false);
            amountStr = s.toString();
            try {
                sendKinPresenter.setAmount(Integer.parseInt(amountStr));
            }catch (NumberFormatException e){
                //not integer format
            }
        }
    };

    public SendAmountPresenterImpl(@NonNull SendKinPresenter sendKinPresenter) {
        this.sendKinPresenter = sendKinPresenter;
    }

    @Override
    public void onSendClicked() {
        if (!amountStr.isEmpty() && !amountStr.startsWith("0")) {
            if (isValidAmount()) {
                sendKinPresenter.onNext();
            } else {
                getView().showAmountValidity(false, true);
            }
        } else {
            getView().showAmountValidity(false, false);
        }
    }

    @Override
    public TextWatcher getAmountTextWatcher() {
        return amountTextWatcher;
    }

    @Override
    public void onBackClicked() {
        sendKinPresenter.onPrevious();
    }

    private boolean isValidAmount() {
        try {
            final int amount = Integer.parseInt(amountStr);
            return amount <= sendKinPresenter.getCurrentBalance();
        } catch (NumberFormatException e) {
            return false;
        }
    }



    @VisibleForTesting
    @Override
    public void setAmount(String amount) {
        amountStr = amount;
    }
}
