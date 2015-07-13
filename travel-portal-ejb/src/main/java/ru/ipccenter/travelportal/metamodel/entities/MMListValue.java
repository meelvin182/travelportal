package ru.ipccenter.travelportal.metamodel.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;

@Entity
@Table(name = "TP_LIST_VALUES")
public class MMListValue implements Serializable {

    @Id
    @GeneratedValue(generator = "id_gen")
    @SequenceGenerator(name = "id_gen", sequenceName = "TP_ID_SEQ", allocationSize = 1)
    @Column(name = "LIST_VALUE_ID", nullable = false, precision=20, scale=0)
    private BigInteger listValueId;

    @Column(name = "LIST_VALUE_TYPE_ID", nullable = false, precision=20, scale=0)
    private BigInteger listValueTypeId;

    @Column(name = "VALUE", nullable = false, length = 250)
    private String value;

    @Column(name = "ORDER_NUM", nullable = false, precision = 10, scale = 0)
    private Integer orderNum;


    public MMListValue() {}

    public BigInteger getListValueId() {
        return listValueId;
    }

    public void setListValueId(BigInteger listValueId) {
        this.listValueId = listValueId;
    }

    public BigInteger getListValueTypeId() {
        return listValueTypeId;
    }

    public void setListValueTypeId(BigInteger listValueTypeId) {
        this.listValueTypeId = listValueTypeId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

}
