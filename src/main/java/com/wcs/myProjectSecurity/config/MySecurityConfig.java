package com.wcs.myProjectSecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableWebSecurity
public class MySecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public UserDetailsService userDetailsService() {
		// ensure the passwords are encoded properly
		UserBuilder users = User.builder();
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		manager.createUser(users.username("Steve").password("motdepasse").roles("CHAMPION").build());
		manager.createUser(users.username("Nick").password("flerken").roles("DIRECTOR").build());
		return manager;
	}

	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		
		.antMatchers("/avengers/assemble").hasRole("CHAMPION")
		.antMatchers("/secret-bases").hasRole("DIRECTOR")
		.antMatchers("/").anonymous()
		.and()
		.formLogin()
		.and()
		.httpBasic();

	}
	
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		
		auth.inMemoryAuthentication()
		.withUser("Steve")
		.password(encoder.encode("motdepasse"))
		.roles("CHAMPION");
		auth.inMemoryAuthentication()
		.withUser("Nick")
		.password(encoder.encode("flerken"))
		.roles("DIRECTOR");
	
	}

}