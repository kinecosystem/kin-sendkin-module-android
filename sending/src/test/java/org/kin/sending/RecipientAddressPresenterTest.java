package org.kin.sending;

import org.junit.Before;
import org.junit.Test;
import org.kin.sending.presenter.RecipientAddressPresenter;
import org.kin.sending.presenter.RecipientAddressPresenterImpl;
import org.kin.sending.presenter.SendKinPresenter;
import org.kin.sending.view.RecipientAddressView;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class RecipientAddressPresenterTest {
    SendKinPresenter sendKinPresenter;
    RecipientAddressView mockView;
    final String validAddress = "GBNY5GQ6WOG5JU4JZEPJ4WZIMYMC5HGPYLSXT7S3GBDJ2S3CM4NPTBNC";
    final String nonValidAddress = "AGBNY5GQ6WOG5JU4JZEP";
    final String emptyAddress = "";

    RecipientAddressPresenter presenter;

    @Before
    public void initMocks() {
        sendKinPresenter = mock(SendKinPresenter.class);
        mockView = mock(RecipientAddressView.class);
        presenter = new RecipientAddressPresenterImpl(sendKinPresenter, null);
    }

    @Test
    public void onNextClickedTest() {
        presenter.onAttach(mockView);
        presenter.setRecipientAddress(emptyAddress);
        presenter.onNextClicked();
        verify(mockView, times(1)).showAddressValidity(false, false);
        presenter.setRecipientAddress(nonValidAddress);
        presenter.onNextClicked();
        verify(mockView, times(1)).showAddressValidity(false, true);
        presenter.setRecipientAddress(validAddress);
        presenter.onNextClicked();
        verify(sendKinPresenter, times(1)).onNext();
    }

    @Test
    public void onShowPublicAddressClickedTest() {
        presenter.onShowPublicAddressClicked();
        verify(sendKinPresenter, times(1)).onShowPublicAddressDialogClicked();
    }

    @Test
    public void onWhatIsPublicAddressClickedTest() {
        presenter.onWhatIsPublicAddressClicked();
        verify(sendKinPresenter, times(1)).onShowWhatIsPublicAddressDialogClicked();
    }
}
