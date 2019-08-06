package org.kin.sending.view;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.kin.sending.R;

public class StartSendingDialogFragment extends DialogFragment implements StartSendingView {
    public static final String TAG = StartSendingView.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sending_dialog_fragment, container, false);

        // Do all the stuff to initialize your custom view

        return v;
    }
}
