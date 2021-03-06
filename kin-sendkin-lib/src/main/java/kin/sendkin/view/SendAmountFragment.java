package kin.sendkin.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import kin.sendkin.R;
import kin.sendkin.presenter.SendAmountPresenter;
import kin.sendkin.presenter.SendAmountPresenterImpl;
import kin.sendkin.presenter.SendKinPresenter;
import kin.sendkin.core.view.SendKinEditText;
import kin.sendkin.core.view.Utils;

public class SendAmountFragment extends Fragment implements SendAmountView {

    public static final String TAG = SendAmountFragment.class.getSimpleName();

    public static SendAmountFragment getInstance() {
        SendAmountFragment fragment = new SendAmountFragment();
        return fragment;
    }


    private SendKinEditText inputAmount;
    private TextView errorInfo;
    private SendAmountPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (!(getActivity() instanceof SendKinView)) {
            getActivity().finish();
        }
        View root = inflater.inflate(R.layout.send_amount_page, container, false);
        SendKinPresenter sendKinPresenter = ((SendKinView) getActivity()).getSendKinPresenter();
        presenter = new SendAmountPresenterImpl(sendKinPresenter);
        presenter.onAttach(this);
        initViews(root, sendKinPresenter.getRecipientAddress());
        return root;
    }

    private void initViews(View root, String recipientAddress) {
        root.findViewById(R.id.sendKin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onSendClicked();
            }
        });
        inputAmount = root.findViewById(R.id.amount);
        inputAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                presenter.setAmount(s.toString());
            }
        });
        errorInfo = root.findViewById(R.id.errorInfo);
        ((TextView) root.findViewById(R.id.recipientAddressShorten)).setText(Utils.getAddressShortenFormat(recipientAddress));
    }

    @Override
    public void showAmountValidity(boolean isValid, boolean errorDetails) {
        if (isValid) {
            inputAmount.clearError();
            errorInfo.setVisibility(View.GONE);
        } else {
            inputAmount.showError();
            if (errorDetails) {
                errorInfo.setVisibility(View.VISIBLE);
            }
        }
    }
}
