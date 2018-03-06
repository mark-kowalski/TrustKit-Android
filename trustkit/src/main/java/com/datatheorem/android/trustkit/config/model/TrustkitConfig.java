package com.datatheorem.android.trustkit.config.model;

import java.util.Set;

public class TrustkitConfig {
    private Boolean enforcePinning = null;
    private Boolean disableDefaultReportUri = null;
    private Set<String> reportUris;

    public Boolean shouldEnforcePinning() {
        return enforcePinning;
    }

    public void setEnforcePinning(Boolean enforcePinning) {
        this.enforcePinning = enforcePinning;
    }

    public Boolean shouldDisableDefaultReportUri() {
        return disableDefaultReportUri;
    }

    public void setDisableDefaultReportUri(Boolean disableDefaultReportUri) {
        this.disableDefaultReportUri = disableDefaultReportUri;
    }

    public Set<String> getReportUris() {
        return reportUris;
    }

    public void setReportUris(Set<String> reportUris) {
        this.reportUris = reportUris;
    }
}