package model.values;

import model.types.RefType;
import model.types.Type;

public class RefValue implements  Value {

    // the address in the heap -> int
    private final int address;
    // what type of value is stored at that address
    private final Type locationType;

    public RefValue(int address, Type locationType) {
        this.address = address;
        this.locationType = locationType;
    }

    public int getAddress() {
        return address;
    }

    public Type getLocationType() {
        return locationType;
    }

    @Override
    public Type getType() {
        return new RefType(locationType);
    }

    @Override
    public String toString() {
        return "RefValue(" + address + ", " + locationType.toString() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof RefValue) {
            RefValue other = (RefValue) o;
            return this.address == other.address && this.locationType.equals(other.locationType);
        }
        return false;
    }
}
