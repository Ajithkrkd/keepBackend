package com.ajith.secondProject.config;

import com.ajith.secondProject.Oauth2.CustomerOAuth2UserService;
import com.ajith.secondProject.auth.LogoutService;
import com.ajith.secondProject.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {


    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;
;
    private final CustomOAuth2UserService customOAuth2UserService;

    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        return http.csrf ( AbstractHttpConfigurer::disable )

                .authorizeHttpRequests ( auth -> {
                    auth.requestMatchers ( "/api/auth/**" ,"/oauth2/**" )
                            .permitAll ( )
                            .requestMatchers ( "/api/v1/admin/**" ).hasRole ( Role.ADMIN.name ( ) )
                            .anyRequest ( ).authenticated ( );

                } )

                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .authorizationEndpoint(authorizationEndpoint ->
                                        authorizationEndpoint
                                                .baseUri("/oauth2/authorize")
                                                .authorizationRequestRepository(cookieAuthorizationRequestRepository())
                                )
                                .redirectionEndpoint(redirectionEndpoint ->
                                        redirectionEndpoint
                                                .baseUri("/oauth2/callback/*")
                                )
                                .userInfoEndpoint(userInfoEndpoint ->
                                        userInfoEndpoint
                                                .userService(customOAuth2UserService)
                                )
                                .successHandler(oAuth2AuthenticationSuccessHandler)
                                .failureHandler(oAuth2AuthenticationFailureHandler)
                )

                .sessionManagement ( session -> session.sessionCreationPolicy ( SessionCreationPolicy.STATELESS ) )
                .authenticationProvider ( authenticationProvider )
                .addFilterBefore ( jwtAuthFilter, UsernamePasswordAuthenticationFilter.class )
                .logout ( logout -> {
                    logout.logoutUrl ( "/api/auth/logout" )
                            .addLogoutHandler ( logoutHandler )
                            .logoutSuccessHandler (
                                    (request, response, authentication) ->
                                            SecurityContextHolder.clearContext ( ) );
                } )
                .build ( );


    }
}