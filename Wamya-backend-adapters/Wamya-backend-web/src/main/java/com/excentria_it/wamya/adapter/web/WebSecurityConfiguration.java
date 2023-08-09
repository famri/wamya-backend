package com.excentria_it.wamya.adapter.web;

import com.excentria_it.wamya.common.annotation.Generated;
import com.excentria_it.wamya.domain.UserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Generated
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    // @Autowired
    // Converter<Jwt, JwtAuthenticationToken> authenticationConverter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // TODO for production enable csrf
        // @formatter:off
        http.csrf().disable().cors().disable().httpBasic().disable().formLogin().disable().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests(authz -> authz
                        .antMatchers("/actuator/**", "/content/**").permitAll()
                        .antMatchers(HttpMethod.POST, "/login/**", "/accounts/**", "/rating-details/**", "/ratings/**")
                        .permitAll()
                        .antMatchers(HttpMethod.GET, "/countries/**", "/locales/**", "/genders/**", "/documents/**",
                                "/rating-details/**")
                        .permitAll().antMatchers(HttpMethod.GET, "/places/**").permitAll()
                        .antMatchers(HttpMethod.GET, "/departments/**").permitAll()
                        .antMatchers(HttpMethod.GET, "/engine-types/**").permitAll()
                        .antMatchers(HttpMethod.GET, "/wamya-ws/**")
                        .hasAnyAuthority(UserRole.ROLE_TRANSPORTER.name(), UserRole.ROLE_CLIENT.name())
                        .antMatchers(HttpMethod.POST, "/user-preferences")
                        .hasAnyAuthority(UserRole.ROLE_TRANSPORTER.name(), UserRole.ROLE_CLIENT.name())
                        .antMatchers(HttpMethod.POST, "/accounts/do-request-password-reset/**",
                                "/accounts/password-reset/**")
                        .permitAll().antMatchers(HttpMethod.GET, "/accounts/password-reset/**").permitAll()
                        .antMatchers(HttpMethod.PATCH, "/accounts/me/device-token/**")
                        .hasAnyAuthority(UserRole.ROLE_TRANSPORTER.name(), UserRole.ROLE_CLIENT.name())
                        .antMatchers("/users/me/discussions/**", "users/me/messages/count")
                        .hasAnyAuthority(UserRole.ROLE_TRANSPORTER.name(), UserRole.ROLE_CLIENT.name())

                        .antMatchers(HttpMethod.GET, "/profiles/**").hasAnyAuthority(UserRole.ROLE_TRANSPORTER.name(), UserRole.ROLE_CLIENT.name())
                        .antMatchers(HttpMethod.POST, "/profiles/**").hasAnyAuthority(UserRole.ROLE_TRANSPORTER.name(), UserRole.ROLE_CLIENT.name())
                        .antMatchers(HttpMethod.PATCH, "/profiles/**").hasAnyAuthority(UserRole.ROLE_TRANSPORTER.name(), UserRole.ROLE_CLIENT.name())

                        .antMatchers(HttpMethod.POST, "/geo-places/**").hasAuthority(UserRole.ROLE_CLIENT.name())
                        .antMatchers(HttpMethod.GET, "/geo-places/**").hasAuthority(UserRole.ROLE_CLIENT.name())

                        .antMatchers(HttpMethod.PATCH, "/journey-requests/{\\d+}/proposals/{\\d+}/**").hasAuthority(UserRole.ROLE_CLIENT.name())
                        .antMatchers(HttpMethod.POST, "/journey-requests/{\\d+}/proposals/**").hasAuthority(UserRole.ROLE_TRANSPORTER.name())
                        .antMatchers(HttpMethod.GET, "/journey-requests/{\\d+}/proposals/**").hasAuthority(UserRole.ROLE_CLIENT.name())
                        .antMatchers(HttpMethod.PATCH, "/journey-requests/{\\d+}/**").hasAuthority(UserRole.ROLE_CLIENT.name())
                        .antMatchers(HttpMethod.GET, "/journey-requests/**").hasAuthority(UserRole.ROLE_TRANSPORTER.name())
                        .antMatchers(HttpMethod.POST, "/journey-requests/**").hasAuthority(UserRole.ROLE_CLIENT.name())

                        .antMatchers(HttpMethod.GET, "/travel-info/**").hasAuthority(UserRole.ROLE_CLIENT.name())

                        .antMatchers(HttpMethod.GET, "/constructors/{\\d+}/models/**", "/constructors**")
                        .hasAuthority(UserRole.ROLE_TRANSPORTER.name())

                        .antMatchers(HttpMethod.POST, "/users/me/vehicles/**").hasAuthority(UserRole.ROLE_TRANSPORTER.name())
                        .antMatchers(HttpMethod.GET, "/users/me/vehicles/**").hasAuthority(UserRole.ROLE_TRANSPORTER.name())

                        .antMatchers(HttpMethod.GET, "/users/me/journey-requests/**")
                        .hasAuthority(UserRole.ROLE_CLIENT.name())
                        .antMatchers(HttpMethod.GET, "/users/me/proposals/**")
                        .hasAuthority(UserRole.ROLE_TRANSPORTER.name())
                        .antMatchers(HttpMethod.POST, "/validation-codes/sms/send/**")
                        .hasAnyAuthority(UserRole.ROLE_TRANSPORTER.name(), UserRole.ROLE_CLIENT.name())
                        .antMatchers(HttpMethod.POST, "/validation-codes/sms/validate/**")
                        .hasAnyAuthority(UserRole.ROLE_TRANSPORTER.name(), UserRole.ROLE_CLIENT.name())
                        .antMatchers(HttpMethod.POST, "/validation-codes/email/send/**")
                        .hasAnyAuthority(UserRole.ROLE_TRANSPORTER.name(), UserRole.ROLE_CLIENT.name()).antMatchers(HttpMethod.POST, "/users/me/identities/**")
                        .hasAnyAuthority(UserRole.ROLE_TRANSPORTER.name(), UserRole.ROLE_CLIENT.name()).antMatchers(HttpMethod.POST, "/vehicles/{\\d+}/images/**")
                        .hasAuthority(UserRole.ROLE_TRANSPORTER.name()).anyRequest().authenticated())
                .oauth2ResourceServer().bearerTokenResolver(bearerTokenResolver()).jwt().jwtAuthenticationConverter(jwtAuthenticationConverter());


        // @formatter:on

    }

    // this is to allow sending access_token as a URI request parameter for
    // webSocket client authentication
    @Bean
    BearerTokenResolver bearerTokenResolver() {
        DefaultBearerTokenResolver bearerTokenResolver = new DefaultBearerTokenResolver();
        bearerTokenResolver.setAllowUriQueryParameter(true);
        return bearerTokenResolver;
    }

    // @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

    //@Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

}
