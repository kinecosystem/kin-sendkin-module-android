package org.kin.sending;

import org.junit.Before;
import org.junit.Test;
import org.kin.sending.presenter.SendKinPresenter;
import org.kin.sending.presenter.SendKinPresenterImpl;
import org.kin.sending.view.Navigator;
import org.kin.sending.view.SendKinView;
import org.kin.sendkin.core.callbacks.BalanceCallback;
import org.kin.sendkin.core.callbacks.SendingKinCallback;
import org.kin.sendkin.core.model.KinManager;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SendKinPresenterTest {

    final String validAddress = "GBNY5GQ6WOG5JU4JZEPJ4WZIMYMC5HGPYLSXT7S3GBDJ2S3CM4NPTBNC";
    final int amount = 5;
    Navigator mockNavigator;
    SendKinView mockView;
    KinManager mockKinManager;
    SendKinPresenter presenter;

    @Before
    public void initMocks() {
        mockNavigator = mock(Navigator.class);
        mockView = mock(SendKinView.class);
        mockKinManager = mock(KinManager.class);
        presenter = new SendKinPresenterImpl(mockKinManager, mockNavigator);
        presenter.onAttach(mockView);
    }

    @Test
    public void onResumeTestBalanceReturned() {
        int balanceReturned = 100;
        presenter.onResume();
        ArgumentCaptor<BalanceCallback> callbackCaptor = ArgumentCaptor.forClass(BalanceCallback.class);
        verify(mockKinManager).getCurrentBalance(callbackCaptor.capture());
        callbackCaptor.getValue().onBalanceReceived(balanceReturned);
        assertEquals(presenter.getBalance(), balanceReturned);
        verify(mockView).updateBalance(balanceReturned);
    }

    @Test
    public void onResumeTestBalanceError() {
        presenter.onResume();
        ArgumentCaptor<BalanceCallback> callbackCaptor = ArgumentCaptor.forClass(BalanceCallback.class);
        verify(mockKinManager).getCurrentBalance(callbackCaptor.capture());
        callbackCaptor.getValue().onBalanceError("some error");
        assertEquals(presenter.getBalance(), 0);
    }

    @Test
    public void onNextTest() {
        presenter.onNext();
        verify(mockNavigator, times(2)).onNext();
    }

    @Test
    public void onNextStartTransferTest() {
        when(mockNavigator.shouldStartTransfer()).thenReturn(true);
        ArgumentCaptor<SendingKinCallback> callbackCaptor = ArgumentCaptor.forClass(SendingKinCallback.class);

        presenter.onNext();
        verify(mockNavigator, times(2)).onNext();
        verify(mockKinManager).sendKin(anyString(), anyInt(), callbackCaptor.capture());
    }


    @Test
    public void onPreviousTest() {
        presenter.onPrevious();
        verify(mockNavigator, times(1)).onPrevious();
    }

    @Test
    public void onShowPublicAddressDialogClickedTest() {
        when(mockKinManager.getPublicAddress()).thenReturn(validAddress);
        presenter.onShowPublicAddressDialogClicked();
        verify(mockView, times(1)).showPublicAddressDialog(validAddress);
    }

    @Test
    public void onShowWhatIsPublicAddressDialogClickedTest() {
        presenter.onShowWhatIsPublicAddressDialogClicked();
        verify(mockView, times(1)).showWhatIsPublicAddressDialog();
    }


    @Test
    public void onCloseClickedTest() {
        presenter.onCloseClicked();
        verify(mockView, times(1)).onClose();
    }


    @Test
    public void onBackClickedTest() {
        presenter.onBackClicked();
        verify(mockNavigator, times(1)).onPrevious();
    }

    @Test
    public void startTransactionCompleteTest() {
        presenter.startTransaction();
        ArgumentCaptor<SendingKinCallback> sendKinCallback = ArgumentCaptor.forClass(SendingKinCallback.class);

        verify(mockKinManager, times(1)).sendKin(anyString(), anyInt(), sendKinCallback.capture());
        sendKinCallback.getValue().onSendKinCompleted(anyString(), validAddress, amount);
        verify(mockNavigator, times(1)).updateStep(Navigator.STEP_TRANSFER_COMPLETE);
        ArgumentCaptor<BalanceCallback> callbackCaptor = ArgumentCaptor.forClass(BalanceCallback.class);
        verify(mockKinManager).getCurrentBalance(callbackCaptor.capture());
    }

    @Test
    public void startTransactionFailedTest() {
        presenter.startTransaction();
        ArgumentCaptor<SendingKinCallback> sendKinCallback = ArgumentCaptor.forClass(SendingKinCallback.class);
        verify(mockKinManager, times(1)).sendKin(anyString(), anyInt(), sendKinCallback.capture());
        sendKinCallback.getValue().onSendKinFailed(anyString(), validAddress, amount);
        verify(mockNavigator, times(1)).updateStep(Navigator.STEP_TRANSFER_FAILED);
    }


    @Test
    public void startTransactionTimeoutTest() {
        presenter.startTransaction();
        ArgumentCaptor<SendingKinCallback> sendKinCallback = ArgumentCaptor.forClass(SendingKinCallback.class);
        verify(mockKinManager, times(1)).sendKin(anyString(), anyInt(), sendKinCallback.capture());
        sendKinCallback.getValue().onSendKinTimeout(anyString(), validAddress, amount);
        verify(mockNavigator, times(1)).updateStep(Navigator.STEP_TRANSFER_TIMEOUT);
    }

    @Test
    public void hasEnoughKinTest() {
        presenter.onResume();
        ArgumentCaptor<BalanceCallback> callbackCaptor = ArgumentCaptor.forClass(BalanceCallback.class);
        verify(mockKinManager).getCurrentBalance(callbackCaptor.capture());
        callbackCaptor.getValue().onBalanceReceived(100);
        assertTrue(presenter.hasEnoughKin(0));
        assertTrue(presenter.hasEnoughKin(99));
        assertTrue(presenter.hasEnoughKin(100));
        assertTrue(!presenter.hasEnoughKin(101));
        assertTrue(!presenter.hasEnoughKin(-1));
    }

    @Test
    public void setContactNamTest() {
        assertFalse(presenter.setContactName(""));
        //TODO check
        //assertFalse(presenter.setContactName(" "));

        assertTrue(presenter.setContactName("a"));
        assertTrue(presenter.setContactName("abcd"));
        assertTrue(presenter.setContactName("Mr Bin"));
    }

    @Test
    public void saveContactTest() {
        //presenter.saveContact();
        //TODO
    }
}
