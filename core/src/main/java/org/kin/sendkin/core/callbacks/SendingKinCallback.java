package org.kin.sendkin.core.callbacks;

public interface SendingKinCallback {
    void onSendKinCompleted(String transactionId, String receiverAddress, int amount);
    void onSendKinFailed(String error, String receiverAddress, int amount);
    void onSendKinTimeout(String error, String receiverAddress, int amount);
}
