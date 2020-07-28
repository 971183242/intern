package com.demo.workshop.intern.sso.util;


import com.demo.workshop.intern.sso.config.AgentConfig;
import com.demo.workshop.intern.sso.config.SSOConfig;
import com.demo.workshop.intern.sso.config.SSOServiceAccount;
import com.demo.workshop.intern.sso.exceptions.SSOException;
import com.pingidentity.opentoken.*;
import com.pingidentity.opentoken.key.KeyManager;
import com.pingidentity.opentoken.key.PasswordKeyManager;

import org.apache.commons.collections.MultiMap;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.MapType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.SOAPHeader;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;


public class SSOHelperUtils {

    /** The Constant CLASS_NAME. */
    private static final String CLASS_NAME = SSOHelperUtils.class.getName();

    /** The Constant CHECKSUM_START_TAG. */
    public static final String CHECKSUM_START_TAG = "<CheckSum>";

    /** The Constant CHECKSUM_END_TAG. */
    public static final String CHECKSUM_END_TAG = "</CheckSum>";

    /** The Constant SECURITY_START_TAG. */
    public static final String SECURITY_START_TAG = "<wsse:Security xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">";

    /** The Constant SECURITY_END_TAG. */
    public static final String SECURITY_END_TAG = "</wsse:Security>";

    /** The Constant SECURITY_ELEMENT_NAME. */
    public static final String SECURITY_ELEMENT_NAME = "Security";

    /** The Constant LOCATION_PROPERTY_NAME. */
    public static final String LOCATION_PROPERTY_NAME = "Location";

    /** The Constant COOKIE_PROPERTY_NAME. */
    public static final String COOKIE_PROPERTY_NAME = "Cookie";

    /** The Constant AUTHORIZATION_PROPERTY_NAME. */
    public static final String AUTHORIZATION_PROPERTY_NAME = "Authorization";

    /** The Constant SETCOOKIE_PROPERTY_NAME. */
    public static final String SETCOOKIE_PROPERTY_NAME = "Set-Cookie";

    /** The Constant WHITESPACE_STRING. */
    public static final String WHITESPACE_STRING = "[ \t\n\r]";

    /** The Constant TOKEN_EMAIL. */
    public static final String TOKEN_EMAIL = "email";

    /** The Constant ERROR_CODE_KEY. */
    public static final String ERROR_CODE_KEY = "frm_error_code";

    /** The Constant FAILURE_MSG_TOKEN_EXPIRY. */
    public static final String FAILURE_MSG_TOKEN_EXPIRY = "TOKEN_EXPIRED";

    // 20140214 ngsc: Custom field key for "login as" user id
    /** The Constant LOGIN_AS_USER_ID_KEY. */
    public static final String LOGIN_AS_USER_ID_KEY = "LoginAsUserId";

    // 20151113 yuer: Custom field key for "LDAPName" LDAP Name
    /** The Constant LDAP_NAME. */
    public static final String LDAP_NAME = "LDAPName";

    // 20150319 yuer: Custom field key for "login as" LDAP Name
    /** The Constant LOGIN_AS_LDAP_NAME_KEY. */
    public static final String LOGIN_AS_LDAP_NAME_KEY = "LoginAsLdapName";

    //20140214 ngsc: reason code for opentoken validation
    public static final int SSO_OPENTOKEN_IS_VALID = 0;     //Success reason: Opentoken is valid
    public static final int SSO_OPENTOKEN_EXPIRED = 1;      //Failed reason: Opentoken has expired (soft or hard ... unable to identify)
    public static final int SSO_OPENTOKEN_SOFT_EXPIRED = 2;     //Failed reason: Opentoken has soft expired
    public static final int SSO_OPENTOKEN_HARD_EXPIRED = 3; //Failed reason: Opentoken has hard expired
    public static final int SSO_OPENTOKEN_MISSING = 4;      //Failed reason: Opentoken is not found in Cookie, or Opentoken string is empty or null
    public static final int SSO_OPENTOKEN_TOKEN_ERROR = 5;  //Failed reason: Opentoken contains error, e.g. invalid Opentoken string, no User attributes found, fail to decrypt... etc
    public static final int SSO_OAUTH_INVALID_TOKEN = 2; //Failed reason: OAuth access token is corrupted or expired
    public static final int SSO_OAUTH_INVALID_REQUEST = 3; //Failed reason: OAuth access token validation request is invalid
    public static final int SSO_OAUTH_INSUFFICIENT_SCOPE = 4; //Failed reason: OAuth access token with insufficient scope
    public static final int SSO_OAUTH_DEFAULT_CONNECTION_TIMEOUT = 60 * 1000; // The default timeout milliseconds of request for validate access token with OAuth server.

    /** The Constant OOCL_USER_DOMAIN. */
    public static final String OOCL_USER_DOMAIN = "OOCLDM";

    /** The Constant OOCL_DOT_COM_DOMAIN. */
    public static final String OOCL_DOT_COM_DOMAIN = "OOCL.COM";

    /** The agent file content. */
    private static byte[] agentFileContent = null;

    /** The agent file content lock obj. */
    private static Object agentFileContentLockObj = new Object();

    // for CS2 backend schedule call WS
    /** The global service token map. */
    private static Map<String, String> globalServiceTokenMap = null;

    /** The mapper. */
    private static ObjectMapper mapper = new ObjectMapper();

    /** The map type. */
    private static MapType mapType = mapper.getTypeFactory().constructMapType(HashMap.class, String.class, Object.class);

    /** The oauth server token issuer. */
    private static volatile String[] oauthServerTokenIssuer = null;

    /** The oauth server verify url. */
    private static volatile String oauthServerVerifyUrl = null;

    /** The oauth user impersonation scope. */
    private static volatile String oauthUserImpersonationScope = null;

    /** The oauth access token allowed user domains. */
    private static volatile List<String> oauthAccessTokenAllowedUserDomains = null;

    /** The oauth access token trusted clients. */
    private static volatile List<String> oauthAccessTokenTrustedClients = Collections.emptyList();

    /** ping server agent for token decode */
    private static Agent agent = null;
    /** agent configuration instance of agent-config.txt */
    private static AgentConfiguration agentConfig = null;

    /** The azure ad token issuer. */
    private static volatile String azureADTokenIssuer = null;

    /** The azure ad token verify url. */
    private static volatile String azureADTokenVerifyUrl = null;

    private static final Logger LOGGER = LoggerFactory.getLogger(SSOHelperUtils.class);

    private static SSOConfig ssoConfig = SSOConfig.getInstance();

    public SSOHelperUtils(SSOConfig ssoConfig){
        this.ssoConfig = ssoConfig;
    }

