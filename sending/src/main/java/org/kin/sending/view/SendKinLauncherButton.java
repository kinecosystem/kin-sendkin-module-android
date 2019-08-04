package org.kin.sending.view;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import org.kin.sending.R;

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
        setText(R.string.kin_send_kin_launcher_button);
        setTextColor(getResources().getColor(R.color.kin_white));
        setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_purple_background));
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                try {
//                    if (SendKinManager.getInstance() != null) {
//                        SendKinManager.getInstance().launchSendActivity(getContext());
//                    } else {
//                        setEnabled(false);
//                    }
//                } catch (ModuleError moduleError) {
//                    setEnabled(false);
//                }
            }
        });
    }
}
