package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.webapp.auth.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.web.cors.CorsConfiguration.ALL;


@EnableWebSecurity
@ComponentScan({"ar.edu.itba.paw.webapp.auth"})
@Configuration
public class WebAuthConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtRequestFilter jwtRequestFilter;
    @Autowired
    private BasicAuthFilter basicAuthFilter;
    @Autowired
    private  AccessHelper accessHelper;


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtTokenUtil jwtTokenUtil(){
        return new JwtTokenUtil(userDetailsService);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList(ALL));
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "DELETE", "OPTIONS"));
        configuration.addAllowedHeader(ALL);
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Link", "Location", "ETag", "X-Total-Count"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }


    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new UnauthorizedRequestHandler();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
            http.sessionManagement()

            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            .and().exceptionHandling()
                .authenticationEntryPoint(new UnauthorizedRequestHandler())
                .accessDeniedHandler(new ForbiddenRequestHandler())

            .and().headers().cacheControl().disable()

            .and().authorizeHttpRequests()
                /*.requestMatchers("/signup", "/login", "/validate", "/forgotPassword", "/resendResetCode/{userId:\\d+}").anonymous()
                .requestMatchers( "/sales", "questions/questions", "/analytics").hasAuthority(UserRoles.WRITER.toString())
                .requestMatchers("/resetPassword/{id:\\d+}/{code:\\d+}").access((a, o) -> new AuthorizationDecision(accessHelper.validResetCode(o.getVariables().get("id"), o.getVariables().get("code"))))
                .requestMatchers( "/sendBuyInfo/{id:\\d+}").access((a, o) -> new AuthorizationDecision(accessHelper.canCreateOrder(o.getVariables().get("id"))))
                .requestMatchers("/receipt/{id:\\d+}").access((a, o) -> new AuthorizationDecision(accessHelper.canAccessReceipt(a.get(), o.getVariables().get("id"))))
                .requestMatchers("/book/edit/{id:\\d+}", "/book/{id:\\d+}/deal", "/book/{id:\\d+}/{dealId:\\d+}/endDeal").access((a, o) -> new AuthorizationDecision(accessHelper.canEditBook(o.getVariables().get("id"))))
                .requestMatchers("/book/file/{id:\\d+}").access((a, o) -> new AuthorizationDecision(accessHelper.canAccessBook(a.get(), o.getVariables().get("id"))))
                .requestMatchers("/book/${id:\\d+}/reviews/review").access((a,o) -> new AuthorizationDecision(accessHelper.canReview(a.get(), o.getVariables().get("id"))))
                .requestMatchers(HttpMethod.POST, "/advanceOrder/{id:\\d+}/**").access((a, o) -> new AuthorizationDecision(accessHelper.canAdvanceOrder(a.get(), o.getVariables().get("id"))))
                .requestMatchers("/profile/{userId:\\d+}/publications").access((a,o) -> new AuthorizationDecision(accessHelper.checkIsWriter(o.getVariables().get("userId"))))
                .requestMatchers("/profile/{userId:\\d+}/wishlist").access((a, o) -> new AuthorizationDecision(accessHelper.checkIsLoggedUser(o.getVariables().get("userId"))))
                .requestMatchers(HttpMethod.POST,"/wishlist/{bookId:\\d+}").access((a, o) -> new AuthorizationDecision(accessHelper.canCreateOrder(o.getVariables().get("bookId"))))
                .requestMatchers(HttpMethod.POST,"/recommendBook/{id:\\d+}").access((a, o) -> new AuthorizationDecision(accessHelper.canRecommendBook(a.get(), o.getVariables().get("id"))))
                .requestMatchers("/book/${id:\\d+}/question").access((a,o) -> new AuthorizationDecision(accessHelper.canQuestion(a.get(), o.getVariables().get("id"))))
                .requestMatchers("/book/{bookId:\\d+}/questions/{questionId:\\d+}/answer", "/questions/questions/{questionId:\\d+}/answer").access((a,o) -> new AuthorizationDecision(accessHelper.canAnswer(a.get(), o.getVariables().get("questionId"))))
                .requestMatchers("/", "/cover/**", "/preview/**", "book/{bookId:\\d+}","/book/{bookId:\\d+}/questions", "/book/{bookId:\\d+}/reviews", "/search/**", "/profilePicture/**", "/profile/{userId:\\d+}/**").permitAll()
                .anyRequest().authenticated()*/
                .anyRequest().permitAll()


            .and().cors()
            .and().csrf().disable()

            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(basicAuthFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(final WebSecurity web) {
        web.ignoring().requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico", "/exception/**", "/pdf/**");
    }
}
