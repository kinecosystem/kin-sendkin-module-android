package kin.sendkin.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.UUID;

import kin.sendkin.R;
import kin.sendkin.core.model.RecipientContact;
import kin.sendkin.core.store.RecipientContactsRepo;

public class RecipientContactsAdapter extends RecyclerView.Adapter<RecipientContactItem> {

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
    public RecipientContactItem onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item, parent, false);
        return new RecipientContactItem(view);
    }

    @Override
    public void onBindViewHolder(RecipientContactItem holder, final int position) {
        final RecipientContact recipientContact = contacts.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final RecipientContact recipientContact = contacts.get(position);
                recipientContactTouchListener.onContactClicked(recipientContact.getId());
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final RecipientContact recipientContact = contacts.get(position);
                recipientContactTouchListener.onEditContact(recipientContact.getId());
            }
        });
        holder.bind(recipientContact);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    void refreshData() {
        this.contacts = recipientContactsRepo.getAll();
        notifyDataSetChanged();
    }
}

