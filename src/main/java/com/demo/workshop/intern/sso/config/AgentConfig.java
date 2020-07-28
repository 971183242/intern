package com.demo.workshop.intern.sso.config;

import lombok.Data;

@Data
public class AgentConfig {

    public static final String PASSWORD_KEY = "password";
    public static final String COOKIE_PATH = "cookie-path";
    public static final String TOKEN_RENEWUNTIL = "token-renewuntil";
    public static final String USE_SUNJCE = "use-sunjce";
    public static final String TOKEN_LIFETIME = "token-lifetime";
    public static final String USE_COOKIE = "use-cookie";
    public static final String TOKEN_NOTBEFORE_TOLERANCE = "token-notbefore-tolerance";
    public static final String CIPHER_SUITE = "cipher-suite";
    public static final String TOKEN_NAME = "token-name";
    public static final String OBFUSCATE_PASSWORD = "obfuscate-password";

    private String password;
    private String cookiePath="/";
    private Integer tokenRenewuntil=90000;
    private Boolean useSunjce=false;
    private Integer tokenLifetime=60000;
    private Boolean useCookie=false;
    private Integer tokenNotbeforeTolerance=3600;
    private Integer cipherSuite=2;

    private String TokenName="opentoken";
    private Boolean obfuscatePassword=false;

}
