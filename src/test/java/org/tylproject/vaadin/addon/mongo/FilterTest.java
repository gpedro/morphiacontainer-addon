package org.tylproject.vaadin.addon.mongo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.Collection;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Test;
import org.springframework.data.mongodb.core.query.Criteria;
import org.tylproject.data.mongo.Customer;
import org.tylproject.vaadin.addon.MongoContainer;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.filter.And;
import com.vaadin.data.util.filter.Not;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;

/**
 * Created by evacchi on 09/01/15.
 */

public class FilterTest extends BaseTest {

    // fetch using Container
    @Test
    public void testContainsFilteredItem() {

        final MongoContainer<Customer> mc = builder().build();

        assertEquals(7, mc.size());

        mc.addContainerFilter(new SimpleStringFilter("firstName", "i", false,
                false));

        List<Customer> bs = mongoOps.find(
                query(where("firstName").regex(".*i.*")), beanClass);

        assertEquals(5, mc.size());

        for (Customer c : bs) {
            System.out.println(c);
            assertEquals(true, mc.containsId(c.getId()));
        }

        mc.removeAllContainerFilters();

        assertEquals(7, mc.size());
    }

    @Test
    public void testFilterList() {
        final Criteria crit = where("firstName").regex(".*i.*");

        final MongoContainer<Customer> mc = builder().build();

        assertEquals(7, mc.size());

        List<Customer> bs = mongoOps.find(
                query(crit), beanClass);

        mc.addContainerFilter(new SimpleStringFilter("firstName", "i", false,
                false));

        ObjectId itemId = mc.firstItemId();
        int i = 0;
        do {
            BeanItem<Customer> item = mc.getItem(itemId);
            assertEquals(bs.get(i++), item.getBean());
            itemId = mc.nextItemId(itemId);
        } while (itemId != null);

    }

    @Test
    @SuppressWarnings("unused")
    public void testOrFilter() {

        final MongoContainer<Customer> mc = builder().build();

        mc.addContainerFilter(new Or(new SimpleStringFilter("firstName", "i",
                false, false), new SimpleStringFilter("firstName", "x", false,
                false)));

        ObjectId itemId = mc.firstItemId();

        // should not raise error
    }

    @Test
    @SuppressWarnings("unused")
    public void testNOrFilter() {

        final MongoContainer<Customer> mc = builder().build();

        mc.addContainerFilter(new Not(new Or(new SimpleStringFilter(
                "firstName", "i", false, false), new SimpleStringFilter(
                "firstName", "x", false, false))));

        ObjectId itemId = mc.firstItemId();

        // should not raise error
    }

    @Test
    @SuppressWarnings("unused")
    public void testAndFilter() {

        final MongoContainer<Customer> mc = builder().build();

        mc.addContainerFilter(new And(new SimpleStringFilter("firstName", "i",
                false, false), new SimpleStringFilter("firstName", "x", false,
                false)));

        ObjectId itemId = mc.firstItemId();

        // should not raise error
    }

    @Test
    public void testAddMoreFilters() {
        final MongoContainer<Customer> mc = builder().build();

        Container.Filter f1 = new SimpleStringFilter("firstName", "i", false,
                false);
        Container.Filter f2 = new SimpleStringFilter("lastName", "x", false,
                false);

        mc.addContainerFilter(f1);
        mc.addContainerFilter(f2);

        Collection<Container.Filter> filters = mc.getContainerFilters();
        assertEquals(2, filters.size());
        assertTrue(filters.contains(f1));
        assertTrue(filters.contains(f2));

    }

    @Test
    public void testRemoveFilters() {
        final MongoContainer<Customer> mc = builder().build();

        Container.Filter f1 = new SimpleStringFilter("firstName", "i", false,
                false);
        Container.Filter f2 = new SimpleStringFilter("lastName", "x", false,
                false);

        mc.addContainerFilter(f1);
        mc.addContainerFilter(f2);

        {
            mc.removeContainerFilter(f1);
            Collection<Container.Filter> filters = mc.getContainerFilters();

            assertEquals(1, filters.size());
            assertFalse(filters.contains(f1));
            assertTrue(filters.contains(f2));
        }

        {
            mc.removeContainerFilter(f2);
            Collection<Container.Filter> filters = mc.getContainerFilters();

            assertEquals(0, filters.size());
            assertFalse(filters.contains(f1));
            assertFalse(filters.contains(f2));
        }
    }

}
