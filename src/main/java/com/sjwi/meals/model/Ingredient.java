package com.sjwi.meals.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Ingredient {
    private int id;
    private String name;

    public Ingredient(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
            // if deriving: appendSuper(super.hashCode()).
            append(name).
            append(id).
            toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
       if (!(obj instanceof Ingredient))
            return false;
        if (obj == this)
            return true;

        Ingredient rhs = (Ingredient) obj;
        return new EqualsBuilder().
            append(name, rhs.name).
            append(id, rhs.id).
            isEquals();
    }
}
