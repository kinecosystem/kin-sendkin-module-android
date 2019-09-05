package kin.sendkin.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import kin.sdk.AccountStatus;
import kin.sdk.Environment;
import kin.sdk.KinAccount;
import kin.sdk.KinClient;
import kin.sdk.exception.CreateAccountException;
import kin.sendkin.KinSenderManager;
import kin.sendkin.events.SendKinEventsListener;
import kin.sendkin.events.SendKinPages;
import kin.sendkin.exceptions.KinSendInitException;
import kin.sendkin.view.SendKinLauncherButton;
import kin.utils.ResultCallback;

public class MainActivity extends AppCompatActivity implements ResultCallback<Integer> {

    private static String TAG = MainActivity.class.getName();

    private static String APP_ID = "SND";

    private KinSenderManager kinSenderManager;
    private OnBoarding onBoarding;
    private KinClient kinClient;
    private KinAccount kinAccount;
    private SendKinLauncherButton sendKinLauncherBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendKinLauncherBtn = findViewById(R.id.sendKinBtn);
        sendKinLauncherBtn.setEnabled(false);
        kinClient = new KinClient(this, Environment.TEST, APP_ID, "");

        onBoarding = new OnBoarding();
        sendKinLauncherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSendKinFlow();
            }
        });
        if(initAccount()){
            checkAccountStatus();
        }
    }

    private void initKinSenderManager() {

        kinSenderManager = new KinSenderManager(kinClient, kinAccount, new SendKinEventsListener() {
            @Override
            public void onViewPage(@NonNull SendKinPages page) {
                Log.d(TAG, "Event onViewPage " + page.name());
            }

            @Override
            public void onTransferFailed() {
                Log.d(TAG, "Event onTransferFailed");
            }

            @Override
            public void onTransferSuccess(@NonNull String transactionId) {
                Log.d(TAG, "Event onTransferSuccess transactionId " + transactionId);
            }

            @Override
            public void onTransactionTimeout() {
                Log.d(TAG, "Event onTransactionTimeout");

            }
        });
        sendKinLauncherBtn.setEnabled(true);
    }

    private void startSendKinFlow() {
        if (kinSenderManager != null) {
            try {
                kinSenderManager.startSendingContactFlow(this);
            } catch (KinSendInitException e) {
                Log.d(TAG, e.getMessage());
            }
        }
    }


    private boolean initAccount() {
        if (!kinClient.hasAccount()) {
            try {
                kinClient.addAccount();
            } catch (CreateAccountException e) {
                e.printStackTrace();
                Log.e(TAG, "Cant add account " + e.getMessage());
                return false;
            }
        }
        kinAccount = kinClient.getAccount(0);
        return true;
    }

    private void checkAccountStatus(){
        kinAccount.getStatus().run(this);
    }

    @Override
    public void onResult(Integer status) {
        if (AccountStatus.CREATED == status) {
            initKinSenderManager();
        } else if (AccountStatus.NOT_CREATED == status) {
            onBoarding.onBoard(kinAccount, new OnBoarding.Callbacks() {
                @Override
                public void onSuccess() {
                    initKinSenderManager();
                }

                @Override
                public void onFailure(Exception e) {
                    Log.e(TAG, "OnBoarding failed " + e.getMessage());
                }
            });
        }
    }

    @Override
    public void onError(Exception e) {
        Log.e(TAG, "get account status failed " + e.getMessage());
    }
}
