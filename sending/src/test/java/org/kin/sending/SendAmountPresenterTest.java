package org.kin.sending;

import org.junit.Before;
import org.junit.Test;
import org.kin.sending.presenter.SendAmountPresenter;
import org.kin.sending.presenter.SendAmountPresenterImpl;
import org.kin.sending.presenter.SendKinPresenter;
import org.kin.sending.view.SendAmountView;
import org.kin.sendkin.core.callbacks.BalanceCallback;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SendAmountPresenterTest {
    final int amount = 100;
    SendKinPresenter sendKinPresenter;
    SendAmountView mockView;
    SendAmountPresenter presenter;

    @Before
    public void initMocks() {
        sendKinPresenter = mock(SendKinPresenter.class);
        when(sendKinPresenter.hasEnoughKin(amount)).thenReturn(true);
        mockView = mock(SendAmountView.class);
        presenter = new SendAmountPresenterImpl(sendKinPresenter);
        presenter.onAttach(mockView);
        when(sendKinPresenter.getCurrentBalance()).thenReturn(new Integer(amount));
    }

    @Test
    public void onSendClickedTestEmpty() {
        presenter.setAmount("");
        verify(mockView, times(1)).showAmountValidity(true, false);
        presenter.onSendClicked();
        verify(mockView, times(1)).showAmountValidity(false, false);
    }

    @Test
    public void onSendClickedTestZero() {
        presenter.setAmount("0");
        verify(mockView, times(1)).showAmountValidity(true, false);
        presenter.onSendClicked();
        verify(mockView, times(1)).showAmountValidity(false, false);
    }

    @Test
    public void onSendClickedTest() {
        when(sendKinPresenter.hasEnoughKin(99)).thenReturn(true);
        presenter.setAmount("99");
        verify(mockView, times(1)).showAmountValidity(true, false);
        presenter.onSendClicked();
        verify(sendKinPresenter, times(1)).onNext();
    }

    @Test
    public void onSendClickedTestExact() {
        presenter.setAmount("100");
        verify(mockView, times(1)).showAmountValidity(true, false);
        presenter.onSendClicked();
        verify(sendKinPresenter, times(1)).onNext();
    }

    @Test
    public void onSendClickedTestNoFee() {
        presenter.setAmount("101");
        verify(mockView, times(1)).showAmountValidity(true, false);
        presenter.onSendClicked();
        verify(mockView, times(1)).showAmountValidity(false, true);
    }

    @Test
    public void onSendClickedTestNoInteger() {
        presenter.setAmount("99999999999999999999999999");
        verify(mockView, times(1)).showAmountValidity(true, false);
        presenter.onSendClicked();
        verify(mockView, times(1)).showAmountValidity(false, true);
    }

}
