package com.inmotionchat.core.data.postgres.journey.templates.edge;

import com.inmotionchat.core.data.postgres.journey.EdgeTemplate;

public class IsoCountryCodeTemplate extends EdgeTemplate {

    private String countryCode;

    private boolean other;

    public static IsoCountryCodeTemplate createOther() {
        IsoCountryCodeTemplate other = new IsoCountryCodeTemplate(null);
        other.setOther(true);
        return other;
    }

    public IsoCountryCodeTemplate() {}

    public IsoCountryCodeTemplate(String countryCode) {
        this.countryCode = countryCode;
        this.other = false;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String isoCountryCode) {
        this.countryCode = isoCountryCode;
    }

    public boolean isOther() {
        return other;
    }

    public void setOther(boolean other) {
        this.other = other;
    }

}
