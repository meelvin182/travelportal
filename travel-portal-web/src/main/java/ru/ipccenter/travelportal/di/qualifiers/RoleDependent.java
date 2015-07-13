package ru.ipccenter.travelportal.di.qualifiers;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * Created by Ivan on 29.04.2015.
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ FIELD, TYPE, METHOD })
public @interface RoleDependent {
    String value();
    //EnumType value();

    //enum EnumType {
    //  ROLE1,
    //  ROLE2,
    //  ROLE3;
    //
    //  public static EnumType valueOf(String value) { ... }
    //}
}