    public void setSsoConfigProperties(SSOConfig ssoConfig){
        this.ssoConfig =ssoConfig;
    }

    public static Object getNodeFromTokenString(String token) {
        return new Object();
    }

    public static String getRequestToken(String endpointAddressProperty) {
        return "";
    }

    public static long computeChecksum(String token) {
        return 0L;
    }

    public static void appendNodeToSOAPHeader(SOAPHeader header, Object nodeFromTokenString) {
    }

    /**
     * The Enum OAuthAccessTokenJwtField.
     */
    public enum OAuthAccessTokenJwtField {

        /** The aud. */
        AUD("aud"),
        /** The iss. */
        ISS("iss"),
        /** The iat. */
        IAT("iat"),
        /** The nbf. */
        NBF("nbf"),
        /** The exp. */
        EXP("exp"),
        /** The sub. */
        SUB("sub"),
        /** The ver. */
        VER("ver"),
        /** The tid. */
        TID("tid"),
        /** The scope. */
        SCOPE("scope"),
        /** The gid. */
        GRP("grp"),
        /** The upn. */
        PREFERRED_USER_NAME("preferred_username"),
        /** The appid. */
        APPID("appid");

        /** The field key. */
        private String fieldKey;

        /**
         * Instantiates a new o auth access token jwt field.
         *
         * @param fieldKey
         *            the field key
         */
        private OAuthAccessTokenJwtField(String fieldKey) {
            this.fieldKey = fieldKey;
        }

        /**
         * Gets the field key.
         *
         * @return the field key
         */
        public String getFieldKey() {
            return this.fieldKey;
        }
    }

    public static boolean checkOpenToken(HttpServletRequest httpRequest, HttpServletResponse httpResponse, String serviceAccountName) throws SSOException {

        final String METHOD_NAME = "checkOpenToken(HttpServletRequest, HttpServletResponse, String)";

        SSOServiceAccount ssoServiceAccount = ssoConfig.getSSOServiceAccountByAccountName(serviceAccountName);

        try {
            Agent agent = getAgent();

            Map<?, ?> userInfo = agent.readToken(httpRequest);

            if (userInfo != null && !userInfo.isEmpty()) {
                String cookieName = ssoConfig.getOpentokenCookieName(serviceAccountName);
                String userIdCookieName = getUserIdCookieName(serviceAccountName);
                String fwkSessionIdCookieName = getFwkSessionIdCookieName(serviceAccountName);
                String tokenRenewUntilCookieName = getOpentokenRenewUntilCookieName(serviceAccountName);

                String userName = (String) userInfo.get(Agent.TOKEN_SUBJECT);
                userInfo.remove(Agent.TOKEN_RENEW_UNTIL);
                String token = agent.writeToken(userInfo);
                userInfo = agent.readToken(token);//getting token attribute again to get updated attributes
                LOGGER.debug(METHOD_NAME +
                        "*********Token Found!!! username = " + userName + "*********opentoken=" + token);
                // OpenToken found, add it to cookies
                String setCookieDomain = "";
                String setCookiePath = "";
                String setCookieHttpOnly = "";
                String setCookieSecure = "";
                String setUserIdCookieDomain = "";
                String setUserIdCookiePath = "";
                String setUserIdCookieSecure = "";
                String fwkSessionId = UUID.randomUUID().toString();
                String setFwkSessionIdCookieDomain = "";
                String setFwkSessionIdCookiePath = "";
                String setFwkSessionIdCookieSecure = "";

                String tokenRenewUntil = (String) userInfo.get(Agent.TOKEN_RENEW_UNTIL);
                String setTokenRenewUntilCookieDomain = "";
                String setTokenRenewUntilCookiePath = "";
                String setTokenRenewUntilCookieSecure = "";

//                String cookieDomain = serviceAccountConfigComponent.getOpentokenCookieDomain();
                String cookieDomain = ssoServiceAccount.getOpenTokenCookieDomain();
                if (cookieDomain != null && cookieDomain.length() > 0) {
                    setCookieDomain = "; Domain=" + cookieDomain;
                    setUserIdCookieDomain = "; Domain=" + cookieDomain;
                    setFwkSessionIdCookieDomain = "; Domain=" + cookieDomain;
                    setTokenRenewUntilCookieDomain = "; Domain=" + cookieDomain;
                }
//                String cookiePath = serviceAccountConfigComponent.getOpentokenCookiePath();
                String cookiePath = ssoServiceAccount.getOpenTokenCookiePath();
                if (cookiePath == null || cookiePath.length() == 0) {
                    cookiePath = httpRequest.getContextPath();
                }
                setCookieHttpOnly = "; HttpOnly";
                setCookiePath = "; Path=" + cookiePath;
                setUserIdCookiePath = "; Path=" + cookiePath;
                setFwkSessionIdCookiePath = "; Path=" + cookiePath;
                setTokenRenewUntilCookiePath = "; Path=" + cookiePath;

                String XForwardProto = httpRequest.getHeader("X-Forwarded-Proto");
                if (XForwardProto != null && XForwardProto.equalsIgnoreCase("https")) {
                    setCookieSecure = "; secure";
                    setUserIdCookieSecure = "; secure";
                    setFwkSessionIdCookieSecure = "; secure";
                    setTokenRenewUntilCookieSecure = "; secure";
                }

                httpResponse.addHeader(SETCOOKIE_PROPERTY_NAME, cookieName + "=" + token + setCookieDomain + setCookiePath + setCookieHttpOnly + setCookieSecure + "; SameSite=strict");
                httpResponse.addHeader(SETCOOKIE_PROPERTY_NAME, userIdCookieName + "=" + userName + setUserIdCookieDomain + setUserIdCookiePath + setUserIdCookieSecure + "; SameSite=strict");
                httpResponse.addHeader(SETCOOKIE_PROPERTY_NAME, fwkSessionIdCookieName + "=" + fwkSessionId + setFwkSessionIdCookieDomain + setFwkSessionIdCookiePath + setFwkSessionIdCookieSecure + "; SameSite=strict");
                httpResponse.addHeader(SETCOOKIE_PROPERTY_NAME, tokenRenewUntilCookieName + "=" + tokenRenewUntil + setTokenRenewUntilCookieDomain + setTokenRenewUntilCookiePath + setTokenRenewUntilCookieSecure + "; SameSite=strict");
                httpRequest.setAttribute(cookieName, token);
                httpRequest.setAttribute(userIdCookieName, userName);
                httpRequest.setAttribute(fwkSessionIdCookieName, fwkSessionId);
                httpRequest.setAttribute(tokenRenewUntilCookieName, tokenRenewUntil);

                return true;
            } else {

                LOGGER.debug(METHOD_NAME + "*****No Valid Token!!!!*******");

                LOGGER.debug(METHOD_NAME + "*****getScheme = " + httpRequest.getScheme());
                LOGGER.debug(METHOD_NAME + "*****isSecure = " + httpRequest.isSecure());
                LOGGER.debug(METHOD_NAME + "*****getLocalPort = " + httpRequest.getLocalPort());
                LOGGER.debug(METHOD_NAME + "*****getServerPort = " + httpRequest.getServerPort());
                LOGGER.debug(METHOD_NAME + "*****getRequestURL = " + httpRequest.getRequestURL());
                LOGGER.debug(METHOD_NAME + "*****getRequestURI = " + httpRequest.getRequestURI());

                for (Enumeration<String> headerNames = httpRequest.getHeaderNames(); headerNames.hasMoreElements();) {
                    String headerName = headerNames.nextElement();
                    String headerValue = httpRequest.getHeader(headerName);
                    LOGGER.debug(METHOD_NAME + "*****header = " + headerName + ", " + headerValue);
                }

                // String targetResource =
                // URLEncoder.encode(httpRequest.getRequestURL().append(
                // (httpRequest.getQueryString() == null) ? "" : "?"
                // + httpRequest.getQueryString()).toString(),
                // System.getProperty("file.encoding"));

                // 20140403 ngsc Check HTTP Header to distinguish HTTP/HTTPS
                // request, and return correct request URL
                String targetResource = URLEncoder.encode(getRequestURL(httpRequest).append(
                        (httpRequest.getQueryString() == null) ? "" : "?" + httpRequest.getQueryString()).toString(), System
                        .getProperty("file.encoding"));

                LOGGER.debug(METHOD_NAME + "******* targetResource = " + targetResource + "********");
                // httpResponse.sendRedirect(serviceAccountConfigComponent
                // .getRequestRedirectUrl()
                // + targetResource);

                // 20140402 ngsc check ssoConfig for TargetResource
                httpResponse.sendRedirect(getRedirectURL(ssoServiceAccount.getRequestRedirectUrl(), targetResource));
            }
        } catch (IOException ioe) {
            LOGGER.warn(CLASS_NAME + "." + METHOD_NAME, "ERROR reading agent file", ioe);
            throw new SSOException("ERROR reading agent file", ioe);
        } catch (TokenException e) {
            LOGGER.warn(CLASS_NAME + "." + METHOD_NAME, "ERROR reading token", e);
            throw new SSOException("ERROR reading token", e);
        }
        return false;
    }

