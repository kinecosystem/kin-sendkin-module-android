package org.kin.sending.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import org.kin.sending.R;

public class KinBalanceActionBar extends ConstraintLayout {

    private TextView balanceTextView;
    private View kinIcon;
    private View back, closeX;
    private OnClickCallback clickCallback;

    public KinBalanceActionBar(Context context) {
        super(context);
        init();
    }

    public KinBalanceActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void addClickListner(@NonNull OnClickCallback clickCallback) {
        this.clickCallback = clickCallback;
    }

    public void updateBalance(String balance) {
        this.kinIcon.setVisibility(VISIBLE);
        this.balanceTextView.setText(balance);
    }

    public void enableBack(Boolean enable) {
        if (enable) {
            back.setVisibility(VISIBLE);
            closeX.setVisibility(GONE);
        } else {
            back.setVisibility(GONE);
            closeX.setVisibility(VISIBLE);
        }
    }

    private void init() {
        inflate(getContext(), R.layout.balnce_action_bar, this);
        balanceTextView = findViewById(R.id.kinBalance);
        kinIcon = findViewById(R.id.kinIcon);
        back = findViewById(R.id.back);
        closeX = findViewById(R.id.closeX);
        closeX.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickCallback != null) {
                    clickCallback.onCloseClicked();
                }
            }
        });
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickCallback != null) {
                    clickCallback.onBackClicked();
                }
            }
        });
    }

    public interface OnClickCallback {
        void onCloseClicked();

        void onBackClicked();
    }


}
