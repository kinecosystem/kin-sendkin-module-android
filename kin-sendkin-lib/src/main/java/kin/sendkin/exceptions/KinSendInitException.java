package kin.sendkin.exceptions;

public class KinSendInitException extends Exception {

    public KinSendInitException(){
        super("KinClient nor KinAccount cant be null - check KinSenderManager constructor");
    }
}
