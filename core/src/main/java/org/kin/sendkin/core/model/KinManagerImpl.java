package org.kin.sendkin.core.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import org.kin.sendkin.core.callbacks.BalanceCallback;
import org.kin.sendkin.core.callbacks.SendingKinCallback;
import org.kin.sendkin.core.exceptions.BalanceError;
import org.kin.sendkin.core.exceptions.SendingKinError;

import java.io.IOException;
import java.math.BigDecimal;

import kin.sdk.Balance;
import kin.sdk.EventListener;
import kin.sdk.KinAccount;
import kin.sdk.ListenerRegistration;
import kin.sdk.PaymentInfo;
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
    public String getPublicAddress() {
        return kinAccount.getPublicAddress();
    }

    @Override
    public void getCurrentBalance(final BalanceCallback callback) {
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
    public int getCurrentBalanceSync() throws BalanceError {
        try {
            return kinAccount.getBalanceSync().value().intValue();
        } catch (OperationFailedException e) {
            throw new BalanceError(e.getMessage());
        }
    }

    @Override
    public void sendKin(@NonNull String receiverAddress, int amount, @NonNull final SendingKinCallback callback) {
        sendKin(receiverAddress, amount, null, callback);
    }

    @Override
    public void sendKin(@NonNull final String receiverAddress, final int amount, @Nullable String memo, @NonNull final SendingKinCallback callback) {
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
                        if (e.getCause() instanceof IOException) {
                            callback.onSendKinTimeout(e.getMessage(), receiverAddress, amount);
                        } else {
                            callback.onSendKinFailed(e.getMessage(), receiverAddress, amount);
                        }
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                callback.onSendKinFailed(e.getMessage(), receiverAddress, amount);
            }
        });
    }

    //return transaction id
    @Override
    public String sendKinSync(@NonNull String receiverAddress, int amount) throws SendingKinError {
        return sendKinSync(receiverAddress, amount, null);
    }

    //return transaction id
    @Override
    public String sendKinSync(@NonNull String receiverAddress, int amount, @Nullable String memo) throws SendingKinError {
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
        return KinAccountUtils.isValidPublicAddress(address);
    }

    @Override
    public void reset() {
        kinAccount = null;
    }

    private void testListeners(KinAccount kinAccount) {
        ListenerRegistration listenerPayment = kinAccount
                .addPaymentListener(new EventListener<PaymentInfo>() {
                    @Override
                    public void onEvent(PaymentInfo payment) {
                        Log.d("####", String
                                .format("######LISTENERS!!!!payment event, to = %s, from = %s, amount = %s", payment.sourcePublicKey(),
                                        payment.destinationPublicKey(), payment.amount().toPlainString()));
                    }
                });

        ListenerRegistration listenerBalance = kinAccount.addBalanceListener(new EventListener<Balance>() {
            @Override
            public void onEvent(Balance balance) {
                Log.d("example", "####LISTENERS!!!!balance event, new balance is = " + balance.value().toPlainString());
            }
        });
    }
}
