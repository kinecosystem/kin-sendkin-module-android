package org.kin.sending.view;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.kin.sending.R;
import org.kin.sending.presenter.SendKinPresenterImpl;
import org.kin.sendkin.core.model.KinAccountUtils;

import kin.sdk.KinAccount;
import kin.sdk.KinClient;

public class SendKinActivity extends AppCompatActivity implements SendKinView {
    private SendKinPresenterImpl presenter;


    public static Intent getIntent(@NonNull Context context, @NonNull KinClient kinClient, @NonNull String publicAddress) {
        Intent intent = new Intent(context, SendKinActivity.class);
        return KinAccountUtils.saveKinAccountData(intent, kinClient, publicAddress);
    }

    protected @LayoutRes
    int getContentLayout() {
        return R.layout.send_kin_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentLayout());
        final KinAccount kinAccount = KinAccountUtils.loadAccountClientData(this, getIntent());
        if (kinAccount != null) {
            presenter = new SendKinPresenterImpl(kinAccount);
            presenter.onAttach(this);
            initViews();
        }
    }

    @Override
    public void updateBalance(int balance) {
        ((TextView) findViewById(R.id.balance)).setText("balance:" + balance);
    }

    @Override
    public void showPublicAddressPopup(@NonNull final String publicAddress) {
        //will be move to another alert manager
        new AlertDialog.Builder(this)
                .setTitle("Public Address")
                .setMessage("your address " + publicAddress)

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.copy, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("public address", publicAddress);
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(SendKinActivity.this, "Public address coppied", Toast.LENGTH_LONG).show();
                        // Continue with delete operation
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    @Override
    public void updateStatus(String status) {
        ((TextView)findViewById(R.id.status)).setText(status);
    }

    @Override
    public void onRecipientAddressNotValid() {
        updateStatus("address not kin address");
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    private void initViews() {
        findViewById(R.id.address).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onShowPublicAddressClicked();
            }
        });

        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onSendClicked();
            }
        });

        ((EditText)findViewById(R.id.to)).addTextChangedListener(presenter.getAddressTextWatcher());
    }
}
