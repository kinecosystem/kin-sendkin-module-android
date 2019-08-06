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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SendKinPresenterTest {

    String validAddress = "GBNY5GQ6WOG5JU4JZEPJ4WZIMYMC5HGPYLSXT7S3GBDJ2S3CM4NPTBNC";
    int amount = 5;
    Navigator mockNavigator = mock(Navigator.class);
    SendKinView mockView = mock(SendKinView.class);
    KinManager mockKinManager = mock(KinManager.class);
    SendKinPresenter presenter;

    @Before
    public void initMocks() {
        presenter = new SendKinPresenterImpl(mockKinManager, mockNavigator);
        presenter.onAttach(mockView);
        verify(mockNavigator, times(1)).updateStep(Navigator.STEP_RECIPIENT_ADDRESS);
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
        verify(mockNavigator, times(1)).onNext();
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

        verify(mockKinManager, times(1)).sendKin(anyString(), anyInt(), anyString(), sendKinCallback.capture());
        sendKinCallback.getValue().onSendKinCompleted(anyString(), validAddress,amount);
        verify(mockNavigator, times(1)).updateStep(Navigator.STEP_TRANSFER_COMPLETE);
        ArgumentCaptor<BalanceCallback> callbackCaptor = ArgumentCaptor.forClass(BalanceCallback.class);
        verify(mockKinManager).getCurrentBalance(callbackCaptor.capture());
    }


    @Test
    public void startTransactionFailedTest() {
        presenter.startTransaction();
        ArgumentCaptor<SendingKinCallback> sendKinCallback = ArgumentCaptor.forClass(SendingKinCallback.class);

        verify(mockKinManager, times(1)).sendKin(anyString(), anyInt(), anyString(), sendKinCallback.capture());
        sendKinCallback.getValue().onSendKinFailed(anyString(), validAddress, amount);
        verify(mockNavigator, times(1)).updateStep(Navigator.STEP_TRANSFER_FAILED);
    }


    @Test
    public void startTransactionTimeoutTest() {
        presenter.startTransaction();
        ArgumentCaptor<SendingKinCallback> sendKinCallback = ArgumentCaptor.forClass(SendingKinCallback.class);

        verify(mockKinManager, times(1)).sendKin(anyString(), anyInt(), anyString(), sendKinCallback.capture());
        sendKinCallback.getValue().onSendKinTimeout(anyString(), validAddress, amount);
        verify(mockNavigator, times(1)).updateStep(Navigator.STEP_TRANSFER_TIMEOUT);
    }



}