    public static boolean doCheckTokenFromCookie(HttpServletRequest httpRequest, HttpServletResponse httpResponse, String serviceAccountName) {

        final String METHOD_NAME = "doCheckTokenFromCookie(HttpServletRequest, HttpServletResponse, String)";

        LOGGER.debug(METHOD_NAME + "doCheckTokenFromCookie(HttpServletRequest httpRequest, HttpServletResponse httpResponse, String serviceAccountName),is sso enabled ? [{}]" , ssoConfig.getSsoEnabled());

        String openTokenCookieName = ssoConfig.getOpentokenCookieName(serviceAccountName);
        Cookie openTokenCookie = CookieUtils.getCookieByName(httpRequest, openTokenCookieName);
        if (openTokenCookie != null) {
            if (openTokenCookie.getValue() != null && openTokenCookie.getValue().length() > 0) {
                Agent agent = null;
                AgentConfiguration agentConfig = null;
                try {
                    agent = getAgent();
                    agentConfig = getAgentConfiguration();
                    agent.readToken(openTokenCookie.getValue());
                } catch (TokenExpiredException e) {
                    LOGGER.error(CLASS_NAME + "." + METHOD_NAME, e.getMessage());
                    SSOServiceAccount ssoServiceAccount = ssoConfig.getSSOServiceAccountByAccountName(serviceAccountName);
                    LOGGER.debug(METHOD_NAME + "*****Token Expired!!!!*******");
                    try {
                        try {
                            KeyManager keyman = new PasswordKeyManager(agentConfig.getPassword(), agentConfig.getCipherSuite(),
                                    false);
                            MultiMap tokenMultiMap = Token.decode(openTokenCookie.getValue(), keyman, agentConfig.isUseSunJCE());
                            String token = agent.writeToken(tokenMultiMap);
                            Map<?, ?> tokenMap = agent.readToken(token);
                            String setOpenTokenCookieDomain = "";
                            String setOpenTokenCookiePath = "";
                            String setOpenTokenCookieHttpOnly = "";
                            String setOpenTokenCookieSecure = "";
//                            String cookieDomain = serviceAccountConfigComponent.getOpentokenCookieDomain();
                            String cookieDomain = ssoServiceAccount.getOpenTokenCookieDomain();
                            String openTokenDomain = ssoServiceAccount.getOpenTokenCookieDomain();
                            String fwkSessionIdCookieName = getFwkSessionIdCookieName(serviceAccountName);
                            String fwkSessionId = UUID.randomUUID().toString();
                            String setFwkSessionIdCookieDomain = "";
                            String setFwkSessionIdCookiePath = "";
                            String setFwkSessionIdCookieSecure = "";


                            String userIdCookieName = getUserIdCookieName(serviceAccountName);
                            String userName = (String) tokenMap.get(Agent.TOKEN_SUBJECT);
                            String setUserIdCookieDomain = "";
                            String setUserIdCookiePath = "";
                            String setUserIdCookieSecure = "";

                            String tokenRenewUntilCookieName = getOpentokenRenewUntilCookieName(serviceAccountName);
                            String tokenRenewUntil = (String) tokenMap.get(Agent.TOKEN_RENEW_UNTIL);
                            String setTokenRenewUntilCookieDomain = "";
                            String setTokenRenewUntilCookiePath = "";
                            String setTokenRenewUntilCookieSecure = "";

                            if (cookieDomain != null && cookieDomain.length() > 0) {
                                setOpenTokenCookieDomain = "; Domain=" + cookieDomain;
                                setUserIdCookieDomain = "; Domain=" + cookieDomain;
                                setFwkSessionIdCookieDomain = "; Domain=" + cookieDomain;
                                setTokenRenewUntilCookieDomain = "; Domain=" + cookieDomain;
                            }
                            String cookiePath = ssoServiceAccount.getOpenTokenCookiePath();
                            if (cookiePath == null || cookiePath.length() == 0) {
                                cookiePath = httpRequest.getContextPath();
                            }
                            setOpenTokenCookieHttpOnly = "; HttpOnly";
                            setOpenTokenCookiePath = "; Path=" + cookiePath;
                            setUserIdCookiePath = "; Path=" + cookiePath;
                            setFwkSessionIdCookiePath = "; Path=" + cookiePath;
                            setTokenRenewUntilCookiePath = "; Path=" + cookiePath;

                            String XForwardProto = httpRequest.getHeader("X-Forwarded-Proto");
                            if (XForwardProto != null && XForwardProto.equalsIgnoreCase("https")) {
                                setOpenTokenCookieSecure = "; secure";
                                setUserIdCookieSecure = "; secure";
                                setFwkSessionIdCookieSecure = "; secure";
                                setTokenRenewUntilCookieSecure = "; secure";
                            }
                            httpResponse.addHeader(SETCOOKIE_PROPERTY_NAME, openTokenCookieName + "=" + token + setOpenTokenCookieDomain + setOpenTokenCookiePath + setOpenTokenCookieHttpOnly + setOpenTokenCookieSecure + "; SameSite=strict");
                            httpResponse.addHeader(SETCOOKIE_PROPERTY_NAME, userIdCookieName + "=" + userName + setUserIdCookieDomain + setUserIdCookiePath + setUserIdCookieSecure + "; SameSite=strict");
                            httpResponse.addHeader(SETCOOKIE_PROPERTY_NAME, fwkSessionIdCookieName + "=" + fwkSessionId + setFwkSessionIdCookieDomain + setFwkSessionIdCookiePath + setFwkSessionIdCookieSecure + "; SameSite=strict");
                            httpResponse.addHeader(SETCOOKIE_PROPERTY_NAME, tokenRenewUntilCookieName + "=" + tokenRenewUntil + setTokenRenewUntilCookieDomain + setTokenRenewUntilCookiePath + setTokenRenewUntilCookieSecure + "; SameSite=strict");

                            httpRequest.setAttribute(openTokenCookieName, token);
                            httpRequest.setAttribute(userIdCookieName, userName);
                            httpRequest.setAttribute(fwkSessionIdCookieName, fwkSessionId);
                            httpRequest.setAttribute(tokenRenewUntilCookieName, tokenRenewUntil);
                            LOGGER.debug(METHOD_NAME + "*****Renew token successfully!!!!*******");
                            return true;
                        } catch (TokenException te) {
                            LOGGER.debug(METHOD_NAME + "*****Token Hard Expired!!!!*******");
                            String cookieDomain = ssoServiceAccount.getOpenTokenCookieDomain();
                            String cookiePath = ssoServiceAccount.getOpenTokenCookiePath();
                            if (cookiePath == null || cookiePath.length() == 0) {
                                cookiePath = httpRequest.getContextPath();
                            }

                            // remove opentoken cookie
                            expireCookie(openTokenCookie, httpRequest, httpResponse, cookieDomain, cookiePath);

                            // remove sessionId cookie
                            Cookie fwkSessionIdCookie = CookieUtils.getCookieByName(httpRequest,
                                    getFwkSessionIdCookieName(serviceAccountName));
                            expireCookie(fwkSessionIdCookie, httpRequest, httpResponse, cookieDomain, cookiePath);

                            // remove UserId cookie
                            Cookie userIdCookieCookie = CookieUtils.getCookieByName(httpRequest, getUserIdCookieName(serviceAccountName));
                            expireCookie(userIdCookieCookie, httpRequest, httpResponse, cookieDomain, cookiePath);

                            // remove token new until cookie
                            Cookie tokenRenewUntilCookie = CookieUtils.getCookieByName(httpRequest, getOpentokenRenewUntilCookieName(serviceAccountName));
                            expireCookie(tokenRenewUntilCookie, httpRequest, httpResponse, cookieDomain, cookiePath);

                            if (Boolean.valueOf(ssoServiceAccount.getRedirectOnTokenExpiry())) {
                                LOGGER.debug(METHOD_NAME +
                                                CLASS_NAME,
                                        METHOD_NAME,
                                        "******* TokenExpiryRedirectUrl = "
                                                + ssoServiceAccount.getTokenExpiryRedirectUrl() + "********");

                                // httpResponse.setHeader(HTTP_HEADER_X_REQUST_STATUS,
                                // "Failure");
                                // httpResponse.setHeader(HTTP_HEADER_X_REQUST_FAILURE_URI,
                                // serviceAccountConfigComponent
                                // .getTokenExpiryRedirectUrl());

                                httpResponse.sendRedirect(ssoServiceAccount.getTokenExpiryRedirectUrl() + "?"
                                        + ERROR_CODE_KEY + "=" + FAILURE_MSG_TOKEN_EXPIRY);
                                return true;
                            } else {
                                String queryString = httpRequest.getQueryString();
                                if (queryString != null) {
                                    queryString = queryString.replaceAll(agentConfig.getTokenName() + "=[^&]*&{0,1}", "");
                                }

                                // String targetResource = URLEncoder
                                // .encode(
                                // httpRequest.getRequestURL()
                                // .append(
                                // (queryString == null || queryString
                                // .length() == 0) ? ""
                                // : "?"
                                // + queryString)
                                // .toString(),
                                // System
                                // .getProperty("file.encoding"));

                                //

                                // 20140403 ngsc Check HTTP Header to
                                // distinguish HTTP/HTTPS request, and return
                                // correct request URL
                                String targetResource = URLEncoder.encode(getRequestURL(httpRequest).append(
                                        (queryString == null || queryString.length() == 0) ? "" : "?" + queryString).toString(),
                                        System.getProperty("file.encoding"));

                                LOGGER.debug(METHOD_NAME +
                                        "******* targetResource = " + targetResource + "********");

                                // httpResponse
                                // .sendRedirect(serviceAccountConfigComponent
                                // .getRequestRedirectUrl()
                                // + targetResource);

                                // 20140402 ngsc check ssoConfig for
                                // TargetResource
                                httpResponse.sendRedirect(getRedirectURL(ssoServiceAccount.getRequestRedirectUrl(),
                                        targetResource));
                                return true;
                            }
                        }
                    } catch (IOException ioe) {
                        LOGGER.warn(CLASS_NAME + "." + METHOD_NAME, "I/O ERROR", ioe);
                    }
                    return false;
                } catch (TokenException e) {
                    LOGGER.warn(CLASS_NAME + "." + METHOD_NAME, "ERROR reading token in cookie", e);
                    return false;
                }
                return true;
            }
            // }
        }

        return false;
    }

