package pl.coderslab;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SpringDataUserDetailsService customUserDetailsService() {
		return new SpringDataUserDetailsService();
	}


	@Override
	protected void configure(HttpSecurity http) throws Exception { 
		http.csrf().disable()
		.authorizeRequests()
		.antMatchers("/").hasAnyRole("ADMIN","OWNER","EMPLOYEE","MANAGER")
		.antMatchers("/client/**").hasAnyRole("OWNER","EMPLOYEE","MANAGER")
		.antMatchers("/contract/**").hasAnyRole("OWNER","EMPLOYEE","MANAGER")
		.antMatchers("/employeeSearch/**").hasAnyRole("OWNER","EMPLOYEE","MANAGER")
		.antMatchers("/managerSearch/**").hasAnyRole("OWNER","MANAGER")
		.antMatchers("/event/**").hasAnyRole("OWNER","EMPLOYEE","MANAGER")
		.antMatchers("/import/**").hasAnyRole("OWNER","EMPLOYEE","MANAGER")
		.antMatchers("/admin/**").hasRole("ADMIN")
		.anyRequest().permitAll()
		.and().formLogin()
		.and().httpBasic();
	}
}
