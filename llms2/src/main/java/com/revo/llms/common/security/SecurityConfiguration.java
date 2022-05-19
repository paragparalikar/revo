package com.revo.llms.common.security;

import static com.revo.llms.LlmsConstants.PREFIX_PAGE;
import static com.revo.llms.LlmsConstants.ROUTE_DASHBOARD;
import static com.revo.llms.LlmsConstants.ROUTE_DEPARTMENTS;
import static com.revo.llms.LlmsConstants.ROUTE_PRODUCTS;
import static com.revo.llms.LlmsConstants.ROUTE_REASONS;
import static com.revo.llms.LlmsConstants.ROUTE_TICKETS;
import static com.revo.llms.LlmsConstants.ROUTE_USERS;
import static com.revo.llms.LlmsConstants.ROUTE_PARTS;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import com.revo.llms.user.UserService;
import com.vaadin.flow.spring.security.VaadinWebSecurityConfigurerAdapter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends VaadinWebSecurityConfigurerAdapter {
	
	private final UserService userService; 

	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
        	.antMatchers("/" + ROUTE_DASHBOARD).hasAuthority(PREFIX_PAGE + ROUTE_DASHBOARD)
        	.antMatchers("/" + ROUTE_TICKETS).hasAuthority(PREFIX_PAGE + ROUTE_TICKETS)
        	.antMatchers("/" + ROUTE_DEPARTMENTS).hasAuthority(PREFIX_PAGE + ROUTE_DEPARTMENTS)
        	.antMatchers("/" + ROUTE_REASONS).hasAuthority(PREFIX_PAGE + ROUTE_REASONS)
        	.antMatchers("/" + ROUTE_PRODUCTS).hasAuthority(PREFIX_PAGE + ROUTE_PRODUCTS)
        	.antMatchers("/" + ROUTE_USERS).hasAuthority(PREFIX_PAGE + ROUTE_USERS)
        	.antMatchers("/" + ROUTE_PARTS + "/*").hasAuthority(PREFIX_PAGE + ROUTE_PARTS);
        super.configure(http);
        setLoginView(http, LoginView.class); 
    }
	
	@Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/images/**", "/h2/**"); 
        super.configure(web);
    }
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService)
			.passwordEncoder(userService.getPasswordEncoder());
	}
	
}
