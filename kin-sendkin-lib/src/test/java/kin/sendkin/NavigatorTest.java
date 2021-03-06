package kin.sendkin;

import org.junit.Before;
import org.junit.Test;
import kin.sendkin.view.Navigator;
import kin.sendkin.view.SendKinView;
import org.mockito.internal.verification.Times;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class NavigatorTest {

    Navigator navigator;
    SendKinView mockView;

    @Before
    public void initMocks() {
        mockView = mock(SendKinView.class);
        navigator = new Navigator(mockView);
        navigator.onNext();
        assertTrue(navigator.isStep(Navigator.STEP_RECIPIENT_ADDRESS));
    }

    @Test
    public void onNextTest(){
        navigator.onNext();
        assertTrue(navigator.isStep(Navigator.STEP_AMOUNT));
        verify(mockView).showAmountPage();
        verify(mockView).enableBack(true);
        navigator.onNext();
        assertTrue(navigator.isStep(Navigator.STEP_CONFIRM));
        verify(mockView).showTransactionDialog(Navigator.STEP_CONFIRM);
        verify(mockView).enableBack(true);
        navigator.onNext();
        assertTrue(navigator.isStep(Navigator.STEP_START_TRANSFER));
        verify(mockView).showTransactionDialog(Navigator.STEP_START_TRANSFER);
    }

    @Test
    public void onPreviousTest(){
        navigator.setStep(Navigator.STEP_CONFIRM);
        navigator.onPrevious();
        assertTrue(navigator.isStep(Navigator.STEP_AMOUNT));
        verify(mockView).showAmountPage();
        verify(mockView).enableBack(true);
        navigator.onPrevious();
        assertTrue(navigator.isStep(Navigator.STEP_RECIPIENT_ADDRESS));
        //TODO check why its called twice
        verify(mockView, new Times(2)).showRecipientAddressPage();
        verify(mockView, new Times(2)).enableBack(false);
        navigator.onPrevious();
        verify(mockView).onClose();
    }

    @Test
    public void shouldStartTransferTest(){
        navigator.setStep(Navigator.STEP_START_TRANSFER);
        assertTrue(navigator.shouldStartTransfer());
        navigator.setStep(Navigator.STEP_INVALID);
        assertFalse(navigator.shouldStartTransfer());
        navigator.setStep(Navigator.STEP_TRANSFER_FAILED);
        assertFalse(navigator.shouldStartTransfer());
        navigator.setStep(Navigator.STEP_TRANSFER_TIMEOUT);
        assertFalse(navigator.shouldStartTransfer());
        navigator.setStep(Navigator.STEP_TRANSFER_COMPLETE);
        assertFalse(navigator.shouldStartTransfer());
        navigator.setStep(Navigator.STEP_AMOUNT);
        assertFalse(navigator.shouldStartTransfer());
        navigator.setStep(Navigator.STEP_CONFIRM);
        assertFalse(navigator.shouldStartTransfer());
        navigator.setStep(Navigator.STEP_RECIPIENT_ADDRESS);
        assertFalse(navigator.shouldStartTransfer());
    }

    @Test
    public void shouldResetData() {
        navigator.setStep(Navigator.STEP_INVALID);
        assertTrue(navigator.shouldResetData());
        navigator.setStep(Navigator.STEP_TRANSFER_FAILED);
        assertTrue(navigator.shouldResetData());
        navigator.setStep(Navigator.STEP_TRANSFER_TIMEOUT);
        assertTrue(navigator.shouldResetData());
        navigator.setStep(Navigator.STEP_TRANSFER_COMPLETE);
        assertTrue(navigator.shouldResetData());
        navigator.setStep(Navigator.STEP_AMOUNT);
        assertFalse(navigator.shouldResetData());
        navigator.setStep(Navigator.STEP_CONFIRM);
        assertFalse(navigator.shouldResetData());
        navigator.setStep(Navigator.STEP_RECIPIENT_ADDRESS);
        assertFalse(navigator.shouldResetData());
        navigator.setStep(Navigator.STEP_START_TRANSFER);
        assertFalse(navigator.shouldResetData());
    }
}
