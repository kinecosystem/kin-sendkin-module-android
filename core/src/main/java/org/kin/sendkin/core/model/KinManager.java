package org.kin.sendkin.core.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.kin.sendkin.core.callbacks.*;
import org.kin.sendkin.core.exceptions.*;

public interface KinManager {
    String getPublicAddress() throws AccountError;

    void getCurrentBalance(final BalanceCallback callback) throws AccountError;

    int getCurrentBalanceSync() throws BalanceError, AccountError;

    void sendKin(@NonNull String receiverAddress, int amount, @NonNull final SendingKinCallback callback)throws AccountError;

    void sendKin(@NonNull final String receiverAddress, final int amount, @Nullable String memo, @NonNull final SendingKinCallback callback)throws AccountError;

    //return transaction id
    String sendKinSync(@NonNull String receiverAddress, int amount)  throws SendingKinError, AccountError;

    //return transaction id
    String sendKinSync(@NonNull String receiverAddress, int amount, @Nullable String memo) throws SendingKinError, AccountError;

    Boolean isValidAddress(String address);

    void reset();
}
