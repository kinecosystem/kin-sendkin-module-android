package kin.sendkin;

import org.junit.Before;
import org.junit.Test;
import kin.sendkin.presenter.SendAmountPresenter;
import kin.sendkin.presenter.SendAmountPresenterImpl;
import kin.sendkin.presenter.SendKinPresenter;
import kin.sendkin.view.SendAmountView;
import kin.sendkin.core.callbacks.BalanceCallback;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SendAmountPresenterTest {
    SendKinPresenter sendKinPresenter;
    SendAmountView mockView;
    SendAmountPresenter presenter;

    @Before
    public void initMocks() {
        final int amount = 100;
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
    public void setAmount() {
        presenter.setAmount("99999999999999999999999999");
        verify(mockView, times(1)).showAmountValidity(true, false);
        presenter.onSendClicked();
        verify(mockView, times(1)).showAmountValidity(false, true);
    }

    @Test
    public void onBackClickedTest() {
        presenter.onBackClicked();
        verify(sendKinPresenter).onPrevious();
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
