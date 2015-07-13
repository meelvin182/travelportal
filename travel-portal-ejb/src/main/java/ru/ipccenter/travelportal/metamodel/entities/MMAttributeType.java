package ru.ipccenter.travelportal.metamodel.entities;

import ru.ipccenter.travelportal.metamodel.entities.converters.DataTypeConverter;
import ru.ipccenter.travelportal.metamodel.entities.values.DataType;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Set;

@Entity
@Table(name = "TP_ATTRIBUTE_TYPES")
public class MMAttributeType implements Serializable{

    @Id
    @GeneratedValue(generator = "id_gen")
    @SequenceGenerator(name = "id_gen", sequenceName = "TP_ID_SEQ", allocationSize = 1)
    @Column(name = "ATTR_TYPE_ID", nullable = false, precision = 20, scale = 0)
    private BigInteger attrTypeId;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "LIST_VALUE_TYPE_ID", referencedColumnName = "LIST_VALUE_TYPE_ID")
    private Set<MMListValue> listValues;

    @Column(name = "NAME", nullable = false, length = 250)
    private String name;

    // @Converter used due to db values isn't correspond to enum's elements order
    @Convert(converter = DataTypeConverter.class)
    @Column(name = "DATA_TYPE", nullable = false, precision = 10, scale = 0)
    private DataType dataType;

    public BigInteger getAttrTypeId() {
        return attrTypeId;
    }

    public void setAttrTypeId(BigInteger attrTypeId) {
        this.attrTypeId = attrTypeId;
    }

    public Set<MMListValue> getListValues() {
        return listValues;
    }

    public void setListValues(Set<MMListValue> listValues) {
        this.listValues = listValues;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public MMAttributeType() {
        super();
    }
}
