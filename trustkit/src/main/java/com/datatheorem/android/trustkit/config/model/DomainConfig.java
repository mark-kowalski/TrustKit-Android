package com.datatheorem.android.trustkit.config.model;

// ToDo complete model
public class DomainConfig {
    private Domain domain;
    private PinSet pinSet;
    private TrustkitConfig trustkitConfig;

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public PinSet getPinSet() {
        return pinSet;
    }

    public void setPinSet(PinSet pinSet) {
        this.pinSet = pinSet;
    }

    public TrustkitConfig getTrustkitConfig() {
        return trustkitConfig;
    }

    public void setTrustkitConfig(TrustkitConfig trustkitConfig) {
        this.trustkitConfig = trustkitConfig;
    }
}