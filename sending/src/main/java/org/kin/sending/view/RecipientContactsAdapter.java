package org.kin.sending.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.kin.sending.R;
import org.kin.sendkin.core.model.RecipientContact;
import org.kin.sendkin.core.store.RecipientContactsRepo;
import org.kin.sendkin.core.view.Utils;

import java.util.ArrayList;
import java.util.UUID;

public class RecipientContactsAdapter extends RecyclerView.Adapter<RecipientContactsAdapter.RecipientContactView> {

    public interface RecipientContactTouchListener{
        void onEditContact(@NonNull UUID id);
        void onContactClicked(@NonNull UUID id);
    }

    private ArrayList<RecipientContact> contacts;
    private RecipientContactsRepo recipientContactsRepo;
    private RecipientContactTouchListener recipientContactTouchListener;

    public RecipientContactsAdapter(RecipientContactsRepo recipientContactsRepo, RecipientContactTouchListener recipientContactTouchListener) {
        this.recipientContactsRepo = recipientContactsRepo;
        this.recipientContactTouchListener = recipientContactTouchListener;
        refreshData();
    }

    @Override
    public RecipientContactView onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item, parent, false);
        return new RecipientContactView(view);
    }

    @Override
    public void onBindViewHolder(RecipientContactView holder, int position) {
        final RecipientContact recipientContact = contacts.get(position);
        holder.bind(recipientContact);
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(recipientContactTouchListener != null){
                    recipientContactTouchListener.onEditContact(recipientContact.getId());
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(recipientContactTouchListener != null){
                    recipientContactTouchListener.onContactClicked(recipientContact.getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public static class RecipientContactView extends RecyclerView.ViewHolder {
        public TextView name, address;
        public View edit;

        public RecipientContactView(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            address = view.findViewById(R.id.address);
            edit = view.findViewById(R.id.edit);
        }

        public void bind(RecipientContact contact) {
            this.name.setText(contact.getName());
            this.address.setText(Utils.getAddressShortenFormat(contact.getAddress()));
        }
    }

    void refreshData(){
        this.contacts = recipientContactsRepo.getAll();
        notifyDataSetChanged();
    }
}

