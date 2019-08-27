package kin.sendkin.core.view;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

public abstract class BaseWideDialogFragment extends DialogFragment {

    protected abstract @LayoutRes int getLayoutRes();

    @Override
    public void onResume() {
        super.onResume();
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes(params);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        return inflater.inflate(getLayoutRes(), container);
    }

    public final void setCancelable(boolean cancelable) {
        super.setCancelable(cancelable);
        getDialog().setCanceledOnTouchOutside(cancelable);
        getDialog().setCancelable(cancelable);
    }
}
