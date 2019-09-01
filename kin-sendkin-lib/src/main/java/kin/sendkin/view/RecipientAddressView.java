package kin.sendkin.view;

import kin.sendkin.core.base.BaseView;

public interface RecipientAddressView extends BaseView {
    void showAddressValidity(boolean isValid, boolean errorDetails);

    void updateReceiverAddress(String pasteAddress);

    void notifyContactChanged();

    void notifyContactAdded(int position);

    void updateListVisibility(boolean isEmptyList);

    void showContactsLoader();
}
