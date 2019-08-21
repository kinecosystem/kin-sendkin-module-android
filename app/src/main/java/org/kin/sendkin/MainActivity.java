package org.kin.sendkin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.kin.sending.KinSenderManager;
import org.kin.sending.events.SendKinEventsListener;
import org.kin.sending.events.SendKinPages;

import kin.sdk.Environment;
import kin.sdk.KinAccount;
import kin.sdk.KinClient;
import kin.sdk.exception.CreateAccountException;

public class MainActivity extends AppCompatActivity {

    private static String TAG = MainActivity.class.getName();
    private KinSenderManager kinSenderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String appId = "abc";
        final KinClient kinClient = new KinClient(this, Environment.TEST, appId, "");
        if (!kinClient.hasAccount()) {
            try {
                kinClient.addAccount();
            } catch (CreateAccountException e) {
                e.printStackTrace();
            }
        }

        final KinAccount kinAccount = kinClient.getAccount(0);
        final String publicAddress = kinAccount.getPublicAddress();
        Log.d(TAG, "sample app public address " + publicAddress);

        kinSenderManager = new KinSenderManager(kinClient, kinAccount, new SendKinEventsListener() {
            @Override
            public void onViewPage(@NonNull SendKinPages page) {
                Log.d(TAG, "Event onViewPage " + page.name());
            }

            @Override
            public void onTransferFailed() {
                Log.d(TAG, "Event onTransferFailed");
            }

            @Override
            public void onTransferSuccess(@NonNull String transactionId) {
                Log.d(TAG, "Event onTransferSuccess transactionId " + transactionId);
            }

            @Override
            public void onTransactionTimeout() {
                Log.d(TAG, "Event onTransactionTimeout");

            }
        });

        findViewById(R.id.sendKinBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSendKinFlow();
            }
        });
    }

    private void startSendKinFlow() {
        kinSenderManager.startSendingContactFlow(this);
    }
}
