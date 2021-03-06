package kin.sendkin.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import kin.sendkin.R;
import kin.sendkin.events.EventsManager;
import kin.sendkin.presenter.SendKinPresenter;
import kin.sendkin.presenter.SendKinPresenterImpl;
import kin.sendkin.core.model.KinAccountUtils;
import kin.sendkin.core.model.KinManagerImpl;
import kin.sendkin.core.store.RecipientContactsRepoImpl;

import java.util.UUID;

import kin.sdk.KinAccount;
import kin.sdk.KinClient;

public class SendKinActivity extends AppCompatActivity implements SendKinView {
    private SendKinPresenterImpl presenter;
    private KinBalanceActionBar actionBar;

    public static Intent getIntent(@NonNull Context context, @NonNull KinClient kinClient, @NonNull String publicAddress) {
        Intent intent = new Intent(context, SendKinActivity.class);
        return KinAccountUtils.saveKinAccountData(intent, kinClient, publicAddress);
    }

    protected @LayoutRes
    int getContentLayout() {
        return R.layout.send_kin_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentLayout());
        final KinAccount kinAccount = KinAccountUtils.loadAccountClientData(this, getIntent());
        if (kinAccount != null) {
            presenter = new SendKinPresenterImpl(new KinManagerImpl(kinAccount), new RecipientContactsRepoImpl(this), new Navigator(this), EventsManager.getInstance());
            initViews();
            presenter.onAttach(this);
        }
    }

    @NonNull
    @Override
    public SendKinPresenter getSendKinPresenter() {
        return presenter;
    }

    @Override
    public void updateBalance(int balance) {
        actionBar.updateBalance(String.valueOf(balance));
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        presenter.onBackClicked();
    }

    @Override
    public void onClose() {
        finish();
    }

    @Override
    public void enableBack(boolean enable) {
        actionBar.enableBack(enable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    private void initViews() {
        actionBar = findViewById(R.id.actionBar);
        actionBar.addClickListner(presenter);
    }

    @Override
    public void showPublicAddressDialog(@NonNull final String publicAddress) {
        PublicAddressDialogFragment dialogFragment = PublicAddressDialogFragment.getInstance(publicAddress);
        dialogFragment.show(getSupportFragmentManager(), PublicAddressDialogFragment.TAG);
    }

    @Override
    public void showWhatIsPublicAddressDialog() {
        PublicAddressInfoDialogFragment dialogFragment = PublicAddressInfoDialogFragment.getInstance();
        dialogFragment.show(getSupportFragmentManager(), PublicAddressInfoDialogFragment.TAG);
    }

    @Override
    public void showContactDialog(@NonNull UUID id) {
        ContactDialogFragment dialogFragment = ContactDialogFragment.getInstance(id);
        dialogFragment.show(getSupportFragmentManager(), ContactDialogFragment.TAG);
    }

    @Override
    public void showRecipientAddressPage() {
        RecipientAddressFragment recipientAddressFragment = (RecipientAddressFragment) getSupportFragmentManager()
                .findFragmentByTag(RecipientAddressFragment.TAG);
        if (recipientAddressFragment == null) {
            recipientAddressFragment = RecipientAddressFragment.getInstance();
        }
        replaceFragment(recipientAddressFragment, RecipientAddressFragment.TAG);
    }

    @Override
    public void showAmountPage() {
        SendAmountFragment sendAmountFragment = (SendAmountFragment) getSupportFragmentManager()
                .findFragmentByTag(SendAmountFragment.TAG);

        if (sendAmountFragment == null)
            sendAmountFragment = SendAmountFragment.getInstance();
        else {
            //updaste data if needed to the fragment
        }
        replaceFragment(sendAmountFragment, SendAmountFragment.TAG);
    }

    @Override
    public void showTransactionDialog(@Navigator.SendKinSteps int status) {
        TransactionDialogFragment dialogFragment = (TransactionDialogFragment) getSupportFragmentManager()
                .findFragmentByTag(TransactionDialogFragment.TAG);

        if (dialogFragment == null) {
            dialogFragment = TransactionDialogFragment.getInstance(status);
            dialogFragment.show(getSupportFragmentManager(), TransactionDialogFragment.TAG);

        } else {
            dialogFragment.setStep(status);
            if (!dialogFragment.getDialog().isShowing()) {
                dialogFragment.show(getSupportFragmentManager(), TransactionDialogFragment.TAG);
            }
        }
    }

    @Override
    public void showAddNewContactDialog() {
        ContactDialogFragment dialogFragment = ContactDialogFragment.getInstance();
        dialogFragment.show(getSupportFragmentManager(), ContactDialogFragment.TAG);
    }

    private void replaceFragment(Fragment sendKinFragment, @NonNull String tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left,
                        R.anim.slide_in_left,
                        R.anim.slide_out_right)
                .replace(R.id.fragmentContainer, sendKinFragment, tag);
        transaction.commit();
    }
}
