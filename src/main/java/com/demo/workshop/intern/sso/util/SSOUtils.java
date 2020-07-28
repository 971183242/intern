package com.demo.workshop.intern.sso.util;


import org.apache.commons.lang3.StringUtils;

/**
 *
 *	SSO Util class for handling non-functional issues.
 */
public class SSOUtils {

    /**
     * Idp adapter id key
     */
    public static String IDP_ADAPTER_ID= "IdpAdapterId=";
    /**
     * Adapter id CDIWA
     */
    public static String ADAPTERID_CDIWA = "CDIWA";
    /**
     * Adapter id MSADAdapter
     */
    public static String ADAPTERID_MSADADAPTER= "MSADAdapter";

    /**
     *
     * @param originalUrl
     * @return adapter id converted url
     */
    public static String convertAdapter(String originalUrl) {

        return StringUtils.replace(originalUrl, IDP_ADAPTER_ID + ADAPTERID_CDIWA, IDP_ADAPTER_ID + ADAPTERID_MSADADAPTER);

    }

}
