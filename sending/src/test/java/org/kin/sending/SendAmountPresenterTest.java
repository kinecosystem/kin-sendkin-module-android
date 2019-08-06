package org.kin.sending;

import org.junit.Before;
import org.junit.Test;
import org.kin.sending.presenter.SendAmountPresenter;
import org.kin.sending.presenter.SendAmountPresenterImpl;
import org.kin.sending.presenter.SendKinPresenter;
import org.kin.sending.view.SendAmountView;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SendAmountPresenterTest {
    SendKinPresenter sendKinPresenter = mock(SendKinPresenter.class);
    SendAmountView mockView = mock(SendAmountView.class);


    SendAmountPresenter presenter;

    @Before
    public void initMocks() {
        presenter = new SendAmountPresenterImpl(sendKinPresenter);
        presenter.onAttach(mockView);
        when(sendKinPresenter.getCurrentBalance()).thenReturn(new Integer(100));
    }

    @Test
    public void onSendClickedTestEmpty() {
        presenter.setAmount("");
        presenter.onSendClicked();
        verify(mockView, times(1)).showAmountValidity(false, false);
    }

    @Test
    public void onSendClickedTestZero() {
        presenter.setAmount("0");
        presenter.onSendClicked();
        verify(mockView, times(1)).showAmountValidity(false, false);
    }

    @Test
    public void onSendClickedTest() {
        presenter.setAmount("99");
        presenter.onSendClicked();
        verify(sendKinPresenter, times(1)).onNext();
    }

    @Test
    public void onSendClickedTestExact() {
        presenter.setAmount("100");
        presenter.onSendClicked();
        verify(sendKinPresenter, times(1)).onNext();
    }

    @Test
    public void onSendClickedTestNoFee() {
        presenter.setAmount("101");
        presenter.onSendClicked();
        verify(mockView, times(1)).showAmountValidity(false, true);
    }

    @Test
    public void onSendClickedTestNoInteger() {
        presenter.setAmount("99999999999999999999999999");
        presenter.onSendClicked();
        verify(mockView, times(1)).showAmountValidity(false, true);
    }

}
