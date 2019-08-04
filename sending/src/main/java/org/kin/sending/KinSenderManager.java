package org.kin.sending;

import android.content.Context;
import android.support.annotation.NonNull;

import kin.sdk.KinAccount;
import kin.sdk.KinClient;

public class KinSenderManager {

    private KinClient kinClient;
    private KinAccount kinAccount;

    public KinSenderManager(@NonNull KinClient kinClient, @NonNull KinAccount kinAccount){
        this.kinAccount = kinAccount;
        this.kinClient = kinClient;
    }

    public void launchSendingActivity(Context context){

    }


}
