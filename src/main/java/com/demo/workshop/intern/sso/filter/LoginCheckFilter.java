package com.demo.workshop.intern.sso.filter;

import com.demo.workshop.intern.sso.exceptions.SSOException;
import com.demo.workshop.intern.sso.config.SSOConfig;
import com.demo.workshop.intern.sso.util.SSOHelperUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginCheckFilter extends SSOBaseFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginCheckFilter.class);

    private SSOHelperUtils ssoHelperUtils;

    public LoginCheckFilter(){}

    public LoginCheckFilter(SSOHelperUtils ssoHelperUtils, SSOConfig ssoConfig){
        this.ssoHelperUtils = ssoHelperUtils;
        super.ssoConfig = ssoConfig;
    }

    public void setSSOHelperUtils(SSOHelperUtils ssoHelperUtils){
        this.ssoHelperUtils = ssoHelperUtils;
    }


    @Override
    public void init(FilterConfig filterConfig){
        final String METHOD_NAME = "init(FilterConfig)";
        try {
            LOGGER.debug("Enter " + CLASS_NAME + METHOD_NAME);
            serviceAccountName = filterConfig
                    .getInitParameter(SERVICE_ACCOUNT_NAME);

            if (filterConfig.getInitParameter(EXCEPT_LIST) != null) {
                // get an array of except list sperated by ","
                exceptList = filterConfig.getInitParameter(EXCEPT_LIST).split(",");
            }

            if (filterConfig.getInitParameter(REFERER_HEADER_ALLOWED_BASE_DOMAIN_LIST) != null) {
                refererHeaderAllowedBaseDomainList = filterConfig.getInitParameter(REFERER_HEADER_ALLOWED_BASE_DOMAIN_LIST).split(",");
                if (refererHeaderAllowedBaseDomainList != null) {
                    for (int i=0; i<refererHeaderAllowedBaseDomainList.length; i++) {
                        refererHeaderAllowedBaseDomainList[i] = StringUtils.trimToEmpty(refererHeaderAllowedBaseDomainList[i]);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error(CLASS_NAME + "." + METHOD_NAME,
                    "Error in init.", e);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        if(null == ssoHelperUtils) {
            ssoHelperUtils = new SSOHelperUtils(SSOConfig.getInstance());
        }

        final String METHOD_NAME = "doFilter(ServletRequest, ServletResponse, FilterChain)";
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {

            if(ssoConfig == null){
                ssoConfig = SSOConfig.getInstance();
            }
            // check is sso enable flag is equal to true
            if (ssoConfig.getSsoEnabled()) {

                if (doIgnoreFilter(httpRequest, httpResponse)) {
                    chain.doFilter(request, response);
                    return;
                }

                if (!doCSRFRefererProtectionFilter(httpRequest, httpResponse)) {
                    httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                            "Unauthorized Referer");
                    return;
                }

                doBeforeCheckTokenFromCookie(httpRequest, httpResponse);
                boolean isTokenValid = doCheckTokenFromCookie(httpRequest,
                        httpResponse);
                doAfterCheckTokenFromCookie(httpRequest, httpResponse);

                if (!isTokenValid) {
                    doBeforeCheckOpenToken(httpRequest, httpResponse);
                    isTokenValid = doCheckOpenToken(httpRequest, httpResponse);
                    doAfterCheckOpenToken(httpRequest, httpResponse);
                }

                if (isTokenValid) {
                    // TODO do we still need to set context user id ?
                }
            }
        } catch (SSOException ssoe) {
            LOGGER.error(CLASS_NAME + "." + METHOD_NAME,
                    "Error in doFilter.", ssoe);
            LOGGER.error(METHOD_NAME,"Error in doFilter.",ssoe);
            throw new ServletException("Error in doFilter.", ssoe);
        }

        if (!httpResponse.isCommitted()) {
            chain.doFilter(request, response);
        }
    }

    public void destroy() {
    }

    @Override
    public boolean doCheckOpenToken(HttpServletRequest httpRequest,
                                    HttpServletResponse httpResponse) throws SSOException {
        return ssoHelperUtils.checkOpenToken(httpRequest, httpResponse,
                serviceAccountName);

    }

    /*
     * (non-Javadoc)
     *
     * @see com.oocl.csc.frm.sso.filter.SSOBaseFilter#doCheckTokenFromCookie(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    public boolean doCheckTokenFromCookie(HttpServletRequest httpRequest,
                                          HttpServletResponse httpResponse) throws SSOException {
        return ssoHelperUtils.doCheckTokenFromCookie(httpRequest, httpResponse,
                serviceAccountName);
    }

}
