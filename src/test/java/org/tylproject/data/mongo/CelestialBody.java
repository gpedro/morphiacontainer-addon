package org.tylproject.data.mongo;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public class CelestialBody implements Serializable {
    @Id
    private ObjectId id = new ObjectId();
    private String name;
    private ObjectId parent;

    public CelestialBody() {
    }

    public CelestialBody(String name, CelestialBody parent) {
        this.name = name;
        this.parent = parent == null ? null : parent.getId();
    }

    public ObjectId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ObjectId getParent() {
        return parent;
    }

    public void setParent(ObjectId parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "CelestialBody{" + "name='" + name + '\'' + '}';
    }
}