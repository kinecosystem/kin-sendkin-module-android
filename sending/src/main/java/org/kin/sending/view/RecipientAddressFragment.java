package org.kin.sending.view;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.kin.sending.R;
import org.kin.sending.presenter.RecipientAddressPresenter;
import org.kin.sending.presenter.RecipientAddressPresenterImpl;
import org.kin.sending.presenter.SendKinPresenter;
import org.kin.sendkin.core.view.SendKinEditText;

public class RecipientAddressFragment extends Fragment implements RecipientAddressView {

    public static final String TAG = RecipientAddressFragment.class.getSimpleName();

    private SendKinEditText inputRecipientAddress;
    private TextView errorInfo;

    public static RecipientAddressFragment getInstance() {
        RecipientAddressFragment fragment = new RecipientAddressFragment();
        return fragment;
    }

    private RecipientAddressPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (!(getActivity() instanceof SendKinView)) {
            getActivity().finish();
        }
        SendKinPresenter sendKinPresenter = ((SendKinView) getActivity()).getSendKinPresenter();
        View root = inflater.inflate(R.layout.recepient_address_page, container, false);
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        presenter = new RecipientAddressPresenterImpl(sendKinPresenter, clipboard);
        presenter.onAttach(this);
        initViews(root);
        return root;
    }

    private void initViews(View root) {
        root.findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onNextClicked();
            }
        });
        inputRecipientAddress = root.findViewById(R.id.recipientAddress);
        inputRecipientAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                presenter.setRecipientAddress(s.toString());
            }
        });
        errorInfo = root.findViewById(R.id.errorInfo);
        root.findViewById(R.id.paste).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onPasteClicked();
            }
        });
        root.findViewById(R.id.showAddress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onShowPublicAddressClicked();
            }
        });
        root.findViewById(R.id.publicAddressInfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onWhatIsPublicAddressClicked();
            }
        });
    }

    @Override
    public void showAddressValidity(boolean isValid, boolean showDetail) {
        if (isValid) {
            inputRecipientAddress.clearError();
            errorInfo.setVisibility(View.GONE);
        } else {
            inputRecipientAddress.showError();
            if (showDetail) {
                errorInfo.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void updateReceiverAddress(String pasteAddress) {
        inputRecipientAddress.setText(pasteAddress);
        //TODO scroll to the end
    }
}
