package kin.sendkin;

import org.junit.Before;
import org.junit.Test;
import kin.sendkin.presenter.ContactDialogPresenter;
import kin.sendkin.presenter.ContactDialogPresenterImpl;
import kin.sendkin.presenter.SendKinPresenter;
import kin.sendkin.view.ContactDialogView;
import kin.sendkin.core.model.RecipientContact;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EditContactDialogPresenterTest {
    SendKinPresenter sendKinPresenter;
    ContactDialogView mockView;

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
        presenter = new ContactDialogPresenterImpl(sendKinPresenter, contact.getId());
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
    public void onResumeTest() {
        verify(mockView).setEditLayout(contact.getName(), contact.getAddress());
    }

    @Test
    public void onSaveClickedTest() {
        presenter.onNameChanged(name);
        presenter.onAddressChanged(address);
        presenter.onSaveClicked();
        verify(sendKinPresenter).setContactName(name);
        verify(sendKinPresenter).setRecipientAddress(address);
        verify(sendKinPresenter).updateContact(contact.getId());
        verify(mockView).dismiss();
    }


    @Test
    public void onSaveClickedInputNotValidTest() {
        presenter.onNameChanged(namNotValid);
        presenter.onAddressChanged(addressNoValid);
        presenter.onSaveClicked();
        verify(mockView).showAddressValidity(false, true);
        verify(mockView).showNameValidity(false);
    }

    @Test
    public void onDeleteClickedTest() {
        presenter.onDeleteClicked();
        verify(mockView).setConfirmDeleteLayout(contact.getName());
    }


    @Test
    public void onConfirmDeleteClickedTest() {
        presenter.onConfirmDeleteClicked();
        verify(sendKinPresenter).removeRecipientContact(contact.getId());
        verify(mockView).dismiss();
    }

}
