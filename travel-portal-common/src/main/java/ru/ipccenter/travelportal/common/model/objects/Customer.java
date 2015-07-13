package ru.ipccenter.travelportal.common.model.objects;

import ru.ipccenter.travelportal.common.model.TPObject;
import ru.ipccenter.travelportal.common.model.bridge.annotations.ObjectType;

import java.io.Serializable;

/**
 * Created by meelvin182 on 05.03.15.
 */
@ObjectType(id = "9223372036854775757",
        name = "customer",
        attributes = {})
public interface Customer extends TPObject, Serializable {
}
