package kin.sendkin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import kin.sendkin.events.EventsManager;
import kin.sendkin.events.SendKinEventsListener;
import kin.sendkin.view.SendKinActivity;

import kin.sdk.KinAccount;
import kin.sdk.KinClient;

public class KinSenderManager {

    private KinClient kinClient;
    private KinAccount kinAccount;
    private EventsManager eventsManager;

    public KinSenderManager(@NonNull KinClient kinClient, @NonNull KinAccount kinAccount) {
        this(kinClient, kinAccount, null);
    }

    public KinSenderManager(@NonNull KinClient kinClient, @NonNull KinAccount kinAccount, @Nullable SendKinEventsListener listener) {
        this.kinAccount = kinAccount;
        this.kinClient = kinClient;
        this.eventsManager = EventsManager.getInstance();
        if (listener != null) {
            eventsManager.setEventsListener(listener);
        }
    }

    public void startSendingContactFlow(Context context) {
        context.startActivity(SendKinActivity.getIntent(context, kinClient, kinAccount.getPublicAddress()));
    }

}
