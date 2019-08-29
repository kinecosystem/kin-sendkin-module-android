package kin.sendkin.view;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.TypedValue;

import kin.sendkin.R;

public class SendKinLauncherButton extends AppCompatButton {
    public SendKinLauncherButton(Context context) {
        super(context);
        init();
    }

    public SendKinLauncherButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setAllCaps(false);
        setText(R.string.send_kin);
        setTextColor(getResources().getColor(R.color.sendkin_white));
        setBackgroundDrawable(getResources().getDrawable(R.drawable.senkin_btn_rounded_purple));
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
    }
}
