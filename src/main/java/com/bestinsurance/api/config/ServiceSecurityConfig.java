package com.bestinsurance.api.config;

import com.bestinsurance.api.security.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.net.http.HttpHeaders;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class ServiceSecurityConfig {

    @Bean
    @Order(1)
    public SecurityFilterChain mainSecurityFilterChain(HttpSecurity http) throws Exception{

        http.authorizeHttpRequests((authz) ->
                        authz.requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/swagger-ui.html").permitAll()

                        .requestMatchers(HttpMethod.POST, "/subscriptions/upload")
                        .hasAuthority(Role.BACK_OFFICE.value())

                        .requestMatchers(HttpMethod.GET, "/subscriptions/revenues")
                        .hasAuthority(Role.BACK_OFFICE.value())

                        .requestMatchers(HttpMethod.GET, "/subscriptions/*/*")
                        .hasAnyAuthority(Role.CUSTOMER.value(), Role.ADMIN.value(), Role.BACK_OFFICE.value(), Role.FRONT_OFFICE.value())
                        .requestMatchers(HttpMethod.PUT, "/subscriptions/*/*")
                        .hasAnyAuthority(Role.BACK_OFFICE.value(), Role.FRONT_OFFICE.value())
                        .requestMatchers(HttpMethod.DELETE, "/subscriptions/*/*")
                        .hasAnyAuthority(Role.BACK_OFFICE.value(), Role.FRONT_OFFICE.value())

                        .requestMatchers(HttpMethod.GET, "/subscriptions")
                        .hasAnyAuthority(Role.CUSTOMER.value(), Role.ADMIN.value(), Role.BACK_OFFICE.value(), Role.FRONT_OFFICE.value())
                        .requestMatchers(HttpMethod.POST, "/subscriptions")
                        .hasAnyAuthority(Role.BACK_OFFICE.value(), Role.FRONT_OFFICE.value())

                        .requestMatchers(HttpMethod.GET, "/policies/*")
                        .hasAnyAuthority(Role.CUSTOMER.value(), Role.ADMIN.value(), Role.BACK_OFFICE.value(), Role.FRONT_OFFICE.value())
                        .requestMatchers(HttpMethod.PUT, "/policies/*")
                        .hasAnyAuthority(Role.BACK_OFFICE.value(), Role.FRONT_OFFICE.value())
                        .requestMatchers(HttpMethod.DELETE, "/policies/*")
                        .hasAnyAuthority(Role.BACK_OFFICE.value(), Role.FRONT_OFFICE.value())

                        .requestMatchers(HttpMethod.GET, "/policies")
                        .hasAnyAuthority(Role.CUSTOMER.value(), Role.ADMIN.value(), Role.BACK_OFFICE.value(), Role.FRONT_OFFICE.value())
                        .requestMatchers(HttpMethod.POST, "/policies")
                        .hasAnyAuthority(Role.BACK_OFFICE.value(), Role.FRONT_OFFICE.value())

                        .requestMatchers(HttpMethod.GET, "/coverages")
                        .hasAnyAuthority(Role.CUSTOMER.value(), Role.ADMIN.value(), Role.BACK_OFFICE.value(), Role.FRONT_OFFICE.value())
                        .requestMatchers(HttpMethod.POST, "/coverages")
                        .hasAnyAuthority(Role.BACK_OFFICE.value(), Role.FRONT_OFFICE.value())

                        .requestMatchers(HttpMethod.GET, "/coverages/*")
                        .hasAnyAuthority(Role.CUSTOMER.value(), Role.ADMIN.value(), Role.BACK_OFFICE.value(), Role.FRONT_OFFICE.value())
                        .requestMatchers(HttpMethod.PUT, "/coverages/*")
                        .hasAnyAuthority(Role.BACK_OFFICE.value(), Role.FRONT_OFFICE.value())
                        .requestMatchers(HttpMethod.DELETE, "/coverages/*")
                        .hasAnyAuthority(Role.BACK_OFFICE.value(), Role.FRONT_OFFICE.value())

                        .requestMatchers(HttpMethod.GET, "/customers")
                        .hasAnyAuthority(Role.CUSTOMER.value(), Role.ADMIN.value(), Role.BACK_OFFICE.value(), Role.FRONT_OFFICE.value())
                        .requestMatchers(HttpMethod.POST, "/customers")
                        .hasAnyAuthority(Role.BACK_OFFICE.value(), Role.FRONT_OFFICE.value(), Role.CUSTOMER.value())

                        .requestMatchers(HttpMethod.GET, "/customers/subscriptions")
                        .hasAuthority(Role.FRONT_OFFICE.value())

                        .requestMatchers(HttpMethod.GET, "/customers/*")
                        .hasAnyAuthority(Role.CUSTOMER.value(), Role.ADMIN.value(), Role.BACK_OFFICE.value(), Role.FRONT_OFFICE.value())
                        .requestMatchers(HttpMethod.PUT, "/customers/*")
                        .hasAnyAuthority(Role.BACK_OFFICE.value(), Role.FRONT_OFFICE.value(), Role.CUSTOMER.value())
                        .requestMatchers(HttpMethod.DELETE, "/customers/*")
                        .hasAnyAuthority(Role.BACK_OFFICE.value(), Role.FRONT_OFFICE.value())



                        .requestMatchers(HttpMethod.GET, "/customers/subscriptions/discountedPrice")
                        .hasAuthority(Role.FRONT_OFFICE.value())

                        .requestMatchers(HttpMethod.GET, "/customers/policy/*")
                        .hasAuthority(Role.FRONT_OFFICE.value())

                        .requestMatchers(HttpMethod.GET, "/customers/coverage/*")
                        .hasAuthority(Role.FRONT_OFFICE.value())
                        .anyRequest().authenticated())
        .oauth2ResourceServer((oauth2ResourceServer) ->
                oauth2ResourceServer
                        .jwt((jwt) ->
                                jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )

        );

        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        grantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
}
