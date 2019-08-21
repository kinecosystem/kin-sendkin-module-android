package org.kin.sending.view;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.kin.sending.R;

public class PublicAddressInfoDialogFragment extends DialogFragment {

    public static final String TAG = PublicAddressInfoDialogFragment.class.getSimpleName();

    public static PublicAddressInfoDialogFragment getInstance(){
        PublicAddressInfoDialogFragment dialog = new PublicAddressInfoDialogFragment();
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View customLayout = inflater.inflate(R.layout.public_address_info_dialog, null);
        customLayout.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return customLayout;
    }
}
