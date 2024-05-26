package com.free.tvtracker.security

import com.free.tvtracker.logging.RequestLoggingInterceptor
import com.free.tvtracker.features.user.data.UserJpaRepository
import kotlinx.serialization.json.Json
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.KotlinSerializationJsonHttpMessageConverter
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * Security setup guide used: https://codersee.com/spring-boot-3-spring-security-6-with-kotlin-jwt/
 */
@Configuration
@EnableConfigurationProperties(JwtProperties::class)
class SecurityConfig(private val requestResponseLoggingInterceptor: RequestLoggingInterceptor) : WebMvcConfigurer {

    @Bean
    fun encoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun userDetailsService(userJpaRepository: UserJpaRepository): UserDetailsService =
        SpringUserDetailsService(userJpaRepository)

    @Bean
    fun authenticationProvider(userJpaRepository: UserJpaRepository): AuthenticationProvider =
        DaoAuthenticationProvider(encoder()).also {
            it.setUserDetailsService(userDetailsService(userJpaRepository))
        }

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager = config.authenticationManager

    override fun addInterceptors(registry: InterceptorRegistry) {
        super.addInterceptors(registry)
        registry.addInterceptor(requestResponseLoggingInterceptor)
    }
//
//    SOMEHOW WE ARE USING KOTLINX SERIALIZATION AND ITS WORKING WITHOUT `ignoreUnknownKeys = true`
//
//    @Bean
//    fun messageConverter(): KotlinSerializationJsonHttpMessageConverter {
//        return KotlinSerializationJsonHttpMessageConverter(Json {
//            ignoreUnknownKeys = true
//        })
//    }
//
//    override fun extendMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
//        val json = Json {
//            ignoreUnknownKeys = true
//            isLenient = true
//            allowSpecialFloatingPointValues = true
//            useArrayPolymorphism = true
//            encodeDefaults = true
//        }
//
//        val converter = KotlinSerializationJsonHttpMessageConverter(json)
//        converters.forEachIndexed { index, httpMessageConverter ->
//            if (httpMessageConverter is KotlinSerializationJsonHttpMessageConverter) {
//                converters[index] = converter
//                return
//            }
//        }
//    }
}
