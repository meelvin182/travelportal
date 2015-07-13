package ru.ipccenter.travelportal.metamodel.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

/**
 * Created by Nazariy Tupitsa on 27.02.2015.
 */

@Entity
@Table(name = "TP_OBJECT_TYPES")
public class MMObjectType implements Serializable {

    @Id
    @GeneratedValue(generator = "id_gen")
    @SequenceGenerator(name = "id_gen", sequenceName = "TP_ID_SEQ", allocationSize = 1)
    @Column(name = "OBJECT_TYPE_ID")
    private BigInteger id;

    @Column(name = "NAME", unique = true)
    private String name;

    @JoinColumn(name = "PARENT_ID", referencedColumnName = "OBJECT_TYPE_ID")
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = MMObjectType.class)
    private MMObjectType parent;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = MMAttribute.class)
    @JoinTable(name = "ATTR_OBJECT_TYPES",
            joinColumns = {@JoinColumn(name = "OBJECT_TYPE_ID", referencedColumnName = "OBJECT_TYPE_ID", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "ATTR_ID", referencedColumnName = "ATTR_ID", nullable = false)}
    )
    @OrderBy("orderNum")
    private List<MMAttribute> attributes;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MMObjectType getParent() {
        return parent;
    }

    public void setParent(MMObjectType parent) {
        this.parent = parent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<MMAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<MMAttribute> attributes) {
        this.attributes = attributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MMObjectType that = (MMObjectType) o;

        if (!id.equals(that.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
