package com.demo.workshop.intern.sso.filter;


import com.demo.workshop.intern.sso.config.SSOConfig;
import com.demo.workshop.intern.sso.exceptions.SSOException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class SSOBaseFilter implements Filter {

    private Logger LOGGER = LoggerFactory.getLogger(SSOBaseFilter.class);

    protected static final String CLASS_NAME = SSOBaseFilter.class.getName();

    public static final String EXCEPT_LIST = "except_list";

    public static final String REFERER_HEADER = "referer";

    public static final String REFERER_HEADER_ALLOWED_BASE_DOMAIN_LIST = "referer_header_allowed_base_domain_list";

    public static final String LOGIN_DOMAIN_URL = "login_domain_url";

    public static final String SERVICE_ACCOUNT_NAME = "service_account_name";

    public static final String ART_IUSER_PREFIX = "SSO-IUser";

    protected String[] exceptList = null;

    protected String[] refererHeaderAllowedBaseDomainList = null;

    protected String serviceAccountName = "";

    protected SSOConfig ssoConfig;

    public void setSsoConfigProperties(SSOConfig ssoConfig){
        this.ssoConfig = ssoConfig;
    }

    /**
     * The <code>doBeforeCheckTokenFromCookie</code> method is called before
     * <code>doCheckTokenFromCookie</code>.
     *
     * @param httpRequest
     *            the HttpServletRequest.
     * @param httpResponse
     *            the HttpServletResponse.
     * @throws SSOException
     *             if any errors occur.
     */
    public void doBeforeCheckTokenFromCookie(HttpServletRequest httpRequest,
                                             HttpServletResponse httpResponse) throws SSOException {
    }

    /**
     * The <code>doAfterCheckTokenFromCookie</code> method is called after
     * <code>doCheckTokenFromCookie</code>.
     *
     * @param httpRequest
     *            the HttpServletRequest.
     * @param httpResponse
     *            the HttpServletResponse.
     * @throws SSOException
     *             if any errors occur.
     */
    public void doAfterCheckTokenFromCookie(HttpServletRequest httpRequest,
                                            HttpServletResponse httpResponse) throws SSOException {
    }

    /**
     * Checks the existence of token from cookie.
     *
     * @param httpRequest
     *            the HttpServletRequest.
     * @param httpResponse
     *            the HttpServletResponse.
     * @return true if token is found; false otherwise.
     * @throws SSOException
     *             if any errors occur.
     */
    public abstract boolean doCheckTokenFromCookie(
            HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws SSOException;

    /**
     * The <code>doBeforeCheckOpenToken</code> method is called before
     * <code>doCheckOpenToken</code>.
     *
     * @param httpRequest
     *            the HttpServletRequest.
     * @param httpResponse
     *            the HttpServletResponse.
     * @throws SSOException
     *             if any errors occur.
     */
    public void doBeforeCheckOpenToken(HttpServletRequest httpRequest,
                                       HttpServletResponse httpResponse) throws SSOException {
    }

    /**
     * The <code>doAfterCheckOpenToken</code> method is called after
     * <code>doCheckOpenToken</code>.
     *
     * @param httpRequest
     *            the HttpServletRequest.
     * @param httpResponse
     *            the HttpServletResponse.
     * @throws SSOException
     *             if any errors occur.
     */
    public void doAfterCheckOpenToken(HttpServletRequest httpRequest,
                                      HttpServletResponse httpResponse) throws SSOException {
    }

    /**
     * Checks the existence of opentoken.
     *
     * @param httpRequest
     *            the HttpServletRequest.
     * @param httpResponse
     *            the HttpServletResponse.
     * @return true if token is found; false otherwise.
     * @throws SSOException
     *             if any errors occur.
     */
    public abstract boolean doCheckOpenToken(HttpServletRequest httpRequest,
                                             HttpServletResponse httpResponse) throws SSOException;

    /**
     * Checks whether the request URI is within the exceptional list.
     *
     * @param httpRequest
     *            the HttpServletRequest.
     * @param httpResponse
     *            the HttpServletResponse.
     * @return true if the URI contains in the exceptional list; false
     *         otherwise.
     * @throws SSOException
     *             if any errors occur.
     */
    public boolean doIgnoreFilter(HttpServletRequest httpRequest,
                                  HttpServletResponse httpResponse) throws SSOException {

        final String METHOD_NAME = "doIgnoreFilter(HttpServletRequest, HttpServletResponse)";

        String requestURI = httpRequest.getRequestURI();

        if (exceptList != null) {

            for (String except : exceptList) {
                if(!except.isEmpty()) {

                    Pattern pattern = Pattern.compile(except);

                    Matcher matcher = pattern.matcher(requestURI);

                    if (matcher.find()) {
                        return true;
                    }
                }
            }
        }
        LOGGER.debug(CLASS_NAME + METHOD_NAME + "NO MATCHED: " + requestURI);
        return false;
    }

    /*
     * Prevent CSRF attack by validating "referer" http header value which would be set by browser.
     */
    public boolean doCSRFRefererProtectionFilter(HttpServletRequest httpRequest,
                                                 HttpServletResponse httpResponse) throws SSOException {

        final String METHOD_NAME = "doCSRFRefererProtectionFilter(HttpServletRequest, HttpServletResponse)";

        /*
         * By default if no configuration found, we just let go (return true)
         */
        if (refererHeaderAllowedBaseDomainList != null) {

            String refereHeaderValue = httpRequest.getHeader(REFERER_HEADER);

            /*
             * If no referer header, we also let go
             */
            if (StringUtils.isNotBlank(refereHeaderValue)) {

                /*
                 * Loop for configured domain pattern and check against referer header value
                 */
                for (String baseDomain : refererHeaderAllowedBaseDomainList) {
                    baseDomain = StringUtils.trimToEmpty(baseDomain);
                    if (!baseDomain.isEmpty()) {

                        /*
                         * edited pattern from the reference at https://stackoverflow.com/a/7933253
                         *
                         * unescaped regex pattern sample: "^http[s]?:\/\/(?:[a-z0-9](?:[a-z0-9\-]{0,61}[a-z0-9])?\.)*oocl\.com(?:\:[0-9]+)?(\/.*)?$"
                         */
                        String refererDomainUrlPattern = new StringBuilder()
                                .append("^http[s]?:\\/\\/(?:[a-z0-9](?:[a-z0-9\\-]{0,61}[a-z0-9])?\\.)*")
                                .append(baseDomain.replaceAll("\\.", "\\\\.")).append("(?:\\:[0-9]+)?(\\/.*)?$")
                                .toString();

                        Pattern pattern = Pattern.compile(refererDomainUrlPattern);

                        Matcher matcher = pattern.matcher(refereHeaderValue);

                        if (matcher.find()) {
                            return true;
                        }
                    }
                }
                LOGGER.debug(CLASS_NAME + METHOD_NAME + "NO MATCHED: " + refereHeaderValue);
                return false;
            }
        }

        return true;
    }

}
