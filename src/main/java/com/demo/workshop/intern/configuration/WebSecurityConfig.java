package com.demo.workshop.intern.configuration;

import com.demo.workshop.intern.domain.profile.entity.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

import javax.annotation.Resource;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private AuthenticationSuccessHandler myAuthenticationSuccessHandler;
    @Resource
    private AuthenticationFailureHandler myAuthenticationFailHandler;
    @Resource
    private AuthenticationProvider provider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(
                        "/assets/**",
                        "/bower_components/**",
                        "/config/**",
                        "/controllers/**",
                        "/service/**",
                        "/css/**",
                        "/js/**",
                        "/img/**",
                        "/fonts/**",
                        "/favicon.ico",
                        "/test",
                        "/error/403",
                        "/toLogout",
                        "/h2-console/**").permitAll()
                .antMatchers(
                        "/admin", "/swagger-ui.html", "/event/**").hasAuthority(Role.SUPER_ADMIN.getFullName())
                .antMatchers(
                        "/intern").hasAuthority(Role.INTERN.getFullName())
                .antMatchers(
                        "/leader").hasAnyAuthority(Role.TEAM_LEADER.getFullName(),Role.SUPER_ADMIN.getFullName())
                .and()
                .formLogin()
                .loginPage("/login").loginProcessingUrl("/login/form")
                .failureUrl("/login?error")
                .successHandler(myAuthenticationSuccessHandler)
                .failureHandler(myAuthenticationFailHandler)
                .permitAll()
                .and()
                .rememberMe()
                .and()
                .logout()
                .logoutUrl("toLogout")
                .deleteCookies("JSESSIONID")
                .permitAll()
                .and().headers().frameOptions().sameOrigin()
                .and().csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(provider);
    }

    /**
     *  允许security防火墙不过滤带 ';'的url
     */
    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowSemicolon(true);
        return firewall;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
        web.httpFirewall(allowUrlEncodedSlashHttpFirewall());
    }
}
