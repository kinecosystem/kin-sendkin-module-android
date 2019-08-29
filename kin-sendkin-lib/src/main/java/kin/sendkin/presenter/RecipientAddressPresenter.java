package kin.sendkin.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import kin.sendkin.view.RecipientAddressView;
import kin.sendkin.core.base.BasePresenter;

public interface RecipientAddressPresenter extends BasePresenter<RecipientAddressView> {

    void onShowPublicAddressClicked();

    void onWhatIsPublicAddressClicked();

    void onAddNewContactClicked();

    void onNextClicked();

    void onPasteClicked();

    void setRecipientAddress(@NonNull String address);

    void onResume();

    @VisibleForTesting
    void deleteAll();
}
