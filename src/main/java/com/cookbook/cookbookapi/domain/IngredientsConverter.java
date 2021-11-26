package com.cookbook.cookbookapi.domain;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.List;

import static java.lang.String.join;
import static java.util.Arrays.asList;

@Converter
class IngredientsConverter implements AttributeConverter<List<String>, String> {

    private static final String SEPERATOR = ",";

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        return join(SEPERATOR, attribute);
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        return asList(dbData.split(SEPERATOR));
    }
}
