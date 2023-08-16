package com.teachsync.utils.converters;

import com.teachsync.utils.enums.Gender;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class GenderConverter implements AttributeConverter<Gender, String> {
    @Override
    public String convertToDatabaseColumn(Gender gender) {
        if (gender == null) {
            return null; }

        return gender.getStringValue();
    }

    @Override
    public Gender convertToEntityAttribute(String gender) {
         if (gender == null) {
             return null; }

        return Stream.of(Gender.values())
                .filter(s -> s.getStringValue().equals(gender))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new); /* Should only happen if Enum was deleted */
        /* TODO: exception handler for deleted enum */
    }
}