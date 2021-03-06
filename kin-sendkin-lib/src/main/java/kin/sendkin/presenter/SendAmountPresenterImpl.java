package kin.sendkin.presenter;

import android.support.annotation.NonNull;

import kin.sendkin.view.SendAmountView;
import kin.sendkin.core.base.BasePresenterImpl;


public class SendAmountPresenterImpl extends BasePresenterImpl<SendAmountView> implements SendAmountPresenter {
    private static final String TAG = SendKinPresenterImpl.class.getSimpleName();
    private String amountStr = "";
    private final SendKinPresenter sendKinPresenter;

    public SendAmountPresenterImpl(@NonNull SendKinPresenter sendKinPresenter) {
        this.sendKinPresenter = sendKinPresenter;
    }

    @Override
    public void onSendClicked() {
        if (!amountStr.isEmpty() && !amountStr.startsWith("0")) {
            if (hasEnoughKin()) {
                sendKinPresenter.onNext();
            } else {
                getView().showAmountValidity(false, true);
            }
        } else {
            getView().showAmountValidity(false, false);
        }
    }

    @Override
    public void setAmount(@NonNull String amount) {
        getView().showAmountValidity(true, false);
        try {
            if(amount.isEmpty()){
                amount = "0";
            }
            sendKinPresenter.setAmount(Integer.parseInt(amount));
            this.amountStr = amount;
        } catch (NumberFormatException e) {
            amountStr = "" +Integer.MAX_VALUE;
        }
    }

    @Override
    public void onBackClicked() {
        sendKinPresenter.onPrevious();
    }

    private boolean hasEnoughKin() {
        try {
            final int amount = Integer.parseInt(amountStr);
            return sendKinPresenter.hasEnoughKin(amount);
        } catch (NumberFormatException e) {
            return false;
        }
    }


}
