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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SendKinPresenterTest {
    private SendKinPresenter presenter;
    private SendKinView mockView;
    private KinBalanceActionBar.OnClickCallback actionBarCallBackMock;

    private final String address = "GCCLBNMWRUKPCWV4J6H42KAQQ6GSOG6YKRNONAT3QDOGRH3T3EQGSXJR";
    private final String name = "James Bond";

    private final String MY_ADDRESS = "GB5HR4O3RTH6EHFAMJTL2ZHGP54K2EHUTFNUIS43WTZUOWRHQMGLFNHH";

    private final String OTHER_ADDRESS = "GCCLBNMWRUKPaWV4J6H42KAQQ6GSOG6YKRNONAT3QDOGRH3T3EQGSXJR";
    private final String OTHER_Name = "Double O Seven";

    private final String namNotValid = "";
    private UUID id;

    private RecipientContact contact;
    private final int amount = 7;

    private KinManager kinManagerMock;
    private Navigator navigatorMock;

    private int balance = 500;
    private RecipientContactsRepo contactsRepoMock;
    private RecipientAddressListener addressListenerMock;
    private ArrayList<RecipientContact> contacts;
    private EventsManager eventsManagerMock;

    @Before
    public void initMocks() {
        mockView = mock(SendKinView.class);
        contact = new RecipientContact(name, address);
        contacts = new ArrayList<>();
        contacts.add(new RecipientContact(name, address));
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
        when(presenter.getRecipientContacts()).thenReturn(contacts);
        //TODO
        //when(presenter.getActionBarCallBack()).thenReturn(actionBarCallBackMock);
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

    @Test
    public void getContactRepoTest() {
        presenter.getContact(id);
        verify(contactsRepoMock).getRecipientContact(id);
    }

    @Test
    public void onNextStartTransactionTest() {
        navigatorMock.setStep(STEP_CONFIRM);
        presenter.setAmount(amount);
        presenter.setRecipientAddress(address);
        when(navigatorMock.shouldStartTransfer()).thenReturn(true);
        presenter.onNext();
        ArgumentCaptor<SendingKinCallback> argument = ArgumentCaptor.forClass(SendingKinCallback.class);
        verify(kinManagerMock).sendKin(anyString(), anyInt(), argument.capture());
    }

    @Test
    public void onNextShouldResetTest() {
        navigatorMock.setStep(Navigator.STEP_TRANSFER_COMPLETE);
        presenter.setAmount(amount);
        presenter.setRecipientAddress(address);
        when(navigatorMock.shouldResetData()).thenReturn(true);
        presenter.onNext();
        assertEquals(presenter.getRecipientAddress(), "");
        assertEquals(presenter.getAmount(), 0);
        assertNull(presenter.getChosenContact());
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

    /****
     *
     *

     @Override
     public void startTransaction() {
     kinManager.sendKin(recipientAddress, amount, new SendingKinCallback() {
     @Override
     public void onSendKinCompleted(String transactionId, String receiverAddress, int amount) {
     navigator.updateStep(Navigator.STEP_TRANSFER_COMPLETE);
     requestBalance();
     eventsManager.onTransferSuccess(transactionId);
     }

     @Override
     public void onSendKinFailed(String error, String receiverAddress, int amount) {
     navigator.updateStep(Navigator.STEP_TRANSFER_FAILED);
     eventsManager.onTransferFailed();
     }

     @Override
     public void onSendKinTimeout(String error, String receiverAddress, int amount) {
     navigator.updateStep(Navigator.STEP_TRANSFER_TIMEOUT);
     eventsManager.onTransactionTimeout();
     }
     });
     }

     @Override
     public boolean setContactName(@NonNull String contactName) {
     if (isValidContactName(contactName)) {
     this.contactName = contactName;
     return true;
     }
     return false;
     }

     @Override
     public void saveNewContact() {
     contactsRepo.add(new RecipientContact(contactName, recipientAddress));
     reset();
     }

     @Override
     public void onEditContact(@NonNull UUID id) {
     getView().showContactDialog(id);
     }

     @Override
     public void onContactClicked(@NonNull UUID id) {
     contactChosen = getContact(id);
     if (addressListener != null && contactChosen != null) {
     addressListener.onRecipientAddressChanged(contactChosen.getAddress());
     }
     }

     @Override
     public void reset() {
     this.contactName = "";
     this.recipientAddress = "";
     this.amount = 0;
     this.contactChosen = null;
     }

     @Override
     public void loadContacts() {
     contactsRepo.load();
     }

     @Override
     public RecipientContactsRepo getRecipientContactsRepo() {
     return contactsRepo;
     }

     @Override
     public RecipientContact getChosenContact() {
     return contactChosen;
     }

     private boolean isValidContactName(@NonNull String contactName) {
     return !contactName.isEmpty() && contactName.length() >= MIN_LENGTH_CONTACT_NAME && contactName.length() <= MAX_LENGTH_CONTACT_NAME;
     }

     private void requestBalance() {
     kinManager.getCurrentBalance(new BalanceCallback() {
     @Override
     public void onBalanceReceived(int balance) {
     SendKinPresenterImpl.this.balance = balance;
     getView().updateBalance(balance);
     }

     @Override
     public void onBalanceError(String error) {

     }
     });
     }

     private void onViewPageEvent(@NonNull SendKinPages page) {
     eventsManager.onViewPage(page);
     }

     @VisibleForTesting
     @Override
     public int getBalance() {
     return balance;
     }

     @Override
     public void updateContact(UUID contactId, String name, String address) {
     contactsRepo.update(contactId, name, address);
     reset();
     }

     @Override
     public void saveNewContact(String name, String address) {
     this.contactName = name;
     this.recipientAddress = address;
     saveNewContact();
     }

      *
     *
     *
     *
     *     @Override
     *     public void onCloseClicked() {
     *         getView().onClose();
     *     }
     *
     *     @Override
     *     public void onBackClicked() {
     *         onPrevious();
     *     }
     *
     *     @Override
     *     public void startTransaction() {
     *         kinManager.sendKin(recipientAddress, amount, new SendingKinCallback() {
     *             @Override
     *             public void onSendKinCompleted(String transactionId, String receiverAddress, int amount) {
     *                 navigator.updateStep(Navigator.STEP_TRANSFER_COMPLETE);
     *                 requestBalance();
     *                 EventsManager.getInstance().onTransferSuccess(transactionId);
     *             }
     *
     *             @Override
     *             public void onSendKinFailed(String error, String receiverAddress, int amount) {
     *                 navigator.updateStep(Navigator.STEP_TRANSFER_FAILED);
     *                 EventsManager.getInstance().onTransferFailed();
     *             }
     *
     *             @Override
     *             public void onSendKinTimeout(String error, String receiverAddress, int amount) {
     *                 navigator.updateStep(Navigator.STEP_TRANSFER_TIMEOUT);
     *                 EventsManager.getInstance().onTransactionTimeout();
     *             }
     *         });
     *     }
     *
     *     @Override
     *     public boolean setContactName(@NonNull String contactName) {
     *         if (isValidContactName(contactName)) {
     *             this.contactName = contactName;
     *             return true;
     *         }
     *         return false;
     *     }
     *
     *     @Override
     *     public void saveNewContact() {
     *         contactsRepo.add(new RecipientContact(contactName, recipientAddress));
     *         reset();
     *     }
     *
     *     @Override
     *     public void onEditContact(@NonNull UUID id) {
     *         getView().showContactDialog(id);
     *     }
     *
     *     @Override
     *     public void onContactClicked(@NonNull UUID id) {
     *         contactChosen = getContact(id);
     *         if (addressListener != null && contactChosen != null) {
     *             addressListener.onRecipientAddressChanged(contactChosen.getAddress());
     *         }
     *     }
     *
     *     @Override
     *     public void reset() {
     *         this.contactName = "";
     *         this.recipientAddress = "";
     *         this.amount = 0;
     *         this.contactChosen = null;
     *     }
     *
     *     @Override
     *     public void loadContacts() {
     *         contactsRepo.load();
     *     }
     *
     *     @Override
     *     public RecipientContactsRepo getRecipientContactsRepo() {
     *         return contactsRepo;
     *     }
     *
     *     @Override
     *     public RecipientContact getChosenContact() {
     *         return contactChosen;
     *     }
     *
     *     private boolean isValidContactName(@NonNull String contactName) {
     *         return !contactName.isEmpty() && contactName.length() >= MIN_LENGTH_CONTACT_NAME && contactName.length() <= MAX_LENGTH_CONTACT_NAME;
     *     }
     *
     *     private void requestBalance() {
     *         kinManager.getCurrentBalance(new BalanceCallback() {
     *             @Override
     *             public void onBalanceReceived(int balance) {
     *                 SendKinPresenterImpl.this.balance = balance;
     *                 getView().updateBalance(balance);
     *             }
     *
     *             @Override
     *             public void onBalanceError(String error) {
     *
     *             }
     *         });
     *     }
     *
     *     private void onViewPageEvent(@NonNull SendKinPages page) {
     *         EventsManager.getInstance().onViewPage(page);
     *     }
     *
     *     @VisibleForTesting
     *     @Override
     *     public int getBalance() {
     *         return balance;
     *     }
     *
     *     @Override
     *     public void updateContact(UUID contactId, String name, String address) {
     *         contactsRepo.update(contactId, name, address);
     *         reset();
     *     }
     *
     *     @Override
     *     public void saveNewContact(String name, String address) {
     *         this.contactName = name;
     *         this.recipientAddress = address;
     *         saveNewContact();
     *     }
     *
     *
     * ***/
}
