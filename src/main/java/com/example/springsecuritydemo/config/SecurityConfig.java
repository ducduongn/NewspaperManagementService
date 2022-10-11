package com.example.springsecuritydemo.config;

import com.example.springsecuritydemo.config.jwt.AuthEntryPointJwt;
import com.example.springsecuritydemo.config.jwt.AuthTokenFilter;
import com.example.springsecuritydemo.service.impl.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableMap;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        // securedEnabled = true,
        // jsr250Enabled = true,
        prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt authEntryPointJwt;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.cors().and().csrf().disable()
//                .exceptionHandling().authenticationEntryPoint(authEntryPointJwt).and()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//                .authorizeRequests()
//                .antMatchers("/api/v1/auth/**").permitAll()
//                .antMatchers("/api/v1/category/**").permitAll()
//                .antMatchers("/api/v1/article/**").permitAll()
//                .antMatchers("/api/v1/access/**").permitAll()
//                .antMatchers("/api/v1/search/**").permitAll()
//                .anyRequest().authenticated();
//
//        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
//    }


    @Bean
    public SecurityWebFilterChain accountAuthorization(ServerHttpSecurity http, @Qualifier("opaWebClient")WebClient opaWebClient) {

        // @formatter:on
        return http
                .httpBasic()
                .and()
                .authorizeExchange(exchanges -> {
                    exchanges
                            .pathMatchers("/account/*")
                            .access(opaAuthManager(opaWebClient));
                })
                .build();
        // @formatter:on

    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public ReactiveAuthorizationManager<AuthorizationContext> opaAuthManager(WebClient opaWebClient) {

        return (auth, context) -> {
            return opaWebClient.post()
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(toAuthorizationPayload(auth,context), Map.class)
                    .exchangeToMono(this::toDecision);
        };
    }

    private Mono<AuthorizationDecision> toDecision(ClientResponse response) {

        if ( !response.statusCode().is2xxSuccessful()) {
            return Mono.just(new AuthorizationDecision(false));
        }

        return response
                .bodyToMono(ObjectNode.class)
                .map(node -> {
                    boolean authorized = node.path("result").path("authorized").asBoolean(false);
                    return new AuthorizationDecision(authorized);
                });

    }

    private Publisher<Map<String,Object>> toAuthorizationPayload(Mono<Authentication> auth, AuthorizationContext context) {
        // @formatter:off
        return auth
                .defaultIfEmpty(new AnonymousAuthenticationToken("**ANONYMOUS**", new Object(), Arrays.asList(new SimpleGrantedAuthority("ANONYMOUS"))))
                .map( a -> {

                    Map<String,String> headers = context.getExchange().getRequest()
                            .getHeaders()
                            .toSingleValueMap();

                    Map<String,Object> attributes = ImmutableMap.<String,Object>builder()
                            .put("principal",a.getName())
                            .put("authorities",
                                    a.getAuthorities()
                                            .stream()
                                            .map(g -> g.getAuthority())
                                            .collect(Collectors.toList()))
                            .put("uri", context.getExchange().getRequest().getURI().getPath())
                            .put("headers",headers)
                            .build();

                    Map<String,Object> input = ImmutableMap.<String,Object>builder()
                            .put("input",attributes)
                            .build();

                    return input;
                });
        // @formatter:on
    }
}
