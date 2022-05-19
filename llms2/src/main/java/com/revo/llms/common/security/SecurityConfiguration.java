package com.revo.llms.common.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import com.revo.llms.LlmsConstants;
import com.vaadin.flow.spring.security.VaadinWebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends VaadinWebSecurityConfigurerAdapter {

	@Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        setLoginView(http, LoginView.class); 
    }
	
	@Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/images/**"); 
        super.configure(web);
    }
	
	@Bean
    @Override
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(User.withUsername("admin") 
            .password(passwordEncoder().encode("admin"))
            .authorities(
            		LlmsConstants.PREFIX_PAGE + LlmsConstants.ROUTE_DASHBOARD,
            		LlmsConstants.PREFIX_PAGE + LlmsConstants.ROUTE_TICKETS,
            		LlmsConstants.PREFIX_PAGE + LlmsConstants.ROUTE_DEPARTMENTS,
            		LlmsConstants.PREFIX_PAGE + LlmsConstants.ROUTE_REASONS)
            .build());
    }
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
}
