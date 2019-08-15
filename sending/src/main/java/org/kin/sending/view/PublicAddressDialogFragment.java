package org.kin.sending.view;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.kin.sending.R;
import org.kin.sendkin.core.view.Utils;

public class PublicAddressDialogFragment extends DialogFragment {

    public static final String TAG = PublicAddressDialogFragment.class.getSimpleName();
    private static final String PUBLIC_ADDRESS_KEY = "PUBLIC_ADDRESS_KEY";


    public static PublicAddressDialogFragment getIntance(String publicAddress){
        PublicAddressDialogFragment dialog = new PublicAddressDialogFragment();
        Bundle args = new Bundle();
        args.putString(PUBLIC_ADDRESS_KEY, publicAddress);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final String publicAddress = getArguments().getString(PUBLIC_ADDRESS_KEY, "");
        final View customLayout = inflater.inflate(R.layout.my_public_address_dialog, null);
        ((TextView) customLayout.findViewById(R.id.publicAddress)).setText(publicAddress);
        customLayout.findViewById(R.id.copy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.saveTo((ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE), publicAddress);
                Toast.makeText(getContext(), "Address copied", Toast.LENGTH_LONG).show();
                dismiss();
            }
        });
        customLayout.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return customLayout;
    }
}
