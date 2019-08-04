package org.kin.sendkin.core;

import org.junit.Test;
import org.kin.sendkin.core.model.KinManager;
import org.kin.sendkin.core.model.KinManagerImpl;
import org.mockito.Mock;

import kin.sdk.KinAccount;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.when;

public class KinManagerImplTest {

    KinAccount kinAccount = mock(KinAccount.class);
    KinManager kinManager = new KinManagerImpl(kinAccount);
    String validAddress = "GBNY5GQ6WOG5JU4JZEPJ4WZIMYMC5HGPYLSXT7S3GBDJ2S3CM4NPTBNC";
    String shortAddress = "GBNY5GQ6WOG5JU4JZEP";
    String nonValidAddress = "ABNY5GQ6WOG5JU4JZEPJ4WZIMYMC5HGPYLSXT7S3GBDJ2S3CM4NPTBNC";

    //receiver address
    String receiverAddress = "GDN2L6SNFA575JGGHXZMGETLQBHJAI5QVOZZSSHWP44Y24LAG7DJLF4T";

    @Test
    public void isValidAddress() {
        when(kinAccount.getPublicAddress()).thenReturn(validAddress);
        assertTrue(kinManager.isValidAddress(validAddress));
    }

    @Test
    public void isValidAddressAddressNotValid() {
        assertFalse(kinManager.isValidAddress(null));
        assertFalse(kinManager.isValidAddress(""));
        assertFalse(kinManager.isValidAddress(nonValidAddress));
        assertFalse(kinManager.isValidAddress(shortAddress));
    }
}