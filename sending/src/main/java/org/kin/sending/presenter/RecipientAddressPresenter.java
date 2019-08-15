package org.kin.sending.presenter;

import android.support.annotation.NonNull;

import org.kin.sending.view.RecipientAddressView;
import org.kin.sendkin.core.base.BasePresenter;

public interface RecipientAddressPresenter extends BasePresenter<RecipientAddressView> {
    void onShowPublicAddressClicked();
    void onWhatIsPublicAddressClicked();
    void onNextClicked();
    void onPasteClicked();
    void setRecipientAddress(@NonNull String toString);
}
