package org.kin.sending.presenter;

import android.support.annotation.NonNull;

public interface RecipientAddressListener {
    void onRecipientAddressChanged(@NonNull String address);
}
