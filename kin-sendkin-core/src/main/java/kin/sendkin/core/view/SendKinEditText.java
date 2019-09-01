package kin.sendkin.core.view;

import android.content.Context;
import android.util.AttributeSet;

import kin.sendkin.core.R;


public class SendKinEditText extends android.support.v7.widget.AppCompatEditText {
    public SendKinEditText(Context context) {
        super(context, null);
    }

    public SendKinEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void showError() {
        hasError(true);
    }

    public void clearError() {
        hasError(false);
    }

    private void hasError(boolean hasError) {
        if (hasError) {
            setBackground(getResources().getDrawable(R.drawable.senkin_edittext_error));
        } else {
            setBackground(getResources().getDrawable(R.drawable.sendkin_edittext_state_list));
        }
    }
}
