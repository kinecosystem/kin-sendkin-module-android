package org.kin.sendkin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.kin.sending.KinSenderManager;

import kin.sdk.Environment;
import kin.sdk.KinAccount;
import kin.sdk.KinClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //GBNY5GQ6WOG5JU4JZEPJ4WZIMYMC5HGPYLSXT7S3GBDJ2S3CM4NPTBNC

        String appId = "abc";
        final KinClient kinClient = new KinClient(this, Environment.TEST, appId, "");
        final KinAccount kinAccount = kinClient.getAccount(0);

        //module usage
        final KinSenderManager kinSenderManager = new KinSenderManager();
        kinSenderManager.init(kinClient, kinAccount);

        findViewById(R.id.sendKinBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    kinSenderManager.startSendingContactFlow(MainActivity.this);
                } catch (Exception e) {
                }
            }
        });
    }
}
