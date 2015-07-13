package ru.ipccenter.travelportal.metamodel.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Anastasia on 01.03.2015.
 */

@Entity
@Table(name = "TP_OBJECTS")
public class MMObject implements Serializable {

    @Id
    @GeneratedValue(generator = "id_gen")
    @SequenceGenerator(name = "id_gen", sequenceName = "TP_ID_SEQ", allocationSize = 1)
    @Column(name = "OBJECT_ID", nullable = false, precision = 20)
    private BigInteger id;

    @JoinColumn(name = "OBJECT_TYPE_ID", nullable = false, referencedColumnName = "OBJECT_TYPE_ID")
    @ManyToOne(optional = false, targetEntity = MMObjectType.class, fetch = FetchType.EAGER)
    private MMObjectType objType;

    @JoinColumn(name = "PARENT_ID", referencedColumnName = "OBJECT_ID")
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = MMObject.class)
    private MMObject parent;

    @OrderBy("orderNum")
    @OneToMany(fetch = FetchType.LAZY, targetEntity = MMObject.class, mappedBy = "parent")
    private List<MMObject> children;

    @Column(name = "NAME", nullable = false, length = 250)
    private String name;

    @Column(name = "DESCRIPTION", length = 1000)
    private String description;

    @Column(name = "ORDER_NUM", precision = 10)
    private Integer orderNum;

    public MMObject() {

    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public MMObjectType getObjTypeId() {
        return objType;
    }

    public void setObjType(MMObjectType objType) {
        this.objType = objType;
    }

    public MMObject getParent() {
        return parent;
    }

    public List<BigInteger> getChildIds() {
        if (children != null) {
            List<BigInteger> idList = new ArrayList<>(children.size());

            for (MMObject child : children) {
                idList.add(child.getId());
            }

            return idList;
        } else {
            return Collections.emptyList();
        }
    }

    public void setParent(MMObject parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MMObject)) return false;

        MMObject object = (MMObject) o;

        if (!id.equals(object.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