    private static StringBuffer getRequestURL(HttpServletRequest httpRequest) {
        final String METHOD_NAME = "getSourceURL(HttpServletRequest)";
        StringBuffer requestUrl = httpRequest.getRequestURL();

        String XForwardProto = httpRequest.getHeader("X-Forwarded-Proto");

        // for local testing only, setup local tunnel to simulate HTTPS
        // the below solution used a non-standard HTTP Header...
        // https://github.com/jugyo/tunnels
        // String XForwardProto = httpRequest.getHeader("X_Forwarded_Proto");

        LOGGER.debug(METHOD_NAME + "Check HTTP Header X-Forwarded-Proto value = " + XForwardProto);

        if (XForwardProto != null && XForwardProto.equalsIgnoreCase("https")) {
            LOGGER.debug(METHOD_NAME + "original request URL is HTTPS URL");
            try {
                int httpIdx = requestUrl.indexOf("http://");
                if (httpIdx > -1) {
                    requestUrl.replace(httpIdx, (httpIdx + "http://".length()), "https://");

                    // replace the port 80 with 443 for HTTPS
                    int port80Idx = requestUrl.indexOf(":80");
                    if (port80Idx > -1) {
                        LOGGER.debug(METHOD_NAME + "replace 80 port with 443...");
                        requestUrl = requestUrl.replace(port80Idx, (port80Idx + ":80".length()), ":443");
                    }

                    LOGGER.debug(METHOD_NAME + "original request URL corrected... " + requestUrl);

                } else {
                    LOGGER.warn(CLASS_NAME + "." + METHOD_NAME,
                            "ERROR original request URL is not a well formed URL... " + requestUrl);
                }
            } catch (Exception e) {
                LOGGER.warn(CLASS_NAME + "." + METHOD_NAME, "ERROR when transforming HTTPS URL...", e);
            }
        } else {
            LOGGER.warn( "original request URL is HTTP URL, no action needed");

        }

        return requestUrl;
    }

