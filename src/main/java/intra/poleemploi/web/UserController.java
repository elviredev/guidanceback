package intra.poleemploi.web;

import intra.poleemploi.dao.AppliRepository;
import intra.poleemploi.dao.UserAppRepository;
import intra.poleemploi.entities.Appli;
import intra.poleemploi.entities.RoleApp;
import intra.poleemploi.entities.UserApp;
import intra.poleemploi.service.AuthService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@RestController
//@RequestMapping("/")
//@CrossOrigin(origins = "http://localhost:4200")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private AuthService authService;
    @Autowired
    private UserAppRepository userAppRepository;
    @Autowired
    private AppliRepository appliRepository;

    @PostMapping("/adminUsers")
    public UserApp register(@RequestBody UserForm userForm){ // données envoyées au format JSON
        return authService.saveUserApp(userForm.getUsername(), userForm.getPassword(), userForm.getConfirmedPassword());
    }

    @PutMapping("/updateUserRoles/{id}")
    public ResponseEntity<UserApp> updateUserRoles(@PathVariable(value="id") Long id, @RequestBody List<RoleApp> roles) throws ResourceNotFoundException {
        UserApp userBdd = userAppRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + id));
        userBdd.setRoles(roles);
        return ResponseEntity.ok(authService.saveUserApp(userBdd));
    }

    @PutMapping("/updateUserApplis/{id}")
    public ResponseEntity<UserApp> updateUserApplis(@PathVariable(value="id") Long id, @RequestBody Set<Appli> applis) throws ResourceNotFoundException {
        UserApp userBdd = userAppRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + id));
        // checker le contenu des applis
        userBdd.setApplis(new HashSet<Appli>());
        // pour chaque appli de la liste des applis on ajoute le contenu de l'appli
        for(Appli appli : applis ){
            userBdd.getApplis().add(appliRepository.findAppliByAppliName(appli.getAppliName()));
        }
        return ResponseEntity.ok(authService.saveUserApp(userBdd));
    }
}
@Data
class UserForm {
    private String username;
    private String password;
    private String confirmedPassword;
}
