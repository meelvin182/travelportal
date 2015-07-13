package ru.ipccenter.travelportal.metamodel.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;

/**
 * Created by Nazariy Tupitsa on 27.02.2015.
 */
@Entity
@Table(name = "TP_ATTRIBUTES")
public class MMAttribute implements Serializable{

    @Id
    @GeneratedValue(generator = "id_gen")
    @SequenceGenerator(name = "id_gen", sequenceName = "TP_ID_SEQ", allocationSize = 1)
    @Column(name = "ATTR_ID")
    private BigInteger id;

    @JoinColumn(name = "ATTR_TYPE_ID", referencedColumnName = "ATTR_TYPE_ID")
    @ManyToOne(optional = false)
    private MMAttributeType attrType;

    @Column(name = "ORDER_NUM")
    private Integer orderNum;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PROPERTIES")
    private String properties;

    @Column(name = "MASK")
    private String mask;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public MMAttributeType getAttrType() {
        return attrType;
    }

    public void setAttrType(MMAttributeType attrType) {
        this.attrType = attrType;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MMAttribute attribute = (MMAttribute) o;

        if (!attrType.equals(attribute.attrType)) return false;
        if (!id.equals(attribute.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + attrType.hashCode();
        return result;
    }
}
