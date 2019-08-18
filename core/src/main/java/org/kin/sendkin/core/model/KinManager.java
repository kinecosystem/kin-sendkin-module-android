package org.kin.sendkin.core.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.kin.sendkin.core.callbacks.*;
import org.kin.sendkin.core.exceptions.*;

public interface KinManager {
    String getPublicAddress();

    void getCurrentBalance(final BalanceCallback callback);

    int getCurrentBalanceSync() throws BalanceError;

    void sendKin(@NonNull String receiverAddress, int amount, @NonNull final SendingKinCallback callback);

    void sendKin(@NonNull final String receiverAddress, final int amount, @Nullable String memo, @NonNull final SendingKinCallback callback);

    //return transaction id
    String sendKinSync(@NonNull String receiverAddress, int amount)  throws SendingKinError;

    //return transaction id
    String sendKinSync(@NonNull String receiverAddress, int amount, @Nullable String memo) throws SendingKinError;

}
