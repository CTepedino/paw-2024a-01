package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.models.users.UserRoles;
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
import java.util.concurrent.TimeUnit;


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
                .requestMatchers("/signup", "/login", "/validate").anonymous()
                .requestMatchers( "/sales").hasAuthority(UserRoles.WRITER.toString())
                .requestMatchers( "/sendBuyInfo/{id:\\d+}").access((a, o) -> new AuthorizationDecision(accessHelper.canCreateOrder(a.get(), o.getVariables().get("id"))))
                .requestMatchers("/receipt/{id:\\d+}").access((a, o) -> new AuthorizationDecision(accessHelper.canAccessReceipt(a.get(), o.getVariables().get("id"))))
                .requestMatchers("/book/edit/{id:\\d+}").access((a, o) -> new AuthorizationDecision(accessHelper.canEditBook(a.get(), o.getVariables().get("id"))))
                .requestMatchers("/book/file/{id:\\d+}").access((a, o) -> new AuthorizationDecision(accessHelper.canAccessBook(a.get(), o.getVariables().get("id"))))
                .requestMatchers("/book/${id:\\d+}/review").access((a,o) -> new AuthorizationDecision(accessHelper.canReview(a.get(), o.getVariables().get("id"))))
                .requestMatchers(HttpMethod.POST, "/advanceOrder/{id:\\d+}/**").access((a, o) -> new AuthorizationDecision(accessHelper.canAdvanceOrder(a.get(), o.getVariables().get("id"))))
                .requestMatchers("/profile/{userId:\\d+}/publications").access((a,o) -> new AuthorizationDecision(accessHelper.checkIsWriter(o.getVariables().get("userId"))))
                .requestMatchers("/", "/cover/**", "/preview/**", "/book/{id:\\d+}", "/search/**", "/profilePicture/**", "/profile/{userId:\\d+}/**").permitAll()
                .anyRequest().authenticated()

            .and().formLogin()
                .loginPage("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/", false)

            .and().rememberMe()
                .rememberMeParameter("rememberMe")
                .userDetailsService(userDetailsService)
                .key(StreamUtils.copyToString(rememberMeKey.getInputStream(), StandardCharsets.UTF_8)) //openssl rand -base64 4000 > src/main/resources/rememberMe.key
                .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(15))

            .and().logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")

            .and().exceptionHandling()
                .accessDeniedPage("/403")

            .and().csrf().disable();

            http.headers().frameOptions().sameOrigin();
    }


    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring().requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico", "/exception/**", "/pdf/**");
    }

}
