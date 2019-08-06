package org.kin.sending;

import org.junit.Test;
import org.kin.sending.presenter.RecipientAddressPresenter;
import org.kin.sending.presenter.RecipientAddressPresenterImpl;
import org.kin.sending.presenter.SendKinPresenter;
import org.kin.sending.view.RecipientAddressView;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class RecipientAddressPresenterTest {
    SendKinPresenter sendKinPresenter = mock(SendKinPresenter.class);
    RecipientAddressView mockView = mock(RecipientAddressView.class);
    String validAddress = "GBNY5GQ6WOG5JU4JZEPJ4WZIMYMC5HGPYLSXT7S3GBDJ2S3CM4NPTBNC";
    String nonValidAddress = "AGBNY5GQ6WOG5JU4JZEP";
    String emptyAddress = "";


    RecipientAddressPresenter presenter = new RecipientAddressPresenterImpl(sendKinPresenter, null);

    @Test
    public void onNextClickedTest() {
        presenter.onAttach(mockView);

        presenter.setReceiverAddressText(emptyAddress);
        presenter.onNextClicked();
        verify(mockView, times(1)).showAddressValidity(false, false);

        presenter.setReceiverAddressText(nonValidAddress);
        presenter.onNextClicked();
        verify(mockView, times(1)).showAddressValidity(false, true);

        presenter.setReceiverAddressText(validAddress);
        presenter.onNextClicked();
        verify(sendKinPresenter, times(1)).onNext();
    }

    @Test
    public void onPasteClickedTest() {
//        presenter.setView(mockView);
//
//        presenter.onPasteClicked();
        //TODO need to mock clipboard
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
