package org.tylproject.vaadin.addon.mongo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.bson.types.ObjectId;
import org.junit.Test;
import org.tylproject.data.mongo.Customer;
import org.tylproject.vaadin.addon.BufferedMongoContainer;

/**
 * Created by evacchi on 12/11/14.
 */
public class BufferedMongoContainerTest extends BaseTest {

    @Test
    public void testRemoveFirstItem() {
        final BufferedMongoContainer<Customer> mc = builder().buildBuffered();

        int initSize = mc.size();

        ObjectId itemId = mc.firstItemId();
        mc.removeItem(itemId);
        ObjectId nextItemId = mc.firstItemId();
        assertNotNull(nextItemId);
        assertNotEquals(itemId, nextItemId);
        mc.removeItem(nextItemId);

        assertEquals(-1, mc.indexOfId(itemId));
        assertFalse(mc.getItemIds(0, 1).contains(nextItemId));
        assertNotEquals(null, mc.getItemIds(0, 1).get(0));
        assertNotEquals(initSize, mc.size());
    }

}
