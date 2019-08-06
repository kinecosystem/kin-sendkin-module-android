package org.kin.sending.view;

import android.support.annotation.IntDef;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class Navigator {
    @Retention(SOURCE)
    @IntDef({STEP_INVALID, STEP_RECIPIENT_ADDRESS, STEP_AMOUNT, STEP_CONFIRM, STEP_START_TRANSFER, STEP_TRANSFER_COMPLETE, STEP_TRANSFER_FAILED, STEP_TRANSFER_TIMEOUT})
    private @interface SendKinSteps {
    }

    public static final int STEP_INVALID = 0;
    public static final int STEP_RECIPIENT_ADDRESS = 10;
    public static final int STEP_AMOUNT = 20;
    public static final int STEP_CONFIRM = 30;
    public static final int STEP_START_TRANSFER = 40;
    public static final int STEP_TRANSFER_COMPLETE = 50;
    public static final int STEP_TRANSFER_FAILED = 60;
    public static final int STEP_TRANSFER_TIMEOUT = 70;

    private @Navigator.SendKinSteps
    int step = STEP_RECIPIENT_ADDRESS;
    private SendKinView view;


    public Navigator(SendKinView view) {
        this.view = view;
    }

    public void onNext() {
        updateNextStep();
        updateView();
    }

    public void onPrevious() {
        updatePreviousStep();
        updateView();
    }

    public void updateStep(@SendKinSteps int step) {
        this.step = step;
        updateView();
    }

    public boolean shouldStartTransfer() {
        return this.step == STEP_START_TRANSFER;
    }

    private void updateNextStep() {
        switch (step) {
            case STEP_AMOUNT:
                step = STEP_CONFIRM;
                break;
            case STEP_CONFIRM:
                step = STEP_START_TRANSFER;
                break;
            case STEP_RECIPIENT_ADDRESS:
                step = STEP_AMOUNT;
                break;
        }
    }

    private void updatePreviousStep() {
        switch (step) {
            case STEP_AMOUNT:
                step = STEP_RECIPIENT_ADDRESS;
                break;
            case STEP_CONFIRM:
                step = STEP_AMOUNT;
                break;
            case STEP_RECIPIENT_ADDRESS:
                step = STEP_INVALID;
                break;
        }
    }

    private void updateView() {
        switch (step) {
            case STEP_INVALID:
                view.onClose();
                break;
            case STEP_AMOUNT:
                view.showAmountPage();
                view.enableBack(true);
                break;
            case STEP_CONFIRM:
                view.showConfirmPage();
                view.enableBack(true);
                break;
            case STEP_RECIPIENT_ADDRESS:
                view.showRecipientAddressPage();
                view.enableBack(false);
                break;
            case STEP_START_TRANSFER:
                view.showStartTransferPage();
                view.enableBack(true);
                break;
            case STEP_TRANSFER_COMPLETE:
                view.showTransferCompletePage();
                view.enableBack(true);
                break;
            case STEP_TRANSFER_FAILED:
                view.showTransferFailedPage();
                view.enableBack(true);
                break;
            case STEP_TRANSFER_TIMEOUT:
                view.showTransferTimeoutPage();
                view.enableBack(true);
                break;
        }
    }

    @VisibleForTesting
    public boolean isStep(@SendKinSteps int step) {
        return this.step == step;
    }

    @VisibleForTesting
    public void setStep(@SendKinSteps int step) {
        this.step = step;
    }
}
