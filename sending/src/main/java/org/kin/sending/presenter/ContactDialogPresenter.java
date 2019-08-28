package org.kin.sending.presenter;

import android.support.annotation.NonNull;

import org.kin.sending.view.ContactDialogView;
import org.kin.sendkin.core.base.BasePresenter;

public interface ContactDialogPresenter extends BasePresenter<ContactDialogView> {
    void onCancelClicked();

    void onDeleteClicked();

    void onConfirmDeleteClicked();

    void onSaveClicked(@NonNull String name, @NonNull String address);

    void onResume();
}
