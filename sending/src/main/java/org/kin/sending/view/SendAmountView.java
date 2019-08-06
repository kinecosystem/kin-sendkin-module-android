package org.kin.sending.view;

import org.kin.sendkin.core.base.BaseView;

public interface SendAmountView extends BaseView {
    void showAmountValidity(boolean isValid, boolean errorDetails);
}
