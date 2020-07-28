package com.demo.workshop.intern.sso.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SSOServiceAccount {

    private String ssoServiceAccountName=SSOConstants.DEFAULT_SSO_SERVICE_ACCOUNT;
    private String requestStarSsoUrl;
    private String requestRedirectUrl;
    private String requestServiceEndPoint;
    private String openTokenCookieName="SSO_OPENTOKEN_COOKIE";
    private String opentokenRenewUntil="SSO_OPENTOKEN_COOKIE_RENEW_UNTIL";
    private String userIdCookieName="SSO_USERID_COOKIE";
    private String fwkSessionCookieName="FWK_SESSION_ID_COOKIE";
    private String openTokenCookieDomain=".oocl.com";
    private String openTokenCookiePath="/";
    private String loginDomainUrl;
    private String redirectOnTokenExpiry;
    private String tokenExpiryRedirectUrl="/SSO_TOKEN_EXPIRED";

    public String getSsoServiceAccountName() {
        return ssoServiceAccountName;
    }

    public String getRequestStarSsoUrl() {
        return requestStarSsoUrl;
    }

    public String getRequestRedirectUrl() {
        return requestRedirectUrl;
    }

    public String getRequestServiceEndPoint() {
        return requestServiceEndPoint;
    }

    public String getOpenTokenCookieName() {
        return openTokenCookieName;
    }

    public String getOpentokenRenewUntil() {
        return opentokenRenewUntil;
    }

    public String getUserIdCookieName() {
        return userIdCookieName;
    }

    public String getFwkSessionCookieName() {
        return fwkSessionCookieName;
    }

    public String getOpenTokenCookieDomain() {
        return openTokenCookieDomain;
    }

    public String getOpenTokenCookiePath() {
        return openTokenCookiePath;
    }

    public String getLoginDomainUrl() {
        return loginDomainUrl;
    }

    public String getRedirectOnTokenExpiry() {
        return redirectOnTokenExpiry;
    }

    public String getTokenExpiryRedirectUrl() {
        return tokenExpiryRedirectUrl;
    }

}
