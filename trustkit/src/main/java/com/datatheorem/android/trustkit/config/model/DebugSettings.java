package com.datatheorem.android.trustkit.config.model;

import java.security.cert.Certificate;
import java.util.Set;

public class DebugSettings {
    private boolean overridePins;
    private Set<Certificate> debugCaCertificates;

    public DebugSettings() {
        overridePins = false;
        debugCaCertificates = null;
    }

    public DebugSettings(boolean overridePins, Set<Certificate> debugCaCertificates) {
        this.overridePins = overridePins;
        this.debugCaCertificates = debugCaCertificates;
    }

    public boolean shouldOverridePins() {
        return overridePins;
    }

    public Set<Certificate> getDebugCaCertificates() {
        return debugCaCertificates;
    }

    public void setOverridePins(boolean overridePins) {
        this.overridePins = overridePins;
    }

    public void setDebugCaCertificates(Set<Certificate> debugCaCertificates) {
        this.debugCaCertificates = debugCaCertificates;
    }
}