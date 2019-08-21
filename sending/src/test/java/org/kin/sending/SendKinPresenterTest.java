package org.kin.sending;

import org.junit.Before;
import org.junit.Test;
import org.kin.sending.presenter.ContactDialogPresenter;
import org.kin.sending.presenter.ContactDialogPresenterImpl;
import org.kin.sending.presenter.RecipientAddressListener;
import org.kin.sending.presenter.SendKinPresenter;
import org.kin.sending.presenter.SendKinPresenterImpl;
import org.kin.sending.view.ContactDialogView;
import org.kin.sending.view.Navigator;
import org.kin.sending.view.SendKinView;
import org.kin.sendkin.core.callbacks.BalanceCallback;
import org.kin.sendkin.core.callbacks.SendingKinCallback;
import org.kin.sendkin.core.model.KinManager;
import org.kin.sendkin.core.model.RecipientContact;
import org.kin.sendkin.core.store.ContactsListener;
import org.kin.sendkin.core.store.RecipientContactsRepo;
import org.mockito.ArgumentCaptor;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SendKinPresenterTest {
    private SendKinPresenter presenter;
    private SendKinView mockView;

    private final String address = "GCCLBNMWRUKPCWV4J6H42KAQQ6GSOG6YKRNONAT3QDOGRH3T3EQGSXJR";
    private final String name = "James Bond";

    private final String MY_ADDRESS = "GB5HR4O3RTH6EHFAMJTL2ZHGP54K2EHUTFNUIS43WTZUOWRHQMGLFNHH";

    private final String OTHER_ADDRESS = "GCCLBNMWRUKPaWV4J6H42KAQQ6GSOG6YKRNONAT3QDOGRH3T3EQGSXJR";
    private final String namNotValid = "";
    private UUID id;

    private RecipientContact contact;
    private final int amount = 7;

    private KinManager kinManagerMock;
    private Navigator navigatorMock;

    private int balance = 500;
    private RecipientContactsRepo contactsRepoMock;
    private RecipientAddressListener addressListenerMock;

    @Before
    public void initMocks() {
        mockView = mock(SendKinView.class);
        contact = new RecipientContact(name, address);
        id = contact.getId();
        navigatorMock = mock(Navigator.class);
        kinManagerMock = mock(KinManager.class);
        contactsRepoMock = mock(RecipientContactsRepo.class);
        when(contactsRepoMock.getRecipientContact(id)).thenReturn(contact);
        addressListenerMock = mock(RecipientAddressListener.class);
        // when(sendKinPresenter.getContact(contact.getId())).thenReturn(contact);


        presenter = new SendKinPresenterImpl(kinManagerMock, contactsRepoMock, navigatorMock);
        presenter.onAttach(mockView);
        presenter.onResume();
    }


    @Test
    public void onContactClickedTest() {
        presenter.setRecipientAddressListener(addressListenerMock);
        presenter.onContactClicked(id);
        //verify(presenter).setRecipientAddress(contact.getAddress());
        assertTrue(presenter.getChosenContact() != null);
    }

    @Test
    public void setAmountTest() {
        presenter.setAmount(amount);
        assertTrue(presenter.getAmount() == amount);
    }

    @Test
    public void getRecipientContactsTest() {
        presenter.getRecipientContacts();
        verify(contactsRepoMock).getAll();
    }

    @Test
    public void setContactsListenerTest() {
        ContactsListener contactsListener = mock(ContactsListener.class);
        presenter.setContactsListener(contactsListener);
        verify(contactsRepoMock).setContactListener(contactsListener);
    }

    @Test
    public void removeRecipientContactTest() {
        presenter.removeRecipientContact(id);
        verify(contactsRepoMock).remove(id);
    }

    @Test
    public void getContactTest() {
        RecipientContact someContact = presenter.getContact(id);
        assertTrue(someContact.equals(contact));
    }

    @Test
    public void onAttachTest() {
        verify(navigatorMock).onNext();
        verify(contactsRepoMock).load();
    }

    @Test
    public void onResumeBalanceReceivedTest() {
        ArgumentCaptor<BalanceCallback> argument = ArgumentCaptor.forClass(BalanceCallback.class);
        verify(kinManagerMock).getCurrentBalance(argument.capture());
        argument.getValue().onBalanceReceived(balance);
        assertEquals(presenter.getBalance(), balance);
        verify(mockView).updateBalance(balance);
    }

    @Test
    public void onResumeBalanceErrorTest() {
        ArgumentCaptor<BalanceCallback> argument = ArgumentCaptor.forClass(BalanceCallback.class);
        verify(kinManagerMock).getCurrentBalance(argument.capture());
    }

    @Test
    public void setRecipientAddressContactClickedTest() {
        presenter.onContactClicked(id);
        presenter.setRecipientAddress(address);
        assertEquals(presenter.getChosenContact(), contact);
    }

    @Test
    public void setRecipientAddressTest() {
        presenter.setRecipientAddress(address);
        assertEquals(presenter.getChosenContact(), null);
    }

    @Test
    public void setRecipientAddressContactClickedSetOtherAddressTest() {
        presenter.onContactClicked(id);
        presenter.setRecipientAddress(OTHER_ADDRESS);
        assertEquals(presenter.getChosenContact(), null);
    }

    @Test
    public void onPreviousTest() {
        presenter.onPrevious();
        verify(navigatorMock).onPrevious();
    }

    @Test
    public void onShowPublicAddressDialogClickedTest() {
        when(kinManagerMock.getPublicAddress()).thenReturn(MY_ADDRESS);
        presenter.onShowPublicAddressDialogClicked();
        verify(mockView).showPublicAddressDialog(MY_ADDRESS);
    }
}
