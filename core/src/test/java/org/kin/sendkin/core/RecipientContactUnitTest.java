package org.kin.sendkin.core;

import org.junit.Test;
import org.kin.sendkin.core.model.RecipientContact;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;


public class RecipientContactUnitTest {

    private final String address1 = "GCCLBNMWRUKPCWV4J6H42KAQQ6GSOG6YKRNONAT3QDOGRH3T3EQGSXJR";
    private final String name1 = "James Bond";

    private final String address2 = "GDK57YFZRKNKJTRLE3CZ2GNQCSRC5NPJQWIEMD43M7V6ZIHNARGBIMFI";
    private final String name2 = "Mr Bond";

    private final RecipientContact contact1a = new RecipientContact(name1, address1);
    private final RecipientContact contact1b = new RecipientContact(name1, address1);

    private final RecipientContact contact2 = new RecipientContact(name2, address2);


    @Test
    public void equalsTest() {
        assertTrue(contact1a.equals(contact1a));
        assertFalse(contact1a.equals(contact1b));
        assertFalse(contact1b.equals(contact1a));
        assertFalse(contact2.equals(contact1a));
        assertFalse(contact1a.equals(contact2));
    }

    @Test
    public void compareTest() {
        assertEquals(contact2.compareTo(contact2), 0);
        assertEquals(contact1a.compareTo(contact1b), 0);
        assertEquals(contact1b.compareTo(contact1a), 0);
        assertNotEquals(contact1a.compareTo(contact2), 0);
        assertNotEquals(contact2.compareTo(contact1a), 0);
    }
}