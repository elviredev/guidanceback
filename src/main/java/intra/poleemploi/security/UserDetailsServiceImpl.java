package intra.poleemploi.security;

import intra.poleemploi.entities.UserApp;
import intra.poleemploi.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private AuthService authService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // récupère user s'il existe
        UserApp userApp = authService.loadUserAppByUsername(username);
        if(userApp == null) throw new UsernameNotFoundException("Invalid user");
        // gestion des roles - authorities
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        userApp.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getRoleName())));
        // retourne User (fournit par Spring avec username, pwd et authorities)
        return new User(userApp.getUsername(), userApp.getPassword(), authorities);
    }
}
