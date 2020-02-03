package intra.poleemploi.service;

import intra.poleemploi.entities.RoleApp;
import intra.poleemploi.entities.UserApp;

public interface AuthService {
    UserApp saveUserApp(String username, String password, String confirmedPassword);
    UserApp saveUserApp(UserApp userApp);
    UserApp loadUserAppByUsername(String username);
    RoleApp saveRoleApp(RoleApp role);
    void addRoleToUser(String username, String roleName);
    void addAppliToUser(String username, String appliName);
}
