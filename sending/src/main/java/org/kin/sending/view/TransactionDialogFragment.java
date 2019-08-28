package org.kin.sending.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.Group;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.kin.sending.R;
import org.kin.sending.presenter.SendKinPresenter;
import org.kin.sendkin.core.model.RecipientContact;
import org.kin.sendkin.core.view.BaseWideDialogFragment;
import org.kin.sendkin.core.view.SendKinEditText;
import org.kin.sendkin.core.view.Utils;

public class TransactionDialogFragment extends BaseWideDialogFragment {

    private static final String KEY_STATUS = "KEY_STATUS";
    public static final String TAG = TransactionDialogFragment.class.getSimpleName();
    private TextView dialogErrorText, recipientAddress, amount, amountSending, errorText, contactNickName, shortAddress;
    private ImageView dialogErrorIcon;
    private SendKinPresenter sendKinPresenter;
    private Group confirmGroup, sendingGroup, completeGroup, errorGroup;
    private String contactName = "";
    private SendKinEditText nameInput;
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
            contactName = editable.toString().trim();
        }
    };

    public static TransactionDialogFragment getInstance(@Navigator.SendKinSteps int status) {
        TransactionDialogFragment dialog = new TransactionDialogFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_STATUS, status);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.transaction_dialog_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sendKinPresenter = ((SendKinView) getActivity()).getSendKinPresenter();
        view.findViewById(R.id.cancelSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancel();
            }
        });
        view.findViewById(R.id.cancelSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancel();
            }
        });
        view.findViewById(R.id.okErrorBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancel();
            }
        });

        view.findViewById(R.id.sendBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNext();
            }
        });
        view.findViewById(R.id.saveBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValidName = sendKinPresenter.setContactName(contactName);
                if (isValidName) {
                    sendKinPresenter.saveNewContact();
                    dismiss();
                    onNext();
                } else {
                    nameInput.showError();
                }
            }
        });
        nameInput = view.findViewById(R.id.contactName);
        nameInput.addTextChangedListener(nameWatcher);
        contactNickName = view.findViewById(R.id.contactNickName);
        shortAddress = view.findViewById(R.id.shortAddress);
        recipientAddress = view.findViewById(R.id.recipientAddress);
        confirmGroup = view.findViewById(R.id.groupConfirm);
        sendingGroup = view.findViewById(R.id.groupSending);
        completeGroup = view.findViewById(R.id.completeGroup);
        errorGroup = view.findViewById(R.id.errorGroup);
        errorText = view.findViewById(R.id.errorText);
        dialogErrorText = view.findViewById(R.id.dialogSendErrorTitle);
        amount = view.findViewById(R.id.amount);
        amountSending = view.findViewById(R.id.sendingAmount);
        dialogErrorIcon = view.findViewById(R.id.dialogErrorIcon);

        final int status = getArguments().getInt(KEY_STATUS);
        setStep(status);
        setCancelable(false);
    }

    public void setStep(@Navigator.SendKinSteps int step) {
        confirmGroup.setVisibility(View.GONE);
        sendingGroup.setVisibility(View.GONE);
        completeGroup.setVisibility(View.GONE);
        errorGroup.setVisibility(View.GONE);
        switch (step) {
            case Navigator.STEP_CONFIRM:
                RecipientContact chosenContact = sendKinPresenter.getChosenContact();
                if (chosenContact != null) {
                    contactNickName.setText(chosenContact.getName());
                    contactNickName.setVisibility(View.VISIBLE);
                    recipientAddress.setText("");
                    shortAddress.setText(Utils.getAddressShortenFormat(sendKinPresenter.getRecipientAddress()));
                    shortAddress.setVisibility(View.VISIBLE);
                    recipientAddress.setVisibility(View.GONE);
                } else {
                    contactNickName.setText("");
                    contactNickName.setVisibility(View.GONE);
                    shortAddress.setVisibility(View.GONE);
                    recipientAddress.setVisibility(View.VISIBLE);
                    shortAddress.setText("");
                    recipientAddress.setText(sendKinPresenter.getRecipientAddress());
                }
                amount.setText(sendKinPresenter.getAmount() + "");
                confirmGroup.setVisibility(View.VISIBLE);
                break;
            case Navigator.STEP_START_TRANSFER:
                amountSending.setText(sendKinPresenter.getAmount() + "");
                sendingGroup.setVisibility(View.VISIBLE);
                break;
            case Navigator.STEP_TRANSFER_COMPLETE:
                if (sendKinPresenter.getChosenContact() != null) {
                    dialogErrorText.setText(getResources().getString(R.string.kin_sent_successfully_title));
                    errorText.setText(R.string.kin_sent_successfully_title);
                    dialogErrorIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_send_icon));
                    errorGroup.setVisibility(View.VISIBLE);
                } else {
                    completeGroup.setVisibility(View.VISIBLE);
                }
                break;
            case Navigator.STEP_TRANSFER_FAILED:
                dialogErrorText.setText(getResources().getString(R.string.transaction_failed_title));
                errorText.setText(R.string.transaction_error_text);
                dialogErrorIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_error_icon));
                errorGroup.setVisibility(View.VISIBLE);
                break;
            case Navigator.STEP_TRANSFER_TIMEOUT:
                dialogErrorText.setText(getResources().getString(R.string.connection_timedout_title));
                errorText.setText(R.string.transaction_timeout_text);
                dialogErrorIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_warning_icon));
                errorGroup.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void onCancel() {
        dismiss();
        sendKinPresenter.onPrevious();
    }

    private void onNext() {
        sendKinPresenter.onNext();
    }
}
