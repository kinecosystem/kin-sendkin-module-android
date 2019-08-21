package org.kin.sending.view;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.Group;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.kin.sending.R;
import org.kin.sending.presenter.ContactDialogPresenter;
import org.kin.sending.presenter.ContactDialogPresenterImpl;
import org.kin.sendkin.core.view.BaseWideDialogFragment;
import org.kin.sendkin.core.view.SendKinEditText;
import org.kin.sendkin.core.view.Utils;

import java.util.UUID;

public class ContactDialogFragment extends BaseWideDialogFragment implements ContactDialogView {
    public static final String TAG = ContactDialogFragment.class.getSimpleName();

    private static final String KEY_ID = "KEY_ID";
    private ImageView deleteIcon;
    private TextView title, addressNotValidWarning, saveBtn, deleteSubtitle;
    private ContactDialogPresenter presenter;
    private SendKinEditText nameInput, addressInput;
    private Group nameAddressLayout;
    private String name = "";
    private String address = "";

    private TextWatcher nameWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            nameInput.clearError();
            name = editable.toString().trim();
        }
    };

    private TextWatcher addressWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            clearAddressErrors();
            address = editable.toString().trim();
        }
    };

    public static ContactDialogFragment getInstance(@NonNull UUID id) {
        ContactDialogFragment dialog = new ContactDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_ID, id);
        dialog.setArguments(args);
        return dialog;
    }

    public static ContactDialogFragment getInstance() {
        return new ContactDialogFragment();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.contact_dialog_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UUID id = null;
        if (getArguments() != null && getArguments().containsKey(KEY_ID)) {
            id = (UUID) getArguments().getSerializable(KEY_ID);
        }

        presenter = new ContactDialogPresenterImpl(((SendKinView) getActivity()).getSendKinPresenter(), id);
        presenter.onAttach(this);
        initView(view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(getActivity() instanceof SendKinView)) {
            getActivity().finish();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void setNewLayout() {
        title.setText(R.string.save_a_new_address);
        deleteIcon.setVisibility(View.GONE);
        nameInput.setText("");
        addressInput.setText("");
        deleteIcon.setVisibility(View.GONE);
        addressNotValidWarning.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setEditLayout(String name, String address) {
        title.setText(R.string.edit_address);
        nameInput.setText(name);
        addressInput.setText(address);
        deleteIcon.setVisibility(View.VISIBLE);
        deleteIcon.setVisibility(View.VISIBLE);
        addressNotValidWarning.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showNameValidity(boolean isValid) {
        if (isValid) {
            nameInput.clearError();
        } else {
            nameInput.showError();
        }
    }

    @Override
    public void showAddressValidity(boolean isValid, boolean errorDetails) {
        if (isValid) {
            addressInput.clearError();
            addressNotValidWarning.setVisibility(View.INVISIBLE);
        } else {
            addressInput.showError();
            if (errorDetails) {
                addressNotValidWarning.setVisibility(View.VISIBLE);
            } else {
                addressNotValidWarning.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void setConfirmDeleteLayout(@NonNull String nickName) {
        clearAddressErrors();
        title.setText(R.string.delete_address);
        saveBtn.setText(getString(R.string.delete));
        deleteSubtitle.setText(getString(R.string.delete_subtitle, nickName));
        deleteSubtitle.setGravity(Gravity.CENTER);
        deleteIcon.setVisibility(View.GONE);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onConfirmDeleteClicked();
            }
        });
        nameAddressLayout.setVisibility(View.GONE);
    }

    private void initView(View view) {
        saveBtn = view.findViewById(R.id.saveBtn);
        nameInput = view.findViewById(R.id.contactName);
        addressInput = view.findViewById(R.id.address);
        deleteIcon = view.findViewById(R.id.deleteIcon);
        title = view.findViewById(R.id.dialogTitle);
        deleteSubtitle = view.findViewById(R.id.contactNameTitle);
        addressNotValidWarning = view.findViewById(R.id.addressInvalidError);
        nameInput.addTextChangedListener(nameWatcher);
        addressInput.addTextChangedListener(addressWatcher);
        nameAddressLayout = view.findViewById(R.id.nameAddressLayout);
        setCancelable(false);

        view.findViewById(R.id.cancelSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onCancelClicked();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onSaveClicked(name, address);
            }
        });

        view.findViewById(R.id.deleteIcon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onDeleteClicked();
            }
        });
        view.findViewById(R.id.paste).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                String pasteData = Utils.getPasteString(clipboard);
                if (pasteData != null) {
                    address = pasteData;
                    addressInput.setText(pasteData);
                }
            }
        });
    }

    private void clearAddressErrors() {
        addressInput.clearError();
        addressNotValidWarning.setVisibility(View.INVISIBLE);
    }
}
