package org.kin.sending.view;

import android.support.annotation.NonNull;

import org.kin.sendkin.core.base.BaseView;

public interface ContactDialogView extends BaseView {

    void setNewLayout();

    void setEditLayout(String name, String address);

    void dismiss();

    void showNameValidity(boolean isValid);

    void showAddressValidity(boolean isValid, boolean errorDetails);

    void setConfirmDeleteLayout(@NonNull String nickName);
}
