package com.revosystems.llms;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.JdbcUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource dataSource;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/", "/h2", "/h2/**").permitAll()
			.antMatchers(HttpMethod.GET, "/reasons").hasRole("USER")
			.antMatchers("/reasons", "/reasons/**").hasRole("ADMIN")
			.anyRequest().authenticated()
			.and().csrf().ignoringAntMatchers("/h2/**")
			.and().headers().frameOptions().disable()
			.and().formLogin().permitAll().and()
			.logout().permitAll();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		final JdbcUserDetailsManagerConfigurer<AuthenticationManagerBuilder> configurer = auth.jdbcAuthentication()
				.dataSource(dataSource)
				.passwordEncoder(passwordEncoder());
		
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
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12);
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

}
