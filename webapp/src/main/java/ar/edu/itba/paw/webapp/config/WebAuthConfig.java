package ar.edu.itba.paw.webapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;



@EnableWebSecurity
@ComponentScan({"ar.edu.itba.paw.webapp.auth"})
@Configuration
public class WebAuthConfig extends WebSecurityConfigurerAdapter {

    @Value("classpath:rememberMe.key")
    private Resource rememberMeKey;


    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception{
            http.sessionManagement()
                .invalidSessionUrl("/login")
            .and().authorizeHttpRequests()
                .requestMatchers("/signup", "/login").anonymous()
                .anyRequest().authenticated()
            .and().formLogin()
                .loginPage("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/login", false)
            .and().rememberMe()
                .rememberMeParameter("rememberMe")
                .userDetailsService(userDetailsService)
                .key(StreamUtils.copyToString(rememberMeKey.getInputStream(), StandardCharsets.UTF_8)) //openssl rand -base64 4000 > src/main/resources/rememberMe.key
                .tokenValiditySeconds(15*24*60*60)//(int) TimeUnit.DAYS.toSeconds(15))
            .and().logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
            .and().exceptionHandling()
                .accessDeniedPage("/403")
            .and().csrf().disable();
    }


    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring().requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico", "/403");
    }



}
