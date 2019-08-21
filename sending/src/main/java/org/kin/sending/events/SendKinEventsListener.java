package org.kin.sending.events;

import android.support.annotation.NonNull;

public interface SendKinEventsListener {
    void onViewPage(@NonNull SendKinPages page);
    void onTransferFailed();
    void onTransferSuccess(@NonNull String transactionId);
    void onTransactionTimeout();
}
