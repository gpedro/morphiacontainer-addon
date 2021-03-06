package org.tylproject.vaadin.addon.mongo;

import java.util.Arrays;
import java.util.Collection;

import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.tylproject.data.mongo.CelestialBody;
import org.tylproject.vaadin.addon.HierarchicalMongoContainer;
import org.tylproject.vaadin.addon.MongoContainer;

import com.mongodb.MongoClient;

/**
 * Created by evacchi on 29/01/15.
 */
public class HierarchicalTest {

    protected final MongoOperations mongoOps;
    protected final Class<CelestialBody> beanClass = CelestialBody.class;

    public HierarchicalTest() {
        try {
            this.mongoOps = new MongoTemplate(new MongoClient(), "database");
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }

    public MongoContainer.Builder<CelestialBody> builder() {
        return MongoContainer.Builder.forEntity(beanClass, mongoOps)
                .withPageSize(3).sortedBy(new Sort("name"));
    }

    @Before
    public void setupDatabase() throws Exception {
        CelestialBody sun = new CelestialBody("The Sun", null);
        CelestialBody mercury = new CelestialBody("Mercury", sun);
        CelestialBody venus = new CelestialBody("Venus", sun);
        CelestialBody earth = new CelestialBody("Earth", sun);
        CelestialBody moon = new CelestialBody("The Moon", earth);
        CelestialBody mars = new CelestialBody("Mars", sun);

        mongoOps.insert(Arrays.asList(sun, mercury, venus, earth, moon, mars),
                beanClass);

    }

    @After
    public void teardownDatabase() {
        for (CelestialBody c : mongoOps.findAll(CelestialBody.class))
            mongoOps.remove(c);
    }

    @Test
    public void testHierarchy() {

        HierarchicalMongoContainer<CelestialBody> mc = builder()
                .buildHierarchical("parent");

        Collection<ObjectId> roots = mc.rootItemIds();
        for (ObjectId r : roots) {
            System.out.println(toStringRecursive(0, r, mc));
        }

    }

    public String toStringRecursive(int depth, ObjectId r,
            HierarchicalMongoContainer<CelestialBody> mc) {

        char[] spaces = new char[depth * 2];
        Arrays.fill(spaces, ' ');

        CelestialBody elem = mc.getItem(r).getBean();

        String children = "";
        for (ObjectId child : mc.getChildren(r)) {
            children += toStringRecursive(depth + 1, child, mc);
        }

        return new String(spaces) + elem + "\n" + children;

    }

}
