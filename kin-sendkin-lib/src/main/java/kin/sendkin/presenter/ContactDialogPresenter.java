package kin.sendkin.presenter;

import android.support.annotation.NonNull;

import kin.sendkin.view.ContactDialogView;
import kin.sendkin.core.base.BasePresenter;

public interface ContactDialogPresenter extends BasePresenter<ContactDialogView> {
    void onCancelClicked();

    void onDeleteClicked();

    void onConfirmDeleteClicked();

    void onSaveClicked(@NonNull String name, @NonNull String address);

    void onResume();
}
