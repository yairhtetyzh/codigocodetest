package com.demo.codetest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.demo.codetest.security.CustomUserDetailsService;
import com.demo.codetest.security.JwtAuthenticationEntryPoint;
import com.demo.codetest.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	CustomUserDetailsService customUserDetailsService;

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors(); // enable cors

		http.csrf().disable().headers().disable() // disable header
				.authorizeRequests()
				.antMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/api/login",
						"/api/user/register")
				.permitAll().and().exceptionHandling() // default response if the client wants to get a resource
				
				.authenticationEntryPoint(new JwtAuthenticationEntryPoint());

		http.sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

	}

}