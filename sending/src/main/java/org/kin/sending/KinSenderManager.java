package org.kin.sending;

import android.content.Context;
import android.support.annotation.NonNull;

import org.kin.sending.view.SendKinActivity;

import kin.sdk.KinAccount;
import kin.sdk.KinClient;

public class KinSenderManager {

    private KinClient kinClient;
    private KinAccount kinAccount;

    public void init(@NonNull KinClient kinClient, @NonNull KinAccount kinAccount) {
        this.kinAccount = kinAccount;
        this.kinClient = kinClient;
    }

    public void startSendingContactFlow(Context context) throws Exception {
        if (kinClient != null && kinAccount != null) {
            context.startActivity(SendKinActivity.getIntent(context, kinClient, kinAccount.getPublicAddress()));
        } else {
            throw new Exception("must call first init(@NonNull KinClient kinClient, @NonNull KinAccount kinAccount)");
        }
    }

}
