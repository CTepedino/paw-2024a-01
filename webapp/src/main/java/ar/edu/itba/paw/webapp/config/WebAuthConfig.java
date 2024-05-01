package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.models.UserRoles;
import ar.edu.itba.paw.webapp.auth.AccessHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
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

    @Autowired
    private AccessHelper accessHelper;

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
                .invalidSessionUrl("/")

            .and().authorizeHttpRequests()
                .requestMatchers("/signup", "/login").anonymous()
                .requestMatchers( "/sales").hasRole(UserRoles.WRITER.toString())
                .requestMatchers(HttpMethod.POST, "/sendBuyInfo").access((a, o) -> new AuthorizationDecision(accessHelper.canCreateOrder(a.get(), o.getRequest())))
                .requestMatchers("/", "/image/**", "/pdf/**", "/book/**", "/search/**").permitAll()
                .anyRequest().authenticated()

            .and().formLogin()
                .loginPage("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/", true)

            .and().rememberMe()
                .rememberMeParameter("rememberMe")
                .userDetailsService(userDetailsService)
                .key(StreamUtils.copyToString(rememberMeKey.getInputStream(), StandardCharsets.UTF_8)) //openssl rand -base64 4000 > src/main/resources/rememberMe.key
                .tokenValiditySeconds(15*24*60*60)//(int) TimeUnit.DAYS.toSeconds(15))

            .and().logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")

            .and().exceptionHandling()
                .accessDeniedPage("/403")

            .and().csrf().disable();
    }


    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring().requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico", "/403", "/pdf/**");
    }

}
