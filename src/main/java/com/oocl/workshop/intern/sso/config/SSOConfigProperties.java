package com.oocl.workshop.intern.sso.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "sfp.config.sso")
@Data
public class SSOConfigProperties {
    /**
     * flag to indicate that if SSO enabled. Default value is 'false' which means SSO disabled.
     */
    private Boolean ssoEnabled = false;
    private String retryCount = "3";
    private String retryTimerInterval = "120";
    private String serviceAccount = "iris4ServiceAccount";
    private String serviceAccountFilePath;
    private String dynamicLoadAgentConfigEnabled = "false";
    private String defaultOpenTokenCookieName = "SSO_OPEN_TOKEN";
    private String defaultUserIdCookieName = "SSO_USER_ID";
    private String defaultFwkSessionIdCookieName = "FWK_SESSION_ID";
    private String ssoFilterUrlPattern = "/*";

    private List<SSOServiceAccount> ssoServiceAccounts = new ArrayList<SSOServiceAccount>();

    private AgentConfig agentConfig = new AgentConfig();

    public SSOServiceAccount getSSOServiceAccountByAccountName(String serviceAccountName) {

        SSOServiceAccount ssoServiceAccount = null;

        for(SSOServiceAccount account : ssoServiceAccounts){
            if(serviceAccountName != null) {
                if(serviceAccountName.equalsIgnoreCase(account.getSsoServiceAccountName())){
                    ssoServiceAccount = account;
                }
            }
        }

        return ssoServiceAccount;
    }

    public String getOpentokenCookieName(String serviceAccountName) {
        return getSSOServiceAccountByAccountName(serviceAccountName).getOpenTokenCookieName();
    }
}
