package kin.sendkin;

import org.junit.Before;
import org.junit.Test;

import kin.sendkin.core.store.ContactsListener;
import kin.sendkin.presenter.RecipientAddressListener;
import kin.sendkin.presenter.RecipientAddressPresenter;
import kin.sendkin.presenter.RecipientAddressPresenterImpl;
import kin.sendkin.presenter.SendKinPresenter;
import kin.sendkin.view.RecipientAddressView;

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
    ContactsListener contactsListener;
    RecipientAddressListener recipientAddressListener;

    @Before
    public void initMocks() {
        sendKinPresenter = mock(SendKinPresenter.class);
        mockView = mock(RecipientAddressView.class);
        presenter = new RecipientAddressPresenterImpl(sendKinPresenter, null);
        contactsListener = (ContactsListener) presenter;
        recipientAddressListener = (RecipientAddressListener) presenter;
        presenter.onAttach(mockView);
        presenter.onResume();
    }

    @Test
    public void onNextClickedTest() {
        presenter.setRecipientAddress(emptyAddress);
        presenter.onNextClicked();
        verify(mockView).showAddressValidity(false, false);
        presenter.setRecipientAddress(nonValidAddress);
        presenter.onNextClicked();
        verify(mockView).showAddressValidity(false, true);
        presenter.setRecipientAddress(validAddress);
        presenter.onNextClicked();
        verify(sendKinPresenter).onNext();
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


    @Test
    public void onAddNewContactClickedTest() {
        presenter.onAddNewContactClicked();
        verify(sendKinPresenter).onAddNewContactClicked();
    }

    @Test
    public void setRecipientAddressTest() {
        presenter.setRecipientAddress(validAddress);
        verify(mockView).showAddressValidity(true, false);
        verify(sendKinPresenter).setRecipientAddress(validAddress);
    }

    @Test
    public void onResumeTest() {
        verify(sendKinPresenter).loadContacts();
    }

    @Test
    public void onBackClickedTest() {
        presenter.onBackClicked();
        verify(sendKinPresenter).onPrevious();
    }

    @Test
    public void onRecipientAddressChanged() {
        recipientAddressListener.onRecipientAddressChanged(validAddress);
        verify(mockView).updateReceiverAddress(validAddress);
    }

    @Test
    public void onContactChangedFullListTest() {
        contactsListener.onContactChanged(false);
        verify(mockView).notifyContactChanged();
        verify(mockView).updateListVisibility(false);
    }

    @Test
    public void onContactChangedEmptyListTest() {
        contactsListener.onContactChanged(true);
        verify(mockView).notifyContactChanged();
        verify(mockView).updateListVisibility(true);
    }

    @Test
    public void onContactAddedTest() {
        int position = 5;
        contactsListener.onContactAdded(position);
        verify(mockView).notifyContactChanged();
        verify(mockView).updateListVisibility(false);
        verify(mockView).scrollToPosition(position, true);
    }

    @Test
    public void onContactsLoadedTest() {
        contactsListener.onContactsLoaded(false);
        verify(mockView).updateListVisibility(false);

        contactsListener.onContactsLoaded(true);
        verify(mockView).updateListVisibility(true);
    }

    @Test
    public void onContactsLoadingTest() {
        contactsListener.onContactsLoading();
        verify(mockView).showContactsLoader();
    }
}
