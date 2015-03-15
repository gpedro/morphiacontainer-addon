package org.tylproject.vaadin.addon.utils;

import java.util.Locale;

import org.bson.types.ObjectId;

import com.vaadin.data.util.converter.Converter;

/**
 * Created by evacchi on 19/01/15.
 */
public class StringToObjectIdConverter implements Converter<String, ObjectId> {

    public ObjectId convertToModel(String value,
            Class<? extends ObjectId> targetType, Locale locale)
            throws ConversionException {
        return value == null || value.equals("") ? null : new ObjectId(value);
    }

    public String convertToPresentation(ObjectId value,
            Class<? extends String> targetType, Locale locale)
            throws ConversionException {
        return value == null ? null : value.toString();
    }

    public Class<ObjectId> getModelType() {
        return ObjectId.class;
    }

    public Class<String> getPresentationType() {
        return String.class;
    }
}
