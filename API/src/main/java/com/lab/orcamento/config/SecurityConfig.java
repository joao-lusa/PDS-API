package com.lab.orcamento.config;

import com.lab.orcamento.model.impl.FuncionarioServiceImpl;
import com.lab.orcamento.rest.security.jwt.JwtAuthFilter;
import com.lab.orcamento.rest.security.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private FuncionarioServiceImpl funcionarioService;
    @Autowired
    private JwtService jwtService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public OncePerRequestFilter jwtFilter(){
        return new JwtAuthFilter(jwtService, funcionarioService);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(funcionarioService)
            .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/exames/**")
                    .hasRole("FUNCIONARIO")
                .antMatchers(HttpMethod.PUT, "/api/exames/**")
                    .hasRole("FUNCIONARIO")
                .antMatchers(HttpMethod.DELETE, "/api/exames/**")
                    .hasRole("FUNCIONARIO")
                .antMatchers(HttpMethod.GET, "/api/exames/**")
                    .permitAll()
                .antMatchers(HttpMethod.GET, "/api/funcionario/**")
                    .hasRole("FUNCIONARIO")
                .antMatchers(HttpMethod.DELETE, "/api/funcionario/**")
                    .hasRole("FUNCIONARIO")
                .antMatchers(HttpMethod.PUT, "/api/funcionario/**")
                    .hasRole("FUNCIONARIO")
                .antMatchers(HttpMethod.POST, "/api/funcionario/**")
                    .permitAll()
                .anyRequest().authenticated()
            .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .addFilterBefore( jwtFilter(), UsernamePasswordAuthenticationFilter.class);
        ;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**");
    }
}
