package intra.poleemploi.dao;

import intra.poleemploi.entities.RoleApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface RoleAppRepository extends JpaRepository<RoleApp, Long> {
    RoleApp findRoleByRoleName(String roleName);
}
