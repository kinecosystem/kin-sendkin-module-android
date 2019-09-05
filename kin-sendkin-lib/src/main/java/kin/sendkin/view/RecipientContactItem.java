package kin.sendkin.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import kin.sendkin.R;
import kin.sendkin.core.model.RecipientContact;
import kin.sendkin.core.view.Utils;

class RecipientContactItem extends RecyclerView.ViewHolder {
    private final long ANIM_ADD_DURATION = 1500L;
    private final long ANIM_ADD_DELAY = 200L;


    private TextView name, address;
    private View cover;
    protected View edit;
    private float coverLen;

    RecipientContactItem(@NonNull View view) {
        super(view);
        name = view.findViewById(R.id.name);
        address = view.findViewById(R.id.address);
        edit = view.findViewById(R.id.edit);
        cover = view.findViewById(R.id.cover);
        cover.setVisibility(View.GONE);
        coverLen = Utils.getScreenWidth(view.getContext());
    }

    void bind(RecipientContact contact) {
        this.name.setText(contact.getName());
        this.address.setText(Utils.getAddressShortenFormat(contact.getAddress()));
        cover.setVisibility(View.GONE);
    }

    void animateAdded() {
        cover.clearAnimation();
        cover.setX(-coverLen);
        cover.setVisibility(View.VISIBLE);
        cover.animate().translationXBy(coverLen * 2).setDuration(ANIM_ADD_DURATION).setStartDelay(ANIM_ADD_DELAY).setInterpolator(new AccelerateDecelerateInterpolator());
    }
}