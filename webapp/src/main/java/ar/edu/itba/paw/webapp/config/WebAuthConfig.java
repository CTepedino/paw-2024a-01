package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.webapp.auth.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

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
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.addAllowedHeader(ALL);
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Link", "Location", "ETag", "X-Total-Count", "X-Refresh-Token"));
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

    private AuthorizationManager<RequestAuthorizationContext> authFilter(BiPredicate<Supplier<Authentication>, RequestAuthorizationContext> filter){
        return (a, o) -> new AuthorizationDecision(filter.test(a, o));
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

                .requestMatchers(HttpMethod.POST,
                        "/api/users"
                ).anonymous()

                .requestMatchers(HttpMethod.PUT,
                        "/api/users/{id:\\d+}",
                        "/api/users/{id:\\d+}/password",
                        "/api/users/{id:\\d+}/profile_picture"
                ).access(authFilter((a, o) ->
                            accessHelper.isLoggedUser(o.getVariables().get("id"))))

                .requestMatchers(HttpMethod.DELETE,
                        "/api/users/{id:\\d+}/profile_picture",
                        "/api/users/{id:\\d+}/wishlist/{bookId:\\d+}",
                        "/api/users/{id:\\d+}/recommendations/{bookId:\\d+}"
                ).access(authFilter((a, o) ->
                            accessHelper.isLoggedUser(o.getVariables().get("id"))))

                .requestMatchers(HttpMethod.GET,
                        "/api/users/{id:\\d+}/monthly_analytics/{date}",
                        "/api/users/{id:\\d+}/wishlist",
                        "/api/users/{id:\\d+}/wishlist/{bookId:\\d+}"
                ).access(authFilter((a, o) ->
                            accessHelper.isLoggedUser(o.getVariables().get("id"))))

                .requestMatchers(HttpMethod.POST,
                        "/api/users/{id:\\d+}/wishlist",
                        "/api/users/{id:\\d+}/recommendations"
                ).access(authFilter((a, o) ->
                            accessHelper.isLoggedUser(o.getVariables().get("id"))))

                .requestMatchers(HttpMethod.POST,
                        "/api/books"
                ).authenticated()

                .requestMatchers(HttpMethod.PUT,
                        "/api/books/{id:\\d+}",
                        "/api/books/{id:\\d+}/cover",
                        "/api/books/{id:\\d+}/preview",
                        "/api/books/{id:\\d+}/book_file",
                        "/api/books/{id:\\d+}/deal"
                ).access(authFilter((a, o) ->
                            accessHelper.isLoggedAndWriter(o.getVariables().get("id"))))

                .requestMatchers(HttpMethod.GET,
                        "/api/books/{id:\\d+}/monthly_analytics/{date}"
                ).access(authFilter( (a, o) ->
                        accessHelper.isLoggedAndWriter(o.getVariables().get("id"))))

                .requestMatchers(HttpMethod.DELETE,
                        "/api/books/{id:\\d+}/deal"
                ).access(authFilter((a, o) ->
                        accessHelper.isLoggedAndWriter(o.getVariables().get("id"))))

                .requestMatchers(HttpMethod.GET,
                        "/api/books/{id:\\d+}/book_file"
                ).access(authFilter((a, o) ->
                        accessHelper.isLoggedAndOwnsBookOrIsWriter(o.getVariables().get("id"))))

                .requestMatchers(HttpMethod.PUT,
                        "/api/books/{bookId:\\d+}/reviews/{userId:\\d+}"
                ).access(authFilter((a, o) ->
                        accessHelper.isLoggedUserAndOwnsBook(o.getVariables().get("bookId"), o.getVariables().get("userId"))))

                .requestMatchers(HttpMethod.POST,
                        "/api/questions",
                        "/api/orders"
                ).authenticated()

                .requestMatchers(HttpMethod.PUT,
                        "/api/questions/{id:\\d+}/answer"
                ).access(authFilter((a, o) ->
                        accessHelper.isLoggedAndCanAnswer(o.getVariables().get("id"))))

                .requestMatchers(HttpMethod.GET,
                        "/api/orders/{id:\\d+}",
                        "/api/orders/{id:\\d+}/receipt"
                ).access(authFilter((a, o) ->
                        accessHelper.isLoggedAndWriterOrBuyer(o.getVariables().get("id"))))

                .requestMatchers(HttpMethod.PATCH,
                        "/api/orders/{id:\\d+}"
                ).access(authFilter((a, o) ->
                        accessHelper.isLoggedAndWriterAndCanAdvanceOrder(o.getVariables().get("id"))))

                .requestMatchers(HttpMethod.PUT,
                        "/api/orders/{id:\\d+}/receipt"
                ).access(authFilter((a, o) ->
                        accessHelper.isLoggedAndBuyerAndCanAdvanceOrder(o.getVariables().get("id"))))

                .anyRequest().permitAll()


            .and().cors()
            .and().csrf().disable()

            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(basicAuthFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(final WebSecurity web) {
        web.ignoring().requestMatchers("/static/**", "/assets/**");
    }
}
