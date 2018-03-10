package com.db.server;

import com.db.server.security.ServerAccessDeniedHandler;
import com.db.server.security.ServerAuthProvider;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.ServletException;
import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan("com.db.server.security")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final static Logger LOGGER = Logger.getLogger(SecurityConfig.class);

    @Autowired
    private ServerAccessDeniedHandler accessDeniedHandler;

    @Autowired
    private ServerAuthProvider authProvider;

    @Autowired
    private SessionRegistry sessionRegistry;

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        LOGGER.debug("configuring basic users");
        auth.inMemoryAuthentication()
                .passwordEncoder(NoOpPasswordEncoder.getInstance())
                .withUser("admin").password("admin").roles("ADMIN")
                .and().withUser("PUBLIC").password("PUBLIC").roles("ADMIN")
                .and().withUser("admin1").password("admin1").roles("USER");
        auth.authenticationProvider(authProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        LOGGER.debug("Configuring HTTP security");
        http
                .csrf().disable()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler)
                .and()
                .authorizeRequests()
//                .antMatchers("/createForToken").anonymous()
//                .antMatchers("/read*").permitAll()
//                .antMatchers("/update*").anonymous()
//                .antMatchers("/delete*").permitAll()
                .antMatchers("/login").authenticated()
//                .antMatchers("/*").hasRole("ADMIN")
                .and()
                .httpBasic()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .maximumSessions(1)
                .sessionRegistry(sessionRegistry)
                .expiredSessionStrategy(new SessionInformationExpiredStrategy() {
                    @Override
                    public void onExpiredSessionDetected(SessionInformationExpiredEvent sessionInformationExpiredEvent) throws IOException, ServletException {
                        System.out.println("Session expired");
                    }
                })
                .and()
                .and()
                .logout().disable();
    }

}
