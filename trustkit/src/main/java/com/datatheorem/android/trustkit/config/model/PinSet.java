package com.datatheorem.android.trustkit.config.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class PinSet {
    private Date expirationDate = null;
    private Set<String> pins = null;

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Set<String> getPins() {
        return pins;
    }

    public void addPin(String pin) {
        if (pins == null) {
            pins = new HashSet<>();
        }
        pins.add(pin);
    }

    public void setPins(Set<String> pins) {
        this.pins = pins;
    }
}