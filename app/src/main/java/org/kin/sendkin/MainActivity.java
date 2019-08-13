package org.kin.sendkin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.kin.sending.KinSenderManager;

import kin.sdk.Environment;
import kin.sdk.KinAccount;
import kin.sdk.KinClient;
import kin.sdk.exception.CreateAccountException;

public class MainActivity extends AppCompatActivity {

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
        Log.d("MainActivity", " sample app public address " + publicAddress);

        //module usage
        final KinSenderManager kinSenderManager = new KinSenderManager();
        kinSenderManager.init(kinClient, kinAccount);

        findViewById(R.id.sendKinBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    kinSenderManager.startSendingContactFlow(MainActivity.this);
                } catch (Exception e) {
                    Log.e("MainActivity", "startSendingContactFlow error " + e.getMessage());
                }
            }
        });
    }
}
