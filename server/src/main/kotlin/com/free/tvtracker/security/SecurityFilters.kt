package com.free.tvtracker.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

private typealias Matcher = AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry

@Configuration
@EnableWebSecurity
class SecurityFilters(private val authenticationProvider: AuthenticationProvider) {
    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(
        http: HttpSecurity,
        jwtAuthenticationFilter: JwtAuthenticationFilter
    ): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests { authz: Matcher ->
                authz
                    .requestMatchers("/user/login", "/user/create", "/error").permitAll()
                    .requestMatchers("/admin").hasRole("ADMIN")
                    .anyRequest().fullyAuthenticated()
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }
}