    private static void expireCookie(Cookie cookie, HttpServletRequest httpRequest, HttpServletResponse httpResponse, String cookieDomain, String cookiePath) {

        if (cookie != null) {
            if (cookieDomain != null && cookieDomain.length() > 0) {
                cookie.setDomain(cookieDomain);
            }
            if (cookiePath != null && cookiePath.length() > 0) {
                cookie.setPath(cookiePath);
            } else {
                cookie.setPath(httpRequest.getContextPath());
            }
            cookie.setMaxAge(0); // Remove the cookie
            httpResponse.addCookie(cookie);
        }

    }

    private static String getOpentokenRenewUntilCookieName(String serviceAccountName) {

        if (serviceAccountName != null && serviceAccountName.length() > 0) {
            String cookieName = ssoConfig.getSSOServiceAccountByAccountName(serviceAccountName).getOpentokenRenewUntil();
            if (cookieName != null && cookieName.trim().length() > 0) {
                return cookieName;
            }
        }
        return ssoConfig.getDefaultOpenTokenCookieName() + "_RENEW_UNTIL";

    }

    private static String getUserIdCookieName(String serviceAccountName) {

        if (serviceAccountName != null && serviceAccountName.length() > 0) {
            String cookieName = ssoConfig.getSSOServiceAccountByAccountName(serviceAccountName).getUserIdCookieName();
            if (cookieName != null && cookieName.trim().length() > 0) {
                return cookieName;
            }
        }
        return ssoConfig.getDefaultUserIdCookieName();

    }

    public static String getRedirectURL(String configRequestRedirectUrl, String encodedSourceUrl) {

        final String METHOD_NAME = "getRedirectURL(String, String)";

        if (configRequestRedirectUrl != null && configRequestRedirectUrl.trim().length() > 0 && encodedSourceUrl != null
                && encodedSourceUrl.trim().length() > 0) {

            // default return the configured REQUEST_REDIRECT_URL + source URL
            String redirectUrl = configRequestRedirectUrl + encodedSourceUrl;

            // Check if the config value have already contains a specific
            // TargetResource to overide the Source URL
            String targetResourceParam = "TargetResource=";
            int targetResourceParamIdx = configRequestRedirectUrl.indexOf("TargetResource=");
            if (targetResourceParamIdx > -1) {

                // Pre-requisite: User included query param TargetResource in
                // REQUEST_REDIRECT_URL of ssoConfig.xml
                LOGGER.debug(METHOD_NAME + "targetResourceParamIdx > -1... " + targetResourceParamIdx);
                int partialLength = targetResourceParamIdx + targetResourceParam.length();

                LOGGER.debug(METHOD_NAME + "partialLength = " + partialLength);
                LOGGER.debug(METHOD_NAME +
                        "configRequestRedirectUrl.length() = " + configRequestRedirectUrl.length());

                if (configRequestRedirectUrl.length() > partialLength) {
                    // Case 1: User have specified a specific TargetResource URL
                    // in REQUEST_REDIRECT_URL
                    LOGGER.debug(METHOD_NAME + "configRequestRedirectUrl.length() > partialLength...");

                    int endIdx = targetResourceParamIdx + targetResourceParam.length();
                    String configRequestRedirectUrlPrefix = configRequestRedirectUrl.substring(0, endIdx);
                    String targetResourceValue = configRequestRedirectUrl.substring(endIdx);
                    LOGGER.debug(METHOD_NAME + "targetResourceValue = " + targetResourceValue);

                    // redirectUrl = configRequestRedirectUrl;

                    // Need to check if the TargetResource URL has been encoded,
                    // and encode it if it's not
                    // redirectUrl = configRequestRedirectUrlPrefix +
                    // URLEncoder.encode(targetResourceValue,
                    // System.getProperty("file.encoding"));

                    String finalTargetResourceValue = encodeIfNecessary(targetResourceValue);
                    redirectUrl = configRequestRedirectUrlPrefix + finalTargetResourceValue;
                } else {
                    // Case 2: User just included "TargetResource=" in
                    // REQUEST_REDIRECT_URL, but no value specified.
                    // Use source Url as default value
                    LOGGER.debug(METHOD_NAME +
                            "user didn't config a specific TargetResource... use the default url ...");
                    // user didn't config a specific TargetResource... use the
                    // default url
                }

            } else {
                // Exception case: User forget to put "TargetResource=" in the
                // REQUEST_REDIRECT_URL
                // Try to help user to re construct the URL by
                // REQUEST_REDIRECT_URL + &amp;TargetResource= +
                // encodedSourceUrl
                LOGGER.debug(METHOD_NAME +
                        "no TargetResource found in the REQUEST_REDIRECT_URL in ssoConfig... help user to append the source URL...");
                // no TargetResource found... just use the REQUEST_REDIRECT_URL
                // in ssoConfig?
                redirectUrl = configRequestRedirectUrl + "&amp;" + targetResourceParam + encodedSourceUrl;

            }

            // Finally... return the resulting URL for httpRedirect ...
            LOGGER.debug(METHOD_NAME +
                    "Finally... return the resulting URL for httpRedirect ..." + redirectUrl);

            // default return the configured url
            return redirectUrl;

        } else {
            // throw new
            // SSOException("REQUEST_REDIRECT_URL is null in ssoConfig or unable to recognize Source URL...configRequestRedirectUrl="
            // + configRequestRedirectUrl + ", encodedSourceUrl=" +
            // encodedSourceUrl);
            LOGGER.warn(
                    CLASS_NAME + "." + METHOD_NAME,
                    "REQUEST_REDIRECT_URL is null in ssoConfig or unable to recognize Source URL...configRequestRedirectUrl="
                            + configRequestRedirectUrl + ", encodedSourceUrl=" + encodedSourceUrl);
            return null;
        }

    }

