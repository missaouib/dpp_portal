package mzc.data.portal.config;

import mzc.data.portal.exception.LoginFailureHandler;
import mzc.data.portal.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private LoginFailureHandler loginFailureHandler;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/login*").permitAll()
                .antMatchers("/loginPage").permitAll()
                .antMatchers("/signUp").permitAll()
                .antMatchers("/resource/**").permitAll()
                .antMatchers("/check-passwd").hasAnyRole("USER","ADMIN","INIT")
                .antMatchers("/").hasAnyRole("USER","ADMIN")
                .antMatchers("/main").hasAnyRole("USER","ADMIN")
                .antMatchers("/buckets").hasAnyRole("USER","ADMIN")
                .antMatchers("/objects").hasAnyRole("USER","ADMIN")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/project/**").hasAnyRole("USER","ADMIN")
                .antMatchers("/meta/**").hasAnyRole("USER","ADMIN");

        http.formLogin()
                .loginPage("/loginPage")
                .loginProcessingUrl("/login")
                .failureHandler(loginFailureHandler)
                .defaultSuccessUrl("/login-success", true);

        http.exceptionHandling().accessDeniedPage("/accessDenied");
        http.logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/loginPage")
                .invalidateHttpSession(true);

        http.sessionManagement()
                .invalidSessionUrl("/loginPage")
                .maximumSessions(1);

    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userServiceImpl).passwordEncoder(passwordEncoder());

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");

    }
}
