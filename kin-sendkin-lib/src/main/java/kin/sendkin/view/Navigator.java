package kin.sendkin.view;

import android.support.annotation.IntDef;
import android.support.annotation.VisibleForTesting;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class Navigator {

    @Retention(SOURCE)
    @IntDef({STEP_INVALID, STEP_RECIPIENT_ADDRESS, STEP_AMOUNT, STEP_CONFIRM, STEP_START_TRANSFER, STEP_TRANSFER_COMPLETE, STEP_TRANSFER_FAILED, STEP_TRANSFER_TIMEOUT})
    public @interface SendKinSteps {
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
    int step = STEP_INVALID;
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

    public boolean shouldResetData() {
        return this.step == STEP_INVALID ||
                this.step == STEP_TRANSFER_FAILED ||
                this.step == STEP_TRANSFER_TIMEOUT ||
                this.step == STEP_TRANSFER_COMPLETE;
    }


    private void updateNextStep() {
        switch (step) {
            case STEP_INVALID:
            case STEP_TRANSFER_FAILED:
            case STEP_TRANSFER_TIMEOUT:
            case STEP_TRANSFER_COMPLETE:
                step = STEP_RECIPIENT_ADDRESS;
                break;
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
            case STEP_TRANSFER_FAILED:
            case STEP_TRANSFER_TIMEOUT:
            case STEP_TRANSFER_COMPLETE:
                step = STEP_RECIPIENT_ADDRESS;
                break;
        }
    }

    private void updateView() {
        switch (step) {
            case STEP_INVALID:
                view.onClose();
                break;
            case STEP_RECIPIENT_ADDRESS:
                view.showRecipientAddressPage();
                view.enableBack(false);
                break;
            case STEP_AMOUNT:
                view.showAmountPage();
                view.enableBack(true);
                break;
            case STEP_CONFIRM:
                view.showTransactionDialog(STEP_CONFIRM);
                break;
            case STEP_START_TRANSFER:
                view.showTransactionDialog(STEP_START_TRANSFER);
                break;
            case STEP_TRANSFER_COMPLETE:
                view.showTransactionDialog(STEP_TRANSFER_COMPLETE);
                break;
            case STEP_TRANSFER_FAILED:
                view.showTransactionDialog(STEP_TRANSFER_FAILED);
                break;
            case STEP_TRANSFER_TIMEOUT:
                view.showTransactionDialog(STEP_TRANSFER_TIMEOUT);
                break;
            default:
                view.onClose();

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
