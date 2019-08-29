package kin.sendkin.view;

import android.support.annotation.NonNull;

import kin.sendkin.presenter.SendKinPresenter;
import kin.sendkin.core.base.BaseView;

import java.util.UUID;

public interface SendKinView extends BaseView {

    @NonNull
    SendKinPresenter getSendKinPresenter();

    void updateBalance(int balance);

    void onClose();

    void enableBack(boolean enable);

    void showPublicAddressDialog(@NonNull final String publicAddress);

    void showWhatIsPublicAddressDialog();

    void showContactDialog(@NonNull UUID id);

    void showRecipientAddressPage();

    void showAmountPage();

    void showTransactionDialog(@Navigator.SendKinSteps int status);

    void showAddNewContactDialog();
}