    private static String encodeIfNecessary(String str) {
        final String METHOD_NAME = "encodeIfNecessary(String)";

        // default return input String...
        String returnStr = str;
        if (str != null && str.length() > 0) {
            if (str.indexOf("%") > -1) {
                LOGGER.debug(METHOD_NAME + "str has already encoded... ");
            } else {
                LOGGER.debug(METHOD_NAME + "str is not encoded... perform encoding...");

                try {
                    returnStr = URLEncoder.encode(str, System.getProperty("file.encoding"));
                } catch (Exception e) {
                    LOGGER.warn(CLASS_NAME + "." + METHOD_NAME, "ERROR when performing encoding... str=" + str, e);
                }
            }
        } else {
            LOGGER.warn(CLASS_NAME + "." + METHOD_NAME, "ERROR str is null or empty... str=" + str);

        }

        return returnStr;
    }

    private static String getFwkSessionIdCookieName(String serviceAccountName) {
        if (serviceAccountName != null && serviceAccountName.length() > 0) {
            String cookieName = ssoConfig.getSSOServiceAccountByAccountName(serviceAccountName).getFwkSessionCookieName();
            if (cookieName != null && cookieName.trim().length() > 0) {
                return cookieName;
            }
        }
        return ssoConfig.getDefaultFwkSessionIdCookieName();
    }

    private static Agent getAgent() {
        Agent innerAgent = null;
        if(null != agent && !Boolean.valueOf(ssoConfig.getDynamicLoadAgentConfigEnabled())) {
            innerAgent =  agent;
        } else {
//            try {

            AgentConfig agentConfig = ssoConfig.getAgentConfig();

            AgentConfiguration agentConfiguration = new AgentConfiguration();

            agentConfiguration.setPassword(agentConfig.getPassword());
            agentConfiguration.setCookiePath(agentConfig.getCookiePath());
            agentConfiguration.setRenewUntilLifetime(agentConfig.getTokenRenewuntil());
            agentConfiguration.setUseSunJCE(agentConfig.getUseSunjce());
            agentConfiguration.setTokenLifetime(agentConfig.getTokenLifetime());
            agentConfiguration.setUseCookie(agentConfig.getUseCookie());
            agentConfiguration.setNotBeforeTolerance(agentConfig.getTokenNotbeforeTolerance());
            agentConfiguration.setCipherSuite(agentConfig.getCipherSuite());
            agentConfiguration.setTokenName(agentConfig.getTokenName());
            agentConfiguration.setObfuscatePasword(agentConfig.getObfuscatePassword());

            innerAgent =  new Agent(agentConfiguration);

//            } catch (IOException e) {
//                LOGGER.warn(CLASS_NAME + "." + "getAgent() I/O ERROR", e);
//            } catch (SSOException e) {
//                LOGGER.warn(CLASS_NAME + "." + "getAgent() I/O ERROR", e);
//            }
        }
        return innerAgent;
    }

//    public static InputStream getAgentFileInputStream() throws SSOException, IOException {
//        try {
//            synchronized (agentFileContentLockObj) {
//                if (agentFileContent == null) {
//
//                    agentFileContent = IOUtils.toByteArray(new FileReader(SSOFileUtils.getAgentConfigFile()));
//                }
//                return new ByteArrayInputStream(agentFileContent);
//            }
//        } catch (IOException ioe) {
//            LOGGER.warn(CLASS_NAME + "." + "getAgentFileInputStream I/O ERROR", ioe);
//            throw ioe;
//        }
//    }

    public static AgentConfiguration getAgentConfiguration() {
        AgentConfiguration innerAgentConfig = null;
        if(null != agentConfig && !Boolean.valueOf(ssoConfig.getDynamicLoadAgentConfigEnabled())) {
            innerAgentConfig =  agentConfig;
        } else {
//            try {

            AgentConfig agentConfig = ssoConfig.getAgentConfig();

            innerAgentConfig = new AgentConfiguration();

//                innerAgentConfig.setPassword(String.valueOf(Base64.getDecoder().decode(agentConfig.getPassword())));
            innerAgentConfig.setPassword(agentConfig.getPassword());
            innerAgentConfig.setCookiePath(agentConfig.getCookiePath());
            innerAgentConfig.setRenewUntilLifetime(agentConfig.getTokenRenewuntil());
            innerAgentConfig.setUseSunJCE(agentConfig.getUseSunjce());
            innerAgentConfig.setTokenLifetime(agentConfig.getTokenLifetime());
            innerAgentConfig.setUseCookie(agentConfig.getUseCookie());
            innerAgentConfig.setNotBeforeTolerance(agentConfig.getTokenNotbeforeTolerance());
            innerAgentConfig.setCipherSuite(agentConfig.getCipherSuite());
            innerAgentConfig.setTokenName(agentConfig.getTokenName());
            innerAgentConfig.setObfuscatePasword(agentConfig.getObfuscatePassword());

//            } catch (IOException e) {
//                LOGGER.error(CLASS_NAME + "." + "getAgentConfiguration()", "I/O ERROR", e);
//            } catch (SSOException e) {
//                LOGGER.error(CLASS_NAME + "." + "getAgentConfiguration()", "I/O ERROR", e);
//            }
        }
        return innerAgentConfig;
    }

    /**
     * Gets the subject in the opentoken.
     *
     * @param httpRequest
     *            the HTTP request.
     * @param serviceAccountName
     *            the service account name.
     * @return the subject of opentoken or null if not found.
     * @throws SSOException
     *             the SSO exception
     * @since SFP.6.1
     */
    public static String getTokenSubject(HttpServletRequest httpRequest, String serviceAccountName) throws SSOException {
        return getFieldFromOpentoken(httpRequest, serviceAccountName, Agent.TOKEN_SUBJECT);
    }

    /**
     * Gets the subject in the opentoken.
     *
     * @param token
     *            the encoded opentoken.
     * @return the subject of opentoken or null if not found.
     * @throws SSOException
     *             the SSO exception
     * @since SFP.6.1
     */
    public static String getTokenSubject(String token) throws SSOException {
        return getFieldFromOpentoken(token, Agent.TOKEN_SUBJECT);
    }

