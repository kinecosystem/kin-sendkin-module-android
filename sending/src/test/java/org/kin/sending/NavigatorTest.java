package org.kin.sending;

import org.junit.Before;
import org.junit.Test;
import org.kin.sending.view.Navigator;
import org.kin.sending.view.SendKinView;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class NavigatorTest {

    Navigator navigator;
    SendKinView mockView = mock(SendKinView.class);

    @Before
    public void initMocks() {
        navigator = new Navigator(mockView);
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
        verify(mockView).showConfirmPage();
        //TODO check
        //verify(mockView).enableBack(true);
        navigator.onNext();
        assertTrue(navigator.isStep(Navigator.STEP_START_TRANSFER));
        verify(mockView).showStartTransferPage();
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
        verify(mockView).showRecipientAddressPage();
        verify(mockView).enableBack(false);
        navigator.onPrevious();
        verify(mockView).onClose();
    }
}
