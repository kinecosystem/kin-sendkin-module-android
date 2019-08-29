package kin.sendkin.core.exceptions;

public class SendingKinError extends Exception {
    public SendingKinError(String receiverAddress, int amount, String error){
        super("Can not send " + amount + " Kin to address "+ receiverAddress + " error " + error);
    }
}
