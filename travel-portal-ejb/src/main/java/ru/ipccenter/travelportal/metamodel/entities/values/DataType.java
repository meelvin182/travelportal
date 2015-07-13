package ru.ipccenter.travelportal.metamodel.entities.values;

/**
 * Created by 111 on 19.03.2015.
 */
/* see model_at sql package */
public enum DataType {
    Text(1, false, false),
    Decimal(2, true, false),
    Double(3, true, true),
    DateTime(4, true, false),
    Masked(5, true, true),
    List(6, false, false),
    Reference(7, false, false),
    Bool(8, false, false);

    private boolean maskSupported;
    private boolean maskRequired;
    private int dbValue;

    DataType(int dbValue, boolean maskSupported, boolean maskRequired) {
        this.dbValue = dbValue;
        this.maskSupported = maskSupported;
        this.maskRequired = maskRequired;
    }

    public boolean isMaskSupported() {
        return maskSupported;
    }

    public boolean isMaskRequired() {
        return maskRequired;
    }

    public int getDbValue() {
        return dbValue;
    }

    public static DataType valueOf(int value) {
        for (DataType type: values()) {
            if (type.getDbValue() == value)
                return type;
        }

        throw new IllegalArgumentException("no DataType with dbValue = " + value);
    }
}
