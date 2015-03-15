package org.tylproject.vaadin.addon;

import java.util.ArrayList;
import java.util.Collection;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.vaadin.data.Container;

/**
 * Created by evacchi on 29/01/15.
 */
public class HierarchicalMongoContainer<Bean> extends MongoContainer<Bean>
        implements Container.Hierarchical {

    private final String parentProperty;
    private Object lastRequestedItemId;
    private Collection<ObjectId> lastRequestedChildren;

    public HierarchicalMongoContainer(Builder<Bean> bldr) {
        super(bldr);
        this.parentProperty = bldr.parentProperty;

    }

    public boolean areChildrenAllowed(Object itemId) {
        return getChildren(itemId).size() > 0;
    }

    public Collection<ObjectId> getChildren(Object itemId) {
        assertIdValid(itemId);

        if (lastRequestedItemId == itemId)
            return lastRequestedChildren;

        lastRequestedItemId = itemId;

        DBObject parentCriteria = new BasicDBObject();
        parentCriteria.put(parentProperty, itemId);
        ArrayList<ObjectId> ids = new ArrayList<ObjectId>();
        DBCursor cur = cursor(parentCriteria);

        while (cur.hasNext()) {
            ids.add((ObjectId) cur.next().get(ID));
        }

        lastRequestedChildren = ids;

        return ids;
    }

    public ObjectId getParent(Object itemId) {
        return (ObjectId) getItem(itemId).getItemProperty(parentProperty)
                .getValue();
    }

    public Collection<ObjectId> rootItemIds() {
        DBObject parentCriteria = new BasicDBObject();
        parentCriteria.put(parentProperty, null);
        ArrayList<ObjectId> ids = new ArrayList<ObjectId>();
        DBCursor cur = cursor(parentCriteria);

        while (cur.hasNext()) {
            ids.add((ObjectId) cur.next().get(ID));
        }

        return ids;

    }

    public boolean setParent(Object itemId, Object newParentId)
            throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public boolean setChildrenAllowed(Object itemId, boolean areChildrenAllowed)
            throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public boolean isRoot(Object itemId) {
        return getParent(itemId) == null;
    }

    public boolean hasChildren(Object itemId) {
        if (lastRequestedItemId == itemId)
            return lastRequestedChildren.size() > 0;
        else
            return getChildren(itemId).size() > 0;
    }
}
