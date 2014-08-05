package com.technion.project;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.google.common.collect.Maps;

@Configuration
@EnableWebSecurity
public class UserSecurityConfig extends WebSecurityConfigurerAdapter
{
	private static final String ROLE_USER = "ROLE_USER";
	private static final String ROLE_ADMIN = "ROLE_ADMIN";
	private static Map<String, String> roleByPath = Maps.newLinkedHashMap();
	static
	{
		roleByPath.put("/admin/**", ROLE_ADMIN);
		roleByPath.put("/addReport", ROLE_USER);
		roleByPath.put("/evacuation/add", ROLE_ADMIN);
		roleByPath.put("/accounts/*/delete", ROLE_ADMIN);
		roleByPath.put("/accounts/*/disable", ROLE_ADMIN);
		roleByPath.put("/accounts/*/deleteself", ROLE_USER);
		roleByPath.put("/accounts/*/event", ROLE_USER);
		roleByPath.put("/accounts/own", ROLE_USER);
		roleByPath.put("/evacuation/id/*", ROLE_ADMIN);
		roleByPath.put("/evacuation/id/*/join", ROLE_USER);
		roleByPath.put("/evacuation/id/*/users", ROLE_USER);
		roleByPath.put("/evacuation/id/*/joinUser", ROLE_ADMIN);
		roleByPath.put("/evacuation/id/*/leave", ROLE_USER);
		roleByPath.put("/evacuation/id/*/leaveUser", ROLE_ADMIN);
		roleByPath.put("/accounts/*/reports", ROLE_USER);
		// roleByPath.put("/evacuation/list", ROLE_USER);

	}
	@Autowired
	@Qualifier("userDetailsService")
	UserDetailsService userDetailsService;

	@Autowired
	public void configureGlobal(final AuthenticationManagerBuilder auth)
			throws Exception
	{
		auth.userDetailsService(userDetailsService).passwordEncoder(
				passwordEncoder());
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception
	{
		for (final String path : roleByPath.keySet())
			http.authorizeRequests().antMatchers(path)
					.access("hasRole('" + roleByPath.get(path) + "')").and()
					.formLogin().loginPage("/login").failureUrl("/login?error")
					.usernameParameter("username")
					.passwordParameter("password").and().logout()
					.logoutSuccessUrl("/login?logout").and().csrf().and()
					.exceptionHandling().accessDeniedPage("/403");

	}

	@Bean
	public PasswordEncoder passwordEncoder()
	{
		final PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}

}