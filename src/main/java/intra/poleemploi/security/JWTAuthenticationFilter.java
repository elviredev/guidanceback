package intra.poleemploi.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import intra.poleemploi.dao.UserAppRepository;
import intra.poleemploi.entities.UserApp;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    private UserAppRepository userAppRepository;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager,  UserAppRepository userAppRepository) {
        this.authenticationManager = authenticationManager;
        this.userAppRepository = userAppRepository;
    }

    // récupération username et pwd de l'utilisateur authentifié et retour a Spring security
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // jackson pour récupérer data envoyées en JSON
            UserApp userApp = new ObjectMapper().readValue(request.getInputStream(), UserApp.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userApp.getUsername(), userApp.getPassword()));
        } catch (IOException e) {
            e.printStackTrace();
            // si pb dans le corps de la requête, une exception est levée
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // récupère l'utilisateur authentifié
        User user = (User) authResult.getPrincipal();
        // déclarer une List de roles
        List<String> roles = new ArrayList<>();
        authResult.getAuthorities().forEach(auth -> {
            roles.add(auth.getAuthority());
        });
        // récupère un userApp et on créé le jeton pour l'userApp avec les claims souhaités
        UserApp userApp = userAppRepository.findUserByUsername(user.getUsername());
        String jwt = JWT.create()
                .withIssuer(request.getRequestURI()) // nom de l'autorité de l'application ayant généré le token
                .withClaim("id", userApp.getId())
                .withClaim("username", userApp.getUsername())
                .withArrayClaim("roles", roles.toArray(new String[roles.size()]))
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityParams.EXPIRATION))
                .sign(Algorithm.HMAC256(SecurityParams.SECRET)); // signature + secret
        response.addHeader(SecurityParams.JWT_HEADER_NAME, jwt);
    }
}
