package kin.sendkin;

import android.content.Context;
import android.content.Intent;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.internal.matchers.Any;
import org.mockito.internal.verification.Times;
import org.mockito.stubbing.OngoingStubbing;

import kin.sdk.KinAccount;
import kin.sdk.KinClient;
import kin.sendkin.events.SendKinEventsListener;
import kin.sendkin.exceptions.KinSendInitException;
import kin.sendkin.view.Navigator;
import kin.sendkin.view.SendKinActivity;
import kin.sendkin.view.SendKinView;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
