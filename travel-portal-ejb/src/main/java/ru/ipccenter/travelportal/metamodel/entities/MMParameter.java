package ru.ipccenter.travelportal.metamodel.entities;

import java.math.BigInteger;
import java.sql.Timestamp;

/**
 * Created by Anastasia on 06.03.2015.
 */
public class MMParameter {
    private BigInteger objId;
    private BigInteger attrId;
    private BigInteger listValueId;
    private Timestamp dateValue;
    private int orderNum;
    private String value;
    private BigInteger reference;

    // only for CGLib
    public MMParameter() {

    }

    public MMParameter(BigInteger objId, BigInteger attrId) {
        this(objId, attrId, 1);
    }

    public MMParameter(BigInteger objId, BigInteger attrId, int orderNum) {
        this.setObjId(objId);
        this.setAttrId(attrId);
        this.setOrderNum(orderNum);
    }

    public MMParameter(BigInteger objId, BigInteger attrId, BigInteger listValueId, Timestamp dateValue, int orderNum,
                       String value, BigInteger reference) {
        this.setObjId(objId);
        this.setAttrId(attrId);
        this.setListValueId(listValueId);
        this.setDateValue(dateValue);
        this.setOrderNum(orderNum);
        this.setValue(value);
        this.setReference(reference);
    }

    public BigInteger getObjId() {
        return objId;
    }

    public void setObjId(BigInteger objId) {
        this.objId = objId;
    }

    public BigInteger getAttrId() {
        return attrId;
    }

    public void setAttrId(BigInteger attrId) {
        this.attrId = attrId;
    }

    public BigInteger getListValueId() {
        return listValueId;
    }

    public void setListValueId(BigInteger listValueId) {
        this.listValueId = listValueId;
    }

    public Timestamp getDateValue() {
        return dateValue;
    }

    public void setDateValue(Timestamp dataValue) {
        this.dateValue = dataValue;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public BigInteger getReference() {
        return reference;
    }

    public void setReference(BigInteger reference) {
        this.reference = reference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MMParameter)) return false;

        MMParameter parameter = (MMParameter) o;

        if (orderNum != parameter.orderNum) return false;
        if (!attrId.equals(parameter.attrId)) return false;
        if (!objId.equals(parameter.objId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = objId.hashCode();
        result = 31 * result + attrId.hashCode();
        result = 31 * result + orderNum;
        return result;
    }

    @Override
    public String toString() {
        String str = "";
        if(objId != null)
            str += "\nObject_id: " + objId.toString();
        if(attrId != null)
            str += "\nattr_id: " + attrId.toString();
        if(listValueId != null)
            str += "\nlistValueId: " + listValueId.toString();
        if(reference != null)
            str += "\nreference: " + reference.toString();
        if(value != null)
            str += "\nvalue: " + value;
        return str;
    }
}
