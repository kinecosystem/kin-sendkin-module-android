package kin.sendkin.view;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.Group;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import kin.sendkin.R;
import kin.sendkin.core.view.SendKinEditText;
import kin.sendkin.presenter.RecipientAddressPresenter;
import kin.sendkin.presenter.RecipientAddressPresenterImpl;
import kin.sendkin.presenter.SendKinPresenter;

public class RecipientAddressFragment extends Fragment implements RecipientAddressView {

    public static final String TAG = RecipientAddressFragment.class.getSimpleName();

    private static final long ANIM_DELAY = 200l;
    private SendKinEditText inputRecipientAddress;
    private TextView errorInfo;
    private RecyclerView contactsList;
    private SendKinPresenter sendKinPresenter;
    private RecipientContactsAdapter listAdapter;
    private Group listGroupView, emptyListGroupView;
    private View loader;
    private LinearLayoutManager listLayoutManager;

    public static RecipientAddressFragment getInstance() {
        RecipientAddressFragment fragment = new RecipientAddressFragment();
        return fragment;
    }

    private RecipientAddressPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (!(getActivity() instanceof SendKinView)) {
            getActivity().finish();
        }
        sendKinPresenter = ((SendKinView) getActivity()).getSendKinPresenter();
        View root = inflater.inflate(R.layout.recepient_address_page, container, false);
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        presenter = new RecipientAddressPresenterImpl(sendKinPresenter, clipboard);
        presenter.onAttach(this);
        initViews(root);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    private void initViews(View root) {
        root.findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onNextClicked();
            }
        });

        inputRecipientAddress = root.findViewById(R.id.recipientAddress);
        contactsList = root.findViewById(R.id.contactsList);
        listLayoutManager = new LinearLayoutManager(getContext());
        listLayoutManager.setSmoothScrollbarEnabled(true);
        contactsList.setLayoutManager(listLayoutManager);
        listAdapter = new RecipientContactsAdapter(sendKinPresenter.getRecipientContactsRepo(), sendKinPresenter);
        contactsList.setAdapter(listAdapter);
        listGroupView = root.findViewById(R.id.groupListContacts);
        emptyListGroupView = root.findViewById(R.id.groupEmptyListContacts);
        loader = root.findViewById(R.id.loader);
        inputRecipientAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                presenter.setRecipientAddress(s.toString());
            }
        });
        errorInfo = root.findViewById(R.id.errorInfo);
        root.findViewById(R.id.paste).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onPasteClicked();
            }
        });
        root.findViewById(R.id.showAddress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onShowPublicAddressClicked();
            }
        });
        root.findViewById(R.id.publicAddressInfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onWhatIsPublicAddressClicked();
            }
        });
        root.findViewById(R.id.addNewContact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onAddNewContactClicked();
            }
        });
        root.findViewById(R.id.addNewContactBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onAddNewContactClicked();
            }
        });
    }

    @Override
    public void showAddressValidity(boolean isValid, boolean showDetail) {
        if (isValid) {
            inputRecipientAddress.clearError();
            errorInfo.setVisibility(View.GONE);
        } else {
            inputRecipientAddress.showError();
            if (showDetail) {
                errorInfo.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void updateReceiverAddress(String pasteAddress) {
        inputRecipientAddress.setText(pasteAddress);
    }

    @Override
    public void notifyContactChanged() {
        listAdapter.refreshData();
    }

    @Override
    public void notifyContactAdded(final int position) {
        listAdapter.refreshData();
        scrollAndAnimate(position, ANIM_DELAY);
    }

    @Override
    public void updateListVisibility(final boolean isEmptyList) {
        if (!isEmptyList) {
            loader.setVisibility(View.GONE);
            listGroupView.setVisibility(View.VISIBLE);
            emptyListGroupView.setVisibility(View.GONE);
        } else {
            loader.setVisibility(View.GONE);
            listGroupView.setVisibility(View.GONE);
            emptyListGroupView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showContactsLoader() {
        loader.setVisibility(View.VISIBLE);
    }

    private void scrollAndAnimate(final int position, final long delay) {
        contactsList.postDelayed(new Runnable() {
            @Override
            public void run() {
                listLayoutManager.scrollToPosition(position);
                RecyclerView.ViewHolder viewHolder = contactsList.findViewHolderForAdapterPosition(position);
                if (viewHolder != null) {
                    RecipientContactItem item = (RecipientContactItem) viewHolder;
                    listLayoutManager.scrollToPosition(position);
                    item.animateAdded();
                } else {
                    scrollAndAnimate(position, delay + 50);
                }
            }
        }, delay);
    }
}
