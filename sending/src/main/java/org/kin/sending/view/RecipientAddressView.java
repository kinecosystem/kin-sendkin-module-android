package org.kin.sending.view;

import org.kin.sendkin.core.base.BaseView;

public interface RecipientAddressView extends BaseView {
    void showAddressValidity(boolean isValid, boolean errorDetails);

    void updateReceiverAddress(String pasteAddress);

    void notifyContactChanged();

    void updateListVisibility(boolean isEmptyList);

    void showContactsLoader();

    void scrollToPosition(int position, boolean animate);
}
