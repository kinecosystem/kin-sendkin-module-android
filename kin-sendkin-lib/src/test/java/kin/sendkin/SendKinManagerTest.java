package kin.sendkin;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;

import kin.sdk.KinAccount;
import kin.sdk.KinClient;
import kin.sendkin.exceptions.KinSendInitException;

import static org.mockito.Mockito.mock;

public class SendKinManagerTest {

    KinSenderManager manager;
    private KinClient kinClientMock;
    private KinAccount kinAccountMock;
    private Context contextMock;


    @Before
    public void initMocks() {
        kinClientMock = mock(KinClient.class);
        kinAccountMock = mock(KinAccount.class);
        contextMock = mock(Context.class);
    }

    @Test(expected = KinSendInitException.class)
    public void constructor2nullsTest() throws KinSendInitException {
        manager = new KinSenderManager(null, null);
        manager.startSendingContactFlow(contextMock);
    }

    @Test(expected = KinSendInitException.class)
    public void constructorKinClientNullTest() throws KinSendInitException {
        manager = new KinSenderManager(null, kinAccountMock);
        manager.startSendingContactFlow(contextMock);
    }

    @Test(expected = KinSendInitException.class)
    public void startSendingContactFlowTest() throws KinSendInitException {
        manager = new KinSenderManager(kinClientMock, kinAccountMock);
        manager.startSendingContactFlow(null);
    }
}