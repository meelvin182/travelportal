package ru.ipccenter.travelportal.di.qualifiers;

import ru.ipccenter.travelportal.common.model.objects.Role;

import javax.enterprise.util.AnnotationLiteral;

/**
 * Created by Ivan on 29.04.2015.
 */
public class RoleQualifier extends AnnotationLiteral<RoleDependent> implements RoleDependent {

    private String role;

    public RoleQualifier(Role role) {
        this.role = role.getName();
    }

    public RoleQualifier(String role) {
        this.role = role;
    }

    @Override
    public String value() {
        return role;
    }
}
