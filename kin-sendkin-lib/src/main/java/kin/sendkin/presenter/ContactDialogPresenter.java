package kin.sendkin.presenter;

import android.support.annotation.NonNull;

import kin.sendkin.view.ContactDialogView;
import kin.sendkin.core.base.BasePresenter;

public interface ContactDialogPresenter extends BasePresenter<ContactDialogView> {
    void onCancelClicked();

    void onDeleteClicked();

    void onConfirmDeleteClicked();

    void onSaveClicked();

    void onResume();

    void onNameChanged(@NonNull String name);

    void onAddressChanged(@NonNull String address);
}
