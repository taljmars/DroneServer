package com.db.server;

import com.db.server.security.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.util.Base64Utils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan("com.db.server.security")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final static Logger LOGGER = Logger.getLogger(SecurityConfig.class);

    public static final String AUTH_USERNAME_KEY = "X-Auth-Username";
    public static final String AUTH_PASSWORD_KEY = "X-Auth-Password";
    public static final String AUTH_TOKEN_KEY = "X-Auth-Token";

    @Autowired
    private ServerAccessDeniedHandler accessDeniedHandler;

    @Autowired
    private UserAuthenticationProvider authProvider;

    @Autowired
    private TokenAuthenticationProvider tokenAuthenticationProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        LOGGER.debug("configuring basic users");
        auth.authenticationProvider(authProvider);
        auth.authenticationProvider(tokenAuthenticationProvider);
    }

    @Bean("sessionLimitation")
    public Integer sessionLimitation() {
        return 3;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        LOGGER.debug("Configuring HTTP security");
        http
                .csrf().disable()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler)
                .and()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
//                .antMatchers("/createForToken").anonymous()
//                .antMatchers("/read*").permitAll()
//                .antMatchers("/update*").anonymous()
//                .antMatchers("/delete*").permitAll()
//                .antMatchers("/login").authenticated()
                .antMatchers("/**").permitAll()
                .antMatchers("/registerNewUser").anonymous()
//                .antMatchers("/*").hasRole("ADMIN")
                .and()
                .httpBasic()
                .and()
//                .anonymous().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .maximumSessions(sessionLimitation())
//                .sessionRegistry(sessionRegistry)
                .expiredSessionStrategy(new SessionInformationExpiredStrategy() {
                    @Override
                    public void onExpiredSessionDetected(SessionInformationExpiredEvent sessionInformationExpiredEvent) {
                        System.out.println("Session expired " +
                                sessionInformationExpiredEvent.getSessionInformation().getSessionId() +
                                " " +
                                sessionInformationExpiredEvent.getSessionInformation().getPrincipal()
                        );
                    }
                })
                .and()
                .and()
                .logout().disable()
                .addFilterBefore(new AuthenticationFilter(authenticationManager()), BasicAuthenticationFilter.class);
    }


    public static final String INTERNAL_SERVER_USER_NAME = "PUBLIC";
    public static final MyToken INTERNAL_SERVER_USER_TOKEN = new MyToken();

    @Bean("internalUserToken")
    public MyToken internalUserToken() {
        return INTERNAL_SERVER_USER_TOKEN;
    }

    @Bean("internalUserName")
    public String internalUserName() {
        return INTERNAL_SERVER_USER_NAME;
    }
}
