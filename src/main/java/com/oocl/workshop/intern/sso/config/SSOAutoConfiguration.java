package com.oocl.workshop.intern.sso.config;


import com.oocl.workshop.intern.sso.exceptions.SSOException;
import com.oocl.workshop.intern.sso.filter.LoginCheckFilter;
import com.oocl.workshop.intern.sso.util.SSOHelperUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConditionalOnClass(LoginCheckFilter.class)
@EnableConfigurationProperties(SSOConfigProperties.class)
public class SSOAutoConfiguration {

    Logger LOGGER = LoggerFactory.getLogger(SSOAutoConfiguration.class);

    @Autowired
    private SSOConfigProperties ssoConfigProperties;

    private static final String SERVICE_ACCOUNT_NAME = "service_account_name";

    @Bean
    @ConditionalOnMissingBean
    public SSOConfig ssoConfig(){

        SSOConfig ssoConfig = SSOConfig.getInstance();

        ssoConfig.setSsoEnabled(ssoConfigProperties.getSsoEnabled());
        ssoConfig.setDefaultFwkSessionIdCookieName(ssoConfigProperties.getDefaultFwkSessionIdCookieName());
        ssoConfig.setDefaultOpenTokenCookieName(ssoConfigProperties.getDefaultOpenTokenCookieName());
        ssoConfig.setDefaultUserIdCookieName(ssoConfigProperties.getDefaultUserIdCookieName());
        ssoConfig.setSsoFilterUrlPattern(ssoConfigProperties.getSsoFilterUrlPattern());
        ssoConfig.setServiceAccount(ssoConfigProperties.getServiceAccount());

        ssoConfig.setSsoServiceAccounts(ssoConfigProperties.getSsoServiceAccounts());
        ssoConfig.setAgentConfig(ssoConfigProperties.getAgentConfig());

        return ssoConfig;
    }

    @Bean
    @ConditionalOnMissingBean
    public SSOHelperUtils ssoHelperUtils(SSOConfig ssoConfig){
        return new SSOHelperUtils(ssoConfig);
    }

    @Bean
    @ConditionalOnMissingBean
    public LoginCheckFilter loginCheckFilter(SSOHelperUtils ssoHelperUtils,SSOConfig ssoConfig){
        return new LoginCheckFilter(ssoHelperUtils,ssoConfig);
    }

    @Bean
    public FilterRegistrationBean setLoginChckFilter(LoginCheckFilter loginCheckFilter) throws SSOException {

        LOGGER.debug("Is sso enabled ? [" + ssoConfigProperties.getSsoEnabled() + "]");

        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(loginCheckFilter);
        if (null == ssoConfigProperties.getSsoFilterUrlPattern() || StringUtils.isEmpty(ssoConfigProperties.getSsoFilterUrlPattern())) {
            // TODO specify more information about this exception.
            throw new SSOException("Cannot enable SSO without setting sso filter url pattern.");
        }
        registrationBean.addUrlPatterns("/login/form", "/login");
        registrationBean.addInitParameter(SERVICE_ACCOUNT_NAME, ssoConfigProperties.getServiceAccount());
        registrationBean.setOrder(Integer.MIN_VALUE + 9);
        return registrationBean;
    }

}
