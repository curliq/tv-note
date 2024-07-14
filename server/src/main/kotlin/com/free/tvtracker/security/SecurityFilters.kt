package com.free.tvtracker.security

import com.free.tvtracker.Endpoints
import com.free.tvtracker.logging.RequestResponseLogIntercepter
import org.springframework.beans.factory.annotation.Autowired
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

    @Autowired
    private var logInterceptor: RequestResponseLogIntercepter? = null

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
                    .requestMatchers(
                        Endpoints.Path.CREATE_ANON_USER,
                        "/user/login",
                        "/user/create",
                        "/error",
                    ).permitAll()
                    .anyRequest().fullyAuthenticated()
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(logInterceptor, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }
}
