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

public class MainActivity extends AppCompatActivity {

    private static String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //GCCLBNMWRUKPCWV4J6H42KAQQ6GSOG6YKRNONAT3QDOGRH3T3EQGSXJR

        //GDK57YFZRKNKJTRLE3CZ2GNQCSRC5NPJQWIEMD43M7V6ZIHNARGBIMFI

        //GB5HR4O3RTH6EHFAMJTL2ZHGP54K2EHUTFNUIS43WTZUOWRHQMGLFNHH

        String appId = "abc";
        final KinClient kinClient = new KinClient(this, Environment.TEST, appId, "");
//        try {
//            kinClient.addAccount();
//        } catch (CreateAccountException e) {
//            e.printStackTrace();
//        }
        final KinAccount kinAccount = kinClient.getAccount(0);
        final String publicAddress = kinAccount.getPublicAddress();
        Log.d(TAG, " sample app public address " + publicAddress);

        final KinSenderManager kinSenderManager = new KinSenderManager(kinClient, kinAccount, new SendKinEventsListener() {
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
                try {
                    kinSenderManager.startSendingContactFlow(MainActivity.this);
                } catch (Exception e) {
                    Log.e(TAG, "startSendingContactFlow error " + e.getMessage());
                }
            }
        });
    }
}
