package org.kin.sending.view;

import android.support.annotation.NonNull;
import org.kin.sendkin.core.base.BaseView;

public interface SendKinView extends BaseView {
    void updateBalance(int balance);
    void showPublicAddressPopup(@NonNull String publicAddress);
    void updateStatus(String status);
    void onRecipientAddressNotValid();


}
