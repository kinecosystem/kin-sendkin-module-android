package kin.sendkin;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.UUID;

import kin.sendkin.core.callbacks.BalanceCallback;
import kin.sendkin.core.callbacks.SendingKinCallback;
import kin.sendkin.core.model.KinManager;
import kin.sendkin.core.model.RecipientContact;
import kin.sendkin.core.store.ContactsListener;
import kin.sendkin.core.store.RecipientContactsRepo;
import kin.sendkin.events.EventsManager;
import kin.sendkin.events.SendKinPages;
import kin.sendkin.presenter.RecipientAddressListener;
import kin.sendkin.presenter.SendKinPresenter;
import kin.sendkin.presenter.SendKinPresenterImpl;
import kin.sendkin.view.KinBalanceActionBar;
import kin.sendkin.view.Navigator;
import kin.sendkin.view.SendKinView;

import static kin.sendkin.view.Navigator.STEP_CONFIRM;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SendKinPresenterTest {
    private final String ADDRESS = "GCCLBNMWRUKPCWV4J6H42KAQQ6GSOG6YKRNONAT3QDOGRH3T3EQGSXJR";
    private final String NAME = "James Bond";
    private final String SHORTEST_NAME = "";
    private final String TRANSACTION_ID = "TxId";

    private final String LONGEST_NAME = "Adolph Blaine Charles David Earl Frederick Gerald Hubert Irvin John Kenneth Lloyd Martin Nero Oliver Paul Quincy Randolph Sherman Thomas Uncas Victor William Xerxes Yancy Zeus";
    private int BALANCE = 500;
    private final String MY_ADDRESS = "GB5HR4O3RTH6EHFAMJTL2ZHGP54K2EHUTFNUIS43WTZUOWRHQMGLFNHH";
    private final String OTHER_ADDRESS = "GCCLBNMWRUKPaWV4J6H42KAQQ6GSOG6YKRNONAT3QDOGRH3T3EQGSXJR";
    private final String OTHER_Name = "Double O Seven";
    private final int AMOUNT = 7;

    private SendKinPresenter presenter;
    private SendKinView mockView;
    private KinBalanceActionBar.OnClickCallback actionBarCallBackMock;
    private UUID id;
    private RecipientContact contact;
    private KinManager kinManagerMock;
    private Navigator navigatorMock;
    private RecipientContactsRepo contactsRepoMock;
    private RecipientAddressListener addressListenerMock;
    private ArrayList<RecipientContact> contacts;
    private EventsManager eventsManagerMock;

    @Before
    public void initMocks() {
        mockView = mock(SendKinView.class);
        contact = new RecipientContact(NAME, ADDRESS);
        contacts = new ArrayList<>();
        contacts.add(new RecipientContact(NAME, ADDRESS));
        contacts.add(new RecipientContact(OTHER_Name, OTHER_ADDRESS));

        id = contact.getId();
        navigatorMock = mock(Navigator.class);
        kinManagerMock = mock(KinManager.class);
        actionBarCallBackMock = mock(KinBalanceActionBar.OnClickCallback.class);
        contactsRepoMock = mock(RecipientContactsRepo.class);
        when(contactsRepoMock.getRecipientContact(id)).thenReturn(contact);
        addressListenerMock = mock(RecipientAddressListener.class);
        eventsManagerMock = mock(EventsManager.class);

        presenter = new SendKinPresenterImpl(kinManagerMock, contactsRepoMock, navigatorMock, eventsManagerMock);
        actionBarCallBackMock = (KinBalanceActionBar.OnClickCallback) presenter;
        when(presenter.getRecipientContacts()).thenReturn(contacts);
        presenter.onAttach(mockView);
        presenter.onResume();
    }


    @Test
    public void onContactClickedTest() {
        presenter.setRecipientAddressListener(addressListenerMock);
        presenter.onContactClicked(id);
        assertTrue(presenter.getChosenContact() != null);
    }

    @Test
    public void setAmountTest() {
        presenter.setAmount(AMOUNT);
        assertTrue(presenter.getAmount() == AMOUNT);
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
        argument.getValue().onBalanceReceived(BALANCE);
        assertEquals(presenter.getBalance(), BALANCE);
        verify(mockView).updateBalance(BALANCE);
    }

    @Test
    public void onResumeBalanceErrorTest() {
        ArgumentCaptor<BalanceCallback> argument = ArgumentCaptor.forClass(BalanceCallback.class);
        verify(kinManagerMock).getCurrentBalance(argument.capture());
    }

    @Test
    public void setRecipientAddressContactClickedTest() {
        presenter.onContactClicked(id);
        presenter.setRecipientAddress(ADDRESS);
        assertEquals(presenter.getChosenContact(), contact);
    }

    @Test
    public void setRecipientAddressTest() {
        presenter.setRecipientAddress(ADDRESS);
        assertEquals(presenter.getChosenContact(), null);
    }

    @Test
    public void setRecipientAddressChosenChangedTest() {
        presenter.setRecipientAddress(ADDRESS);
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

    @Test
    public void getContactRepoTest() {
        presenter.getContact(id);
        verify(contactsRepoMock).getRecipientContact(id);
    }

    @Test
    public void onNextStartTransactionTest() {
        navigatorMock.setStep(STEP_CONFIRM);
        presenter.setAmount(AMOUNT);
        presenter.setRecipientAddress(ADDRESS);
        when(navigatorMock.shouldStartTransfer()).thenReturn(true);
        presenter.onNext();
        ArgumentCaptor<SendingKinCallback> argument = ArgumentCaptor.forClass(SendingKinCallback.class);
        verify(kinManagerMock).sendKin(anyString(), anyInt(), argument.capture());
    }

    @Test
    public void onNextShouldResetTest() {
        navigatorMock.setStep(Navigator.STEP_TRANSFER_COMPLETE);
        presenter.setAmount(AMOUNT);
        presenter.setRecipientAddress(ADDRESS);
        when(navigatorMock.shouldResetData()).thenReturn(true);
        presenter.onNext();
        verifyResetTest();
    }

    @Test
    public void onShowWhatIsPublicAddressDialogClickedTest() {
        presenter.onShowWhatIsPublicAddressDialogClicked();
        verify(mockView).showWhatIsPublicAddressDialog();
        verify(eventsManagerMock).onViewPage(SendKinPages.WHATIS_PUBLIC_ADDRESS_POPUP);
    }

    @Test
    public void onAddNewContactClickedTest() {
        presenter.onAddNewContactClicked();
        verify(mockView).showAddNewContactDialog();
    }

    @Test
    public void hasEnoughKin() {
        final int newBalance = 100;
        ArgumentCaptor<BalanceCallback> argument = ArgumentCaptor.forClass(BalanceCallback.class);
        verify(kinManagerMock).getCurrentBalance(argument.capture());
        argument.getValue().onBalanceReceived(newBalance);
        assertTrue(presenter.hasEnoughKin(0));
        assertTrue(presenter.hasEnoughKin(newBalance));
        assertTrue(presenter.hasEnoughKin(newBalance - 1));
        assertFalse(presenter.hasEnoughKin(-1));
        assertFalse(presenter.hasEnoughKin(newBalance + 1));
    }

    @Test
    public void saveNewContactTest() {
        presenter.setContactName(NAME);
        presenter.setRecipientAddress(ADDRESS);
        presenter.saveNewContact();
        verifyResetTest();
    }

    private void verifyResetTest() {
        assertEquals(presenter.getRecipientAddress(), "");
        assertEquals(presenter.getAmount(), 0);
        assertNull(presenter.getChosenContact());
    }

    @Test
    public void onBackClicked() {
        presenter.onBackClicked();
        verify(navigatorMock).onPrevious();
    }


    @Test
    public void setContactName() {
        assertTrue(presenter.setContactName(NAME));
        assertFalse(presenter.setContactName(LONGEST_NAME));
        assertFalse(presenter.setContactName(SHORTEST_NAME));
    }


    @Test
    public void onEditContact() {
        presenter.onEditContact(id);
        verify(mockView).showContactDialog(id);
    }


    @Test
    public void resetTest() {
        presenter.reset();
        verifyResetTest();
    }

    @Test
    public void loadContactsTest() {
        presenter.loadContacts();
        verify(contactsRepoMock, times(2)).load();
    }

    @Test
    public void onResumeTest() {
        verify(contactsRepoMock).load();
    }


    @Test
    public void updateContact() {
        presenter.updateContact(id, NAME, ADDRESS);
        verify(contactsRepoMock).update(id, NAME, ADDRESS);
        verifyResetTest();
    }

    @Test
    public void onContactClicked() {
        presenter.onContactClicked(id);
        assertEquals(presenter.getChosenContact(), contact);
    }


    @Test
    public void onCloseClickedTest() {
        actionBarCallBackMock.onCloseClicked();
        verify(mockView).onClose();
    }


    @Test
    public void onBackClickedTest() {
        actionBarCallBackMock.onBackClicked();
        verify(navigatorMock).onPrevious();
    }

    @Test
    public void startTransactionCompleteTest() {
        presenter.setRecipientAddress(ADDRESS);
        presenter.setAmount(AMOUNT);
        presenter.startTransaction();
        ArgumentCaptor<SendingKinCallback> transactionCaptor = ArgumentCaptor.forClass(SendingKinCallback.class);
        verify(kinManagerMock).sendKin(anyString(), anyInt(), transactionCaptor.capture());
        transactionCaptor.getValue().onSendKinCompleted(TRANSACTION_ID, ADDRESS, AMOUNT);
        verify(navigatorMock).updateStep(Navigator.STEP_TRANSFER_COMPLETE);
        ArgumentCaptor<BalanceCallback> balanceCaptor = ArgumentCaptor.forClass(BalanceCallback.class);
        verify(kinManagerMock, times(2)).getCurrentBalance(balanceCaptor.capture());
        verify(eventsManagerMock).onTransferSuccess(TRANSACTION_ID);
    }

    @Test
    public void startTransactionFailedTest() {
        presenter.setRecipientAddress(ADDRESS);
        presenter.setAmount(AMOUNT);
        presenter.startTransaction();
        ArgumentCaptor<SendingKinCallback> transactionCaptor = ArgumentCaptor.forClass(SendingKinCallback.class);
        verify(kinManagerMock).sendKin(anyString(), anyInt(), transactionCaptor.capture());
        transactionCaptor.getValue().onSendKinFailed("SOME ERROR", ADDRESS, AMOUNT);
        verify(navigatorMock).updateStep(Navigator.STEP_TRANSFER_FAILED);
        verify(eventsManagerMock).onTransferFailed();
    }

    @Test
    public void startTransactionTimeOutTest() {
        presenter.setRecipientAddress(ADDRESS);
        presenter.setAmount(AMOUNT);
        presenter.startTransaction();
        ArgumentCaptor<SendingKinCallback> transactionCaptor = ArgumentCaptor.forClass(SendingKinCallback.class);
        verify(kinManagerMock).sendKin(anyString(), anyInt(), transactionCaptor.capture());
        transactionCaptor.getValue().onSendKinTimeout("SOME ERROR", ADDRESS, AMOUNT);
        verify(navigatorMock).updateStep(Navigator.STEP_TRANSFER_TIMEOUT);
        verify(eventsManagerMock).onTransactionTimeout();
    }
}