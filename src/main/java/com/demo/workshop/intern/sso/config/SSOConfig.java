package com.demo.workshop.intern.sso.config;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SSOConfig {
    private volatile static SSOConfig ssoConfig;
    private static final Object lock = new Object();

    /**
     * flag to indicate that if SSO enabled. Default value is 'false' which means SSO disabled.
     */
    private Boolean ssoEnabled = false;
    //    private String agentFileLocation;
    private String retryCount = "3";
    private String retryTimerInterval = "120";
    private String serviceAccount = SSOConstants.DEFAULT_SSO_SERVICE_ACCOUNT;
    private String serviceAccountFilePath;
    private String dynamicLoadAgentConfigEnabled = "false";
    private String defaultOpenTokenCookieName = "SSO_OPEN_TOKEN";
    private String defaultUserIdCookieName = "SSO_USER_ID";
    private String defaultFwkSessionIdCookieName = "FWK_SESSION_ID";
    private String ssoFilterUrlPattern = "/secured/*";

    private AgentConfig agentConfig = new AgentConfig();

    private List<SSOServiceAccount> ssoServiceAccounts = new ArrayList<>();


    private SSOConfig(Boolean ssoEnabled,String ssoServerURL,String cookieDomain){

        this.ssoEnabled = ssoEnabled;

        SSOServiceAccount ssoServiceAccount = new SSOServiceAccount();
        ssoServiceAccount.setRequestRedirectUrl(ssoServerURL);
        ssoServiceAccount.setOpenTokenCookieDomain(cookieDomain);
        ssoServiceAccounts.add(ssoServiceAccount);
    }

    private SSOConfig(){
        SSOServiceAccount ssoServiceAccount = new SSOServiceAccount();
        ssoServiceAccounts.add(ssoServiceAccount);
    }

    public static SSOConfig getInstance(){
        if(ssoConfig == null) {
            synchronized(lock){
                if(ssoConfig == null){
                    ssoConfig = new SSOConfig();
                }
            }
        }
        return ssoConfig;
    }

    public static SSOConfig getInstance(Boolean ssoEnabled,String ssoServerURL,String cookieDomain){
        if(ssoConfig == null) {
            synchronized(lock){
                if(ssoConfig == null){
                    ssoConfig = new SSOConfig(ssoEnabled,ssoServerURL,cookieDomain);
                }
            }
        }
        return ssoConfig;
    }

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
