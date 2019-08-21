package org.kin.sendkin.core.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class RecipientContact implements Comparable<RecipientContact> {
    @SerializedName("contact_name")
    private String name;
    @SerializedName("contact_address")
    private String address;
    @SerializedName("contact_id")
    private UUID id;

    public RecipientContact(String name, String address) {
        this.name = name;
        this.address = address;
        id = UUID.randomUUID();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof RecipientContact)) {
            return false;
        }
        RecipientContact contact = (RecipientContact) o;
        return name.equals(contact.name) &&
                address.equals(contact.address) &&
                id.equals(contact.id);
    }

    @Override
    public int hashCode() {
        return System.identityHashCode(this);
    }

    @Override
    public int compareTo(@NonNull RecipientContact contact) {
        if (!name.toLowerCase().equals(contact.name.toLowerCase()))
            return name.toLowerCase().compareTo(contact.name.toLowerCase());
        if (!address.equals(contact.address)) {
            return address.compareTo(contact.address);
        }
        return 0;
    }

    @Override
    public String toString() {
        return "Name:[" + name + "] address:[" + address + "] id:[" + id + "]";
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
        id = UUID.randomUUID();
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public UUID getId() {
        return id;
    }
}
