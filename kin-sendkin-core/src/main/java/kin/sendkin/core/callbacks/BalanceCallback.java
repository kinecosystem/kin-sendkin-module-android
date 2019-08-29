package kin.sendkin.core.callbacks;

public interface BalanceCallback {
    void onBalanceReceived(int balance);
    void onBalanceError(String error);
}
