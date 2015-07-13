package ru.ipccenter.travelportal.metamodel.entities.converters;

import ru.ipccenter.travelportal.metamodel.entities.values.DataType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Created by 111 on 19.03.2015.
 */
@Converter(autoApply = true)
public class DataTypeConverter implements AttributeConverter<DataType, Integer> {
    @Override
    public Integer convertToDatabaseColumn(DataType attribute) {
        return attribute.getDbValue();
    }

    @Override
    public DataType convertToEntityAttribute(Integer dbData) {
        return DataType.valueOf(dbData);
    }
}
