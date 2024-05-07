package org.design_manager_project.util;

import org.modelmapper.AbstractConverter;
import org.modelmapper.Conditions;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AppUtils {

    public static final ModelMapper mapper;

    static {
        mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        mapper.getConfiguration().setFieldMatchingEnabled(true);
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());

        Converter<String, Instant> toStringInstant = new AbstractConverter<>() {
            @Override
            protected Instant convert(String source) {
                try {
                    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    return Instant.from(format.parse(source));
                } catch (DateTimeParseException e){
                    throw new IllegalArgumentException("Invalid Instant format: " + source, e);
                }
            }
        };

        Converter<String, LocalDate> toStringDate = new AbstractConverter<>() {
            @Override
            protected LocalDate convert(String source) {
                try{
                    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    return LocalDate.parse(source, format);
                } catch (DateTimeParseException e){
                    throw new IllegalArgumentException("Invalid LocalDate format: " + source, e);
                }
            }
        };

        mapper.createTypeMap(String.class, Instant.class);
        mapper.addConverter(toStringInstant);

        mapper.createTypeMap(String.class, LocalDate.class);
        mapper.addConverter(toStringDate);
    }
}
