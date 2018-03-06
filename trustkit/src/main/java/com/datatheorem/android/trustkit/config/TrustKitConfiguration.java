package com.datatheorem.android.trustkit.config;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.datatheorem.android.trustkit.config.model.DebugSettings;
import com.datatheorem.android.trustkit.config.model.Domain;
import com.datatheorem.android.trustkit.config.model.DomainConfig;
import com.datatheorem.android.trustkit.config.model.PinSet;
import com.datatheorem.android.trustkit.config.model.TrustkitConfig;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class TrustKitConfiguration {

    @NonNull private final Set<DomainPinningPolicy> domainPolicies;

    // For simplicity, this works slightly differently than Android N as we use shouldOverridePins
    // as a global setting instead of a per-<certificates> setting like Android N does
    private final boolean shouldOverridePins;
    @Nullable private final Set<Certificate> debugCaCertificates;

    public static TrustKitConfiguration fromXmlPolicy(@NonNull Context context,
                                                      @NonNull XmlPullParser parser)
            throws CertificateException, XmlPullParserException, IOException {
        return TrustKitConfigurationParser.fromXmlPolicy(context, parser);
    }

    public static TrustKitConfiguration fromCustomConfiguration(List<DomainConfig> domainConfigs,
                                                                DebugSettings debugSettings) {
        HashSet<DomainPinningPolicy> domainConfigSet = new HashSet<>();

        for (DomainConfig domainConfig : domainConfigs) {
            DomainPinningPolicy.Builder builder = new DomainPinningPolicy.Builder();

            builder.setHostname(domainConfig.getDomain().getHostName()).
                    setShouldIncludeSubdomains(domainConfig.getDomain().getIncludeSubdomains());
            builder.setPublicKeyHashes(domainConfig.getPinSet().getPins())
                    .setExpirationDate(domainConfig.getPinSet().getExpirationDate());
            builder.setHostname("").setShouldIncludeSubdomains(true);
            builder.setReportUris(domainConfig.getTrustkitConfig().getReportUris())
                    .setShouldEnforcePinning(domainConfig.getTrustkitConfig().shouldEnforcePinning())
                    .setShouldDisableDefaultReportUri(domainConfig.getTrustkitConfig().shouldDisableDefaultReportUri());

            try {
                domainConfigSet.add(builder.build());
            } catch (MalformedURLException e) {
                throw new ConfigurationException("Could not parse network security policy file");
            }
        }

        return new TrustKitConfiguration(domainConfigSet, debugSettings);
    }

    TrustKitConfiguration(@NonNull Set<DomainPinningPolicy> domainConfigSet) {
        this(domainConfigSet, new DebugSettings(false, null));
    }

    TrustKitConfiguration(@NonNull Set<DomainPinningPolicy> domainConfigSet, DebugSettings debugSettings) {
        if (debugSettings == null) {
            debugSettings = new DebugSettings(false, null);
        }

        if (domainConfigSet.size() < 1) {
            throw new ConfigurationException("Policy contains 0 domains to pin");
        }

        Set<String> hostnameSet = new HashSet<>();
        for (DomainPinningPolicy domainConfig : domainConfigSet) {
            if (hostnameSet.contains(domainConfig.getHostname())) {
                throw new ConfigurationException("Policy contains the same domain defined twice: "
                        + domainConfig.getHostname());
            }
            hostnameSet.add(domainConfig.getHostname());
        }
        this.domainPolicies = domainConfigSet;
        this.shouldOverridePins = debugSettings.shouldOverridePins();
        this.debugCaCertificates = debugSettings.getDebugCaCertificates();
    }

    public boolean shouldOverridePins() {
        return shouldOverridePins;
    }

    @Nullable
    public Set<Certificate> getDebugCaCertificates() {
        return debugCaCertificates;
    }

    /**
     * Get the {@link DomainPinningPolicy} corresponding to the provided hostname.
     * When matching the most specific matching domain rule will be used, if no match exists
     * then null will be returned.
     *
     * @param serverHostname the server's hostname
     * @return DomainPinningPolicy the domain's policy or null if the supplied hostname has no
     * policy defined
     */
    @Nullable
    public DomainPinningPolicy getPolicyForHostname(@NonNull String serverHostname) {
        // Check if the hostname seems valid
        DomainValidator domainValidator = DomainValidator.getInstance(false);
        if (!domainValidator.isValid(serverHostname)) {
            throw new IllegalArgumentException("Invalid domain supplied: " + serverHostname);
        }

        DomainPinningPolicy bestMatchPolicy = null;
        for (DomainPinningPolicy domainPolicy : this.domainPolicies) {
            if (domainPolicy.getHostname().equals(serverHostname)) {
                // Found an exact match for this domain
                bestMatchPolicy = domainPolicy;
                break;
            }

            // Look for the best match for pinning policies that include subdomains
            if (domainPolicy.shouldIncludeSubdomains()
                    && isSubdomain(domainPolicy.getHostname(), serverHostname)) {
                if (bestMatchPolicy == null) {
                    bestMatchPolicy = domainPolicy;
                } else if (domainPolicy.getHostname().length() > bestMatchPolicy.getHostname().length()) {
                    bestMatchPolicy = domainPolicy;
                }
            }
        }
        return bestMatchPolicy;
    }

    /**
     * Return true for all subdomains, including subdomains of subdomains, similar to how
     * Android N handles includeSubdomains
     */
    private static boolean isSubdomain(@NonNull String domain, @NonNull String subdomain) {
        return subdomain.endsWith(domain)
                && subdomain.charAt(subdomain.length() - domain.length() - 1) == '.';
    }
}
