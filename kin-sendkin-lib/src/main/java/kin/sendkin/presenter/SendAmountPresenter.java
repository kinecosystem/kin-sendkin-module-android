package kin.sendkin.presenter;

import android.support.annotation.NonNull;

import kin.sendkin.view.SendAmountView;
import kin.sendkin.core.base.BasePresenter;

public interface SendAmountPresenter extends BasePresenter<SendAmountView> {
    void onSendClicked();
    void setAmount(@NonNull String amount);
}
