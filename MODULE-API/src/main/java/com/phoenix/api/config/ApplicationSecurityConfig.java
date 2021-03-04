/*
 * MIT License
 *
 * Copyright (c) 2020 Đình Tài
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.phoenix.api.config;

import com.phoenix.api.filter.JwtAuthenticationFilter;
import com.phoenix.api.security.DefaultBcryptPasswordEncoder;
import com.phoenix.api.security.JwtAuthenticationEntryPoint;
import com.phoenix.common.security.TokenProvider;
import com.phoenix.domain.model.UrlAntMatcherModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Application security config
 */
@Configuration
@EnableWebSecurity(debug = false)
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final TokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;
    private final ApplicationUrlLoader applicationUrlLoader;

    public ApplicationSecurityConfig(
            @Qualifier("JwtAuthenticationEntryPoint") JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            @Qualifier("TokenProvider") TokenProvider tokenProvider,
            @Qualifier("DefaultUserDetailsService") UserDetailsService userDetailsService,
            @Qualifier("ApplicationUrlLoader") ApplicationUrlLoader applicationUrlLoader) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
        this.applicationUrlLoader = applicationUrlLoader;
    }

    @Bean(name = "JwtAuthenticationFilter")
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(tokenProvider, userDetailsService);
    }

    @Bean(name = "DefaultAuthenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean(name = "PasswordEncoder")
    public PasswordEncoder passwordEncoder() {
        return new DefaultBcryptPasswordEncoder();
    }

    // Used by spring security if CORS is enabled.
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String authPattern = "/**" + ApplicationUrls.AUTH_PREFIX + "/**";

        http.cors().and()
                .csrf()
                .disable();


        for (UrlAntMatcherModel matcherModel : applicationUrlLoader.getPermitAllAntMatchers()) {
            System.out.println(matcherModel.getUrl());
            http.authorizeRequests().antMatchers(matcherModel.getUrl()).permitAll();
        }

        for (UrlAntMatcherModel matcherModel : applicationUrlLoader.getNeedAuthAntMatchers()) {
            System.out.println(matcherModel.getUrl());
            http.authorizeRequests().antMatchers(matcherModel.getUrl()).hasAnyRole(matcherModel.getPermissions());
        }

        http
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

//        http.cors().and()
//                .csrf()
//                .disable()
//                .authorizeRequests()
//                .antMatchers(authPattern).permitAll()
//                .antMatchers(ApplicationUrls.PUBLIC_MATCHERS).permitAll()
//                .antMatchers(ApplicationUrls.SWAGGER_MATCHERS).permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
//                .and()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
