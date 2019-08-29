package kin.sendkin.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.UUID;
import kin.sendkin.core.model.RecipientContact;

import kin.sendkin.R;
import kin.sendkin.core.store.RecipientContactsRepo;
import kin.sendkin.core.view.Utils;

public class RecipientContactsAdapter extends RecyclerView.Adapter<RecipientContactsAdapter.RecipientContactView> {

    public interface RecipientContactTouchListener {
        void onEditContact(@NonNull UUID id);
        void onContactClicked(@NonNull UUID id);
    }

    private ArrayList<RecipientContact> contacts;
    private RecipientContactsRepo recipientContactsRepo;
    private RecipientContactTouchListener recipientContactTouchListener;

    public RecipientContactsAdapter(@NonNull RecipientContactsRepo recipientContactsRepo, @NonNull RecipientContactTouchListener recipientContactTouchListener) {
        this.recipientContactsRepo = recipientContactsRepo;
        this.recipientContactTouchListener = recipientContactTouchListener;
        refreshData();
    }

    @Override
    public RecipientContactView onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item, parent, false);
        return new RecipientContactView(view, contacts, recipientContactTouchListener);
    }

    @Override
    public void onBindViewHolder(RecipientContactView holder, int position) {
        final RecipientContact recipientContact = contacts.get(position);
        holder.bind(recipientContact);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public static class RecipientContactView extends RecyclerView.ViewHolder {
        private TextView name, address;
        private View edit;

        public RecipientContactView(@NonNull View view, @NonNull final ArrayList<RecipientContact> contacts, @NonNull final RecipientContactTouchListener recipientContactTouchListener) {
            super(view);
            name = view.findViewById(R.id.name);
            address = view.findViewById(R.id.address);
            edit = view.findViewById(R.id.edit);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final RecipientContact recipientContact = contacts.get(getAdapterPosition());
                    recipientContactTouchListener.onEditContact(recipientContact.getId());
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final RecipientContact recipientContact = contacts.get(getAdapterPosition());
                    recipientContactTouchListener.onContactClicked(recipientContact.getId());
                }
            });
        }

        void bind(RecipientContact contact) {
            this.name.setText(contact.getName());
            this.address.setText(Utils.getAddressShortenFormat(contact.getAddress()));
        }
    }

    void refreshData() {
        this.contacts = recipientContactsRepo.getAll();
        notifyDataSetChanged();
    }
}

