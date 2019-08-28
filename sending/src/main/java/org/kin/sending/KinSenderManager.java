package org.kin.sending;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.kin.sending.events.EventsManager;
import org.kin.sending.events.SendKinEventsListener;
import org.kin.sending.view.SendKinActivity;

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
