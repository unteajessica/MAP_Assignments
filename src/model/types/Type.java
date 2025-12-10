package model.types;

import model.values.Value;

public interface Type {
    boolean equals(Object o);
    String toString();
    Value defaultValue();
}
