package org.kin.sendkin.core.model;

import java.util.UUID;

public class RecipientContact {
    private String name = "";
    private String address = "";
    private UUID id = UUID.randomUUID();

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public RecipientContact(String name, String address) {
        this.name = name;
        this.address = address;
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
