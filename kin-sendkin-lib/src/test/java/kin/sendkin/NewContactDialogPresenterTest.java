package kin.sendkin;

import org.junit.Before;
import org.junit.Test;
import kin.sendkin.presenter.ContactDialogPresenter;
import kin.sendkin.presenter.ContactDialogPresenterImpl;
import kin.sendkin.presenter.SendKinPresenter;
import kin.sendkin.view.ContactDialogView;
import kin.sendkin.core.model.RecipientContact;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class NewContactDialogPresenterTest {
    private SendKinPresenter sendKinPresenter;
    private ContactDialogView mockView;

    private final String address = "GCCLBNMWRUKPCWV4J6H42KAQQ6GSOG6YKRNONAT3QDOGRH3T3EQGSXJR";
    private final String name = "James Bond";

    private final String addressNoValid = "GCCLBNMWRUKPaWV4J6H42KAQQ6GSOG6YKRNONAT3QDOGRH3T3EQGSXJR";
    private final String namNotValid = "";

    private RecipientContact contact;

    private ContactDialogPresenter presenter;

    @Before
    public void initMocks() {
        contact = new RecipientContact(name, address);
        sendKinPresenter = mock(SendKinPresenter.class);
        when(sendKinPresenter.getContact(contact.getId())).thenReturn(contact);
        mockView = mock(ContactDialogView.class);
        presenter = new ContactDialogPresenterImpl(sendKinPresenter, null);
        presenter.onAttach(mockView);
        presenter.onResume();
    }

    @Test
    public void onCancelClickedTest() {
        presenter.onCancelClicked();
        verify(sendKinPresenter).reset();
        verify(mockView).dismiss();
    }

    @Test
    public void onDeleteClickedTest() {
        presenter.onDeleteClicked();
        verify(mockView, never()).setConfirmDeleteLayout(contact.getName());
    }

    @Test
    public void onConfirmDeleteClickedTest() {
        presenter.onConfirmDeleteClicked();
        verify(sendKinPresenter, never()).removeRecipientContact(contact.getId());
        verify(mockView, never()).dismiss();
    }

    @Test
    public void onResumeTest() {
        verify(mockView).setNewLayout();
    }

    @Test
    public void onSaveClickedTest() {
        presenter.onSaveClicked(name, address);
        verify(sendKinPresenter).saveNewContact(name, address);
        verify(mockView).dismiss();
    }

    @Test
    public void onSaveClickedInputNotValidTest() {
        presenter.onSaveClicked(namNotValid, addressNoValid);
        verify(mockView).showAddressValidity(false, true);
        verify(mockView).showNameValidity(false);
    }

    @Test
    public void onSaveClickedAddressNotValidTest() {
        presenter.onSaveClicked(name, addressNoValid);
        verify(mockView).showAddressValidity(false, true);
        verify(mockView, never()).showNameValidity(false);
    }

    @Test
    public void onSaveClickedAddressEmptyTest() {
        presenter.onSaveClicked(name, "");
        verify(mockView).showAddressValidity(false, false);
        verify(mockView, never()).showNameValidity(false);
    }

    @Test
    public void onSaveClickedNameNotValidTest() {
        presenter.onSaveClicked(namNotValid, address);
        verify(mockView, never()).showAddressValidity(false, true);
        verify(mockView, never()).showAddressValidity(false, false);
        verify(mockView).showNameValidity(false);
    }
}
