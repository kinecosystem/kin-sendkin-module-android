package org.kin.sendkin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.kin.sending.KinSenderManager;

import kin.sdk.Environment;
import kin.sdk.KinAccount;
import kin.sdk.KinClient;

public class MainActivity extends AppCompatActivity {

    private KinSenderManager kinSenderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //GBNY5GQ6WOG5JU4JZEPJ4WZIMYMC5HGPYLSXT7S3GBDJ2S3CM4NPTBNC

        //receiver address GDN2L6SNFA575JGGHXZMGETLQBHJAI5QVOZZSSHWP44Y24LAG7DJLF4T

        String appId = "abc";
        final KinClient kinClient = new KinClient(this, Environment.TEST, appId, "sampleStrKey");
        // kinClient.addAccount();
        KinAccount kinAccount = kinClient.getAccount(0);
        Log.d("####", "###### address " + kinAccount.getPublicAddress());

        kinSenderManager = new KinSenderManager(kinClient, kinAccount);
        findViewById(R.id.sendKinBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kinSenderManager.launchSendingActivity(MainActivity.this);
            }
        });


    }
}
