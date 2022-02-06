package com.revosystems.llms;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.JdbcUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter implements AuthenticationSuccessHandler {

	@Autowired private ObjectMapper objectMapper;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/", "/h2", "/h2/**").permitAll()
			//.antMatchers(HttpMethod.GET, "/reasons").hasRole("USER")
			//.antMatchers("/reasons", "/reasons/**").hasRole("ADMIN")
			.anyRequest().authenticated().and()
			.csrf().disable()
			.cors().disable()
			.headers().frameOptions().disable().and()
			.formLogin().successHandler(this).permitAll().and()
			.logout().permitAll();
	}
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		if(null != authentication && authentication.isAuthenticated()) {
			objectMapper.writeValue(response.getWriter(), authentication.getPrincipal());
		}
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth, JdbcUserDetailsManager userDetailsManager, DataSource dataSource, PasswordEncoder enc) throws Exception {
		auth.userDetailsService(userDetailsManager);
	    final JdbcUserDetailsManagerConfigurer<AuthenticationManagerBuilder> configurer =
	            new JdbcUserDetailsManagerConfigurer<>(userDetailsManager);
	    configurer.dataSource(dataSource);
	    auth.apply(configurer);
	    
	    if (!dataSource.getConnection().getMetaData().getTables(null, "", "USERS", null).first()) {
			configurer.withDefaultSchema();
			configurer.withUser("parag")
				.password(passwordEncoder().encode("parag"))
				.roles("USER")
				.authorities("MF");
			configurer.withUser("admin")
				.password(passwordEncoder().encode("admin"))
				.roles("ADMIN");
		}
	}
	
	@Bean
	public JdbcUserDetailsManager userDetailsManager(DataSource dataSource) {
	    final JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager();
	    userDetailsManager.setDataSource(dataSource);
	    return userDetailsManager;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12);
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

}
