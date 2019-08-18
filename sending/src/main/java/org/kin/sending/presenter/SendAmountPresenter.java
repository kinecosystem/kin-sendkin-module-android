package org.kin.sending.presenter;

import android.support.annotation.NonNull;

import org.kin.sending.view.SendAmountView;
import org.kin.sendkin.core.base.BasePresenter;

public interface SendAmountPresenter extends BasePresenter<SendAmountView> {
    void onSendClicked();
    void setAmount(@NonNull String amount);
}
