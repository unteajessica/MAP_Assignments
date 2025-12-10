package model.values;

import model.types.IntType;
import model.types.Type;

public class IntValue implements Value {

    private final int val;

    public IntValue(int v) {
        this.val = v;
    }

    public int getVal() {
        return val;
    }

    @Override
    public String toString() {
        return "" + val;
    }

    @Override
    public Type getType() {
        return new IntType();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof IntValue) {
            return ((IntValue) o).getVal() == val;
        }
        return false;
    }
}
