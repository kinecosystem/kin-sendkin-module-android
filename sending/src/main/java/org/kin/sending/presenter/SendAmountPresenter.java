package org.kin.sending.presenter;

import android.support.annotation.VisibleForTesting;
import android.text.TextWatcher;

import org.kin.sending.view.SendAmountView;
import org.kin.sendkin.core.base.BasePresenter;

public interface SendAmountPresenter extends BasePresenter<SendAmountView> {
    void onSendClicked();
    TextWatcher getAmountTextWatcher();

    @VisibleForTesting
    void setAmount(String amount);
}