    /**
     * Gets the specified field in the opentoken.
     *
     * @param token
     *            the encoded opentoken.
     * @param field
     *            the field in the opentoken.
     * @return the field of opentoken or null if not found.
     * @throws SSOException
     *             the SSO exception
     * @since SFP.6.1
     */
    public static String getFieldFromOpentoken(String token, String field) throws SSOException {
        final String METHOD_NAME = "getFieldFromOpentoken(String, String)";
        String fieldValue = null;
        try {

            Agent agent = getAgent();

            if (token != null) {
                Map userInfo = agent.readToken(token);
                fieldValue = (String) userInfo.get(field);
            }
        } catch (TokenException e) {
            LOGGER.warn(CLASS_NAME + "." + METHOD_NAME, "ERROR reading token in cookie", e);
        }

        /*
         * fallback try to look up field from access token.
         */
        if (StringUtils.isBlank(fieldValue)){
            if (StringUtils.isNotBlank(token)){
                if (checkTokenIsAzureAdToken(token)){
                    String preferredUserNameFieldValue = getFieldFromOAuthAccessToken(token, OAuthAccessTokenJwtField.PREFERRED_USER_NAME.getFieldKey());

                    boolean isGettingUserId = Agent.TOKEN_SUBJECT.equalsIgnoreCase(field) || LOGIN_AS_USER_ID_KEY.equalsIgnoreCase(field);
                    boolean isGettingUserDomain = LDAP_NAME.equalsIgnoreCase(field) || LOGIN_AS_LDAP_NAME_KEY.equalsIgnoreCase(field);

                    if (isGettingUserId || isGettingUserDomain){
                        String userDomain = null;
                        String userId = null;
                        if (StringUtils.isNotBlank(preferredUserNameFieldValue) && preferredUserNameFieldValue.indexOf("@") != -1 && !preferredUserNameFieldValue.endsWith("@")){
                            userId = preferredUserNameFieldValue.substring(0, preferredUserNameFieldValue.indexOf("@")).toUpperCase();
                            userDomain = preferredUserNameFieldValue.substring(preferredUserNameFieldValue.indexOf("@") + 1).toUpperCase();
                            if (StringUtils.isNotBlank(userDomain) && oauthAccessTokenAllowedUserDomains != null && !oauthAccessTokenAllowedUserDomains.isEmpty() && !oauthAccessTokenAllowedUserDomains.contains(userDomain)){
                                return null;//invalid user domain, we don't want to return the token value for safe
                            }
                            if (isGettingUserId){
                                return userId;
                            }
                            if (isGettingUserDomain){
                                return getUserDomainMapping(userDomain);
                            }
                        }
                    } else {
                        fieldValue = getFieldFromOAuthAccessToken(token, field);
                    }
                } else if (checkAccessTokenIsOOCLOAuth(token)){
                    fieldValue = getFieldFromOAuthAccessToken(token, field);
                }
            }
        }
        return fieldValue;
    }

    /**
     * Gets the email in the opentoken.
     *
     * @param httpRequest
     *            the HTTP request.
     * @param serviceAccountName
     *            the service account name.
     * @return the subject of opentoken or null if not found.
     * @throws SSOException
     *             the SSO exception
     * @since SFP.6.1
     */
    public static String getTokenEmail(HttpServletRequest httpRequest, String serviceAccountName) throws SSOException {
        return getFieldFromOpentoken(httpRequest, serviceAccountName, TOKEN_EMAIL);
    }

    /**
     * Gets the specified field in the opentoken.
     *
     * @param httpRequest
     *            the http request
     * @param serviceAccountName
     *            the service account name
     * @param field
     *            the field in the opentoken.
     * @return the field of opentoken or null if not found.
     * @throws SSOException
     *             the SSO exception
     * @since SFP.6.1
     */
    public static String getFieldFromOpentoken(HttpServletRequest httpRequest, String serviceAccountName, String field)
            throws SSOException {
        String cookieName = ssoConfig.getOpentokenCookieName(serviceAccountName);
        String token = (String) httpRequest.getAttribute(cookieName);
        if (token == null || token.length() == 0) {
            Cookie[] cookies = httpRequest.getCookies();
            if (cookies != null && cookies.length > 0) {
                for (int i = 0; i < cookies.length; i++) {
                    if (cookieName.equals(cookies[i].getName())) {
                        token = cookies[i].getValue();
                        break;
                    }
                }
            }
        }
        String fieldValue = getFieldFromOpentoken(token, field);

        /*
         * fallback try to look up field from access token.
         */
        if (StringUtils.isBlank(fieldValue)){
            String accessToken = getBearerTokenFromRequest(httpRequest);
            if (StringUtils.isNotBlank(accessToken)){
                if (checkTokenIsAzureAdToken(accessToken)){
                    String preferredUserNameFieldValue = getFieldFromOAuthAccessToken(accessToken, OAuthAccessTokenJwtField.PREFERRED_USER_NAME.getFieldKey());
                    if (Agent.TOKEN_SUBJECT.equalsIgnoreCase(field) || LOGIN_AS_USER_ID_KEY.equalsIgnoreCase(field)){
                        if (StringUtils.isNotBlank(preferredUserNameFieldValue) && preferredUserNameFieldValue.indexOf("@") != -1){
                            fieldValue = preferredUserNameFieldValue.substring(0, preferredUserNameFieldValue.indexOf("@")).toUpperCase();
                        }
                    } else if (LDAP_NAME.equalsIgnoreCase(field) || LOGIN_AS_LDAP_NAME_KEY.equalsIgnoreCase(field)){
                        if (StringUtils.isNotBlank(preferredUserNameFieldValue) && preferredUserNameFieldValue.indexOf("@") != -1 && !preferredUserNameFieldValue.endsWith("@")){
                            String userDomain = preferredUserNameFieldValue.substring(preferredUserNameFieldValue.indexOf("@") + 1).toUpperCase();
                            fieldValue = getUserDomainMapping(userDomain);
                        }
                    }
                } else if (checkAccessTokenIsOOCLOAuth(accessToken)){
                    fieldValue = getFieldFromOAuthAccessToken(accessToken, field);
                }
            }
        }
        return fieldValue;
    }

