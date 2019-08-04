package org.kin.sendkin.core.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.kin.sendkin.core.Consts;
import org.kin.sendkin.core.callbacks.BalanceCallback;
import org.kin.sendkin.core.callbacks.SendingKinCallback;
import org.kin.sendkin.core.exceptions.AccountError;
import org.kin.sendkin.core.exceptions.BalanceError;
import org.kin.sendkin.core.exceptions.SendingKinError;

import java.math.BigDecimal;

import kin.sdk.Balance;
import kin.sdk.KinAccount;
import kin.sdk.Transaction;
import kin.sdk.TransactionId;
import kin.sdk.exception.OperationFailedException;
import kin.utils.ResultCallback;

public class KinManagerImpl implements KinManager {

    private KinAccount kinAccount;
    private static final int FEE = 100;

    public KinManagerImpl(@NonNull KinAccount kinAccount) {
        this.kinAccount = kinAccount;
    }

    @Override
    public String getPublicAddress() throws AccountError {
        checkAccountValidity();
        return kinAccount.getPublicAddress();
    }

    @Override
    public void getCurrentBalance(final BalanceCallback callback) throws AccountError {
        checkAccountValidity();
        kinAccount.getBalance().run(new ResultCallback<Balance>() {
            @Override
            public void onResult(Balance balance) {
                callback.onBalanceReceived(balance.value().intValue());
            }

            @Override
            public void onError(Exception e) {
                callback.onBalanceError(e.getMessage());
            }
        });
    }

    @Override
    public int getCurrentBalanceSync() throws BalanceError, AccountError {
        checkAccountValidity();
        try {
            return kinAccount.getBalanceSync().value().intValue();
        } catch (OperationFailedException e) {
            throw new BalanceError(e.getMessage());
        }
    }

    @Override
    public void sendKin(@NonNull String receiverAddress, int amount, @NonNull final SendingKinCallback callback) throws AccountError {
        sendKin(receiverAddress, amount, null, callback);
    }

    @Override
    public void sendKin(@NonNull final String receiverAddress, final int amount, @Nullable String memo, @NonNull final SendingKinCallback callback) throws AccountError {
        checkAccountValidity();
        kinAccount.buildTransaction(receiverAddress, new BigDecimal(amount), FEE, memo).run(new ResultCallback<Transaction>() {
            @Override
            public void onResult(final Transaction transaction) {
                kinAccount.sendTransaction(transaction).run(new ResultCallback<TransactionId>() {
                    @Override
                    public void onResult(TransactionId transactionId) {
                        callback.onSendKinCompleted(transactionId.id(), receiverAddress, amount);
                    }

                    @Override
                    public void onError(Exception e) {
                        callback.onSendKinFailed(e.getMessage(), receiverAddress, amount);
                    }
                });
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    //return transaction id
    @Override
    public String sendKinSync(@NonNull String receiverAddress, int amount) throws SendingKinError, AccountError {
        return sendKinSync(receiverAddress, amount, null);
    }

    //return transaction id
    @Override
    public String sendKinSync(@NonNull String receiverAddress, int amount, @Nullable String memo) throws SendingKinError, AccountError {
        checkAccountValidity();
        try {
            final Transaction transaction = kinAccount.buildTransactionSync(receiverAddress, new BigDecimal(amount), FEE, memo);
            return kinAccount.sendTransactionSync(transaction).id();
        } catch (OperationFailedException e) {
            e.printStackTrace();
            throw new SendingKinError(receiverAddress, amount, e.getMessage());
        }
    }

    @Override
    public Boolean isValidAddress(String address) {
        return address!= null && address.length() == Consts.ADDRESS_LENGTH && address.toUpperCase().startsWith(Consts.ADDRESS_PREFIX);
    }

    private boolean isAccountValid() {
        return kinAccount != null;//Todo check status?
    }

    private void checkAccountValidity() throws AccountError {
        if (!isAccountValid()) throw new AccountError("Account must be init");
    }

    @Override
    public void reset() {
        kinAccount = null;
    }
}
