package kin.sendkin.view;

import kin.sendkin.core.base.BaseView;

public interface SendAmountView extends BaseView {
    void showAmountValidity(boolean isValid, boolean errorDetails);
}