    /**
     * Gets the access token from request.
     *
     * @author YUER
     * @param httpRequest the http request
     * @return the access token from request
     * @since SFP.10.6
     */
    public static String getBearerTokenFromRequest(HttpServletRequest httpRequest){
        String authorizationHeader = httpRequest.getHeader(AUTHORIZATION_PROPERTY_NAME);
        if (!StringUtils.isEmpty(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring("Bearer ".length());
        }
        return null;
    }

    /**
     * Check access token is ooclo auth.
     *
     * @param accessToken the access token
     * @return true, if successful
     */
    public static boolean checkAccessTokenIsOOCLOAuth(String accessToken){
        if (StringUtils.isNotBlank(accessToken)){
            HashMap<String, Object> jwtFieldMap = jwtDecodeAccessTokenPayload(accessToken);
            if (jwtFieldMap != null){
                String issuer = String.valueOf(jwtFieldMap.get(OAuthAccessTokenJwtField.ISS.fieldKey));
                if (oauthServerTokenIssuer != null && oauthServerTokenIssuer.length > 0){
                    for(int i=0; i<oauthServerTokenIssuer.length; i++){
                        if (oauthServerTokenIssuer[i] != null && oauthServerTokenIssuer[i].equals(issuer)){
                            return true;
                        }
                    }
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Gets the user domain mapping.
     *
     * @param userDomain the user domain
     * @return the user domain mapping
     */
    private static String getUserDomainMapping(String userDomain){
        /*
         * "OOCLDM"->"OOCLDM"
         * "OOCL.COM"->"OOCL.COM"
         */
        if (OOCL_USER_DOMAIN.equals(userDomain) || OOCL_DOT_COM_DOMAIN.equals(userDomain)){
            return OOCL_USER_DOMAIN;
        }
        return userDomain;
    }

    /**
     * Gets the field from o auth access token.
     *
     * @author YUER
     * @param accessToken
     *            the access token
     * @param field
     *            the field
     * @return the field from o auth access token
     * @since SFP.10.4.2
     */
    public static String getFieldFromOAuthAccessToken(String accessToken, String field) {

        String METHOD_NAME = "getFieldFromOAuthAccessToken()";
        String mappedFieldKey = getMappedKeyForOAuthAccessToken(field);
        if (accessToken != null) {
            Jwt jwtToken = decodeJwtToken(accessToken);
            if (jwtToken != null && jwtToken.getClaims() != null) {
                HashMap<String, Object> jwtFieldMap = null;
                try {
                    jwtFieldMap = mapper.readValue(jwtToken.getClaims(), mapType);
                } catch (JsonParseException e) {
                    LOGGER.error(CLASS_NAME + "." + METHOD_NAME, e.getMessage(), e);
                } catch (JsonMappingException e) {
                    LOGGER.error(CLASS_NAME + "." + METHOD_NAME, e.getMessage(), e);
                } catch (IOException e) {
                    LOGGER.error(CLASS_NAME + "." + METHOD_NAME, e.getMessage(), e);
                }

                if (jwtFieldMap != null) {
                    return String.valueOf(jwtFieldMap.get(mappedFieldKey));
                }
            }
        }
        return null;
    }

    /**
     * Gets the mapped key for o auth access token.
     *
     * @author YUER
     * @param fieldKey the field key
     * @return the mapped key for o auth access token
     * @since SFP.10.6
     */
    private static String getMappedKeyForOAuthAccessToken(String fieldKey){
        /*
         * "subject"->"sub"
         * "LoginAsUserId"->"sub"
         * "LDAPName"->"grp"
         * "LoginAsLdapName"->"grp"
         */
        if (Agent.TOKEN_SUBJECT.equalsIgnoreCase(fieldKey) || LOGIN_AS_USER_ID_KEY.equalsIgnoreCase(fieldKey)){
            return OAuthAccessTokenJwtField.SUB.getFieldKey();
        } else if (LDAP_NAME.equalsIgnoreCase(fieldKey) || LOGIN_AS_LDAP_NAME_KEY.equalsIgnoreCase(fieldKey)){
            return OAuthAccessTokenJwtField.GRP.getFieldKey();
        }
        return fieldKey;
    }

    /**
     * Check token is azure ad token.
     *
     * @param token the access token
     * @return true, if successful
     */
    public static boolean checkTokenIsAzureAdToken(String token){
        if (StringUtils.isNotBlank(token)){
            HashMap<String, Object> jwtFieldMap = jwtDecodeAccessTokenPayload(token);
            if (jwtFieldMap != null){
                String issuer = String.valueOf(jwtFieldMap.get(OAuthAccessTokenJwtField.ISS.fieldKey));
                return azureADTokenIssuer != null && azureADTokenIssuer.equals(issuer);
            }
        }
        return false;
    }

    /**
     * Jwt decode access token payload.
     *
     * @param accessToken the access token
     * @return the hash map
     */
    private static HashMap<String, Object> jwtDecodeAccessTokenPayload(String accessToken) {
        String METHOD_NAME = "jwtDecodeAccessTokenPayload()";

        Jwt jwt = jwtDecodeAccessToken(accessToken);
        HashMap<String, Object> jwtFieldMap = null;

        if (jwt != null && jwt.getClaims() != null) {
            try {
                jwtFieldMap = mapper.readValue(jwt.getClaims(), mapType);
            } catch (JsonParseException e) {
                LOGGER.error(CLASS_NAME + "." + METHOD_NAME, e.getMessage(), e);
            } catch (JsonMappingException e) {
                LOGGER.error(CLASS_NAME + "." + METHOD_NAME, e.getMessage(), e);
            } catch (IOException e) {
                LOGGER.error(CLASS_NAME + "." + METHOD_NAME, e.getMessage(), e);
            }
        }

        return jwtFieldMap;
    }

    /**
     * Jwt decode access token.
     *
     * @param accessToken the access token
     * @return the jwt
     */
    private static Jwt jwtDecodeAccessToken(String accessToken) {
        String METHOD_NAME = "jwtDecodeAccessToken()";

        Jwt jwtToken = null;

        /**
         * decode the jwtToken and check expire and nbf.
         */
        if (!StringUtils.isBlank(accessToken)) {
            try {
                jwtToken = decodeJwtToken(accessToken);
            } catch (IllegalArgumentException e) {
                LOGGER.error(CLASS_NAME + "." + METHOD_NAME, e.getMessage(), e);
            }
        }
        return jwtToken;
    }

    /**
     * Decode jwt token.
     *
     * @param jwtToken the jwt token
     * @return the jwt
     */
    private static Jwt decodeJwtToken(String jwtToken){
        try{
            return JwtHelper.decode(jwtToken);
        } catch (IllegalArgumentException e){
            return null;
        }
    }

    public static String getDomainIdFromSSOToken(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws SSOException {
        String domainId = SSOHelperUtils.getFieldFromOpentoken(httpRequest,
                    "iris4ServiceAccount", "LoginAsUserId");
        if(domainId == null || domainId.trim().length() == 0){
            domainId = SSOHelperUtils.getTokenSubject(httpRequest, "iris4ServiceAccount");
        }
        //make authorization case-insensitive for user id
        domainId = domainId.toUpperCase();

        return domainId;
    }

}
