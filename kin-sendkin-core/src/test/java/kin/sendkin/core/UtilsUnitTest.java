package kin.sendkin.core;

import org.junit.Before;
import org.junit.Test;
import kin.sendkin.core.model.RecipientContact;
import kin.sendkin.core.view.Utils;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class UtilsUnitTest {

    private final String address1 = "GCCLBNMWRUKPCWV4J6H42KAQQ6GSOG6YKRNONAT3QDOGRH3T3EQGSXJR";
    private final String name1 = "James Bond";

    private final String address2 = "GDK57YFZRKNKJTRLE3CZ2GNQCSRC5NPJQWIEMD43M7V6ZIHNARGBIMFI";
    private final String name2 = "Mr Bond";

    private RecipientContact contact1;
    private RecipientContact contact2;


    @Before
    public void initMocks() {
        contact1 = new RecipientContact(name1, address1);
        contact2 = new RecipientContact(name2, address2);
    }


    @Test
    public void parseTest() {
        String contactStr = Utils.parseToString(contact1);
        final RecipientContact parsedContact = Utils.parseFromString(contactStr);
        assertTrue(contact1.equals(parsedContact));
    }

    @Test
    public void parseArrayTest() {
        ArrayList<RecipientContact> originContacts = new ArrayList<>();
        String contactStr = Utils.parseToString(originContacts);
        ArrayList<RecipientContact> parsedContact = Utils.parseContactsFromString(contactStr);
        assertTrue(parsedContact.equals(originContacts));
        originContacts.add(contact1);
        assertFalse(parsedContact.equals(originContacts));
        contactStr = Utils.parseToString(originContacts);
        parsedContact = Utils.parseContactsFromString(contactStr);
        assertTrue(parsedContact.equals(originContacts));
        originContacts.add(contact2);
        assertFalse(parsedContact.equals(originContacts));
        contactStr = Utils.parseToString(originContacts);
        parsedContact = Utils.parseContactsFromString(contactStr);
        assertTrue(parsedContact.equals(originContacts));
    }

    @Test
    public void AddressShortenFormatTest() {
        final String shortAddress1 = Utils.getAddressShortenFormat(address1);
        assertEquals(shortAddress1, "XXX-SXJR");
    }

}