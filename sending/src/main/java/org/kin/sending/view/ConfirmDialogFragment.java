package org.kin.sending.view;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.kin.sending.R;
import org.kin.sending.presenter.SendKinPresenter;

public class ConfirmDialogFragment extends DialogFragment implements StartSendingView {
    public static final String TAG = ConfirmDialogFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (!(getActivity() instanceof SendKinView)) {
            getActivity().finish();
        }
        final SendKinPresenter sendKinPresenter = ((SendKinView) getActivity()).getSendKinPresenter();

        View view = inflater.inflate(R.layout.confirm_dialog_fragment, container, false);
        final SendKinView sendKinView = (SendKinView) getActivity();
        ((TextView) view.findViewById(R.id.recipientAddress)).setText(sendKinPresenter.getRecipientAddress());
        ((TextView) view.findViewById(R.id.amount)).setText("" + sendKinPresenter.getAmount());
        view.findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendKinPresenter.onNext();
            }
        });
        return view;
    }
}
