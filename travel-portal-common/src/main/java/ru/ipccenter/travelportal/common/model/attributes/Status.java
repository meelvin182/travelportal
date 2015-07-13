package ru.ipccenter.travelportal.common.model.attributes;

import ru.ipccenter.travelportal.common.model.TPListAttribute;
import ru.ipccenter.travelportal.common.model.bridge.annotations.ListValueType;

import java.io.Serializable;

/**
 * Created by meelvin182 on 05.03.15.
 */
@ListValueType(id = "9223372036854775778")
public interface Status extends TPListAttribute<String>, Serializable {

}
