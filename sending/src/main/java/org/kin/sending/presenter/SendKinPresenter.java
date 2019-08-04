package org.kin.sending.presenter;

import android.text.TextWatcher;

import org.kin.sending.view.SendKinView;
import org.kin.sendkin.core.base.BasePresenter;

public interface SendKinPresenter extends BasePresenter<SendKinView> {
    void onResume();
    void onShowPublicAddressClicked();
    void onSendClicked();
    TextWatcher getAddressTextWatcher();
}
