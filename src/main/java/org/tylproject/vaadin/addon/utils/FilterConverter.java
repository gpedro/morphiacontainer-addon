package org.tylproject.vaadin.addon.utils;

import java.util.Collection;
import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;

import com.vaadin.data.Container;

/**
 * Created by evacchi on 02/12/14.
 */
public interface FilterConverter {
    Criteria convert(Container.Filter f);

    Criteria convertNegated(Container.Filter filter);

    List<Criteria> convertAll(Collection<Container.Filter> fs);
}
