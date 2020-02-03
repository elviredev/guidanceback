package intra.poleemploi.security;

import intra.poleemploi.dao.UserAppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // Spring Security se base sur interface UserDetailsService (fournie par Spring) pour gérer authentification
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    UserAppRepository userAppRepository;

    // définit les utilisateurs qui ont accès à l'appli => permet de les authentifier
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    // autorisations et filtres
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // ***** A ENLEVER POUR LA PROD >> IL FAUT ETRE ADMIN POUR MODIFIER LES ROLES ET LES APPLIS DE USER ***** //
        http.authorizeRequests()
                .antMatchers("/home", "/contact/**", "/login/**", "/adminUsers/**", "/updateUserRoles/**", "/updateUserApplis/**").permitAll()
                .antMatchers(HttpMethod.GET,  "/userApps/**", "/roleApps/**", "/applis/**", "/contents/**").permitAll()
                .antMatchers("/userApps/**", "/roleApps/**", "/applis/**").hasAuthority("ADMIN")
                .anyRequest().authenticated();

        // ajout du filtre JWTAuth pour générer le token
        http.addFilter(new JWTAuthenticationFilter(authenticationManager(), userAppRepository));
        http.addFilterBefore(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

    }
}
