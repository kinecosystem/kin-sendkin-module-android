package org.kin.sending.events;

import android.support.annotation.NonNull;

public class EventsManager {
    private static EventsManager instance = null;
    private SendKinEventsListener listener = null;

    public static EventsManager getInstance() {
        if (instance == null) {
            instance = new EventsManager();
        }
        return instance;
    }

    private EventsManager() {
    }

    public void setEventsListener(@NonNull SendKinEventsListener listener) {
        this.listener = listener;
    }

    public void onViewPage(@NonNull SendKinPages page) {
        if (listener != null) {
            listener.onViewPage(page);
        }
    }

    public void onTransferFailed() {
        if (listener != null) {
            listener.onTransferFailed();
        }
    }

    public void onTransferSuccess(@NonNull String transactionId) {
        if (listener != null) {
            listener.onTransferSuccess(transactionId);
        }
    }

    public void onTransactionTimeout() {
        if (listener != null) {
            listener.onTransactionTimeout();
        }
    }

}
